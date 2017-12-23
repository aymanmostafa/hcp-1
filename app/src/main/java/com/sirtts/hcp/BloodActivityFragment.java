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
public class BloodActivityFragment extends Fragment implements View.OnClickListener,  Response.Listener<JSONArray>,
        Response.ErrorListener {

    TextView tempTv,date,date2,time,time2,error,bloodtv,info;
    Button save,view;
    EditText tempEt;
    LinearLayout tempLayout,mainListLayout;
    private RequestQueue mQueue;
    ProgressBar mProgressbar;
    DatePickerDialog datePicker;
    SharedPreferences sharedPref;
    public static final String REQUEST_TAG_View = "ViewBloodVolley";
    public static final String REQUEST_TAG_Save = "SaveBloodVolley";
    public BloodActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_blood, container, false);


        mainListLayout = (LinearLayout) rootView.findViewById(R.id.blood_linearLayout);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.blood_progressBar);

        save = (Button) rootView.findViewById(R.id.blood_savebtnid);
        view = (Button) rootView.findViewById(R.id.blood_viewbtnid);
        info = (Button) rootView.findViewById(R.id.blood_Infobtnid);

        date = (TextView) rootView.findViewById(R.id.blood_datetvid);
        date2 = (TextView) rootView.findViewById(R.id.blood_date2tvid);
        time = (TextView) rootView.findViewById(R.id.blood_timetvid);
        time2 = (TextView) rootView.findViewById(R.id.blood_time2tvid);
        error = (TextView) rootView.findViewById(R.id.blood_error);
        bloodtv = (TextView) rootView.findViewById(R.id.blood_bloodTv);

        save.setOnClickListener(this);
        view.setOnClickListener(this);
        info.setOnClickListener(this);

        date.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time.setText(getCurrentDateAndTime("HH:mm"));

        date.setOnClickListener(this);
        time.setOnClickListener(this);

        date2.setOnClickListener(this);
        time2.setOnClickListener(this);

        setHiddenFields(View.INVISIBLE);

        if (isNetworkAvailable(getContext())) {

            mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                    .getRequestQueue();
            final JSONArrayRequest jsonRequest = new JSONArrayRequest(Request.Method
                    .GET, getString(R.string.api_url_blood_columns),
                    new JSONObject(), this, this);
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
        }
    }

    @Override
    public void onResponse(JSONArray response) {
        try {
            mProgressbar.setVisibility(View.INVISIBLE);
            setHiddenFields(View.VISIBLE);
            for(int i=0;i<response.length();i++){
                tempLayout = new LinearLayout(getContext());
                tempEt = new EditText(getContext());
                tempTv = new TextView(getContext());

                LinearLayout.LayoutParams tt = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                tempLayout.setLayoutParams(tt);

                tempLayout.setOrientation(LinearLayout.HORIZONTAL);

                tempEt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,3.0f));

                tempTv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));

                tempTv.setText(String.valueOf(response.optJSONObject(i).optString(getString(R.string.api_receive_json_blood_fields))));
                tempEt.setText("");
                tempTv.setTypeface(null, Typeface.BOLD);

                tempLayout.addView(tempTv);
                tempLayout.addView(tempEt);

                mainListLayout.addView(tempLayout);
            }
        }
        catch(Exception e){
            mProgressbar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressbar.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), "Unexpected Error happened!", Toast.LENGTH_SHORT).show();
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
        bloodtv.setVisibility(con);
    }
    @Override
    public void onClick(View v) {
        if(v == view){
            startActivity(new Intent(getContext(), ListBloodActivity.class));
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
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_url_blood_set),
                            sendData(sharedPref.getInt(getString(R.string.shared_userId),0), date.getText().toString(),time.getText().toString(),
                                    mainListLayout),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        Boolean userStatus = response.optBoolean(getString(R.string.api_receive_json_status));

                                        if (userStatus) {
                                            Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_LONG).show();
                                        } else {
                                            error.setText("Unexpected Error happened!");
                                        }
                                    }
                                    catch(Exception e){
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        error.setText("Unexpected Error happened!");
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError errork) {
                                    mProgressbar.setVisibility(View.INVISIBLE);
                                    error.setText("Unexpected Error happened!");
                                }
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

            alertBuilder.setMessage("Blood tests are an essential diagnostic tool. Blood is made" +
                    " up of different kinds of cells and contains other compounds, including " +
                    "various salts and certain proteins. Blood tests reveal details about " +
                    "these Blood cells and, Blood compounds, salts and proteins.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    public JSONObject sendData(int userid, String date, String time, LinearLayout ll){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_blood_userId),userid);
        m.put(getString(R.string.api_send_json_blood_date),date);
        m.put(getString(R.string.api_send_json_blood_time),time);
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
                if(v instanceof EditText) {
                    value.setLength(0);
                    value.append(((EditText) v).getText().toString());
                }
                else if(v instanceof TextView) {
                    key.setLength(0);
                    key.append(((TextView) v).getText().toString());
                }
            }
            if(!value.toString().equals("")) m.put(key.toString(),value.toString());
        }
        Log.e("Send blood Data", "sendData:"+(new JSONObject(m)).toString());
        return new JSONObject(m);
    }
}
