package com.example.gotrabahomobile.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.gotrabahomobile.LoginActivity;
import com.example.gotrabahomobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessageService extends FirebaseMessagingService {

    private NotificationManager notificationManager;
    @Override
    public void onNewToken(@NonNull String token){
        super.onNewToken(token);
        updateNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Map<String, String> data  = message.getData();

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern ={0,10,100,200};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");

        Intent resultIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getActivity(this, 1,resultIntent,PendingIntent.FLAG_IMMUTABLE);
        builder.setContentTitle(message.getNotification().getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("body")));
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setPriority(Notification.PRIORITY_MAX);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = "Notification";
            NotificationChannel channel = new NotificationChannel(
                    channelID, "Coding", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);
            channel.canBypassDnd();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.canBubble();
            }


            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelID);
        }

        notificationManager.notify(100,builder.build());


    }

    private void updateNewToken(String token){

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                Log.e("token: ", " "+ task.getResult());
            }
        });
    }
}
