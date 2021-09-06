package com.allvideodownloader.quickdownloader2020.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.ChingariActivity;
import com.allvideodownloader.quickdownloader2020.activity.FacebookActivity;
import com.allvideodownloader.quickdownloader2020.activity.InstagramActivity;
import com.allvideodownloader.quickdownloader2020.activity.JoshActivity;
import com.allvideodownloader.quickdownloader2020.activity.MojActivity;
import com.allvideodownloader.quickdownloader2020.activity.MxTakatakActivity;
import com.allvideodownloader.quickdownloader2020.activity.RoposoActivity;
import com.allvideodownloader.quickdownloader2020.activity.SharechatActivity;
import com.allvideodownloader.quickdownloader2020.activity.TikTokActivity;
import com.allvideodownloader.quickdownloader2020.activity.TwitterActivity;
import com.allvideodownloader.quickdownloader2020.activity.WhatsappActivity;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;

public class AppsFragment extends Fragment implements View.OnClickListener {


    private static final String COPY_INTENT_TAG = "CopyIntent";
    Activity context;
    String copyKey = "";
    String copyValue = "";
    private EditText etText;
    private ImageView downloadBtn;
    private CardView instagram;
    private CardView wp;
    private CardView fb;
    private CardView twitter;
    private CardView roposo;
    private CardView takatak;
    private CardView sharechat;
    private CardView moj;
    private CardView josh;
    private CardView tiktok;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apps, container, false);

        initView(view);

        context = getActivity();
        initializeAds();

        downloadBtn.setOnClickListener(v -> {

            String text = etText.getText().toString();

            if (text.equals("") || !Utils.checkURL(text)) {
                Toast.makeText(context, "Please enter your url", Toast.LENGTH_SHORT).show();

            } else {

                callText(text.trim());

                Utils.showInterstitialAds(interstitialAd, googleAds);


            }

        });


        tiktok.setOnClickListener(this);
        wp.setOnClickListener(this);
        instagram.setOnClickListener(this);
        josh.setOnClickListener(this);
        roposo.setOnClickListener(this);
        takatak.setOnClickListener(this);
        twitter.setOnClickListener(this);
        fb.setOnClickListener(this);
        sharechat.setOnClickListener(this);
        moj.setOnClickListener(this);

        return view;
    }

    public void initViews() {
        if (context.getIntent().getExtras() != null) {
            for (String key : context.getIntent().getExtras().keySet()) {
                copyKey = key;
                String value = context.getIntent().getExtras().getString(copyKey);
                if (copyKey.equals("android.intent.extra.TEXT")) {
                    copyValue = context.getIntent().getExtras().getString(copyKey);
                    callText(value);
                } else {
                    copyValue = "";
                    callText(value);
                }
            }
        }

        createFileFolder();

    }

    private void callText(String copiedText) {

        startAppsActivity(copiedText);

    }

    public void startAppsActivity(String url) {


        if (url.contains("sharechat")) {

            startActivity(new Intent(context, SharechatActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));
        } else if (url.contains("instagram")) {
            startActivity(new Intent(context, InstagramActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("facebook")) {
            startActivity(new Intent(context, FacebookActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("tiktok")) {
            startActivity(new Intent(context, TikTokActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("twitter")) {
            startActivity(new Intent(context, TwitterActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("myjosh")) {
            startActivity(new Intent(context, JoshActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("chingari")) {
            startActivity(new Intent(context, ChingariActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("roposo.com")) {
            startActivity(new Intent(context, RoposoActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("likee")) {
            startActivity(new Intent(context, MxTakatakActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("mxtakatak")) {
            startActivity(new Intent(context, MxTakatakActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else if (url.contains("moj")) {
            startActivity(new Intent(context, MojActivity.class)
                    .putExtra(COPY_INTENT_TAG, url));

        } else {

            Toast.makeText(context, "Url is not supported", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tiktok:
                startActivity(new Intent(getActivity(), TikTokActivity.class));

                break;
            case R.id.wp:
                startActivity(new Intent(getActivity(), WhatsappActivity.class));

                break;
            case R.id.roposo:
                startActivity(new Intent(getActivity(), RoposoActivity.class));

                break;
            case R.id.josh:
                startActivity(new Intent(getActivity(), JoshActivity.class));

                break;

            case R.id.sharechat:
                startActivity(new Intent(getActivity(), SharechatActivity.class));

                break;


            case R.id.fb:

                startActivity(new Intent(getActivity(), FacebookActivity.class));

                break;
            case R.id.instagram:
                startActivity(new Intent(getActivity(), InstagramActivity.class));

                break;
            case R.id.twitter:

                startActivity(new Intent(getActivity(), TwitterActivity.class));

                break;
            case R.id.moj:

                startActivity(new Intent(getActivity(), MojActivity.class));


                break;
            case R.id.takatak:

                startActivity(new Intent(getActivity(), MxTakatakActivity.class));


                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        etText = (EditText) view.findViewById(R.id.et_text);
        downloadBtn = (ImageView) view.findViewById(R.id.downloadBtn);
        instagram = (CardView) view.findViewById(R.id.instagram);
        wp = (CardView) view.findViewById(R.id.wp);
        fb = (CardView) view.findViewById(R.id.fb);
        twitter = (CardView) view.findViewById(R.id.twitter);
        roposo = (CardView) view.findViewById(R.id.roposo);
        takatak = (CardView) view.findViewById(R.id.takatak);
        sharechat = (CardView) view.findViewById(R.id.sharechat);
        moj = (CardView) view.findViewById(R.id.moj);
        josh = (CardView) view.findViewById(R.id.josh);
        tiktok = (CardView) view.findViewById(R.id.tiktok);
    }
}
