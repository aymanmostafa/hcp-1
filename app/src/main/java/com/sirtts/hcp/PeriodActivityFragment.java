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
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PeriodActivityFragment extends Fragment implements View.OnClickListener{

    Button view, save,info;
    TextView startdate, starttime, error, enddate, endtime;
    ProgressBar mProgressbar;
    SharedPreferences sharedPre;
    DatePickerDialog datePickerDialog;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "savePeriodVolleyActivity";

    public PeriodActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_period, container, false);

        view = (Button) rootView.findViewById(R.id.period_Viewbtnid);
        save = (Button) rootView.findViewById(R.id.period_savebtnid);
        info = (Button) rootView.findViewById(R.id.period_Infobtnid);

        startdate = (TextView) rootView.findViewById(R.id.period_startdatetvid);
        starttime = (TextView) rootView.findViewById(R.id.period_starttimetvid);
        enddate = (TextView) rootView.findViewById(R.id.period_enddatetvid);
        endtime = (TextView) rootView.findViewById(R.id.period_endtimetvid);
        error = (TextView) rootView.findViewById(R.id.period_error);


        mProgressbar = (ProgressBar) rootView.findViewById(R.id.period_progressBar);

        view.setOnClickListener(this);
        save.setOnClickListener(this);
        info.setOnClickListener(this);

        startdate.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        starttime.setText(getCurrentDateAndTime("HH:mm"));

        enddate.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        endtime.setText(getCurrentDateAndTime("HH:mm"));

        startdate.setOnClickListener(this);
        starttime.setOnClickListener(this);

        enddate.setOnClickListener(this);
        endtime.setOnClickListener(this);

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
        if (v == view) {
            startActivity(new Intent(getContext(), ListperiodActivity.class));
        } else if (v == startdate) {
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
                            startdate.setText(year +"-"+ month + (monthOfYear + 1) + "-"+day +dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        } else if (v == enddate) {
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
                            enddate.setText(year +"-"+ month + (monthOfYear + 1) + "-"+day +dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        } else if (v == starttime) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    starttime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } else if (v == endtime) {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    endtime.setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } else if (v == save) {
            long duration = 0;
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                duration = TimeUnit.MILLISECONDS.toDays((dateFormat.parse(enddate.getText()
                        .toString())).getTime() - (dateFormat.parse(startdate.getText()
                        .toString())).getTime());

            }catch(ParseException e){
                error.setText("Unexpected Error happened!");
            }
            if (startdate.getText().toString().equals("")) {
                error.setText("Enter The Start Date");
            } else if (starttime.getText().toString().equals("")) {
                error.setText("Enter The Start Time");
            } else if (enddate.getText().toString().equals("")) {
                error.setText("Enter The End Date");
            } else if (endtime.getText().toString().equals("")) {
                error.setText("Enter The End Time");
            } else if (duration < 1) {
                error.setText("Period start should be earlier than end!");
            } else {
                error.setText("");
                if (!isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                else {
                    sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                    mProgressbar.setVisibility(View.VISIBLE);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                            getString(R.string.api_url_period),
                            sendData(startdate.getText().toString(), starttime.getText().toString(),
                                    enddate.getText().toString(), endtime.getText().toString()),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(), "Data Saved!",
                                                    Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
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
                            }){ //Send the token with the request
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

            alertBuilder.setTitle("Menstrual cycle");
            alertBuilder.setMessage("The menstrual cycle is the regular natural change that " +
                    "occurs in the female reproductive system (specifically the uterus and " +
                    "ovaries) that makes pregnancy possible. The cycle is required for the " +
                    "production of ovocytes, and for the preparation of the uterus for" +
                    " pregnancy. Up to 80% of women report having some symptoms during " +
                    "the one to two weeks prior to menstruation.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    public JSONObject sendData(String startdate, String starttime,
                               String enddate, String endtime) {
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_period_startdate), startdate+"T"+starttime+":00");
        m.put(getString(R.string.api_send_json_period_enddate), enddate+"T"+endtime+":00");
        Log.e("saveperiodVoll", "sendData:" + (new JSONObject(m)).toString());
        return new JSONObject(m);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String getCurrentDateAndTime(String format) {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }
}

