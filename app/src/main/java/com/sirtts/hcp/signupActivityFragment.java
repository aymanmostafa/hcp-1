package com.sirtts.hcp;

import android.app.DatePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class signupActivityFragment extends Fragment implements View.OnClickListener{

    EditText username,password,repassword;
    Button signup;
    TextView error,date;
    RadioGroup genderGroup;
    RadioButton gender;
    ProgressBar mProgressbar;
    DatePickerDialog datePickerDialog;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "signupVolleyActivity";

    public signupActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        username = (EditText) rootView.findViewById(R.id.signup_usernametxtid);
        password = (EditText) rootView.findViewById(R.id.signup_passwordtxtid);
        repassword = (EditText) rootView.findViewById(R.id.signup_repasswordtxtid);
        date = (TextView) rootView.findViewById(R.id.signup_datebirthtxtid);

        signup = (Button) rootView.findViewById(R.id.signup_signupbtnid);

        error = (TextView) rootView.findViewById(R.id.signup_errortvid);

        genderGroup = (RadioGroup) rootView.findViewById(R.id.signup_gendergroup);

        signup.setOnClickListener(this);

        date.setOnClickListener(this);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.signup_progressBar);

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

        if(v == date){
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
        else if(v == signup){
            if(username.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()){
                error.setText("Enter a Valid Email");
            }
            else if(password.getText().toString().equals("")){
                error.setText("Enter a Valid Password");
            }
            else if(!repassword.getText().toString().equals(password.getText().toString())){
                error.setText("Passwords aren't equal");
            }
            else if(date.getText().toString().equals("")){
                error.setText("Enter a Valid Date");
            }
            else{
                gender = (RadioButton) getActivity().findViewById(genderGroup.getCheckedRadioButtonId());
                error.setText("");
                if (!isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                else {
                    mProgressbar.setVisibility(View.VISIBLE);

                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.api_url_signup),
                            sendData(username.getText().toString(), password.getText().toString(),date.getText().toString(),gender.getText().toString()),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        mProgressbar.setVisibility(View.INVISIBLE);
                                        Boolean userStatus = ((JSONObject) response).optBoolean(getString(R.string.api_receive_json_signup_status));

                                        if (userStatus) {
                                            Toast.makeText(getActivity(), "Congratulations! Check your Email for activation", Toast.LENGTH_LONG).show();
                                            getActivity().onBackPressed();
                                        } else {
                                            error.setText("This Email already Exists");
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
                    jsonRequest.setTag(REQUEST_TAG);
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    mQueue.add(jsonRequest);
                }
            }
        }
    }

    public JSONObject sendData(String username,String password,String date,String gender){
        HashMap<String,String> m = new HashMap<>();
        m.put(getString(R.string.api_send_json_signup_email),username);
        m.put(getString(R.string.api_send_json_signup_password),password);
        m.put(getString(R.string.api_send_json_signup_dateOfBirth),date);
        m.put(getString(R.string.api_send_json_signup_gender),gender);
        Log.e(REQUEST_TAG, "sendData: "+(new JSONObject(m)).toString());
        return new JSONObject(m);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}

