package com.kamaldeep.a_eye.UIFragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamaldeep.a_eye.HttpHandler;
import com.kamaldeep.a_eye.R;
import com.kamaldeep.a_eye.VARIABLES;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AttendanceReportFragment extends Fragment
{
    private static final String TAG = AttendanceReportFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> rollNumber;
    private List<String> studentName;


    public AttendanceReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView =  inflater.inflate(R.layout.fragment_attendance_report, container, false);

        new AsyncTask<String, String, Boolean>() {

            @Override
            protected void onPreExecute() {
                rollNumber = new ArrayList<>();
                studentName = new ArrayList<>();
            }

            @Override
            protected Boolean doInBackground(String... partUrl) {
                HttpHandler sh = new HttpHandler();
                String url = partUrl[0];
                String jsonStr = sh.makeServiceCall(url);
                Log.d(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);


                        JSONArray sRollNo = jsonObj.getJSONArray("student_id");
                        JSONArray sName = jsonObj.getJSONArray("student_name");

                        for (int i = 0; i < sRollNo.length(); i++) {
                            String stuRollNoString = sRollNo.getString(i);
                            String stuName = sName.getString(i);

                            rollNumber.add(stuRollNoString);
                            studentName.add(stuName);

                            //JSON_Contents.add(stuRollNoString);
                            //Log.e(TAG, "STR:" + stuRollNoString);
                            //Log.e(TAG, "JSON_Contents: " + JSON_Contents.get(i));
                        }


                    } catch (final Exception e) { //JSONException
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {

                for(int i=0;i<rollNumber.size();i++)
                {
                    Log.e(TAG,"Roll No: " + rollNumber.get(i));
                    Log.e(TAG,"Name   : " + studentName.get(i));
                }

                recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
                // use this setting to
                // improve performance if you know that changes
                // in content do not change the layout size
                // of the RecyclerView
                recyclerView.setHasFixedSize(true);
                // use a linear layout manager
                layoutManager = new LinearLayoutManager(rootView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                List<String> input = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    input.add("Test" + i);
                }// define an adapter
                mAdapter = new MyAdapter(rollNumber,studentName);
                recyclerView.setAdapter(mAdapter);


            }
        }.execute(VARIABLES.retriveFinalURL);

        return rootView;
    }
}