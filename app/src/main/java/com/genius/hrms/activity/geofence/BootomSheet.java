package com.genius.hrms.activity.geofence;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.adapter.AddLocationAdapter;
import com.genius.hrms.activity.model.AddedLocationModel;
import com.genius.hrms.activity.payroll.SalaryActivity;
import com.genius.hrms.activity.utility.RecyclerItemClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class BootomSheet extends BottomSheetDialogFragment implements RecyclerItemClickListener.OnItemClickListener {


    View v;
    TextView tvReset,tvClose;
    ArrayList<AddedLocationModel> arraylist;
    RecyclerView rvItem;
    String latValue,longValue,locationpoint;
    ArrayList<String>latList=new ArrayList<>();
    ArrayList<String>longList=new ArrayList<>();
    ArrayList<String>locationNameList=new ArrayList<>();



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
        rvItem.setLayoutManager(new GridLayoutManager(getContext(), 1));
        //rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        AddLocationAdapter adAdapter=new AddLocationAdapter(arraylist);
        rvItem.setAdapter(adAdapter);
        rvItem.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), BootomSheet.this));
        tvReset=(TextView)v.findViewById(R.id.tvReset);
        tvClose=(TextView)v.findViewById(R.id.tvClose);
        latValue= getArguments().getString("latValue");
        longValue=getArguments().getString("longtiValue");
        locationpoint= getArguments().getString("locationList");
        latList= getArguments().getStringArrayList("arrayLatValue");
        Log.d("data", String.valueOf(latList));
        longList=getArguments().getStringArrayList("arrayLongValue");
        locationNameList=getArguments().getStringArrayList("locationName");
        Log.d("locationNameList", String.valueOf(locationNameList));


    }
public  void sendData(int pos){
    Intent intent = new Intent(getActivity(), MapPointActivity.class);
    intent.putExtra("latValue", latValue);
    intent.putExtra("longtiValue", longValue);
    intent.putExtra("arrayLatValue", latList);
    intent.putExtra("arrayLongValue", longList);
    intent.putExtra("locationName",locationNameList);
    intent.putExtra("locationList",locationpoint);
    intent.putExtra("updateflag","2");
    startActivity(intent);
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


    @Override
    public void onItemClick(View childView, int position) {
        sendData(position);
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
