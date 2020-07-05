package com.kamaldeep.a_eye.UIFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.kamaldeep.a_eye.HttpHandler;
import com.kamaldeep.a_eye.R;
import com.kamaldeep.a_eye.VARIABLES;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class ReviewAttendanceFragment extends Fragment
{
    private String TAG = ReviewAttendanceFragment.class.getSimpleName();
    private String changeAdapterTAG = "changeAdapterTAG";

    private ArrayList<String> JSON_Contents;
    private Button mStartButton;

    private Spinner facultySpinner;
    private Spinner departmentSpinner;
    private Spinner programmeIDSpinner;
    private Spinner subjectIDSpinner;
    private Spinner semesterSpinner;
    private Spinner sectionSpinner;
    private Spinner periodSpinner;

    private TextView datePopUpText;

    private String selectedFacultyID;
    private String selectedDeptID;
    private String selectedProgrammeID;
    private String selectedSubjectID;
    private String selectedSemester;
    private String selectedSection;
    private String selectedPeriod;
    private String selectedDate;
    private String finalURL;

    private HashMap<String, Integer> map = new HashMap<>();

    final Calendar myCalendar = Calendar.getInstance();
    public ReviewAttendanceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        final View rootView = inflater.inflate(R.layout.fragment_review_attendance, container, false);

        facultySpinner = rootView.findViewById(R.id.faculty_spinner);
        departmentSpinner = rootView.findViewById(R.id.department_spinner);
        programmeIDSpinner = rootView.findViewById(R.id.programme_spinner);
        subjectIDSpinner = rootView.findViewById(R.id.subject_spinner);
        semesterSpinner = rootView.findViewById(R.id.semester_spinner);
        sectionSpinner = rootView.findViewById(R.id.section_spinner);
        periodSpinner = rootView.findViewById(R.id.period_spinner);

        datePopUpText = rootView.findViewById(R.id.datePopUp);

        mStartButton = rootView.findViewById(R.id.reviewAttendance);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePopUpText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(rootView.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        setDefaultAdapters(0);

        //populate faculty spinner
        new AsyncTask<String, String, Boolean>()
        {
            @Override
            protected void onPreExecute()
            {
                setDefaultAdapters(1);
                JSON_Contents = new ArrayList<>();
                JSON_Contents.add("--");
            }

            @Override
            protected Boolean doInBackground(String... partUrl)
            {
                HttpHandler sh = new HttpHandler();
                String url = "http://"+VARIABLES.FLASK_IP_ADDRESS +":5000/"
                        + partUrl[0];
                String jsonStr = sh.makeServiceCall(url);
                Log.d(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null)
                {
                    try
                    {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray contacts = jsonObj.getJSONArray("faculty_id");
                        for (int i = 0; i < contacts.length(); i++)
                        {
                            String s = contacts.getString(i);
                            JSON_Contents.add(s);
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
            protected void onPostExecute(Boolean result)
            {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                        JSON_Contents);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                facultySpinner.setAdapter(adapter);

                //JSON_Contents.clear();
            }
        }.execute("auto_select/faculty");





        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Log.e(TAG, "onItemSelected");
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                selectedFacultyID = itemAtPosition;
                Log.e(TAG, "sel Item=>" + itemAtPosition);

                if(!itemAtPosition.equals("--"))
                {
                    //populating department spinner
                    new AsyncTask<String, String, Boolean>()
                    {

                        @Override
                        protected void onPreExecute()
                        {
                            setDefaultAdapters(2);
                            JSON_Contents = new ArrayList<>();
                            JSON_Contents.add("--");
                        }

                        @Override
                        protected Boolean doInBackground(String... partUrl)
                        {
                            HttpHandler sh = new HttpHandler();
                            String url = "http://"+VARIABLES.FLASK_IP_ADDRESS +":5000/"
                                    + partUrl[0];
                            String jsonStr = sh.makeServiceCall(url);
                            Log.d(TAG, "Response from url: " + jsonStr);

                            if (jsonStr != null)
                            {
                                try
                                {
                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                    JSONArray contacts = jsonObj.getJSONArray("department_name");
                                    for (int i = 0; i < contacts.length(); i++)
                                    {
                                        String s = contacts.getString(i);
                                        JSON_Contents.add(s);
                                        //Log.e(TAG, "STR:" + s);
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
                        protected void onPostExecute(Boolean result)
                        {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    JSON_Contents);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            departmentSpinner.setAdapter(adapter);

                            //JSON_Contents.clear();
                        }
                    }.execute("auto_select/department?faculty_name="+ itemAtPosition);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Log.e(TAG, "onItemSelected");
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                selectedDeptID = itemAtPosition;
                Log.e(TAG, "sel Item=>" + itemAtPosition);

                if(!itemAtPosition.equals("--"))
                {
                    //populating course spinner
                    new AsyncTask<String, String, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            setDefaultAdapters(3);
                            JSON_Contents = new ArrayList<>();
                            JSON_Contents.add("--");
                        }

                        @Override
                        protected Boolean doInBackground(String... partUrl) {
                            HttpHandler sh = new HttpHandler();
                            String url = "http://" + VARIABLES.FLASK_IP_ADDRESS + ":5000/"
                                    + partUrl[0];
                            String jsonStr = sh.makeServiceCall(url);
                            Log.d(TAG, "Response from url: " + jsonStr);

                            if (jsonStr != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                    JSONArray contacts = jsonObj.getJSONArray("course_name");
//                                    String no_of_sem = jsonObj.getString("");
//                                    noOfSemester = Integer.parseInt(no_of_sem);

                                    JSONObject jsonObj2 = new JSONObject(jsonStr);
                                    JSONArray contacts2 = jsonObj2.getJSONArray("semester");



                                    for (int i = 0; i < contacts.length(); i++) {
                                        String cid = contacts.getString(i);
                                        String nos = contacts2.getString(i);
                                        int nos_ = Integer.parseInt(nos);
                                        JSON_Contents.add(cid);

                                        map.put(cid,nos_);


                                        Log.e(TAG, "STR:" + cid);
                                        //Log.e(TAG, "JSON_Contents: " + JSON_Contents.get(i));
                                    }

//                                    JSONObject jsonObj2 = new JSONObject(jsonStr);
//                                    JSONArray contacts2 = jsonObj2.getJSONArray("semester");

//                                    for (int i = 0; i < contacts2.length(); i++) {
//                                        String s = contacts2.getString(i);
//                                        JSON_Contents.add(s);
//                                        Log.e(TAG, "STR:" + s);
//                                        //Log.e(TAG, "JSON_Contents: " + JSON_Contents.get(i));
//                                    }


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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    JSON_Contents);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            programmeIDSpinner.setAdapter(adapter);


//                            for(int i=0;i<map.size();i++)
//                            {
//                                Log.e(TAG, "LIST OF MAP: "+map.get(JSON_Contents.get(i)));
//                            }


                            //populate semester also
                            //JSON_Contents.clear();
                        }
                    }.execute("auto_select/course?dept_name=" + itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        programmeIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //Log.e(TAG, "onItemSelected");
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                selectedProgrammeID = itemAtPosition;


                if(!itemAtPosition.equals("--"))
                {
                    int nos = map.get(itemAtPosition);
                    Log.e(TAG,"nos from map : " + nos);
                    Log.e(TAG, "sel Item=>" + itemAtPosition);

                    final ArrayList<String> nos_list = new ArrayList<>();
                    final ArrayList<String> section_list = new ArrayList<>();
                    final ArrayList<String> period_list = new ArrayList<>();

                    nos_list.add("--");
                    period_list.add("--");

                    section_list.add("--");
                    section_list.add("A");
                    section_list.add("B");
                    section_list.add("C");
                    section_list.add("D");



                    for(int i=1;i<=nos;i++)
                    {
                        nos_list.add(""+i);
                    }

                    for(int i=1;i<=8;i++)
                    {
                        period_list.add(""+i);
                    }

                    //populating course spinner
                    new AsyncTask<String, String, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            JSON_Contents = new ArrayList<>();
                            JSON_Contents.add("--");
                        }

                        @Override
                        protected Boolean doInBackground(String... partUrl) {
                            HttpHandler sh = new HttpHandler();
                            String url = "http://" + VARIABLES.FLASK_IP_ADDRESS + ":5000/"
                                    + partUrl[0];
                            String jsonStr = sh.makeServiceCall(url);
                            Log.d(TAG, "Response from url: " + jsonStr);

                            if (jsonStr != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                    JSONArray contacts = jsonObj.getJSONArray("subjects_name");
                                    for (int i = 0; i < contacts.length(); i++) {
                                        String s = contacts.getString(i);
                                        JSON_Contents.add(s);
                                        Log.e(TAG, "STR:" + s);
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    JSON_Contents);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            subjectIDSpinner.setAdapter(adapter);

                            //JSON_Contents.clear();


                            //pupulate semester spinner
                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    nos_list);

                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            semesterSpinner.setAdapter(adapter2);

                            //populate section spinner

                            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    section_list);

                            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sectionSpinner.setAdapter(adapter3);

                            //populate period list

                            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(
                                    getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                                    period_list);

                            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            periodSpinner.setAdapter(adapter4);


                        }
                    }.execute("auto_select/subject?programme_name=" + itemAtPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        subjectIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                //selectedProgrammeID = itemAtPosition;

                if( !itemAtPosition.equals("") )
                {
                    selectedSubjectID = itemAtPosition;

                    Log.e(TAG,"SUBJECT CHOSEN:" + itemAtPosition);

                    new AsyncTask<String, String, Boolean>() {

                        @Override
                        protected void onPreExecute() {
                            //setDefaultAdapters(5);
//                            JSON_Contents = new ArrayList<>();
//                            JSON_Contents.add("--");
                        }

                        @Override
                        protected Boolean doInBackground(String... partUrl) {
                            HttpHandler sh = new HttpHandler();
                            String url = "http://" + VARIABLES.FLASK_IP_ADDRESS + ":5000/"
                                    + partUrl[0];
                            String jsonStr = sh.makeServiceCall(url);
                            Log.d(TAG, "Response from url: " + jsonStr);

                            if (jsonStr != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject(jsonStr);
                                    //JSONArray contacts = jsonObj.getJSONArray("subjects_name");
//                                    for (int i = 0; i < contacts.length(); i++) {
//                                        String s = contacts.getString(i);
//                                        JSON_Contents.add(s);
//                                        Log.e(TAG, "STR:" + s);
//                                        //Log.e(TAG, "JSON_Contents: " + JSON_Contents.get(i));
//                                    }
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

                        }
                    }.execute("auto_select/subject_chosen?subject_name=" + itemAtPosition);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String itemAtPosition = (String) parent.getItemAtPosition(position);

                //selectedProgrammeID = itemAtPosition;

                if( !itemAtPosition.equals("--") )
                {
                    selectedSemester = itemAtPosition;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                //selectedProgrammeID = itemAtPosition;

                if( !itemAtPosition.equals("--") )
                {
                    selectedSection = itemAtPosition;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String itemAtPosition = (String) parent.getItemAtPosition(position);

                if( !itemAtPosition.equals("--") )
                {
                    selectedPeriod = itemAtPosition;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.attendanceReportFragment2);
            }
        });

        return rootView;
    }

    private Context getThisContext()
    {
        return this.getContext();
    }

    private void updateLabel() {
        String myFormat = "YYYY/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        String date = sdf.format(myCalendar.getTime());
        selectedDate = date;
        Log.d(TAG,date);
        datePopUpText.setText(sdf.format(myCalendar.getTime()));
        makeFinalUrl();
    }

    private void makeFinalUrl()
    {
        String TAG2 = "parameters";

        Log.e(TAG2,"selectedFacultyID: "+ selectedFacultyID);
        Log.e(TAG2,"selectedDeptID : "+selectedDeptID);
        Log.e(TAG2,"selectedProgrammeID : "+selectedProgrammeID);
        Log.e(TAG2,"selectedSubjectID : "+selectedSubjectID);
        Log.e(TAG2,"selectedSemester : "+selectedSemester);
        Log.e(TAG2,"selectedSection : "+selectedSection);
        Log.e(TAG2,"selectedPeriod : "+selectedPeriod);

        finalURL = "http://" + VARIABLES.FLASK_IP_ADDRESS + ":5000/_fetching_data_from_attendance" +
                "?semester="+selectedSemester+
                "&section="+selectedSection+"&period="+selectedPeriod+"&date=" + selectedDate;

        Log.e(TAG2,"FINAL URL:" + finalURL);

        VARIABLES.retriveFinalURL = finalURL;
    }

    private void setDefaultAdapters(int no)
    {
        ArrayList<String> defaultList = new ArrayList<>();
        defaultList.add("--");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getThisContext(), android.R.layout.simple_spinner_dropdown_item,
                defaultList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        switch (no)
        {
            case 0 :
                facultySpinner.setAdapter(adapter);
                departmentSpinner.setAdapter(adapter);
                programmeIDSpinner.setAdapter(adapter);
                subjectIDSpinner.setAdapter(adapter);
                semesterSpinner.setAdapter(adapter);
                sectionSpinner.setAdapter(adapter);
                periodSpinner.setAdapter(adapter);
            case 1 :
                departmentSpinner.setAdapter(adapter);
                programmeIDSpinner.setAdapter(adapter);
                subjectIDSpinner.setAdapter(adapter);
                semesterSpinner.setAdapter(adapter);
                sectionSpinner.setAdapter(adapter);
                periodSpinner.setAdapter(adapter);
                break;
            case 2 :
                programmeIDSpinner.setAdapter(adapter);
                subjectIDSpinner.setAdapter(adapter);
                semesterSpinner.setAdapter(adapter);
                sectionSpinner.setAdapter(adapter);
                periodSpinner.setAdapter(adapter);
                break;
            case 3 :
                subjectIDSpinner.setAdapter(adapter);
                semesterSpinner.setAdapter(adapter);
                sectionSpinner.setAdapter(adapter);
                periodSpinner.setAdapter(adapter);
                break;
        }
    }

}