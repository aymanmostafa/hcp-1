package com.sirtts.hcp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListBloodVitalSignsActivityFragment extends Fragment implements View.OnClickListener {

    private RequestQueue mQueue;
    Button graph;
    ListView listview;
    VitalListAdapter adp;
    ArrayList<String> date_ArrayList;
    ArrayList<String> time_ArrayList;
    ArrayList<String> val1_ArrayList;
    ProgressBar mProgressbar;
    ArrayList<String> sys_ArrayList;
    ArrayList<String> dia_ArrayList;
    DatePickerDialog datePickerDialog;
    String startDate,endDate;
    AlertDialog.Builder alertBuilder;
    int offset;
    boolean flag_loading;
    public static final String REQUEST_TAG = "ListBloodVitalVolley";


    public ListBloodVitalSignsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_blood_vital_signs, container, false);

        listview = (ListView) rootView.findViewById(R.id.ListBloodVitalSigns_listView);
        mProgressbar = (ProgressBar) rootView.findViewById(R.id.ListBloodVitalSigns_progressBar);

        graph = (Button) rootView.findViewById(R.id.bloodvital_graphbtn);
        graph.setOnClickListener(this);
        graph.setVisibility(View.INVISIBLE);

        date_ArrayList = new ArrayList<String>();
        time_ArrayList = new ArrayList<String>();
        val1_ArrayList = new ArrayList<String>();
        sys_ArrayList = new ArrayList<>();
        dia_ArrayList = new ArrayList<>();

        offset = 0;
        flag_loading = false;
        sendVolley(true);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(flag_loading == false)
                    {
                        flag_loading = true;
                        offset ++;
                        sendVolley(false);
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public  void onResume(){
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mProgressbar.setVisibility(View.INVISIBLE);
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onClick(View v) {
        if (v == graph) {
            startDate = getCurrentDateAndTime("yyyy-MM-dd");
            endDate = getCurrentDateAndTime("yyyy-MM-dd");
            alertBuilder = new AlertDialog.Builder(getActivity());

            alertBuilder.setTitle("Set the start & end date");
            alertBuilder.setMessage(startDate + " -> " + endDate);

            DialogInterface.OnClickListener endListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    datePicker(false);
                }
            };

            DialogInterface.OnClickListener startListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    datePicker(true);

                }
            };

            DialogInterface.OnClickListener viewListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    long duration = 0;
                    try {
                        duration = TimeUnit.MILLISECONDS.toDays((dateFormat.parse(endDate)).
                                getTime() - (dateFormat.parse(startDate)).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (duration < 95 && duration > -1) {
                        if (isNetworkAvailable(getContext())) {
                            mProgressbar.setVisibility(View.VISIBLE);
                            String url = getString(R.string.api_url_bloodVital_list)+ "?"+
                                    getString(R.string.api_send_json_list_arr_startDate)+startDate
                                    +"&"+getString(R.string.api_send_json_list_arr_endDate)+endDate+"&size=10000";
                            final SharedPreferences sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);
                            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,
                                    new JSONArray(),
                                    new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            try {
                                                mProgressbar.setVisibility(View.INVISIBLE);
                                                ArrayList<String> date_ArrayList_graph = new ArrayList<String>();
                                                ArrayList<String> sys_ArrayList_graph = new ArrayList<String>();
                                                ArrayList<String> dia_ArrayList_graph= new ArrayList<String>();
                                                for (int i = 0; i < response.length(); i++) {
                                                    date_ArrayList_graph.add(String.valueOf(response.optJSONObject(i).optString(getString(R.string.api_receive_json_vital_list_arr_date))));
                                                    sys_ArrayList_graph.add(String.valueOf(response.optJSONObject(i).optInt(getString(R.string.api_receive_json_vital_bloodPressure_list_arr_systolic))));
                                                    dia_ArrayList_graph.add(String.valueOf(response.optJSONObject(i).optInt(getString(R.string.api_receive_json_vital_bloodPressure_list_arr_diastolic))));
                                                }
                                                Intent intent = new Intent(getContext(), blood_vital_graphActivity.class);
                                                intent.putStringArrayListExtra("graphDate", date_ArrayList_graph);
                                                intent.putStringArrayListExtra("graphVal1", sys_ArrayList_graph);
                                                intent.putStringArrayListExtra("graphVal2", dia_ArrayList_graph);
                                                intent.putExtra("graphName1", getString(R.string.api_receive_json_vital_bloodPressure_list_arr_systolic));
                                                intent.putExtra("graphName2", getString(R.string.api_receive_json_vital_bloodPressure_list_arr_diastolic));
                                                startActivity(intent);
                                            } catch (Exception e) {
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
                                    }){//Send the token with the request
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

                        } else
                            Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Duration should be within 3 months", Toast.LENGTH_SHORT).show();
                        alertBuilder.show();
                    }
                }

            };

            alertBuilder.setPositiveButton("End", endListener);
            alertBuilder.setNegativeButton("Start", startListener);
            alertBuilder.setNeutralButton("View", viewListener);
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }
    public void datePicker(final boolean start){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if(start) startDate = (year +"-" + (monthOfYear + 1) + "-" +dayOfMonth);
                        else endDate = (year +"-" + (monthOfYear + 1) + "-" +dayOfMonth);
                        alertBuilder.setMessage(startDate+" -> "+endDate);
                        alertBuilder.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
    public String getCurrentDateAndTime(String format)
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }
    public void sendVolley(final boolean first){
        flag_loading = false;
        if (isNetworkAvailable(getContext())) {
            final SharedPreferences sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged), Context.MODE_PRIVATE);

            mQueue = VolleyRequestQueue.getInstance(getContext().getApplicationContext())
                    .getRequestQueue();

            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, getString(R.string.api_url_bloodVital_list)+"?page="+offset,
                    new JSONArray(),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mProgressbar.setVisibility(View.INVISIBLE);

                                for(int i=0;i<response.length();i++){
                                    date_ArrayList.add("");
                                    time_ArrayList.add(String.valueOf(response.optJSONObject(i).optString(getString(R.string.api_receive_json_vital_list_arr_time))).replace('T',' '));
                                    val1_ArrayList.add(String.valueOf(response.optJSONObject(i).optInt(getString(R.string.api_receive_json_vital_bloodPressure_list_arr_systolic)))
                                            +"/"+String.valueOf(response.optJSONObject(i).optInt(getString(R.string.api_receive_json_vital_bloodPressure_list_arr_diastolic))));
                                }
                                if(response.length() == 0) flag_loading = true;
                                if(first) {adp = new VitalListAdapter(getContext(),date_ArrayList,time_ArrayList,val1_ArrayList,new ArrayList<String>());
                                    listview.setAdapter(adp);
                                    graph.setVisibility(View.VISIBLE);
                                }
                                else {
                                    adp.notifyDataSetChanged();
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
                    }){//Send the token with the request
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
        else Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
    }
}