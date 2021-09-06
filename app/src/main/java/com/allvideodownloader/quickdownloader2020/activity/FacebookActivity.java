package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.api.MyApiClass;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static com.allvideodownloader.quickdownloader2020.util.Utils.DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.showInterstitialAds;
import static com.allvideodownloader.quickdownloader2020.util.Utils.startDownload;

public class FacebookActivity extends AppCompatActivity {
    FacebookActivity activity;
    MyApiClass myApiClass;
    Activity context;
    AppLangSessionManager appLangSessionManager;
    private String videoUrl;
    private ClipboardManager clipBoard;
    private ImageView imBack;
    private RelativeLayout llopenfacebbook;
    private EditText etText;
    private Button tvPaste;
    private Button loginBtn1;
    private InterstitialAd interstitialAd;
//    private com.google.android.gms.ads.InterstitialAd googleAds;
//
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

//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        initView();
        context = activity = this;

        //Initialize Ads
//        initializeAds();

        // Show banner Ads
        Utils.showBannerAds(context);


        // set the selected app  language
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage(), this);


        myApiClass = MyApiClass.getInstance(activity);

        // Create  Folder if not exists
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
        imBack.setOnClickListener(view -> onBackPressed());

        loginBtn1.setOnClickListener(v -> {
            String editTextData = etText.getText().toString();
            if (editTextData.equals("")) {
                Utils.setToast(activity, getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(editTextData).matches()) {
                Utils.setToast(activity, getString(R.string.enter_valid_url));
            } else {

                getFacebookData();
//                showInterstitialAds(interstitialAd, googleAds);

            }
        });

        tvPaste.setOnClickListener(v -> pasteText());

        llopenfacebbook.setOnClickListener(v -> Utils.openApp(activity, "com.facebook.katana"));

    }

    private void getFacebookData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);

            if (host.contains("facebook.com")) {
                Utils.showProgressDialog(activity);
                new GetFacebookData().execute(etText.getText().toString());
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pasteText() {
        try {
            etText.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            if (copyIntent.equals("")) {
                if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("facebook.com")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("facebook.com")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains("facebook.com")) {
                    etText.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName() + ".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    private void initView() {
        imBack = findViewById(R.id.imBack);
        llopenfacebbook = findViewById(R.id.LLOpenFacebbook);
        etText = findViewById(R.id.et_text);
        tvPaste = findViewById(R.id.paste);
        loginBtn1 = findViewById(R.id.downloadBtn);
    }

    class GetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;

        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return facebookDoc;
        }


        @Override
        protected void onPostExecute(Document result) {
            Utils.hideProgressDialog();
            try {

                videoUrl = result.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute: ", videoUrl);
                if (!videoUrl.equals("")) {
                    try {
                        startDownload(videoUrl, DOWNLOAD_DIRECTORY, activity, getFilenameFromURL(videoUrl));
                        videoUrl = "";
                        etText.setText("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}