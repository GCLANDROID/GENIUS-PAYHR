package com.genius.hrms.activity.geofence;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AddLocationAdapter;
import com.genius.hrms.activity.model.AddedLocationModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BootomSheet extends BottomSheetDialogFragment {


    View v;
    TextView tvReset,tvClose;
    ArrayList<AddedLocationModel> arraylist;
    RecyclerView rvItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.bottomsheet, container, false);
        initView();
        onClick();
        return v;
    }

    private void initView(){
        Bundle test = getArguments();
        arraylist = test.getParcelableArrayList("arraylist");
        Log.d("sixzz", String.valueOf(arraylist.size()));
        tvReset=(TextView)v.findViewById(R.id.tvReset);
        rvItem=(RecyclerView)v.findViewById(R.id.rvItem);
        rvItem.setLayoutManager(new GridLayoutManager(getContext(), 3));
        AddLocationAdapter adAdapter=new AddLocationAdapter(arraylist);
        rvItem.setAdapter(adAdapter);
        tvReset=(TextView)v.findViewById(R.id.tvReset);
        tvClose=(TextView)v.findViewById(R.id.tvClose);


    }

    private void onClick(){
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), FenceNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
