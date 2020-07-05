package com.kamaldeep.a_eye;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity
{
    private static String TAG = LoginActivity.class.getSimpleName();

    private UserLoginTask mAuthTask = null;

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;
    private Button ipAddSettingButton;

    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);

        sharedpreferences = getSharedPreferences(VARIABLES.sharedPreferences_IP_Addresses_name, Context.MODE_PRIVATE);

        if(!sharedpreferences.contains(VARIABLES.RTMP_IP_ADDRESS_SharedPreferences_Key) ||
                !sharedpreferences.contains(VARIABLES.RTMP_IP_ADDRESS_SharedPreferences_Key))
        {
            editIPAddressDialogBox();
        }else
        {
            VARIABLES.FLASK_IP_ADDRESS = sharedpreferences
                    .getString(VARIABLES.FLASK_IP_ADDRESS_SharedPreferences_Key,"");
            VARIABLES.RTMP_IP_ADDRESS = sharedpreferences
                    .getString(VARIABLES.RTMP_IP_ADDRESS_SharedPreferences_Key,"");
            checkConnection_assign_listners();
        }

        mEmailView =  findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        ipAddSettingButton = findViewById(R.id.login_settings_button);
        ipAddSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editIPAddressDialogBox();
            }
        });

    }

    private void attemptLogin()
    {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mFacultyID;
        private final String mPassword;

        UserLoginTask(String facultyID, String password) {
            mFacultyID = facultyID;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String hashedPassword="";

            String url= "http://"+VARIABLES.FLASK_IP_ADDRESS+":5000/fac_creden/fetch" +
                    "?faculty_id="+mFacultyID;

            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(url);
            Log.d(TAG, "Response from url: " + jsonStr);



            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    hashedPassword = jsonObj.getString("password");
                    Log.e(TAG,"PASSWORD: " + hashedPassword);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                return false;
            }

            boolean verified = BCrypt.checkpw(mPassword,hashedPassword);
            Log.e(TAG,"VERIFY:" + verified);

            if(verified)
            {
                VARIABLES.empID = mFacultyID;
            }

            return verified;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {

            Log.e(TAG,"empid: " + VARIABLES.empID);

            mAuthTask = null;
            if (success)
            {
                finish();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            } else {
                mEmailView.setError(getString(R.string.error_incorrect_email_password));
                mPasswordView.setError(getString(R.string.error_incorrect_email_password));
            }
        }
    }


    private boolean checkConnectivity(String ip_address){
        System.out.println("checkConnectivity");
        Runtime runtime = Runtime.getRuntime();
        try
        {
            String command = "/system/bin/ping -c 1 " + ip_address;
            Process  mIpAddrProcess = runtime.exec(command);
            int mExitValue = mIpAddrProcess.waitFor();
            //System.out.println(" mExitValue "+mExitValue);
            Log.e(TAG,"ip add" + ip_address);
            Log.e(TAG," mExitValue "+mExitValue);
            if(mExitValue==0){
                return true;
            }else{
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
            System.out.println(" Exception:"+ignore);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println(" Exception:"+e);
        }
        return false;
    }

    private void testingLogin()
    {
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }


    public void editIPAddressDialogBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogBoxLayout = inflater.inflate(R.layout.ip_address_dialog_box, null);
        final EditText flaskTextView = dialogBoxLayout.findViewById(R.id.flask_ip_address);
        final EditText rtmpTextView = dialogBoxLayout.findViewById(R.id.rtmp_ip_address);

        flaskTextView.setText(sharedpreferences
                .getString(VARIABLES.FLASK_IP_ADDRESS_SharedPreferences_Key,""));
        rtmpTextView.setText(sharedpreferences
                .getString(VARIABLES.RTMP_IP_ADDRESS_SharedPreferences_Key,""));

        builder.setTitle("IP Address")
                .setMessage("Enter your flask and rtmp server's IP Address")
                .setView(dialogBoxLayout)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String flaskIPAddress = flaskTextView.getText().toString();
                        String rtmpIPAddress = rtmpTextView.getText().toString();

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(VARIABLES.FLASK_IP_ADDRESS_SharedPreferences_Key, flaskIPAddress);
                        editor.putString(VARIABLES.RTMP_IP_ADDRESS_SharedPreferences_Key, rtmpIPAddress);
                        editor.apply();

                        VARIABLES.FLASK_IP_ADDRESS = flaskIPAddress;
                        VARIABLES.RTMP_IP_ADDRESS = rtmpIPAddress;

                        checkConnection_assign_listners();

                        Log.e(TAG,"flask: " + flaskIPAddress);
                        Log.e(TAG,"rtmp: " + rtmpIPAddress);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void checkConnection_assign_listners()
    {
        String flask_ipadd = VARIABLES.FLASK_IP_ADDRESS;
        String rtmp_ipadd = VARIABLES.RTMP_IP_ADDRESS;

        //removes the port number
        String tempFlaskIP="";
        if(flask_ipadd.indexOf(':') != -1){
            tempFlaskIP = flask_ipadd.substring(0, flask_ipadd.indexOf(':'));
        }else{
            tempFlaskIP = flask_ipadd;
        }

        final boolean hasConnection_flask = checkConnectivity(tempFlaskIP);
        final boolean hasConnection_rtmp = checkConnectivity(rtmp_ipadd);

        if(hasConnection_flask && hasConnection_rtmp)
        {
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }else
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!hasConnection_flask && !hasConnection_rtmp){
                        Toast.makeText(getApplicationContext(),"Couldn't connect to Internal Server"
                                ,Toast.LENGTH_LONG).show();
                    }else if(!hasConnection_flask){
                        Toast.makeText(getApplicationContext(),"Couldn't connect to Internal Server (flask)"
                                ,Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Couldn't connect to Internal Server (rtmp)"
                                ,Toast.LENGTH_LONG).show();
                    }

                }
            });

//        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                testingLogin();
//            }
//        });

    }

}