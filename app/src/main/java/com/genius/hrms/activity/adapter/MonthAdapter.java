package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.genius.hrms.R;
import com.genius.hrms.activity.model.MonthModel;

import java.util.ArrayList;

public class MonthAdapter extends BaseAdapter {

    Context context;

    ArrayList<MonthModel> modes=new ArrayList<>();
    LayoutInflater inflter;

    public MonthAdapter(Context applicationContext, ArrayList<MonthModel> modes) {
        this.context = applicationContext;

        this.modes = modes;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return modes.size();
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
        view = inflter.inflate(R.layout.spinner_forget_raw, null);

        TextView tvMode = (TextView) view.findViewById(R.id.tvMode);

        tvMode.setText(modes.get(i).getMonthName());
        return view;
    }
}

