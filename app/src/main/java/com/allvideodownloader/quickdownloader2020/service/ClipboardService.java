package com.allvideodownloader.quickdownloader2020.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.MainActivity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class ClipboardService extends Service {

    public String tag = "clipboard";


    /* renamed from: b */
    public ClipboardManager clipboardManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        this.clipboardManager = clipboardManager;
        clipboardManager.addPrimaryClipChangedListener(new PrimaryClipChanged());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
        showNotification("Started");

        return START_STICKY;
    }

    public void showNotification(String text) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", text);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                    getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(getResources().getColor(R.color.black))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher_round))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentTitle("Copied text")
                .setContentText(text)
                .setChannelId(getResources().getString(R.string.app_name))
                .setFullScreenIntent(pendingIntent, true);
        notificationManager.notify(1, notificationBuilder.build());
    }

    public class PrimaryClipChanged implements ClipboardManager.OnPrimaryClipChangedListener {


        public void onPrimaryClipChanged() {
            try {
                ClipData primaryClip = ClipboardService.this.clipboardManager.getPrimaryClip();
                if (primaryClip != null && primaryClip.getItemCount() > 0 && primaryClip.getItemAt(0).getText() != null) {
                    ClipData.Item itemAt = primaryClip.getItemAt(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(itemAt.getText().toString());

                    showNotification(sb.toString());

                    Log.i(tag,"Copied"+sb.toString());


                }
            } catch (Exception unused) {
            }
        }
    }
}


