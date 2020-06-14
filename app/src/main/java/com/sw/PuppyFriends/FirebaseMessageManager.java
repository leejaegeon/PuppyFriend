package com.sw.PuppyFriends;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FirebaseMessageManager extends FirebaseMessagingService {

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAUSXyyZ8:APA91bGmFRBzXdUc1zjbE-S5KU-v0Eup2WWN2HUGgCVdpxAigxznH0q3b76tCLqeXAO5FNtgk_BOtV6tXMH2oPxDy3T1eFcGecsUbcQBZ8h23mZLkmnUL3ygrsIs4nZMsxVly04vFha3";
    private DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference().child("fcm_token");

    @Override
    public void onNewToken(String token){
        Log.d("FCM", "refreshed token" + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("FCM", "message data payload" + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d("FCM", "Message data payload: " + remoteMessage.getData());
        }

        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }

    public void sendNotification(String title, String body){
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        String chId = "test";
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 알림 왔을때 사운드.

        NotificationCompat.Builder notiBuilder =
                new NotificationCompat.Builder(this, chId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String chName = "ch name";
            NotificationChannel channel = new NotificationChannel(chId, chName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, notiBuilder.build());
    }

    public void postUpstreamMsg(final String id, final String title, final String body){

        conditionRef.addValueEventListener(new ValueEventListener() {
            String token;

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("getFirebaseDB","key : " + dataSnapshot.getChildrenCount());

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    FcmToken get = postSnapshot.getValue(FcmToken.class);

                    if(get.id.equals(id)) {
                        token = get.token;
                        sendUpstreamMsg(token, title, body);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void sendUpstreamMsg(final String token, final String title, final String body){

//        getUserToken(id);
//        Log.d("get user token", mUserToken);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("title", title);
                    notification.put("body", body);
                    root.put("notification", notification);
                    // user FCM token
                    root.put("to", token);

                    URL Url = new URL(FCM_MESSAGE_URL);
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("Content-type", "application/json");

                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
