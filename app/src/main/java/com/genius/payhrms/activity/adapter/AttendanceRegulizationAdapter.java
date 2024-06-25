package com.genius.payhrms.activity.adapter;


import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.AttendanceRegulizationActivity;
import com.genius.payhrms.activity.model.BackLogModel;

import java.util.ArrayList;
import java.util.Calendar;


public class AttendanceRegulizationAdapter extends RecyclerView.Adapter<AttendanceRegulizationAdapter.MyViewHolder> {
    ArrayList<BackLogModel> itemList = new ArrayList<>();
    Context mContex;
    ArrayList<String> item = new ArrayList<>();
    ArrayList<String>issueList=new ArrayList();


    public AttendanceRegulizationAdapter(ArrayList<BackLogModel> blockLogList, Context mContex) {
        this.itemList = blockLogList;
        this.mContex = mContex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_regulization_raw, viewGroup, false);

        return new MyViewHolder(itemView, new MyCustomEditTextListener(), new MycustomFocus());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        issueList.clear();
        issueList.add("Please Select");
        issueList.add("MissedSwipe");
        issueList.add("Outdoor");
        issueList.add("Other");

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvAppInTime.setText(itemList.get(i).getInTime());
        myViewHolder.tvAppOutTime.setText(itemList.get(i).getOutTime());

        myViewHolder.myCustomEditTextListener.updatePosition(myViewHolder.getAdapterPosition());
        myViewHolder.cutomfocus.updatePosition(myViewHolder.getAdapterPosition());
        myViewHolder.etRemarks.setText(itemList.get(i).getRemarks());

        myViewHolder.imgInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContex,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                itemList.get(i).setInTime(intime);
                                myViewHolder.tvAppInTime.setText(itemList.get(i).getInTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        myViewHolder.imgOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(mContex,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //txtTime.setText(hourOfDay + ":" + minute);
                                String intime = hourOfDay + ":" + minute;
                                itemList.get(i).setOutTime(intime);
                                myViewHolder.tvAppOutTime.setText(itemList.get(i).getOutTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (mContex, android.R.layout.simple_spinner_item,
                        issueList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myViewHolder.spIssue.setAdapter(spinnerArrayAdapter);

        myViewHolder.spIssue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos==1 || pos==2){
                    String issue=issueList.get(pos);
                    itemList.get(i).setRemarks(issue);
                    ((AttendanceRegulizationActivity) mContex).updateItemStatus(i);
                    myViewHolder.llIssue.setVisibility(View.VISIBLE);
                    myViewHolder.etRemarks.setVisibility(View.GONE);
                }else if (pos==3){
                    myViewHolder.llIssue.setVisibility(View.GONE);
                    myViewHolder.etRemarks.setVisibility(View.VISIBLE);
                }else {
                    myViewHolder.llIssue.setVisibility(View.VISIBLE);
                    myViewHolder.etRemarks.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



       /* myViewHolder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemList.get(i).isSelected()){
                    myViewHolder.imgLike.setVisibility(View.GONE);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                    BacklogActivity.newBacklogArray=itemList;

                }else {
                    myViewHolder.imgLike.setVisibility(View.VISIBLE);
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();
                    BacklogActivity.newBacklogArray=itemList;
                }


            }
        });*/


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvAppInTime,tvAppOutTime;
        EditText etRemarks;
        MyCustomEditTextListener myCustomEditTextListener;
        MycustomFocus cutomfocus;
        ImageView imgInTime, imgOutTime, imgLike;
        Spinner spIssue;
        LinearLayout llIssue;


        public MyViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener, MycustomFocus mycustomFocus) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            tvAppInTime = (TextView) itemView.findViewById(R.id.tvAppInTime);
            tvAppOutTime = (TextView) itemView.findViewById(R.id.tvAppOutTime);
            etRemarks = (EditText) itemView.findViewById(R.id.etRemarks);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.cutomfocus = mycustomFocus;
            etRemarks.addTextChangedListener(myCustomEditTextListener);
            etRemarks.setOnFocusChangeListener(cutomfocus);
            imgInTime = (ImageView) itemView.findViewById(R.id.imgInTime);
            imgOutTime = (ImageView) itemView.findViewById(R.id.imgOutTime);
            spIssue=(Spinner) itemView.findViewById(R.id.spIssue);
            llIssue=(LinearLayout) itemView.findViewById(R.id.llIssue);




        }
    }


    private class MycustomFocus implements View.OnFocusChangeListener {
        private int position;


        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {

                itemList.get(position).setRemarks("");

            } else {

                ((AttendanceRegulizationActivity) mContex).updateItemStatus(position);

            }


        }
    }


    private class MyCustomEditTextListener implements TextWatcher {

        private int position;


        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() > 0) {
                itemList.get(position).setRemarks(s.toString());
                String test = itemList.get(position).getRemarks();
                Log.d("ppp", test);
                item.add(itemList.get(position).getDate() + "_" + itemList.get(position).getRemarks());
                Log.d("competitoritem", item.toString());



            }else {

            }

        }


    }



}
