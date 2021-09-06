package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.util.Log;
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
import com.allvideodownloader.quickdownloader2020.model.TwitterResponse;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.allvideodownloader.quickdownloader2020.util.Utils.DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.startDownload;

public class TwitterActivity extends AppCompatActivity {
    private static final CharSequence TAG_TWITTER_NAME = "twitter.com";
    TwitterActivity activity;
    MyApiClass myApiClass;
    Activity context;
    private String videoUrl;
    private ClipboardManager clipBoard;
    private ImageView imBack;
    private RelativeLayout llopentwitter;
    private EditText etText;
    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            Utils.hideProgressDialog();
            try {
                videoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")) {
                    startDownload(videoUrl, DOWNLOAD_DIRECTORY, activity, getFilenameFromURL(videoUrl, "image"));
                    etText.setText("");
                } else {
                    videoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size() - 1).getUrl();
                    startDownload(videoUrl, DOWNLOAD_DIRECTORY, activity, getFilenameFromURL(videoUrl, "mp4"));
                    etText.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Utils.setToast(activity, "No Media on Tweet or Invalid Link");
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
    private Button loginBtn1;
    private InterstitialAd interstitialAd;
    private com.google.android.gms.ads.InterstitialAd googleAds;
    private TextView tvToolbarText;
    private CircleImageView appLogo;
    private TextView title;


//    public void initializeAds() {
//        AudienceNetworkAds.initialize(context);
//        MobileAds.initialize(context);
//
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
//
//
//    }


    public void updateUi() {

        tvToolbarText.setText(R.string.twitter_tag);
        appLogo.setImageResource(R.drawable.ic_twitter);
        title.setText("Twitter");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_other_apps);
        initView();

        updateUi();


        context = activity = this;
//        initializeAds();


        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(this);
        setLocale(appLangSessionManager.getLanguage(), this);
        // Show banner Ads
        Utils.showBannerAds(context);

        myApiClass = MyApiClass.getInstance(activity);
        createFileFolder();
        initViews();

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

        imBack.setOnClickListener(view -> {

                    onBackPressed();
                    Utils.showInterstitialAds(interstitialAd, googleAds);

                }
        );

        loginBtn1.setOnClickListener(v -> {
            String ll = etText.getText().toString();
            if (ll.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(ll).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {

                getTwitterData();
                Utils.showInterstitialAds(interstitialAd, googleAds);
            }
        });

        llopentwitter.setOnClickListener(v -> Utils.openApp(activity, "com.twitter.android"));
    }

    private void getTwitterData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            if (host.contains(TAG_TWITTER_NAME)) {
                Long id = getTweetId(etText.getText().toString());
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void pasteText() {
        try {
            etText.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            if (copyIntent.equals("")) {
                if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains(TAG_TWITTER_NAME)) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains(TAG_TWITTER_NAME)) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains(TAG_TWITTER_NAME)) {
                    etText.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetTwitterData(String id) {
        String url = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (myApiClass != null) {
                    Utils.showProgressDialog(activity);
                    myApiClass.twitterApi(observer, url, id);
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")) {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
        }
    }

    private void initView() {
        imBack = (ImageView) findViewById(R.id.imBack);
        llopentwitter = (RelativeLayout) findViewById(R.id.LLOpenApp);
        etText = (EditText) findViewById(R.id.et_text);
        loginBtn1 = (Button) findViewById(R.id.downloadBtn);
        tvToolbarText = findViewById(R.id.tv_toolbar_text);
        appLogo = findViewById(R.id.app_logo);
        title = findViewById(R.id.title);
    }
}