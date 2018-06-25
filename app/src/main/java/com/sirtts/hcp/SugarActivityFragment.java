package com.sirtts.hcp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class SugarActivityFragment extends Fragment implements View.OnClickListener{

    Button view,save,info;
    TextView date,time,error,date2,time2,mg;
    ProgressBar mProgressbar;
    SharedPreferences sharedPre;
    DatePickerDialog datePickerDialog;
    SeekBar seekBar;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "savesugarVolleyActivity";

    public SugarActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sugar, container, false);


        view = (Button) rootView.findViewById(R.id.sugar_Viewbtnid);
        save = (Button) rootView.findViewById(R.id.sugar_savebtnid);
        info = (Button) rootView.findViewById(R.id.sugar_Infobtnid);

        date = (TextView) rootView.findViewById(R.id.sugar_datetvid);
        time = (TextView) rootView.findViewById(R.id.sugar_timetvid);
        date2 = (TextView) rootView.findViewById(R.id.dentist_date2tvid);
        time2 = (TextView) rootView.findViewById(R.id.sugar_time2tvid);
        error = (TextView) rootView.findViewById(R.id.sugar_error);

        mg = (TextView) rootView.findViewById(R.id.sugar_mgtxtid);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.sugar_progressBar);

        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar_sugar);

        view.setOnClickListener(this);
        save.setOnClickListener(this);
        info.setOnClickListener(this);

        date.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time.setText(getCurrentDateAndTime("HH:mm"));

        date.setOnClickListener(this);
        time.setOnClickListener(this);

        date2.setOnClickListener(this);
        time2.setOnClickListener(this);

        this.mg.setText((seekBar.getProgress()+20) + " mg/dl");

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                mg.setText((progressValue+20) + " mg/dl");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Instantiate the RequestQueue.
        mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                .getRequestQueue();
    }

    @Override
    public void onStop() {
        super.onStop();
        mProgressbar.setVisibility(View.INVISIBLE);
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == view){
            startActivity(new Intent(getContext(), ListsugarActivity.class));
        }
        else if(v == date || v == date2){
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String month = "", day = "";
                            if(monthOfYear + 1 < 10) month = "0";
                            if(dayOfMonth < 10) day = "0";
                            date.setText(year +"-"+ month + (monthOfYear + 1) + "-"+day +dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
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
            }, hour, minute, true);
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
            else if(mg.getText().toString().equals("")){
                error.setText("Enter Your mg/dL");
            }
            else{
                error.setText("");
                if (!isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                else {
                    sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                    mProgressbar.setVisibility(View.VISIBLE);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_url_sugar),
                            sendData(date.getText().toString(),time.getText().toString(),
                                    (seekBar.getProgress()+20)),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getActivity(), "Data Saved!",
                                                Toast.LENGTH_LONG).show();
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
                            }){
                        //Send the token with the request
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String,String>();
                            headers.put(getString(R.string.api_send_json_auth_header),
                                    sharedPre.getString(getString(R.string.api_receive_json_login_idToken),""));
                            return headers;
                        }
                    };

                    jsonRequest.setTag(REQUEST_TAG);
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

            alertBuilder.setTitle("Blood Sugar");
            alertBuilder.setMessage("commonly referred to as diabetes, is a group of metabolic" +
                    " disorders in which there are high blood sugar levels over a prolonged " +
                    "period. Symptoms of high blood sugar include frequent urination, " +
                    "increased thirst, and increased hunger.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    public JSONObject sendData(String date,String time,int bpm){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_sugar_date),date+"T"+time+":00");
        m.put(getString(R.string.api_send_json_sugar_mg),bpm);
        Log.e(REQUEST_TAG, "sendData:"+(new JSONObject(m)).toString());
        return new JSONObject(m);
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

}

