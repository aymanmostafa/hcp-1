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
public class analyticsActivityFragment extends Fragment implements View.OnClickListener {

    Button bloodPressure, temp, heartRate, respRate, spo2, bloodSugar, dentistVisit, bloodTest,
            period;
    private SharedPreferences sharedPre ;
    public analyticsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analytics, container, false);

        bloodPressure = (Button) rootView.findViewById(R.id.analytics_vitalBloodbtnid);
        bloodPressure.setOnClickListener(this);

        temp = (Button) rootView.findViewById(R.id.analytics_vitalTempbtnid);
        temp.setOnClickListener(this);

        heartRate = (Button) rootView.findViewById(R.id.analytics_vitalHearttbtnid);
        heartRate.setOnClickListener(this);

        respRate = (Button) rootView.findViewById(R.id.analytics_VitalRespbtnid);
        respRate.setOnClickListener(this);

        spo2 = (Button) rootView.findViewById(R.id.analytics_VitalSpo2);
        spo2.setOnClickListener(this);

        bloodSugar = (Button) rootView.findViewById(R.id.analytics_BloodSugarbtnid);
        bloodSugar.setOnClickListener(this);

        dentistVisit = (Button) rootView.findViewById(R.id.analytics_dentist);
        dentistVisit.setOnClickListener(this);

        bloodTest = (Button) rootView.findViewById(R.id.analytics_BloodTest);
        bloodTest.setOnClickListener(this);

        period = (Button) rootView.findViewById(R.id.analytics_periodbtnid);
        period.setOnClickListener(this);

        sharedPre = getActivity().getSharedPreferences(getString(R.string.shared_isUserLoged),
                Context.MODE_PRIVATE);
        if(sharedPre.getBoolean(getString(R.string.shared_female),false))
            period.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == bloodPressure){
            startActivity(new Intent(getContext(), ListBloodVitalSignsActivity.class));
        }
        else if(v == temp){
            startActivity(new Intent(getContext(), ListtempVitalSignsActivity.class));
        }
        else if(v == heartRate){
            startActivity(new Intent(getContext(), ListheartVitalSignsActivity.class));
        }
        else if(v == respRate){
            startActivity(new Intent(getContext(), ListrespVitalSignsActivity.class));
        }
        else if(v == spo2){
            startActivity(new Intent(getContext(), Listspo2VitalSignsActivity.class));
        }
        else if(v == bloodSugar){
            startActivity(new Intent(getContext(), ListsugarActivity.class));
        }
        else if(v == dentistVisit){
            startActivity(new Intent(getContext(), ListDentistVisitsActivity.class));
        }
        else if(v == bloodTest){
            startActivity(new Intent(getContext(), ListBloodActivity.class));
        }
        else if(v == period){
            startActivity(new Intent(getContext(), ListperiodActivity.class));
        }
    }
}
