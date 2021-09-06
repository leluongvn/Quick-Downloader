package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.api.MyApiClass;
import com.allvideodownloader.quickdownloader2020.model.Edge;
import com.allvideodownloader.quickdownloader2020.model.EdgeSidecarToChildren;
import com.allvideodownloader.quickdownloader2020.model.ResponseModel;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.SharePrefs;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.allvideodownloader.quickdownloader2020.util.Utils.DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.startDownload;

public class InstagramActivity extends AppCompatActivity {


    Activity context;
    MyApiClass myApiClass;

    private InstagramActivity activity;
    private ClipboardManager clipBoard;
    private String photoUrl;
    private String videoUrl;

    private ImageView imBack;
    private RelativeLayout llopeninstagram;
    private RelativeLayout rllogininstagram;
    private Switch switchLogin;
    private EditText etText;
    private final DisposableObserver<JsonObject> instaobserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject versionList) {
            Utils.hideProgressDialog();
            try {
                Log.e("onNext: ", versionList.toString());
                Type listType = new TypeToken<ResponseModel>() {
                }.getType();
                ResponseModel responseModel = new Gson().fromJson(versionList.toString(), listType);
                EdgeSidecarToChildren edgeSidecarToChildren = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edgeSidecarToChildren != null) {
                    List<Edge> edgeArrayList = edgeSidecarToChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            videoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            startDownload(videoUrl, DOWNLOAD_DIRECTORY, activity, getVideoFilenameFromURL(videoUrl));
                            etText.setText("");
                            videoUrl = "";

                        } else {
                            photoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            startDownload(photoUrl, DOWNLOAD_DIRECTORY, activity, getImageFilenameFromURL(photoUrl));
                            photoUrl = "";
                            etText.setText("");
                        }
                    }
                } else {
                    boolean isVideo = responseModel.getGraphql().getShortcode_media().isIs_video();
                    if (isVideo) {
                        videoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                        startDownload(videoUrl, DOWNLOAD_DIRECTORY, activity, getVideoFilenameFromURL(videoUrl));
                        videoUrl = "";
                        etText.setText("");
                    } else {
                        photoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources()
                                .get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        startDownload(photoUrl, DOWNLOAD_DIRECTORY, activity, getImageFilenameFromURL(photoUrl));
                        photoUrl = "";
                        etText.setText("");

                    }
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
    private Button tvPaste;
    private Button loginBtn1;


    private InterstitialAd interstitialAd;
    private com.google.android.gms.ads.InterstitialAd googleAds;

    public void initializeAds() {
        AudienceNetworkAds.initialize(context);
        MobileAds.initialize(context);

        interstitialAd = new InterstitialAd(context, getString(R.string.fb_inter_home));
        interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                googleAds = new com.google.android.gms.ads.InterstitialAd(context);
                googleAds.setAdUnitId(context.getString(R.string.Admob_inter_home));
                googleAds.loadAd(new AdRequest.Builder().addTestDevice(Utils.ADMOB_TEST_ID).build());

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        initView();
        context = activity = this;
        initializeAds();

        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(activity);
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
        context = activity = this;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        pastetext();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        imBack.setOnClickListener(view -> {
            onBackPressed();
            Utils.showInterstitialAds(interstitialAd, googleAds);

        });

        loginBtn1.setOnClickListener(v -> {

            String edittext = etText.getText().toString();
            if (edittext.equals("")) {
                Utils.setToast(activity, "Enter Url");
            } else if (!Patterns.WEB_URL.matcher(edittext).matches()) {
                Utils.setToast(activity, "Enter Valid Url");
            } else {
                Utils.showInterstitialAds(interstitialAd, googleAds);

                getInstagramData();
            }


        });

        tvPaste.setOnClickListener(v -> pastetext());
        llopeninstagram.setOnClickListener(v -> Utils.openApp(activity, "com.instagram.android"));

        switchLogin.setChecked(SharePrefs.getInstance(activity).getBoolean(SharePrefs.isInstaLogin));

        rllogininstagram.setOnClickListener(v -> {
            if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.isInstaLogin)) {
                Intent intent = new Intent(activity,
                        LoginActivity.class);
                startActivityForResult(intent, 100);
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setPositiveButton("Yes", (dialog, id) -> {
                    SharePrefs.getInstance(activity).putBoolean(SharePrefs.isInstaLogin, false);
                    SharePrefs.getInstance(activity).putString(SharePrefs.cookies, "");
                    SharePrefs.getInstance(activity).putString(SharePrefs.csrf, "");
                    SharePrefs.getInstance(activity).putString(SharePrefs.sessionId, "");
                    SharePrefs.getInstance(activity).putString(SharePrefs.userId, "");

                    switchLogin.setChecked(SharePrefs.getInstance(activity).getBoolean(SharePrefs.isInstaLogin));
                    dialog.cancel();

                });
                ab.setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
                AlertDialog alert = ab.create();
                alert.setTitle("Don't Want to Download Media from Private Account?");
                alert.show();
            }

        });

    }

    private void getInstagramData() {
        try {
            createFileFolder();
            URL url = new URL(etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);
            if (host.equals("www.instagram.com")) {
                callDownload(etText.getText().toString());
            } else {
                Utils.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pastetext() {
        try {
            etText.setText("");
            String copyIntent = getIntent().getStringExtra("CopyIntent");
            if (copyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("instagram.com")) {
                        etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (copyIntent.contains("instagram.com")) {
                    etText.setText(copyIntent);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithoutParameters(String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null, // Ignore the query part of the input url
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setToast(activity, "Enter Valid Url");
            return "";
        }
    }

    private void callDownload(String url) {
        String urlWithoutParameters = getUrlWithoutParameters(url);
        urlWithoutParameters = urlWithoutParameters + "?__a=1";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (myApiClass != null) {
                    Utils.showProgressDialog(activity);
                    myApiClass.callResult(instaobserver, urlWithoutParameters,
                            "ds_user_id=" + SharePrefs.getInstance(activity).getString(SharePrefs.userId)
                                    + "; sessionid=" + SharePrefs.getInstance(activity).getString(SharePrefs.sessionId));
                }
            } else {
                Utils.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instaobserver.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {
                switchLogin.setChecked(SharePrefs.getInstance(activity).getBoolean(SharePrefs.isInstaLogin));

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void initView() {
        imBack = findViewById(R.id.imBack);
        llopeninstagram = findViewById(R.id.LLOpenInstagram);
        rllogininstagram = findViewById(R.id.RLLoginInstagram);
        switchLogin = findViewById(R.id.SwitchLogin);
        etText = findViewById(R.id.et_text);
        tvPaste = findViewById(R.id.paste);
        loginBtn1 = findViewById(R.id.downloadBtn);
    }

}
