package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.api.MyApiClass;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.allvideodownloader.quickdownloader2020.util.Utils.DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.startDownload;

public class MxTakatakActivity extends AppCompatActivity {
    Activity activity;
    Activity context;
    MyApiClass myApiClass;


    AppLangSessionManager appLangSessionManager;
    Dialog progressDialog;
    ProgressBar progressBar;
    private ClipboardManager clipBoard;
    private ImageView imBack;
    private RelativeLayout openApp;
    private EditText etText;
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

        tvToolbarText.setText(R.string.download_takatak_videos);
        appLogo.setImageResource(R.drawable.mx);
        title.setText("Mx Takatak");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_other_apps);
        activity = context = this;
//        initializeAds();

        initView();
        updateUi();
        // Show banner Ads
        Utils.showBannerAds(context);

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        myApiClass = MyApiClass.getInstance(activity);
        createFileFolder();
        initViews();


        initiliazeDialog();

    }

    public void initiliazeDialog() {

        progressDialog = new Dialog(this);

        progressDialog.setContentView(R.layout.dialog_download_file);

        progressBar = progressDialog.findViewById(R.id.progress_download_video);

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
            String string = etText.getText().toString();

            if (string.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));

            } else if (!Patterns.WEB_URL.matcher(string).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));

            } else {

                Utils.showInterstitialAds(interstitialAd, googleAds);

                getLikeeData();
            }


        });

        openApp.setOnClickListener(v -> Utils.openApp(activity, "com.next.innovation.takatak"));


    }

    private void getLikeeData() {
        try {
            createFileFolder();
            String url = etText.getText().toString().trim();
            if (url.contains("mxtakatak")) {
                Utils.showProgressDialog(activity);
                callVideoDownload(url.trim());
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
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
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("mxtakatak")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("mxtakatak")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains("mxtakatak")) {
                    etText.setText(copyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName() + "";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

    private void initView() {
        imBack = (ImageView) findViewById(R.id.imBack);
        openApp = (RelativeLayout) findViewById(R.id.LLOpenApp);
        etText = (EditText) findViewById(R.id.et_text);
        loginBtn1 = (Button) findViewById(R.id.downloadBtn);
        tvToolbarText = findViewById(R.id.tv_toolbar_text);
        appLogo = findViewById(R.id.app_logo);
        title = findViewById(R.id.title);
    }

    private void callVideoDownload(String url) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {

                AndroidNetworking.post("https://takatak.katarmal.in/v1/processUrl")
                        .addBodyParameter("takatakurl", url)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                String videoUrl;
                                try {

                                    JSONObject jsonObject = new JSONObject(response.toString());

                                    videoUrl = jsonObject.getString("videourl");


                                    startDownload(videoUrl,
                                            DOWNLOAD_DIRECTORY, activity, "MX_" + System.currentTimeMillis() + ".mp4");
                                    etText.setText("");

                                    Utils.hideProgressDialog();

                                } catch (Exception e) {

                                    e.printStackTrace();
                                    Utils.hideProgressDialog();

                                }


                            }

                            @Override/**/
                            public void onError(ANError error) {

                                Utils.hideProgressDialog();
                            }
                        });

            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //FB INTERSTITIAL ADS : END

}