package com.sirtts.hcp;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Owner on 11-Sep-17.
 */

public class VitalListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> date;
    ArrayList<String> time;
    ArrayList<String> val1;
    ArrayList<String> id;


    public VitalListAdapter(
            Context context2,
            ArrayList<String> date,
            ArrayList<String> time,
            ArrayList<String> val1,
            ArrayList<String> id
    )
    {

        this.context = context2;
        this.date = date;
        this.time = time;
        this.val1 = val1;
        this.id = id;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return date.size();
    }

    public String getItem(int position) {
        // TODO Auto-generated method stub
        return id.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listviewdatalayout, null);

            holder = new Holder();

            holder.textviewdate = (TextView) child.findViewById(R.id.VitalList_textviewdate);
            holder.textviewtime = (TextView) child.findViewById(R.id.VitalList_textviewtime);
            holder.textviewval1 = (TextView) child.findViewById(R.id.VitalList_textviewval1);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewdate.setText(date.get(position));
        holder.textviewtime.setText(time.get(position));
        holder.textviewval1.setText(val1.get(position));

        return child;
    }

    public class Holder {
        TextView textviewdate;
        TextView textviewtime;
        TextView textviewval1;
    }

}
