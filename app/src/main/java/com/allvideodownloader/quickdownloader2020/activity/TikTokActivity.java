package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.api.MyApiClass;
import com.allvideodownloader.quickdownloader2020.model.TiktokModel;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.net.URL;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.allvideodownloader.quickdownloader2020.util.Utils.DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.startDownload;

public class TikTokActivity extends AppCompatActivity {
    public static final String TAG_TIKTOK_NAME = "tiktok.com";
    TikTokActivity activity;
    MyApiClass myApiClass;
    boolean iswithwaternark = true;
    Activity context;
    private ClipboardManager clipBoard;
    private ImageView imBack;
    private RelativeLayout llopentiktok;
    private EditText etText;
    private Button tvWithoutMark;
//    private InterstitialAd interstitialAd;
    private com.google.android.gms.ads.InterstitialAd googleAds;
    private ImageView appLogo;
    private TextView title;
    private TextView tvToolbarText;
    private DisposableObserver<TiktokModel> tiktokObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Utils.hideProgressDialog();
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    startDownload(tiktokModel.getData().getMainvideo(),
                            DOWNLOAD_DIRECTORY, activity, "tiktok_" + System.currentTimeMillis() + ".mp4");
                    etText.setText("");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

//    public void initializeAds() {
//        AudienceNetworkAds.initialize(context);
//        MobileAds.initialize(context);

//        interstitialAd = new InterstitialAd(context, getString(R.string.fb_inter_home));
//        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//                googleAds = new com.google.android.gms.ads.InterstitialAd(context);
//                googleAds.setAdUnitId(context.getString(R.string.Admob_inter_home));
//                googleAds.loadAd(new AdRequest.Builder().addTestDevice(Utils.ADMOB_TEST_ID).build());
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        }).build());


//    }

    public void updateUi() {

        tvToolbarText.setText(R.string.download_tiktok_videos);
        appLogo.setImageResource(R.drawable.tiktok_logo);
        title.setText("Tiktok");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_other_apps);

        initView();
        context = activity = this;
//        initializeAds();
        updateUi();

        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(this);
        setLocale(appLangSessionManager.getLanguage(), this);

        // Show banner Ads
        Utils.showBannerAds(context);

        myApiClass = MyApiClass.getInstance(activity);
        createFileFolder();
        initViews();

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        pasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        imBack.setOnClickListener(view -> onBackPressed());


        tvWithoutMark.setOnClickListener(v -> {
            iswithwaternark = false;
            String ll = etText.getText().toString();
            if (ll.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(ll).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                getTikTokData();
//                Utils.showInterstitialAds(interstitialAd, googleAds);
            }
        });

        llopentiktok.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Utils.setToast(activity, "App Not Available.");
            }

        });
    }

    private void getTikTokData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            if (host.contains(TAG_TIKTOK_NAME)) {
                Utils.showProgressDialog(activity);

                callVideoDownload(etText.getText().toString());

            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVideoDownload(String url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (myApiClass != null) {
                    myApiClass.getTiktokVideo(tiktokObserver, url);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void pasteText() {
        try {
            etText.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            assert copyIntent != null;
            if (copyIntent.equals("")) {
                if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(TAG_TIKTOK_NAME)) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains(TAG_TIKTOK_NAME)) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains(TAG_TIKTOK_NAME)) {
                    etText.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
//        if (interstitialAd != null) {
//            interstitialAd.destroy();
//        }
        super.onDestroy();
    }

    private void initView() {
        imBack = (ImageView) findViewById(R.id.imBack);
        llopentiktok = (RelativeLayout) findViewById(R.id.LLOpenApp);
        etText = (EditText) findViewById(R.id.et_text);
        tvWithoutMark = (Button) findViewById(R.id.downloadBtn);
        appLogo = findViewById(R.id.app_logo);
        title = findViewById(R.id.title);
        tvToolbarText = findViewById(R.id.tv_toolbar_text);
    }

}