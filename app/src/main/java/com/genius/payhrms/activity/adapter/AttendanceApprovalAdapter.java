package com.genius.payhrms.activity.adapter;

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
import com.genius.payhrms.activity.attendance.AttendanceApprovalFragment;
import com.genius.payhrms.activity.model.AttendanceApprovalModel;

import java.util.ArrayList;


public class AttendanceApprovalAdapter extends RecyclerView.Adapter<AttendanceApprovalAdapter.MyViewHolder> {
    ArrayList<AttendanceApprovalModel> itemList = new ArrayList<>();
    Context context;
    Fragment fContext;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attendance_approval_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final AttendanceApprovalModel approvalModel = itemList.get(i);

        if (approvalModel.isSelected()){
            myViewHolder.imgTick.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgTick.setVisibility(View.GONE);
        }

        myViewHolder.tvName.setText(itemList.get(i).getName()+"\n"+" ("+itemList.get(i).getCode()+")");
        myViewHolder.tvAttnDate.setText(itemList.get(i).getAttenDate());
        myViewHolder.tvInTime.setText(itemList.get(i).getInTime());
        myViewHolder.tvOutTime.setText(itemList.get(i).getOutTime());
        myViewHolder.tvType.setText(itemList.get(i).getNature());

        if (!itemList.get(i).getType().equals("")||itemList.get(i).getType()!=null){
            myViewHolder.llType.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.llType.setVisibility(View.GONE);
        }



        myViewHolder.llSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                approvalModel.setSelected(!approvalModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (approvalModel.isSelected()) {

                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                    ((AttendanceApprovalFragment) fContext).updateAttendanceStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    myViewHolder.imgTick.setVisibility(View.GONE);
                    ((AttendanceApprovalFragment) fContext).updateAttendanceStatus(i, false);
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
        TextView  tvName, tvAttnDate, tvInTime, tvOutTime,tvType;
        LinearLayout llSelected,llType;
        ImageView imgTick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAttnDate = (TextView) itemView.findViewById(R.id.tvAttnDate);
            tvInTime = (TextView) itemView.findViewById(R.id.tvInTime);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            tvType=(TextView)itemView.findViewById(R.id.tvType);

            llSelected=(LinearLayout)itemView.findViewById(R.id.llSelected);

            llType=(LinearLayout)itemView.findViewById(R.id.llType);

            imgTick=(ImageView)itemView.findViewById(R.id.imgTick);


        }
    }

    public AttendanceApprovalAdapter(ArrayList<AttendanceApprovalModel> itemList, Context context, Fragment fContext) {
        this.itemList = itemList;
        this.context = context;
        this.fContext = fContext;
    }


}
