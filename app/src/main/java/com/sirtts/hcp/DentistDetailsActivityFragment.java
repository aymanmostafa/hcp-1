package com.sirtts.hcp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class DentistDetailsActivityFragment extends Fragment {

    TextView tempTv,error,notes,notestxt;
    CheckBox tempEt;
    LinearLayout tempLayout,mainListLayout;
    private RequestQueue mQueue;
    ProgressBar mProgressbar;
    public static final String REQUEST_TAG_View = "ViewDentistVolley";
    public DentistDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_dentist_details, container, false);


        mainListLayout = (LinearLayout) rootView.findViewById(R.id.dentistDetails_linearLayout);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.dentistDetails_progressBar);

        notes = (TextView) rootView.findViewById(R.id.dentistDetails_notesid);
        notestxt = (TextView) rootView.findViewById(R.id.dentistDetails_notestxtid);

        if (isNetworkAvailable(getContext())) {
            Intent i = getActivity().getIntent();
            mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                    .getRequestQueue();

            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, getString(R.string.api_url_dentist_get),
                    sendData(i.getIntExtra("dentistID",0)),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                notes.setVisibility(View.VISIBLE);
                                notestxt.setVisibility(View.VISIBLE);
                                mProgressbar.setVisibility(View.INVISIBLE);
                                JSONObject responseObj = response.getJSONObject(0);
                                Log.e("Send dentist Data", "sendData: "+response.getJSONObject(0).toString());
                                for(int i=0;i<responseObj.names().length();i++) {
                                    if (String.valueOf(responseObj.names().get(i).toString()).equals("notes")) {
                                        notes.setText(responseObj.optString(responseObj.names().get(i).toString()));
                                    } else {
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

                                        tempTv.setText(String.valueOf(responseObj.names().get(i).toString()));
                                        tempEt.setChecked(responseObj.optBoolean(responseObj.names().get(i).toString()));
                                        tempTv.setTypeface(null, Typeface.BOLD);

                                        tempEt.setEnabled(false);

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
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public JSONArray sendData(int id){
        HashMap m = new HashMap();
        m.put(getString(R.string.api_send_json_dentist_Id),id);
        JSONArray x = new JSONArray();
        x.put(new JSONObject(m));
        Log.e("Send dentist Data", "sendData: "+x.toString());
        return x;
    }
}
