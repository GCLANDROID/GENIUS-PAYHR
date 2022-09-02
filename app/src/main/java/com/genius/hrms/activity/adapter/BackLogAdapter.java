package com.genius.hrms.activity.adapter;


import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.attendance.BacklogActivity;
import com.genius.hrms.activity.leaveapplication.ApproverFragment;
import com.genius.hrms.activity.model.ApprovalModel;
import com.genius.hrms.activity.model.BackLogModel;

import java.util.ArrayList;
import java.util.Calendar;


public class BackLogAdapter extends RecyclerView.Adapter<BackLogAdapter.MyViewHolder> {
    ArrayList<BackLogModel> itemList = new ArrayList<>();
    Context mContex;
    ArrayList<String> item = new ArrayList<>();

    public BackLogAdapter(ArrayList<BackLogModel> blockLogList, Context mContex) {
        this.itemList = blockLogList;
        this.mContex = mContex;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.back_log_adapter_screen, viewGroup, false);

        return new MyViewHolder(itemView, new MyCustomEditTextListener(), new MycustomFocus());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

       if (itemList.get(i).isSelected()){
           myViewHolder.imgLike.setVisibility(View.VISIBLE);
       }else {
           myViewHolder.imgLike.setVisibility(View.GONE);
       }

        myViewHolder.tvDate.setText(itemList.get(i).getDate());
        myViewHolder.tvInTime.setText(itemList.get(i).getInTime());
        myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());

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
                                myViewHolder.tvInTime.setText(itemList.get(i).getInTime());


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
                                myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
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
        TextView tvDate, tvInTime, tvOutTime;
        EditText etRemarks;
        MyCustomEditTextListener myCustomEditTextListener;
        MycustomFocus cutomfocus;
        ImageView imgInTime, imgOutTime, imgLike;
        LinearLayout llLike;

        public MyViewHolder(@NonNull View itemView, MyCustomEditTextListener myCustomEditTextListener, MycustomFocus mycustomFocus) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvInTime = (TextView) itemView.findViewById(R.id.tvInTime);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            etRemarks = (EditText) itemView.findViewById(R.id.etRemarks);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.cutomfocus = mycustomFocus;
            etRemarks.addTextChangedListener(myCustomEditTextListener);
            etRemarks.setOnFocusChangeListener(cutomfocus);
            imgInTime = (ImageView) itemView.findViewById(R.id.imgInTime);
            imgOutTime = (ImageView) itemView.findViewById(R.id.imgOutTime);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            llLike = (LinearLayout) itemView.findViewById(R.id.llLike);


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
                ((BacklogActivity) mContex).updateItemStatus(position);


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
