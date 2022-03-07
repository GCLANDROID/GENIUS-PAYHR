package com.genius.hrms.activity.chat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.genius.hrms.R;
import com.genius.hrms.activity.chat.data.Chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;
    private List<Chats> mChat;
    String mid;
    String userid;
    private String imageUrl;
    FirebaseUser fUser;
    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;


    public MessageAdapter(Context mContext, List<Chats> mChat,String imageUrl,String mid,String userid) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl=imageUrl;
        this.mid=mid;
        this.userid=userid;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right_sent,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left_received,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chats chat=mChat.get(position);
        holder.tvShowMessage.setText(chat.getMessage());
        String timeStamp = chat.getTimestamp();
       // long intTimeStamp = Long.parseLong(timeStamp);

        //String time_msg_received = timeStampConversionToTime(intTimeStamp);
        holder.tvTime.setText(timeStamp);


    }
    public String timeStampConversionToTime(long timeStamp) {

        Date date = new Date(timeStamp);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("hh:mm a");
        jdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return jdf.format(date);

    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvShowMessage;
        public ImageView imgProfile;
        public TextView tvTime;
        public TextView tvTextSeen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShowMessage=itemView.findViewById(R.id.tv_chat_received);
            imgProfile=itemView.findViewById(R.id.imgProfile);
            tvTime = itemView.findViewById(R.id.tv_chat_time_received);
           // tvTextSeen=itemView.findViewById(R.id.tvTextSeen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        //fUser= FirebaseAuth.getInstance().getCurrentUser();
        Log.d("mid",mid);
        Log.d("userid",userid);
        //Log.d("data",mChat.get(position).getSenderId());
        String check=mChat.get(position).getSender();
        if (check.equals(mid)){
            return MSG_TYPE_RIGHT;

        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
