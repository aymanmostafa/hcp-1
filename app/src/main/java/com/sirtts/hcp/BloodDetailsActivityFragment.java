package com.sirtts.hcp;

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

import com.android.volley.AuthFailureError;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class BloodDetailsActivityFragment extends Fragment {

    TextView tempTv,error;
    EditText tempEt;
    LinearLayout tempLayout,mainListLayout;
    private RequestQueue mQueue;
    ProgressBar mProgressbar;
    public static final String REQUEST_TAG_View = "ViewBloodVolley";
    public BloodDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_blood_details, container, false);


        mainListLayout = (LinearLayout) rootView.findViewById(R.id.bloodDetails_linearLayout);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.bloodDetails_progressBar);

        if (isNetworkAvailable(getContext())) {
            Intent i = getActivity().getIntent();
            mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                    .getRequestQueue();

            String url = getString(R.string.api_url_blood_get)+i.getStringExtra("bloodID");
            final SharedPreferences sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,url ,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                mProgressbar.setVisibility(View.INVISIBLE);
                                for(int i=0;i<response.names().length();i++){
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

                                    tempTv.setText(String.valueOf(response.names().get(i).toString()));
                                    Double value = response.optDouble(response.names().get(i).toString());
                                    if(!Double.isNaN(value)) {
                                        tempEt.setText(value.toString());
                                        tempTv.setTypeface(null, Typeface.BOLD);

                                        tempEt.setFocusable(false);

                                        tempLayout.addView(tempTv);
                                        tempLayout.addView(tempEt);

                                        mainListLayout.addView(tempLayout);
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
                    }) {
                //Send the token with the request
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put(getString(R.string.api_send_json_auth_header),
                            sharedPre.getString(getString(R.string.api_receive_json_login_idToken), ""));
                    return headers;
                }
            };


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
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
