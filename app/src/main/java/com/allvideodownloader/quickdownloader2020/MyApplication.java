package com.allvideodownloader.quickdownloader2020;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;

import com.allvideodownloader.quickdownloader2020.activity.MainActivity;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.androidnetworking.AndroidNetworking;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
        AndroidNetworking.initialize(this);

        List<String> testDeviceIds = Arrays.asList(Utils.ADMOB_TEST_ID);
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        // Change facebook Test Id
        AdSettings.setTestMode(true);

        AdSettings.addTestDevice("45c490e0-e2cb-4d1a-ac12-46688a25d62b");
        AdSettings.addTestDevice("02fccc39-5a40-4920-8b03-486c18396884");

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            String launchUrl = result.notification.payload.launchURL; // update docs launchUr
            if (launchUrl != null) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(launchUrl));
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            } else {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }


        }

    }

}
