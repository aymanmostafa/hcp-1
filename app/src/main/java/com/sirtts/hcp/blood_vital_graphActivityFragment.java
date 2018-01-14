package com.sirtts.hcp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class blood_vital_graphActivityFragment extends Fragment {

    public blood_vital_graphActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blood_vital_graph, container, false);

        Intent intent = getActivity().getIntent();
        ArrayList<String> date_ArrayList = intent.getStringArrayListExtra("graphDate");
        ArrayList<String> val1_ArrayList = intent.getStringArrayListExtra("graphVal1");

        Log.e("dateArr", date_ArrayList.toString());
        Log.e("val1Arr", val1_ArrayList.toString());

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        DataPoint[] dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), Double.valueOf(val1_ArrayList.get(i)));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointsArr);

        graph.addSeries(series);

        if(intent.hasExtra("graphVal2")){
            ArrayList<String> val2_ArrayList = intent.getStringArrayListExtra("graphVal2");
            dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), Double.valueOf(val2_ArrayList.get(i)));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        series = new LineGraphSeries<>(dataPointsArr);

        graph.addSeries(series);
    }

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(date_ArrayList.size());

        try {
            graph.getViewport().setMaxX(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                    .parse(date_ArrayList.get(0))).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            graph.getViewport().setMinX(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                    .parse(date_ArrayList.get(date_ArrayList.size()-1))).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

        return rootView;
    }
}
