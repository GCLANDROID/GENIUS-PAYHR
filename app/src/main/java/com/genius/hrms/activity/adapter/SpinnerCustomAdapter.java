package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.genius.hrms.R;

import com.genius.hrms.activity.geofence.EmpMappingActivity;
import com.genius.hrms.activity.model.SpinnerModel;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class SpinnerCustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<SpinnerModel>itemList=new ArrayList<>();


    public SpinnerCustomAdapter(Context context, ArrayList<SpinnerModel> itemList) {
        this.context = context;
        this.itemList = itemList;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView tvItem = (TextView) view.findViewById(R.id.tvItem);
        tvItem.setText(itemList.get(i).getItem());
        String trans=(((EmpMappingActivity)context).translate(i));
        tvItem.setText(trans);
        return view;
    }
}
