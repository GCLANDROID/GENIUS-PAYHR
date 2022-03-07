package com.genius.hrms.activity.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.genius.hrms.R;


import com.genius.hrms.activity.chat.MessageActivity;
import com.genius.hrms.activity.chat.Token;
import com.genius.hrms.activity.geofence.NotificationActivity;
import com.genius.hrms.activity.utility.Constants;
import com.genius.hrms.activity.utility.Pref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;




/**
 * Created by Farman on 6/3/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    Pref pref;

    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        //if (null != firebaseUser) {
            updateToken(s);
        //}

    }
    private void updateToken(String refreshToken) {
        pref=new Pref(this);
        //FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        Token token=new Token(refreshToken);
        databaseReference.child(pref.getempcode()).setValue(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MyFirebaseMsgService", "From: " + remoteMessage.getFrom());
        Log.d("MyFirebaseMsgService", "Notification Message Body: " + remoteMessage.getData().toString());
//        String msg  = remoteMessage.getData().get("msg");
//        String title  = remoteMessage.getData().get("title");
        super.onMessageReceived(remoteMessage);
        String sent = remoteMessage.getData().get("sent");
        String user = remoteMessage.getData().get("user");

//        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
//        String currentUser = preferences.getString("currentuser", "none");

       // FirebaseAuth mAuth = FirebaseAuth.getInstance();
       // FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert sent != null;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                }else{
                sendNotification(remoteMessage);
            }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOreoNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));

        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", user);
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        long[] v = {500,1000};


        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        assert user != null;
        int j = Integer.parseInt(user.replaceAll("[\\D]", ""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId", user);

        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        assert icon != null;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setColor(Color.GREEN)
                .setVibrate(v)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        assert notificationManager != null;
        notificationManager.notify(i, builder.build());
    }

}
