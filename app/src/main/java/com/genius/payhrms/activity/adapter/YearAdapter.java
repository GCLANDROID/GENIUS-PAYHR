package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.YearModel;

import java.util.ArrayList;

public class YearAdapter extends BaseAdapter {

    Context context;

    ArrayList<YearModel> modes=new ArrayList<>();
    LayoutInflater inflter;

    public YearAdapter(Context applicationContext, ArrayList<YearModel> modes) {
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

        tvMode.setText(modes.get(i).getYear());
        return view;
    }
}


