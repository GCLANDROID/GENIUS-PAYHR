package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;
import com.genius.payhrms.activity.model.CompOffDetailsModel;
import com.genius.payhrms.activity.utility.Pref;

import java.util.ArrayList;


public class CompOffAdapter extends RecyclerView.Adapter<CompOffAdapter.MyViewHolder> {
    ArrayList<CompOffDetailsModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.compoff_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final CompOffDetailsModel dayModel = itemList.get(i);
        final Pref pref=new Pref(context1);
        final ProgressDialog pd=new ProgressDialog(context1);
        pd.setMessage("loading..");
        pd.setCancelable(false);

        if (pref.getLanguage().equals("hi")) {
            myViewHolder.tvFullDay.setText("पूरा दिन");
            myViewHolder.tvHalfDay.setText("आधा दिन");


        }else {

            myViewHolder.tvFullDay.setText("Full day");
            myViewHolder.tvHalfDay.setText("Half Day");

        }
        myViewHolder.tvLeaveDate.setText(itemList.get(i).getBrkUpDate());
        myViewHolder.tvLeaveValue.setText(itemList.get(i).getLeaveValue());
        if (itemList.get(i).getLeaveValue().equals("0.5")){
            myViewHolder.llFullDay.setVisibility(View.GONE);
        }else {
            myViewHolder.llFullDay.setVisibility(View.VISIBLE);
        }

        myViewHolder.llHalfDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());


                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgHalfDay.setVisibility(View.VISIBLE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                    itemList.get(i).setDayValue("0.5");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                   ((ApplicationFragment) context).updateStatusForComPff(i, true );



                } else {
                    myViewHolder.imgHalfDay.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                   ((ApplicationFragment) context).updateStatusForComPff(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });




        myViewHolder.llFullDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgHalfDay.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.VISIBLE);
                    itemList.get(i).setDayValue("1");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((ApplicationFragment) context).updateStatusForComPff(i, true );



                } else {
                    myViewHolder.imgHalfDay.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.VISIBLE);
                    ((ApplicationFragment) context).updateStatusForComPff(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveDate, tvLeaveValue;
        LinearLayout llHalfDay, llFullDay;
        ImageView imgHalfDay,imgFull;
        TextView tvFullDay,tvHalfDay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveDate = (TextView) itemView.findViewById(R.id.tvLeaveDate);
            tvLeaveValue = (TextView) itemView.findViewById(R.id.tvLeaveValue);


            tvFullDay = (TextView) itemView.findViewById(R.id.tvFullDay);
            tvHalfDay = (TextView) itemView.findViewById(R.id.tvHalfDay);


            llHalfDay = (LinearLayout) itemView.findViewById(R.id.llHalfDay);
            llFullDay = (LinearLayout) itemView.findViewById(R.id.llFullDay);

            imgHalfDay=(ImageView)itemView.findViewById(R.id.imgHalfDay);
            imgFull=(ImageView)itemView.findViewById(R.id.imgFull);
        }
    }

    public CompOffAdapter(ArrayList<CompOffDetailsModel> itemList, Fragment context, Context context1) {
        this.itemList = itemList;
        this.context = context;
        this.context1=context1;
    }
}
