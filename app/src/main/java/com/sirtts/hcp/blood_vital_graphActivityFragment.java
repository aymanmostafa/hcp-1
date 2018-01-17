package com.sirtts.hcp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
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
        String graphName1 = intent.getStringExtra("graphName1");

        Log.e("dateArr#@#@#", date_ArrayList.toString());
        Log.e("val1Arr", val1_ArrayList.toString());

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        //First Value drawing
        double mean = 0,median = 0;
        DataPoint[] dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), Double.valueOf(val1_ArrayList.get(i)));
                mean += Double.valueOf(val1_ArrayList.get(i));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointsArr);
        series.setTitle(graphName1);
        series.setAnimated(true);
        graph.addSeries(series);

        //Mean for first Value drawing
        if(date_ArrayList.size() > 0) mean /= date_ArrayList.size();
        dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), mean);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        series = new LineGraphSeries<>(dataPointsArr);
        series.setTitle("Mean for "+graphName1);
        series.setColor(Color.RED);
        series.setAnimated(true);
        graph.addSeries(series);

        //Median for first Value drawing
        if(date_ArrayList.size() > 0){
            if (date_ArrayList.size() % 2 == 0)
                median = (Double.valueOf(val1_ArrayList.get((date_ArrayList.size() / 2))) + (Double.valueOf(val1_ArrayList.get((date_ArrayList.size() / 2) - 1)))) / 2;
            else
                median = Double.valueOf(val1_ArrayList.get((date_ArrayList.size() / 2)));
        }

        dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), median);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        series = new LineGraphSeries<>(dataPointsArr);
        series.setTitle("Median for "+graphName1);
        series.setColor(Color.YELLOW);
        series.setAnimated(true);
        graph.addSeries(series);

        //Second Value drawing
        if(intent.hasExtra("graphVal2")){
            mean = 0;
            String graphName2 = intent.getStringExtra("graphName2");
            ArrayList<String> val2_ArrayList = intent.getStringArrayListExtra("graphVal2");
            dataPointsArr = new DataPoint[date_ArrayList.size()];
        for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
            try {
                dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                        .parse(date_ArrayList.get(i))), Double.valueOf(val2_ArrayList.get(i)));
                mean += Double.valueOf(val2_ArrayList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            series = new LineGraphSeries<>(dataPointsArr);
            series.setColor(Color.GREEN);
            series.setAnimated(true);
            series.setTitle(graphName2);
            graph.addSeries(series);

            //Mean for second Value drawing
            if(date_ArrayList.size() > 0) mean /= val2_ArrayList.size();
            dataPointsArr = new DataPoint[date_ArrayList.size()];
            for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
                try {
                    dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                            .parse(date_ArrayList.get(i))), mean);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            series = new LineGraphSeries<>(dataPointsArr);
            series.setTitle("Mean for "+graphName2);
            series.setColor(Color.BLACK);
            series.setAnimated(true);
            graph.addSeries(series);

            //Median for second Value drawing
            if(date_ArrayList.size() > 0) {
                if (date_ArrayList.size() % 2 == 0)
                    median = (Double.valueOf(val2_ArrayList.get((date_ArrayList.size() / 2))) + (Double.valueOf(val2_ArrayList.get((date_ArrayList.size() / 2) - 1)))) / 2;
                else
                    median = Double.valueOf(val2_ArrayList.get((date_ArrayList.size() / 2)));
            }

            dataPointsArr = new DataPoint[date_ArrayList.size()];
            for (int i = date_ArrayList.size() - 1, k = 0; i >= 0; i--, k++) {
                try {
                    dataPointsArr[k] = new DataPoint(((new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH))
                            .parse(date_ArrayList.get(i))), median);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            series = new LineGraphSeries<>(dataPointsArr);
            series.setTitle("Median for "+graphName2);
            series.setColor(Color.CYAN);
            series.setAnimated(true);
            graph.addSeries(series);
    }

        //Set graph properties
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(date_ArrayList.size());
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
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        return rootView;
    }
}
