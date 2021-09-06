package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.adapter.ShowImagesAdapter;
import com.allvideodownloader.quickdownloader2020.fragment.WhatsappSavedFragment;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setToast;
import static com.allvideodownloader.quickdownloader2020.util.Utils.shareImage;
import static com.allvideodownloader.quickdownloader2020.util.Utils.shareImageVideoOnWhatsapp;
import static com.allvideodownloader.quickdownloader2020.util.Utils.shareVideo;
import static com.allvideodownloader.quickdownloader2020.util.Utils.showInterstitialAds;


public class FullViewActivity extends AppCompatActivity {
    public String saveFilePath = Utils.ROOT_DOWNLOAD_DIRECTORY + "/Wp";

    ShowImagesAdapter showImagesAdapter;
    String fileName = "";
    Activity context;
    ArrayList<WhatsappStatusModel> fileArrayList;
    private FullViewActivity activity;
    private int position = 0;
    private int isStatus = 0;
    private ViewPager vpView;
    private ImageView imClose;
    private ImageView imShare;
    private ImageView imWhatsappShare;
    private ImageView imDelete;

    private InterstitialAd interstitialAd;
//    private com.google.android.gms.ads.InterstitialAd googleAds;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        initView();
        context = activity = this;

        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage(), this);

        //Initialize Ads
//        initializeAds();


        // Show banner Ads
        Utils.showBannerAds(context);

        Intent extras = getIntent();
        if (extras.getExtras() != null) {

            fileArrayList = extras.getParcelableArrayListExtra("ImageDataFile");
            position = extras.getIntExtra("Position", 0);
            isStatus = extras.getExtras().getInt(WhatsappSavedFragment.IS_STATUS);

        }


        fileName = fileArrayList.get(position).getName().substring(12);
        initViews();

        if (new File(saveFilePath + fileName).exists()) {
            if (isStatus == 0) {

                imDelete.setImageResource(R.drawable.ic_saved);

            } else {
                imDelete.setImageResource(R.drawable.ic_delete_black_24dp);
            }

        }

    }

    public void initViews() {
        showImagesAdapter = new ShowImagesAdapter(this, fileArrayList, FullViewActivity.this);
        vpView.setAdapter(showImagesAdapter);
        vpView.setCurrentItem(position);
        vpView.setPageTransformer(true, new ZoomOutSlideTransformer());

        vpView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int arg, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FullViewActivity.this.position = position;

                fileName = fileArrayList.get(FullViewActivity.this.position).getName().substring(12);
                if (new File(saveFilePath + fileArrayList.get(FullViewActivity.this.position).getName().substring(12)).exists()) {

                    if (isStatus == 0) {

                        imDelete.setImageResource(R.drawable.ic_saved);

                    } else {

                        imDelete.setImageResource(R.drawable.ic_delete_black_24dp);

                    }

                } else {
                    if (isStatus == 0) {

                        imDelete.setImageResource(R.drawable.ic_download);

                    } else {

                        imDelete.setImageResource(R.drawable.ic_delete_black_24dp);

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (isStatus == 0) {

            imDelete.setImageResource(R.drawable.ic_download);
            imDelete.setOnClickListener(view -> {

                if (!new File(saveFilePath + fileName).exists()) {

                    downloadFile();

//                    showInterstitialAds(interstitialAd, googleAds);
                } else {
                    Toast.makeText(activity, "Already Downloaded", Toast.LENGTH_SHORT).show();
                }

            });

        } else {
            imDelete.setImageResource(R.drawable.ic_delete_black_24dp);
            imDelete.setOnClickListener(view -> {

                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setPositiveButton("Yes", (dialog, id) -> {
                    boolean b = new File(fileArrayList.get(position).getPath()).delete();
                    if (b) {
                        deleteFileAA(position);
                    }
                });

                ab.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = ab.create();
                alert.setTitle("Are you sure to delete it?");
                alert.setIcon(R.drawable.logo);
                alert.show();
            });

        }
        imShare.setOnClickListener(view -> {
            if (fileArrayList.get(position).getName().contains(".mp4")) {
                Log.d("SSSSS", "onClick: " + fileArrayList.get(position) + "");
                shareVideo(activity, fileArrayList.get(position).getPath());
            } else {
                shareImage(activity, fileArrayList.get(position).getPath());
            }

//            showInterstitialAds(interstitialAd, googleAds);

        });
        imWhatsappShare.setOnClickListener(view -> {
            shareImageVideoOnWhatsapp(activity, fileArrayList.get(position).getPath(), fileArrayList.get(position).getName().contains(".mp4"));

//            showInterstitialAds(interstitialAd, googleAds);

        });

        imClose.setOnClickListener(v -> onBackPressed());
    }


    public void downloadFile() {
        final String path = fileArrayList.get(position).getPath();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        final File file = new File(path);
        File destFile = new File(saveFilePath);


        try {

            FileUtils.copyFileToDirectory(file, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileNameChange = filename.substring(12);
        File newFile = new File(saveFilePath + fileNameChange);

        String contentType = "image/*";
        if (fileArrayList.get(position).getName().endsWith(".mp4")) {
            contentType = "video/*";
        } else {
            contentType = "image/*";
        }
        MediaScannerConnection.scanFile(activity, new String[]{newFile.getAbsolutePath()}, new String[]{contentType},
                new MediaScannerConnection.MediaScannerConnectionClient() {
                    public void onMediaScannerConnected() {
                    }

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });

        File from = new File(saveFilePath, filename);
        File to = new File(saveFilePath, fileNameChange);
        from.renameTo(to);

        imDelete.setImageResource(R.drawable.ic_saved);
        Toast.makeText(activity, "Saved!", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    public void deleteFileAA(int position) {
        fileArrayList.remove(position);
        showImagesAdapter.notifyDataSetChanged();
        setToast(activity, "File Deleted.");
        if (fileArrayList.isEmpty()) {
            onBackPressed();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        vpView = findViewById(R.id.vp_view);
        imClose = findViewById(R.id.im_close);
        imShare = findViewById(R.id.imShare);
        imWhatsappShare = findViewById(R.id.imWhatsappShare);
        imDelete = findViewById(R.id.imDelete);
    }


    public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;


        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }


        }
    }
}
