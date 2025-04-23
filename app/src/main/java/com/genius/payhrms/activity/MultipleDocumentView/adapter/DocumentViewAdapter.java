package com.genius.payhrms.activity.MultipleDocumentView.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.MultipleDocumentView.MultipleDocumentViewActivity;
import com.genius.payhrms.activity.adapter.ApproverAdapter;

import java.util.ArrayList;

public class DocumentViewAdapter extends RecyclerView.Adapter<DocumentViewAdapter.MyViewHolder>{
    private static final String TAG = "DocumentViewAdapter";
    Context context;
    ArrayList<DocumentViewModel> docList;

    public DocumentViewAdapter(Context context, ArrayList<DocumentViewModel> docList) {
        this.context = context;
        this.docList = docList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_doc_item_view,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: \nName: "+docList.get(position).name
                +"\ntype:"+docList.get(position).type);
        if(docList.get(position).name.contains("pdf")){
            holder.imageType.setImageResource(R.drawable.pdficon);
        } else {
            holder.imageType.setImageResource(R.drawable.img);
        }

        holder.txtDocName.setText(docList.get(position).name);
        holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MultipleDocumentViewActivity) context).imageAlert(docList.get(position).base64String,docList.get(position).name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageType;
        TextView txtDocName,txtView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDocName = itemView.findViewById(R.id.txtDocName);
            txtView = itemView.findViewById(R.id.txtView);
            imageType = itemView.findViewById(R.id.imageType);
        }
    }
}
