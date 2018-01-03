package com.sirtts.hcp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DentistVisitsActivityFragment extends Fragment implements View.OnClickListener{

    TextView tempTv,date,date2,time,time2,error,dentisttv,info,nextView;
    Button save,view,next;
    CheckBox tempEt;
    EditText notes;
    LinearLayout tempLayout,mainListLayout;
    private RequestQueue mQueue;
    ProgressBar mProgressbar,nextProgressbar;
    DatePickerDialog datePicker;
    SharedPreferences sharedPref;
    public static final String REQUEST_TAG_View = "ViewDentistVolley";
    public static final String REQUEST_TAG_Save = "SaveDentistVolley";
    public static final String REQUEST_TAG_Save_next = "SaveNextDentistVolley";
    public DentistVisitsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_dentist_visits, container, false);


        mainListLayout = (LinearLayout) rootView.findViewById(R.id.dentist_linearLayout);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.dentist_progressBar);
        nextProgressbar = (ProgressBar) rootView.findViewById(R.id.dentist_nextProgressBar);

        save = (Button) rootView.findViewById(R.id.dentist_savebtnid);
        view = (Button) rootView.findViewById(R.id.dentist_viewbtnid);
        info = (Button) rootView.findViewById(R.id.dentist_Infobtnid);
        next = (Button) rootView.findViewById(R.id.dentist_nextbtn);

        date = (TextView) rootView.findViewById(R.id.dentist_datetvid);
        date2 = (TextView) rootView.findViewById(R.id.dentist_date2tvid);
        time = (TextView) rootView.findViewById(R.id.dentist_timetvid);
        time2 = (TextView) rootView.findViewById(R.id.dentist_time2tvid);
        error = (TextView) rootView.findViewById(R.id.dentist_error);
        dentisttv = (TextView) rootView.findViewById(R.id.dentist_dentistTv);
        nextView = (TextView) rootView.findViewById(R.id.dentist_nextView);

        notes = (EditText) rootView.findViewById(R.id.dentist_notesid);

        save.setOnClickListener(this);
        view.setOnClickListener(this);
        info.setOnClickListener(this);
        next.setOnClickListener(this);

        date.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time.setText(getCurrentDateAndTime("HH:mm"));

        date.setOnClickListener(this);
        time.setOnClickListener(this);

        date2.setOnClickListener(this);
        time2.setOnClickListener(this);

        setHiddenFields(View.INVISIBLE);

        if (isNetworkAvailable(getContext())) {

            sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

            mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                    .getRequestQueue();
            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method
                    .POST, getString(R.string.api_url_dentist_columns),
                    sendDataNew(sharedPref.getInt(getString(R.string.shared_userId),0)),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mProgressbar.setVisibility(View.INVISIBLE);
                                setHiddenFields(View.VISIBLE);
                                Log.e("qqqqqqqqq",response.toString());
                                for(int i=0;i<response.length();i++){
                                    if(!response.optJSONObject(i).optString(getString(R.string.api_receive_json_dentist_fields)).equals("null")) {
                                        tempLayout = new LinearLayout(getContext());
                                        tempEt = new CheckBox(getContext());
                                        tempTv = new TextView(getContext());

                                        LinearLayout.LayoutParams tt = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.MATCH_PARENT);

                                        tempLayout.setLayoutParams(tt);

                                        tempLayout.setOrientation(LinearLayout.HORIZONTAL);

                                        tempEt.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT, 5.0f));

                                        tempTv.setLayoutParams(new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

                                        tempTv.setText(String.valueOf(response.optJSONObject(i).optString(getString(R.string.api_receive_json_dentist_fields))));
                                        tempTv.setTypeface(null, Typeface.BOLD);

                                        tempLayout.addView(tempTv);
                                        tempLayout.addView(tempEt);

                                        mainListLayout.addView(tempLayout);
                                    }
                                    else if(!response.optJSONObject(i).optString(getString(R.string.api_receive_json_dentist_date)).equals("null")) {
                                        nextView.setText("Next Visit:    " + response.optJSONObject(i).optString(getString(R.string.api_receive_json_dentist_date)));
                                    }
                                }
                            }
                            catch(Exception e){
                                mProgressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError errork) {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                        }
                    });
            jsonRequest.setTag(REQUEST_TAG_View);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(jsonRequest);
        }
        else Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();

        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        mProgressbar.setVisibility(View.INVISIBLE);
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG_View);
            mQueue.cancelAll(REQUEST_TAG_Save);
            mQueue.cancelAll(REQUEST_TAG_Save_next);
        }
    }


    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String getCurrentDateAndTime(String format)
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public void setHiddenFields(int con){
        save.setVisibility(con);
        date.setVisibility(con);
        date2.setVisibility(con);
        time.setVisibility(con);
        time2.setVisibility(con);
        dentisttv.setVisibility(con);
        notes.setVisibility(con);
    }
    @Override
    public void onClick(View v) {
        if(v == view){
            startActivity(new Intent(getContext(), ListDentistVisitsActivity.class));
        }
        else if(v == date || v == date2){
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePicker = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            date.setText(year +"-" + (monthOfYear + 1) + "-" +dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePicker.getDatePicker().setMaxDate(new Date().getTime());
            datePicker.show();
        }
        else if(v == time || v == time2){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    time.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }
        else if(v == next){
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePicker = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, final int year,
                                              final int monthOfYear, final int dayOfMonth) {
                            if (!isNetworkAvailable(getContext()))
                                Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                            else {
                                sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                                nextProgressbar.setVisibility(View.VISIBLE);
                                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_url_dentist_update_next_visit),
                                        sendDataForNextVisit(sharedPref.getInt(getString(R.string.shared_userId),0),
                                                year +"-" + (monthOfYear + 1) + "-" +dayOfMonth),
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    nextProgressbar.setVisibility(View.INVISIBLE);
                                                    Boolean userStatus = response.optBoolean(getString(R.string.api_receive_json_status));

                                                    if (userStatus) {
                                                        Toast.makeText(getActivity(), "Data Updated!", Toast.LENGTH_LONG).show();
                                                        nextView.setText("Next Visit:    "+year +"-" + (monthOfYear + 1) + "-" +dayOfMonth);
                                                    } else {
                                                        Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                catch(Exception e){
                                                    nextProgressbar.setVisibility(View.INVISIBLE);Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError errork) {
                                                nextProgressbar.setVisibility(View.INVISIBLE);Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                jsonRequest.setTag(REQUEST_TAG_Save_next);
                                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        0,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                mQueue.add(jsonRequest);
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePicker.getDatePicker().setMinDate(new Date().getTime());
            datePicker.show();
        }
        else if(v == save){
            if(date.getText().toString().equals("")){
                error.setText("Enter The Date");
            }
            else if(time.getText().toString().equals("")){
                error.setText("Enter The Time");
            }
            else{
                error.setText("");
                if (!isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                else {
                    sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                    mProgressbar.setVisibility(View.VISIBLE);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_url_dentist_set),
                            sendData(sharedPref.getInt(getString(R.string.shared_userId),0), date.getText().toString(),time.getText().toString(),
                                    notes.getText().toString(), mainListLayout),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        Boolean userStatus = response.optBoolean(getString(R.string.api_receive_json_status));

                                        if (userStatus) {
                                            Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch(Exception e){
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError errork) {
                                    mProgressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();                                }
                            });


                    jsonRequest.setTag(REQUEST_TAG_Save);
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(jsonRequest);
                }
            }
        }
        else if(v == info){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setMessage("Record your dental treatments such as teeth cleaning, " +
                    "whitening,  restoration, crowns, bridges, braces, endodontic therapy, " +
                    "periodontal therapy, extraction and oral surgery. As well as examinations, " +
                    "radiographs (x-rays) and diagnosis.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    public JSONObject sendData(int userid, String date, String time,String notes, LinearLayout ll){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_dentist_userId),userid);
        m.put(getString(R.string.api_send_json_dentist_date),date);
        m.put(getString(R.string.api_send_json_dentist_time),time);
        m.put(getString(R.string.api_send_json_dentist_notes),notes);
        final int childCount = ll.getChildCount();
        int childChildCount;
        LinearLayout vll ;
        View v;
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        for(int i=0; i< childCount ; i++){
            vll = (LinearLayout) ll.getChildAt(i);
            childChildCount = vll.getChildCount();
            for(int k =0;k< childChildCount ; k++){
                v = vll.getChildAt(k);
                if(v instanceof CheckBox) {
                    value.setLength(0);
                    value.append((((CheckBox) v).isChecked())? 1:0);
                }
                else if(v instanceof TextView) {
                    key.setLength(0);
                    key.append(((TextView) v).getText().toString());
                }
            }
            if(!value.toString().equals("")) m.put(key.toString(),value.toString());
        }
        Log.e("Send dentist Data", "sendData:"+(new JSONObject(m)).toString());
        return new JSONObject(m);
    }

    public JSONObject sendDataForNextVisit(int userid,String date){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_dentist_userId),userid);
        if(!date.isEmpty()) m.put(getString(R.string.api_send_json_dentist_date),date);
        Log.e("SaveNextDentistVolley", "sendData:"+(new JSONObject(m)).toString());
        return new JSONObject(m);
    }

    public JSONArray sendDataNew(int userid){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_dentist_userId),userid);
        JSONArray x = new JSONArray();
        x.put(new JSONObject(m));
        Log.e("Send dentist Data", "sendData: "+x.toString());
        return x;
    }
}
