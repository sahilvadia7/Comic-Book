package com.example.comicbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

   // private static final int NOTIFICATION_ID=100;
NotificationManager nm;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.0){
//
//            NotificationChannel channel=new NotificationChannel("My noti","my notif",NotificationManager.IMPORTANCE_DEFAULT);
//            nm=getSystemService(NotificationManager.class);
//            nm.createNotificationChannel(channel);
//
//        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"my notification")
        .setSmallIcon(R.drawable.app_logo)
        .setContentTitle("Notification Alert, Click Me!")
        .setContentText("Hi, This is Android Notification Detail!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.app_logo));
        NotificationManagerCompat ncm=NotificationManagerCompat.from(HomeActivity.this);

//        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        ncm.notify(1, mBuilder.build());


//        Drawable drawable= ResourcesCompat.getDrawable(getResources(), R.drawable.app_logo,null);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap largeIcon = bitmapDrawable.getBitmap();
//        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification=new Notification.Builder(this)
//                .setLargeIcon(largeIcon)
//                .setSmallIcon(R.drawable.app_logo)
//                .setContentText("New Message")
//                .setSubText("New book arrive")
//                .build();
//        nm.notify(NOTIFICATION_ID,notification);
    }



}