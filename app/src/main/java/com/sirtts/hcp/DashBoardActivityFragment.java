package com.sirtts.hcp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashBoardActivityFragment extends Fragment implements View.OnClickListener {

    Button logout,vital,period,sugar,dentist,blood,analytics;
    private SharedPreferences sharedPre ;
    public DashBoardActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View rootView =inflater.inflate(R.layout.fragment_dash_board, container, false);

        logout= (Button) rootView.findViewById(R.id.dashboard_logoutbtnid);
        logout.setOnClickListener(this);

        vital= (Button) rootView.findViewById(R.id.dashboard_vitalbtnid);
        vital.setOnClickListener(this);

        period= (Button) rootView.findViewById(R.id.dashboard_periodbtnid);
        period.setOnClickListener(this);

        sugar= (Button) rootView.findViewById(R.id.dashboard_sugarbtnid);
        sugar.setOnClickListener(this);

        dentist = (Button) rootView.findViewById(R.id.dashboard_dentistbtnid);
        dentist.setOnClickListener(this);

        blood = (Button) rootView.findViewById(R.id.dashboard_bloodbtnid);
        blood.setOnClickListener(this);

        analytics = (Button) rootView.findViewById(R.id.dashboard_Analytics);
        analytics.setOnClickListener(this);

        sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged),Context.MODE_PRIVATE);
        if(sharedPre.getString(getString(R.string.shared_gender),"").equals("FEMALE"))
            period.setVisibility(View.VISIBLE);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if(v == logout){
            sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPre.edit();
            editor.putBoolean(getString(R.string.shared_isUserLoged), false);
            editor.commit();
            startActivity(new Intent(getContext(), loginActivity.class));
            getActivity().finish();
        }
        else if(v == vital){
            startActivity(new Intent(getContext(), VitalSignsActivity.class));
        }
        else if(v == period){
            startActivity(new Intent(getContext(), PeriodActivity.class));
        }
        else if(v == sugar){
            startActivity(new Intent(getContext(), SugarActivity.class));
        }
        else if(v == dentist){
            startActivity(new Intent(getContext(), DentistVisitsActivity.class));
        }
        else if(v == blood){
            startActivity(new Intent(getContext(), BloodActivity.class));
        }
        else if(v == analytics){
            startActivity(new Intent(getContext(), analyticsActivity.class));
        }
    }
}
