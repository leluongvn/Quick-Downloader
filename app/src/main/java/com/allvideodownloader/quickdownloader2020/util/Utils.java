package com.allvideodownloader.quickdownloader2020.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.MainActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Utils {
    // To serve ads on the app, Make it 'true' otherwise make  it false
    public static final boolean IS_ADS_ENABLE = true;
    //   Unity Ads to show Test Ads, Don't forget to make it 'false' while uploading to playstore
    public static final boolean ENABLE_UNITY_TEST_ADS = true;

    // Change Your Privacy Policy URL
    public static final String PRIVACY_POLICY_URL = "https://sites.google.com/view/allinonedownloaderprivacy/home";
    //Enter App Id from Unity
    public static final String APP_ID = "223154";
    //Enter your Admob test id
    public static final String ADMOB_TEST_ID = "";
    public static final String TikTokUrl = "http://androidqueue.com/tiktokapi/api.php";
    // Change with your one.
    public static final String FEED_BACK_EMAIL = "makerindiaapps@gmail.com";
    public static final String DOWNLOAD_DIRECTORY = "/" + Constants.DOWNLOAD_DIRECTORY + "/";
    public static final File ROOT_DOWNLOAD_DIRECTORY = new File(Environment.getExternalStorageDirectory() + "/Download/" + Constants.DOWNLOAD_DIRECTORY);
    public static Dialog customDialog;
    private static Context context;

    public Utils(Context _mContext) {
        context = _mContext;
    }

    public static void setToast(Context context, String str) {
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void createFileFolder() {
        if (!ROOT_DOWNLOAD_DIRECTORY.exists()) {
            ROOT_DOWNLOAD_DIRECTORY.mkdirs();
        }

    }

    public static String extractLinks(String text) {
        Matcher m = Patterns.WEB_URL.matcher(text);
        String url = "";
        while (m.find()) {
            url = m.group();
            Log.d("New URL", "URL extracted: " + url);

            break;
        }
        return url;
    }

    public static void onlyInitializeAds(Activity context) {
        AudienceNetworkAds.initialize(context);
        MobileAds.initialize(context);

    }

    public static void showBannerAds(Activity context) {

        if (IS_ADS_ENABLE) {
            AdView fbAds = new AdView(context, context.getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_50);
            LinearLayout bannerContainer = context.findViewById(R.id.fb_container);
            bannerContainer.addView(fbAds);
            fbAds.loadAd(fbAds.buildLoadAdConfig().withAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    com.google.android.gms.ads.AdView googleAdView = context.findViewById(R.id.adView);
                    AdRequest adRequest = new AdRequest.Builder().addTestDevice(Utils.ADMOB_TEST_ID).build();

                    googleAdView.loadAd(adRequest);

                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());
        } else {

            Log.i("ADS", context.getString(R.string.ads_not_enable));
        }


    }

    public static void setLocale(String lang, Activity context) {

        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static void setLocaleMain(String lang, Activity context) {

        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(context, MainActivity.class);
        context.startActivity(refresh);
        context.finish();
    }

    public static void showInterstitialAds(InterstitialAd interstitialAd, com.google.android.gms.ads.InterstitialAd googleAds) {

        if (IS_ADS_ENABLE) {

            if (interstitialAd.isAdLoaded()) {
                interstitialAd.show();

            } else {

                interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {

                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {

                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {

                        googleAds.loadAd(new AdRequest.Builder().addTestDevice(ADMOB_TEST_ID).build());
                        if (googleAds.isLoaded()) {
                            googleAds.show();

                        } else {
                            googleAds.loadAd(new AdRequest.Builder().addTestDevice(ADMOB_TEST_ID).build());
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                    }

                    @Override
                    public void onAdClicked(Ad ad) {

                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {

                    }
                }).build());

            }


        } else {
            Log.i("ADS", "Ads not Enable");
        }

    }

    public static void setUpRecycler(RecyclerView recyclerView) {


        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static void showProgressDialog(Activity activity) {
        if (customDialog != null) {
            customDialog.dismiss();
            customDialog = null;
        }
        customDialog = new Dialog(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.item_progress_dialog, null);
        customDialog.setCancelable(false);
        customDialog.setContentView(mView);
        if (!customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.show();
        }
    }

    public static void hideProgressDialog() {
        System.out.println("Hide");
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }
    }

    public static void startDownload(String downloadPath, String destinationPath, Context context, String fileName) {
        setToast(context, "Download Started");
        Uri uri = Uri.parse(downloadPath); // Path where you want to download file.
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(fileName + ""); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS, destinationPath + fileName);  // Storage directory path
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading

        try {
            if (Build.VERSION.SDK_INT >= 19) {
                MediaScannerConnection.scanFile(context, new String[]{new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + fileName).getAbsolutePath()},
                        null, (path, uri1) -> {
                        });
            } else {
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(new File(DIRECTORY_DOWNLOADS + "/" + destinationPath + fileName))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg) {

        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();

    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return isURL;
    }

    public static void shareImage(Context context, String filePath) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.share_txt));
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, "", null);
            Uri screenshotUri = Uri.parse(path);
            intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_image_via)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void shareImageVideoOnWhatsapp(Context context, String filePath, boolean isVideo) {
        Uri imageUri = Uri.parse(filePath);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        if (isVideo) {
            shareIntent.setType("video/*");
        } else {
            shareIntent.setType("image/*");
        }
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            Utils.setToast(context, "Whtasapp not installed.");
        }
    }

    public static void shareVideo(Context context, String filePath) {
        Uri mainUri = Uri.parse(filePath);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, mainUri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No application found to open this file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void rateApp(Context context) {
        final String appName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
        }
    }

    public static void openApp(Context context, String packageNamee) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageNamee);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        } else {
            setToast(context, "App Not Available.");
        }
    }

    public static boolean isNullOrEmpty(String s) {
        return (s == null) || (s.length() == 0) || (s.equalsIgnoreCase("null")) || (s.equalsIgnoreCase("0"));
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}