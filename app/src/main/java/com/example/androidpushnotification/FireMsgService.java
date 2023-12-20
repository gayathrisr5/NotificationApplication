package com.example.androidpushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

public class FireMsgService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e(FireMsgService.class.getSimpleName(), "Token : " + token);
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("Msg", "Message received [" + remoteMessage.getNotification().getBody().contains("https:") + "]");
        Log.d("Msg", "Body!!!!!!!! received [" + remoteMessage.getData().get("image") + "]");
        Intent intent = null;

        // Create Notification
        if(remoteMessage.getNotification().getBody().contains("https:")){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(remoteMessage.getNotification().getBody()));}
        else{
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);}

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_IMMUTABLE);
        Bitmap image = null;
        try {
            URL url = new URL(remoteMessage.getData().get("image"));
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());}
        catch(IOException e) {
           System.out.println(e);}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channelId";
            CharSequence channelName = "Your Channel Name";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channelId")
                .setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(image)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setAutoCancel(true)
                .setSubText("Incoming Client")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("title")))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(image))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1410, notificationBuilder.build());

        if (remoteMessage.getData().size() > 0) {
            String url = remoteMessage.getData().get("url");
            Log.e(FireMsgService.class.getSimpleName(),"URL : "+url);
        }

    }
}



