package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.MultipleDocModel;

import java.util.ArrayList;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.MyViewHolder> {
    Context context;
    //ArrayList<Uri> imageURI;
    ArrayList<MultipleDocModel> multipleImageUriList;
    public MultipleImageAdapter(Context context, ArrayList<MultipleDocModel> multipleImageUriList) {
        this.context = context;
        //this.imageURI = imageURI;
        this.multipleImageUriList = multipleImageUriList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_doc_layout,parent,false);
        //View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (multipleImageUriList.get(position).getFileType().equalsIgnoreCase("image/jpg") ){
            holder.image.setImageURI(multipleImageUriList.get(position).getFileUri());
        } else {
            holder.image.setImageResource(R.drawable.pdficon);
        }

        holder.txtFileName.setText(multipleImageUriList.get(position).getFileName());
        //holder.image.setImageURI(imageURI.get(position));
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipleImageUriList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,multipleImageUriList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return multipleImageUriList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image,imgRemove;
        TextView txtFileName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imgRemove = itemView.findViewById(R.id.imgRemove);
            txtFileName = itemView.findViewById(R.id.txtFileName);
        }
    }
}
