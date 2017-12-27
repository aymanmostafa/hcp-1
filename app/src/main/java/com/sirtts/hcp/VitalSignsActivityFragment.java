package com.sirtts.hcp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class VitalSignsActivityFragment extends Fragment  implements View.OnClickListener{

    Button view_blood, save_blood, view_heart, save_heart, view_resp, save_resp, view_temp, save_temp,
            info_blood, info_heart, info_resp, info_temp, view_spo2, save_spo2, info_spo2;
    TextView date_blood, time_blood, date_heart, time_heart, date_resp, time_resp, date_temp,
            time_temp, sys_blood, date_spo2, time_spo2;
    TextView bpm_heart, bpm_resp, celsuis_temp, percentage_spo2;
    ProgressBar mProgressbar_blood, mProgressbar_heart, mProgressbar_resp,
            mProgressbar_temp, mProgressbar_spo2;
    SharedPreferences sharedPre;
    DatePickerDialog datePickerDialog;
    SeekBar seekBar_sys_blood, seekBar_dia_blood, seekBar_temp, seekBar_heart, seekBar_resp,
            seekBar_spo2;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "VitalSignsVolleyActivity";

    public VitalSignsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vital_signs, container, false);

        //blood
        view_blood = (Button) rootView.findViewById(R.id.bloodVital_Viewbtnid);
        save_blood = (Button) rootView.findViewById(R.id.bloodVital_savebtnid);
        info_blood = (Button) rootView.findViewById(R.id.bloodVital_Infobtnid);

        date_blood = (TextView) rootView.findViewById(R.id.bloodVital_datetvid);
        time_blood = (TextView) rootView.findViewById(R.id.bloodVital_timetvid);

        sys_blood = (TextView) rootView.findViewById(R.id.bloodVital_systxtid);

        seekBar_sys_blood = (SeekBar) rootView.findViewById(R.id.seekBarSys_bloodVital);
        seekBar_dia_blood = (SeekBar) rootView.findViewById(R.id.seekBarDia_bloodVital);


        mProgressbar_blood = (ProgressBar) rootView.findViewById(R.id.bloodVital_progressBar);

        view_blood.setOnClickListener(this);
        save_blood.setOnClickListener(this);
        info_blood.setOnClickListener(this);

        date_blood.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time_blood.setText(getCurrentDateAndTime("HH:mm"));

        date_blood.setOnClickListener(this);
        time_blood.setOnClickListener(this);

        //heart
        view_heart = (Button) rootView.findViewById(R.id.heartVital_Viewbtnid);
        save_heart = (Button) rootView.findViewById(R.id.heartVital_savebtnid);
        info_heart = (Button) rootView.findViewById(R.id.heartVital_Infobtnid);

        date_heart = (TextView) rootView.findViewById(R.id.heartVital_datetvid);
        time_heart = (TextView) rootView.findViewById(R.id.heartVital_timetvid);

        bpm_heart = (TextView) rootView.findViewById(R.id.heartVital_bpmtxtid);

        mProgressbar_heart = (ProgressBar) rootView.findViewById(R.id.heartVital_progressBar);


        seekBar_heart = (SeekBar) rootView.findViewById(R.id.seekBar_heartVital);

        view_heart.setOnClickListener(this);
        save_heart.setOnClickListener(this);
        info_heart.setOnClickListener(this);

        date_heart.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time_heart.setText(getCurrentDateAndTime("HH:mm"));

        date_heart.setOnClickListener(this);
        time_heart.setOnClickListener(this);

        //resp
        view_resp = (Button) rootView.findViewById(R.id.respVital_Viewbtnid);
        save_resp = (Button) rootView.findViewById(R.id.respVital_savebtnid);
        info_resp = (Button) rootView.findViewById(R.id.respVital_Infobtnid);

        date_resp = (TextView) rootView.findViewById(R.id.respVital_datetvid);
        time_resp = (TextView) rootView.findViewById(R.id.respVital_timetvid);

        bpm_resp = (TextView) rootView.findViewById(R.id.respVital_bpmtxtid);

        mProgressbar_resp = (ProgressBar) rootView.findViewById(R.id.respVital_progressBar);

        seekBar_resp = (SeekBar) rootView.findViewById(R.id.seekBar_respVital);

        view_resp.setOnClickListener(this);
        save_resp.setOnClickListener(this);
        info_resp.setOnClickListener(this);

        date_resp.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time_resp.setText(getCurrentDateAndTime("HH:mm"));

        date_resp.setOnClickListener(this);
        time_resp.setOnClickListener(this);

        //temp
        view_temp = (Button) rootView.findViewById(R.id.tempVital_Viewbtnid);
        save_temp = (Button) rootView.findViewById(R.id.tempVital_savebtnid);
        info_temp = (Button) rootView.findViewById(R.id.tempVital_Infobtnid);

        date_temp = (TextView) rootView.findViewById(R.id.tempVital_datetvid);
        time_temp = (TextView) rootView.findViewById(R.id.tempVital_timetvid);

        celsuis_temp = (TextView) rootView.findViewById(R.id.tempVital_celsiustxtid);

        mProgressbar_temp = (ProgressBar) rootView.findViewById(R.id.tempVital_progressBar);

        seekBar_temp = (SeekBar) rootView.findViewById(R.id.seekBar_tempVital);

        view_temp.setOnClickListener(this);
        save_temp.setOnClickListener(this);
        info_temp.setOnClickListener(this);

        date_temp.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time_temp.setText(getCurrentDateAndTime("HH:mm"));

        date_temp.setOnClickListener(this);
        time_temp.setOnClickListener(this);

        //spo2
        view_spo2 = (Button) rootView.findViewById(R.id.spo2Vital_Viewbtnid);
        save_spo2 = (Button) rootView.findViewById(R.id.spo2Vital_savebtnid);
        info_spo2 = (Button) rootView.findViewById(R.id.spo2Vital_Infobtnid);

        date_spo2 = (TextView) rootView.findViewById(R.id.spo2Vital_datetvid);
        time_spo2 = (TextView) rootView.findViewById(R.id.spo2Vital_timetvid);

        percentage_spo2 = (TextView) rootView.findViewById(R.id.spo2Vital_percentagetxtid);

        mProgressbar_spo2 = (ProgressBar) rootView.findViewById(R.id.spo2Vital_progressBar);

        seekBar_spo2 = (SeekBar) rootView.findViewById(R.id.seekBar_spo2Vital);

        view_spo2.setOnClickListener(this);
        save_spo2.setOnClickListener(this);
        info_spo2.setOnClickListener(this);

        date_spo2.setText(getCurrentDateAndTime("yyyy-MM-dd"));
        time_spo2.setText(getCurrentDateAndTime("HH:mm"));

        date_spo2.setOnClickListener(this);
        time_spo2.setOnClickListener(this);


        sys_blood.setText(seekBar_sys_blood.getProgress() + " / " + seekBar_sys_blood.getProgress());

        this.seekBar_spo2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                sys_blood.setText(seekBar_sys_blood.getProgress() + " / " + progressValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.seekBar_sys_blood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                sys_blood.setText(progressValue + " / " + seekBar_dia_blood.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        celsuis_temp.setText(seekBar_temp.getProgress()/10 + " Celsius");

        this.seekBar_temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                celsuis_temp.setText((float) progressValue/10 + " Celsius");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bpm_heart.setText(seekBar_heart.getProgress() + " bpm");

        this.seekBar_heart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                bpm_heart.setText(progressValue + " bpm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bpm_resp.setText(seekBar_resp.getProgress() + " bpm");

        this.seekBar_resp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                bpm_resp.setText(progressValue + " bpm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        percentage_spo2.setText(seekBar_spo2.getProgress() + " %");

        this.seekBar_spo2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                percentage_spo2.setText(progressValue + " %");
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
    public void onClick(View v) {
        if(v == date_blood || v == date_heart || v == date_temp || v == date_resp || v == date_spo2){
            datePicker((TextView) v);
        }
        else if(v == time_blood || v == time_heart || v == time_temp || v == time_resp || v == time_spo2){
            timePacker((TextView) v);
        }
        else if(v == save_blood){
            if(date_blood.getText().toString().equals("")){
                date_blood.requestFocus();
                date_blood.setError("Enter The Date");
            }
            else if(time_blood.getText().toString().equals("")){
                time_blood.requestFocus();
                time_blood.setError("Enter The Time");
            }
            else if(sys_blood.getText().toString().equals("")){
                sys_blood.requestFocus();
                sys_blood.setError("Enter Your Systolic");
            }
            else {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                HashMap m = new HashMap();

                m.put(getString(R.string.api_send_json_vital_userId), sharedPre.getInt(getString(R.string.shared_userId), 0));
                m.put(getString(R.string.api_send_json_bloodVital_date), date_blood.getText().toString());
                m.put(getString(R.string.api_send_json_bloodVital_time), time_blood.getText().toString());

                m.put(getString(R.string.api_send_json_bloodVital_sys), seekBar_sys_blood.getProgress());
                m.put(getString(R.string.api_send_json_bloodVital_dia), seekBar_dia_blood.getProgress());

                requestTheRequest(mProgressbar_blood, getString(R.string.api_url_bloodVital),
                        new JSONObject(m));
            }
        }
        else if(v == save_heart){
            if(date_heart.getText().toString().equals("")){
                date_heart.requestFocus();
                date_heart.setError("Enter The Date");
            }
            else if(time_heart.getText().toString().equals("")){
                time_heart.requestFocus();
                time_heart.setError("Enter The Time");
            }
            else if(bpm_heart.getText().toString().equals("")){
                bpm_heart.requestFocus();
                bpm_heart.setError("Enter Your BPM");
            }
            else {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                HashMap m = new HashMap();

                m.put(getString(R.string.api_send_json_vital_userId), sharedPre.getInt(getString(R.string.shared_userId), 0));
                m.put(getString(R.string.api_send_json_heartVital_date), date_heart.getText().toString());
                m.put(getString(R.string.api_send_json_heartVital_time), time_heart.getText().toString());

                m.put(getString(R.string.api_send_json_heartVital_cel), seekBar_heart.getProgress());

                requestTheRequest(mProgressbar_heart, getString(R.string.api_url_heartVital),
                        new JSONObject(m));
            }
        }
        else if(v == save_resp){
            if(date_resp.getText().toString().equals("")){
                date_resp.requestFocus();
                date_resp.setError("Enter The Date");
            }
            else if(time_resp.getText().toString().equals("")){
                time_resp.requestFocus();
                time_resp.setError("Enter The Time");
            }
            else if(bpm_resp.getText().toString().equals("")){
                bpm_resp.requestFocus();
                bpm_resp.setError("Enter Your Breaths/Min.");
            }
            else {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                HashMap m = new HashMap();

                m.put(getString(R.string.api_send_json_vital_userId), sharedPre.getInt(getString(R.string.shared_userId), 0));
                m.put(getString(R.string.api_send_json_respVital_date), date_resp.getText().toString());
                m.put(getString(R.string.api_send_json_respVital_time), time_resp.getText().toString());

                m.put(getString(R.string.api_send_json_respVital_cel), seekBar_resp.getProgress());

                requestTheRequest(mProgressbar_resp, getString(R.string.api_url_respVital),
                        new JSONObject(m));
            }
        }
        else if(v == save_temp){
            if(date_temp.getText().toString().equals("")){
                date_temp.requestFocus();
                date_temp.setError("Enter The Date");
            }
            else if(time_temp.getText().toString().equals("")){
                time_temp.requestFocus();
                time_temp.setError("Enter The Time");
            }
            else if(celsuis_temp.getText().toString().equals("")){
                celsuis_temp.requestFocus();
                celsuis_temp.setError("Enter Your Temperature");
            }
            else {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                HashMap m = new HashMap();

                m.put(getString(R.string.api_send_json_vital_userId), sharedPre.getInt(getString(R.string.shared_userId), 0));
                m.put(getString(R.string.api_send_json_tempVital_date), date_temp.getText().toString());
                m.put(getString(R.string.api_send_json_tempVital_time), time_temp.getText().toString());

                m.put(getString(R.string.api_send_json_tempVital_cel), (float)seekBar_temp.getProgress()/10);

                requestTheRequest(mProgressbar_temp, getString(R.string.api_url_tempVital),
                        new JSONObject(m));
            }
        }
        else if(v == save_spo2){
            if(date_spo2.getText().toString().equals("")){
                date_spo2.requestFocus();
                date_spo2.setError("Enter The Date");
            }
            else if(time_spo2.getText().toString().equals("")){
                time_spo2.requestFocus();
                time_spo2.setError("Enter The Time");
            }
            else if(percentage_spo2.getText().toString().equals("")){
                percentage_spo2.requestFocus();
                percentage_spo2.setError("Enter Your spo2");
            }
            else {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

                HashMap m = new HashMap();

                m.put(getString(R.string.api_send_json_vital_userId), sharedPre.getInt(getString(R.string.shared_userId), 0));
                m.put(getString(R.string.api_send_json_spo2Vital_date), date_spo2.getText().toString());
                m.put(getString(R.string.api_send_json_spo2Vital_time), time_spo2.getText().toString());

                m.put(getString(R.string.api_send_json_spo2Vital_cel), seekBar_spo2.getProgress());

                requestTheRequest(mProgressbar_spo2, getString(R.string.api_url_spo2Vital),
                        new JSONObject(m));
            }
        }
        else if(v == view_blood){
            startActivity(new Intent(getContext(), ListBloodVitalSignsActivity.class));
        }
        else if(v == view_heart){
            startActivity(new Intent(getContext(), ListheartVitalSignsActivity.class));
        }
        else if(v == view_resp){
            startActivity(new Intent(getContext(), ListrespVitalSignsActivity.class));
        }
        else if(v == view_temp){
            startActivity(new Intent(getContext(), ListtempVitalSignsActivity.class));
        }
        else if(v == view_spo2){
            startActivity(new Intent(getContext(), Listspo2VitalSignsActivity.class));
        }
        else if(v == info_blood){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setTitle("Blood Pressure");
            alertBuilder.setMessage("When your heart beats, it squeezes and pushes blood " +
                    "through your arteries to the rest of your body. This force creates" +
                    " pressure on those blood vessels, and that's your systolic blood" +
                    " pressure.  A normal systolic pressure is below 120. " +
                    " A reading of 140 or more is high blood pressure");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
        else if(v == info_heart){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setTitle("Heart Rate");
            alertBuilder.setMessage("Heart rate is the speed of the heartbeat measured by the" +
                    " number of contractions of the heart per minute (bpm). The heart rate can " +
                    "vary according to the body's physical needs, including the need to absorb" +
                    " oxygen and excrete carbon dioxide. It is usually equal or close to the " +
                    "pulse measured at any peripheral point.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
        else if(v == info_temp){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setTitle("Body Temperature");
            alertBuilder.setMessage("The normal human body temperature range is " +
                    "typically stated as 36.5–37.5 °C .Individual body temperature " +
                    "depends upon the age, exertion, infection, sex, time of day, and " +
                    "reproductive status of the subject, the place in the body at which " +
                    "the measurement is made, the subject's state of consciousness " +
                    "(waking or sleeping), activity level, and emotional state.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
        else if(v == info_resp){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setTitle("Respiratary Rate");
            alertBuilder.setMessage("The respiratory rate in humans is measured when a person is " +
                    "at rest and involves counting the number of breaths for one minute by " +
                    "counting how many times the chest rises. An optical breath rate sensor " +
                    "can be used for monitoring patients during a magnetic resonance imaging " +
                    "scan. Respiration rates may increase with fever, illness, " +
                    "or other medical conditions.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
        else if(v == info_spo2){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

            alertBuilder.setTitle("SpO2");
            alertBuilder.setMessage("SpO2 is an estimate of arterial oxygen saturation, or SaO2, " +
                    "which refers to the amount of oxygenated haemoglobin in the blood.\n" +
                    "\n" +
                    "Haemoglobin is a protein that carries oxygen in the blood. It is found " +
                    "inside red blood cells and gives them their red colour.");

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                .getRequestQueue();

    }

    @Override
    public void onStop() {
        super.onStop();
        //mProgressbar.setVisibility(View.INVISIBLE);
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public String getCurrentDateAndTime(String format)
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public void datePicker(final TextView date){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date.setText(year +"-" + (monthOfYear + 1) + "-" +dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void timePacker(final TextView time){
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

    public void requestTheRequest(final ProgressBar mProgressbar, String URL, JSONObject data){
        mProgressbar.setVisibility(View.VISIBLE);

        Log.e("VitalSend-->", "sendData:"+(data).toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URL, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Boolean userStatus = ((JSONObject) response).optBoolean(getString(R.string.api_receive_json_status));

                            if (userStatus) {
                                Toast.makeText(getActivity(), "Data Saved!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Unexpected Error happened!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                        catch(Exception e){
                            mProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Unexpected Error happened!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError errork) {
                        mProgressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Unexpected Error happened!",
                                Toast.LENGTH_LONG).show();
                    }
                });

        jsonRequest.setTag(REQUEST_TAG);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(jsonRequest);
    }
}
