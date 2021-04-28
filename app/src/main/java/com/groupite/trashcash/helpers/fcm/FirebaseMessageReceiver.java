package com.groupite.trashcash.helpers.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.groupite.trashcash.R;
import com.groupite.trashcash.activities.MainActivity;

import java.util.ArrayList;

public class FirebaseMessageReceiver extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData().size() > 0 ){
            showNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }

        if(remoteMessage.getNotification() != null){
            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    private RemoteViews getCustomDesign(String title  , String message ) {

        RemoteViews rView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_layout);

        rView.setTextViewText(R.id.tv_title_notification,title);
        rView.setTextViewText(R.id.tv_message_notification,message);
        rView.setImageViewResource(R.id.imageView_notification,R.drawable.logo);

        return  rView;
    }

    private void showNotification(String title , String  message){
        Intent  x = new Intent(this, MainActivity.class);

        x.putExtra("comeFromNotification",true);

        String chanel_id = "web_app_channel";
        x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent y = PendingIntent.getActivity(this,0,x, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(getApplicationContext(),chanel_id)
                .setSmallIcon(R.drawable.logo)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{100, 100, 100, 100, 100})
                .setContentIntent(y);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN ){
            builder = builder.setContent(getCustomDesign(title,message));
        }else{
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.logo);

        }

        NotificationManager notifiationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            NotificationChannel notificationChannel  =new NotificationChannel(chanel_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notifiationManager.createNotificationChannel(notificationChannel);
        }
        notifiationManager.notify(0,builder.build());

    }
}
