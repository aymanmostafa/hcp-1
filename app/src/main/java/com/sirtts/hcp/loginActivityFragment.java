package com.sirtts.hcp;

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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class loginActivityFragment extends Fragment implements View.OnClickListener, Response.Listener,
        Response.ErrorListener {

    private EditText username,password;
    private TextView signup,error;
    private Button login;
    private ProgressBar mProgressbar;
    private SharedPreferences sharedPre ;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "LoginVolleyActivity";

    public loginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged),Context.MODE_PRIVATE);

        if(sharedPre.getBoolean(getString(R.string.shared_isUserLoged), false)){
            startActivity(new Intent(getContext(), DashBoardActivity.class));
            getActivity().finish();
        }

        username = (EditText) rootView.findViewById(R.id.login_usernametxtid);
        password = (EditText) rootView.findViewById(R.id.login_passwordtxtid);

        signup = (TextView) rootView.findViewById(R.id.login_signuptvid);
        error = (TextView) rootView.findViewById(R.id.login_errortvid);

        login = (Button) rootView.findViewById(R.id.login_loginbtnid);

        mProgressbar = (ProgressBar) rootView.findViewById(R.id.login_progressBar);

        signup.setOnClickListener(this);
        login.setOnClickListener(this);

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

        if(v == signup){
            startActivity(new Intent(getContext(), signupActivity.class));
        }
        else if(v == login){
            if(username.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()){
                error.setText("Enter a Valid Email");
            }
            else if(password.getText().toString().equals("")){
                error.setText("Enter a Valid Password");
            }
            else{
                error.setText("");
                if (!isNetworkAvailable(getContext()))
                    Toast.makeText(getActivity(), "Failed to Connect! Check your Connection", Toast.LENGTH_SHORT).show();
                else {
                    mProgressbar.setVisibility(View.VISIBLE);
                    final JSONObjectRequest jsonRequest = new JSONObjectRequest(Request.Method
                            .POST, getString(R.string.api_url_login),
                            sendData(username.getText().toString(), password.getText().toString()), this, this);
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

    @Override
    public void onResponse(Object response) {
        try {
            mProgressbar.setVisibility(View.INVISIBLE);
            Boolean userStatus = ((JSONObject) response).optBoolean(getString(R.string.api_receive_json_login_status));
            int userId = ((JSONObject) response).optInt(getString(R.string.api_receive_json_login_userId));
            String errorMsg = ((JSONObject) response).optString(getString(R.string.api_receive_json_login_errorMsg));
            Boolean userFemale = ((JSONObject) response).optBoolean(getString(R.string.api_receive_json_login_female));

            if (userStatus && userId > 0 && errorMsg.equals("")) {
                sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.putBoolean(getString(R.string.shared_isUserLoged), true);
                editor.putInt(getString(R.string.shared_userId), userId);
                editor.putBoolean(getString(R.string.shared_female), userFemale);
                editor.commit();
                startActivity(new Intent(getContext(), DashBoardActivity.class));
                getActivity().finish();
            } else {
                error.setText(errorMsg);
            }

        }
        catch(Exception e){
            mProgressbar.setVisibility(View.INVISIBLE);
            error.setText("Unexpected Error happened!");
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressbar.setVisibility(View.INVISIBLE);
        this.error.setText("Unexpected Error happened!");
    }

    public JSONObject sendData(String username,String password){
        HashMap<String,String> m = new HashMap<>();
        m.put(getString(R.string.api_send_json_login_email),username);
        m.put(getString(R.string.api_send_json_login_password),password);
        Log.e(REQUEST_TAG, "sendData: "+(new JSONObject(m)).toString());
        return new JSONObject(m);
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
