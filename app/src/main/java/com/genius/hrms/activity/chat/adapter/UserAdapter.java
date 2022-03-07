package com.genius.hrms.activity.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.genius.hrms.R;
import com.genius.hrms.activity.chat.MessageActivity;
import com.genius.hrms.activity.chat.data.User;
import com.genius.hrms.activity.utility.Pref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashSet;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUsers;
    private boolean ischat;
    String uid;
    String theLastMsg;



    public UserAdapter(Context mContext, List<User> mUsers, boolean isChat,String uid) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat=isChat;
        this.uid=uid;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final User user=mUsers.get(position);
       holder.tvUsername.setText(user.getUsername());
//        if (user.getImageUrl().equals("default")){
//            holder.imgProfile.setImageResource(R.drawable.avatar);
//        }else{
//            Glide.with(mContext).load(user.getImageUrl()).into(holder.imgProfile);
//        }
//        if (ischat){
//            lastMsg(user.getId(),holder.tvLastMsg);
//        }else{
//            holder.tvLastMsg.setVisibility(View.GONE);
//        }
//        if (ischat){
//            if (user.getStatus().equals("online")){
//                holder.imgOn.setVisibility(View.VISIBLE);
//                holder.imgOff.setVisibility(View.GONE);
//            }else{
//                holder.imgOn.setVisibility(View.GONE);
//                holder.imgOff.setVisibility(View.VISIBLE);
//            }
//        }else {
//            holder.imgOn.setVisibility(View.GONE);
//            holder.imgOff.setVisibility(View.GONE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, MessageActivity.class);
                i.putExtra("userId",user.getId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvUsername;
        public ImageView imgProfile;
        public ImageView imgOn;
        public ImageView imgOff;
        public TextView tvLastMsg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername=itemView.findViewById(R.id.tvUsername);
            imgProfile=itemView.findViewById(R.id.imgProfile);
            imgOn=itemView.findViewById(R.id.imgOn);
            imgOff=itemView.findViewById(R.id.imgOff);
            tvLastMsg=itemView.findViewById(R.id.tvLastMsg);

        }
    }
//    private void lastMsg(String userId,TextView lastMsg){
//        theLastMsg="default";
//        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    Chat chat=dataSnapshot.getValue(Chat.class);
//                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
//                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId)
//                                || chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
//                            theLastMsg=chat.getMessage();
//                        }
//                    }
//
//                }
//                switch (theLastMsg){
//                    case "default":
//                        lastMsg.setText("No Message");
//                        break;
//                    default:
//                        lastMsg.setText(theLastMsg);
//                        break;
//                }
//                theLastMsg="default";
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}
