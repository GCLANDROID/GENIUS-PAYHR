package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.AdjustmentModel;

import java.util.ArrayList;

public class SpinnerAdjustmentAdapter extends ArrayAdapter<AdjustmentModel> {
    public SpinnerAdjustmentAdapter(Context context, ArrayList<AdjustmentModel> AdjustmentList) {
        super(context, 0, AdjustmentList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_list, parent, false);
        }

        TextView txtShiftTime = convertView.findViewById(R.id.txtItem);
        AdjustmentModel currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            txtShiftTime.setText(currentItem.getName());
        }
        return convertView;
    }
}
