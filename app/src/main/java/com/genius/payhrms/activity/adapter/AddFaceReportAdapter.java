package com.genius.payhrms.activity.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.AddPerson;
import com.genius.payhrms.activity.model.AddFaceModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddFaceReportAdapter extends RecyclerView.Adapter<AddFaceReportAdapter.MyViewHolder> {
    ArrayList<AddFaceModel> itemList=new ArrayList();
    Context context;
    @NonNull
    @Override
    public AddFaceReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addface_raw,viewGroup,false);

        return new AddFaceReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFaceReportAdapter.MyViewHolder myViewHolder, final int i) {

        Picasso.with(context)
                .load(itemList.get(i).getUrl())
                .placeholder(R.drawable.load)   // optional
                .error(R.drawable.load)      // optional
                .into(myViewHolder.imgFace);

        myViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete this Image ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //((AddPerson)context).deleFace(itemList.get(i).getFaceId());
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();
            }

        });

        myViewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddPerson)context).deleFace();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedOn;
        ImageView imgFace;
        LinearLayout llDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvCreatedOn);
            imgFace=(ImageView)itemView.findViewById(R.id.imgFace);
            llDelete=(LinearLayout)itemView.findViewById(R.id.llDelete);

        }
    }

    public AddFaceReportAdapter(ArrayList<AddFaceModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
