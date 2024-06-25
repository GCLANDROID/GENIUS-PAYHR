package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.OfflineDailyLogModel;


import java.util.List;



public class OfflineAttenAdapter extends ArrayAdapter<OfflineDailyLogModel> {
    private List<OfflineDailyLogModel> saleList;

    //context object
    private Context context;

    //constructor
    public OfflineAttenAdapter(Context context, int resource, List<OfflineDailyLogModel> saleList) {
        super(context, resource, saleList);
        this.context = context;
        this.saleList = saleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.offline_dailylog_raw, null, true);
        TextView tvInTime = (TextView) listViewItem.findViewById(R.id.tvInTime);
        TextView tvDate=(TextView)listViewItem.findViewById(R.id.tvDate);
        TextView tvLocation=(TextView)listViewItem.findViewById(R.id.tvInLocation);
        TextView tvRemarksIn=(TextView)listViewItem.findViewById(R.id.tvRemarksIn);


        //getting the current name
        OfflineDailyLogModel name = saleList.get(position);

        tvInTime.setText(name.getActivityDate());
        tvDate.setText(name.getActivityDate());
        tvLocation.setText(name.getActivityInLocation());
        tvRemarksIn.setText(name.getInRemarks());

        //setting the name to textview


        return listViewItem;
    }
}
