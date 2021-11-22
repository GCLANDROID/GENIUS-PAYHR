package com.genius.hrms.activity.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.genius.hrms.R;
import com.genius.hrms.activity.chat.adapter.MessageAdapter;
import com.genius.hrms.activity.chat.api.APIService;
import com.genius.hrms.activity.chat.api.Client;
import com.genius.hrms.activity.chat.api.Data;
import com.genius.hrms.activity.chat.api.MyResponse;
import com.genius.hrms.activity.chat.api.Sender;
import com.genius.hrms.activity.chat.data.Chats;
import com.genius.hrms.activity.chat.data.User;
import com.genius.hrms.activity.chat.data.Users;
import com.genius.hrms.activity.chat.viewmodel.DatabaseViewModel;
import com.genius.hrms.activity.chat.viewmodel.LogInViewModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.api.core.ApiService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    ImageView imgBack;
    CircleImageView imgProfile;
    TextView tvUsername;

    DatabaseReference reference;
    Intent intent;
    ImageView btnSend;
    EditText etSend;
    String userId;
    MessageAdapter messageAdapter;
    ArrayList<Chats> mChat;
    RecyclerView rvItem;
    ValueEventListener seenListner;
    APIService apiService;
    boolean notify=false;
    Pref pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initview();
        onClick();

    }

    private void onClick() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String timeStamp= String.valueOf(timestamp);

                notify=true;
                String msg=etSend.getText().toString();
                if (!msg.equals(" ")){
                    sendMessage(pref.getempcode(),userId,timeStamp,msg);
                }else{
                    Toast.makeText(MessageActivity.this, "Please type a message", Toast.LENGTH_SHORT).show();
                }
                etSend.setText(" ");
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,ChatHomePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    private void initview() {
        pref=new Pref(this);
        apiService= (APIService) Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        rvItem=findViewById(R.id.rvItem);
        rvItem.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvItem.setLayoutManager(linearLayoutManager);
        imgProfile=findViewById(R.id.imgProfile);
        tvUsername=findViewById(R.id.tvUsername);
        btnSend=findViewById(R.id.btnSend);
        etSend=findViewById(R.id.etSend);
        imgBack=findViewById(R.id.imgBack);
        intent=getIntent();
        userId=intent.getStringExtra("userId");
        reference= FirebaseDatabase.getInstance().getReference("Users-"+pref.getSecurityCode()).child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                tvUsername.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    imgProfile.setImageResource(R.drawable.avatar);

                }else{
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(imgProfile);
                }
                readMesage(pref.getempcode(),userId,user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            }

    private void readMesage(final String myId, final String userinfo, final String imageUrl) {

            mChat=new ArrayList<>();
            reference=FirebaseDatabase.getInstance().getReference("Chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mChat.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Chats chat=dataSnapshot.getValue(Chats.class);
                        if (chat.getReceiver().equals(myId) && chat.getSender().equals(userId)
                               ||chat.getReceiver().equals(userId) && chat.getSender().equals(myId)){
                            mChat.add(chat);
                        }
                        String mid=pref.getempcode();

                        messageAdapter=new MessageAdapter(MessageActivity.this,mChat,imageUrl,mid,userId);
                        rvItem.setAdapter(messageAdapter);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    private void sendMessage(String sender, final String receiver,String timestamp, String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> map=new HashMap<>();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("message",message);
        map.put("timestamp",timestamp);
        map.put("isseen",false);
        reference.child("Chats").push().setValue(map);
        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("Chatlist-"+pref.getempcode()).child(pref.getempcode()).child(userId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Users-"+pref.getSecurityCode()).child(pref.getempcode());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if (notify){
                    sendNotification(receiver,user.getUsername(),msg);
                }

                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data(pref.getempcode(),R.mipmap.ic_launcher,username+": "+message,"New Message",userId);
                    Sender sender=new Sender(data,token.getToken());
                    apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code()==200){

                                if (response.body().success!=1){
                                    //Toast.makeText(MessageActivity.this, "Failed!.."+response.body(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //using below code gives same result
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .header("Authorization", "key=AAAA65eV3Pg:APA91bHqRtF8vHbU4Ln_O33Bof9WaiDVao1flWz_Z4VKzQS4rj6Y3YLGLwQ2rLsjvwQnEgFUAc7TYtNIZNIKEVphoID9A-mRQL_Jf2hS4dgp3Dpt7urwtDd0wwdYBGA-vPBwYdHCDs2D"); // <-- this is the important line
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });
//
//        httpClient.addInterceptor(logging);
//        OkHttpClient client = httpClient.build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://fcm.googleapis.com")//url of FCM message server
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
//                .build();
//
//// prepare call in Retrofit 2.0
//        FirebaseAPI firebaseAPI = retrofit.create(FirebaseAPI.class);
//
////for messaging server
//        NotifyData notifydata = new NotifyData("New Message",message);
//        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
//       Query query=tokens.orderByKey().equalTo(receiver);
//       query.addValueEventListener(new ValueEventListener() {
//           @Override
//           public void onDataChange(@NonNull DataSnapshot snapshot) {
//               for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
//                   Token token=dataSnapshot.getValue(Token.class);
//
//                   Call<Message> call2 = firebaseAPI.sendMessage(new Message(token.getToken(), notifydata));
//
//                   call2.enqueue(new Callback<Message>() {
//                       @Override
//                       public void onResponse(Call<Message> call, Response<Message> response) {
//
//                           Log.d("Response ", "onResponse");
//                           //t1.setText("Notification sent");
//
//                       }
//
//                       @Override
//                       public void onFailure(Call<Message> call, Throwable t) {
//                           Log.d("Response ", "onFailure");
//                           //t1.setText("Notification failure");
//                       }
//                   });
//               }
//           }
//
//           @Override
//           public void onCancelled(@NonNull DatabaseError error) {
//
//           }
//       });



    }


    @Override
    protected void onPause() {
        super.onPause();
//        reference.removeEventListener(seenListner);
//        status("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //status("online");
    }
}


