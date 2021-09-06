package com.allvideodownloader.quickdownloader2020.activity

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.allvideodownloader.quickdownloader2020.R
import com.allvideodownloader.quickdownloader2020.tasks.DownloadVideoFile
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager
import com.allvideodownloader.quickdownloader2020.util.Utils
import com.facebook.ads.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class SharechatActivity : AppCompatActivity() {


    private var appLangSessionManager: AppLangSessionManager?=null
    private var context: Activity? = null;

    private lateinit var mImBack: ImageView
    private lateinit var mTvToolbarText: TextView
    private lateinit var mHelp: ImageView
    private lateinit var mIvAppIcon: ImageView
    private lateinit var mTitle: TextView
    private lateinit var mLLOpenApp: RelativeLayout
    private lateinit var mRLTopLayout: RelativeLayout
    private lateinit var mEtText: EditText
    private lateinit var mDownloadBtn: Button
    private lateinit var mRLEdittextLayout: RelativeLayout
    private lateinit var mLnrMain: LinearLayout


//    private lateinit var adView: AdView
    private lateinit var fbContainer: LinearLayout
    private lateinit var unityBannerView: RelativeLayout
//    private var interstitialAd: InterstitialAd? = null
//    private var googleAds: com.google.android.gms.ads.InterstitialAd? = null



//    fun initializeAds() {
//        AudienceNetworkAds.initialize(context)
//        MobileAds.initialize(context)
//        interstitialAd = InterstitialAd(context, getString(R.string.fb_inter_home))
//        interstitialAd!!.loadAd(interstitialAd!!.buildLoadAdConfig().withAdListener(object : InterstitialAdListener {
//            override fun onInterstitialDisplayed(ad: Ad) {}
//            override fun onInterstitialDismissed(ad: Ad) {}
//            override fun onError(ad: Ad, adError: AdError) {
//                googleAds = com.google.android.gms.ads.InterstitialAd(context)
//                googleAds!!.adUnitId = context!!.getString(R.string.Admob_inter_home)
//                googleAds!!.loadAd(AdRequest.Builder().addTestDevice(Utils.ADMOB_TEST_ID).build())
//            }
//
//            override fun onAdLoaded(ad: Ad) {}
//            override fun onAdClicked(ad: Ad) {}
//            override fun onLoggingImpression(ad: Ad) {}
//        }).build())
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_other_apps)

        context = this;
        initView()

        appLangSessionManager = AppLangSessionManager(this)
        Utils.setLocale(appLangSessionManager!!.getLanguage(), this);
//        initializeAds()
        Utils.showBannerAds(context)


        val urlIntentData: String? = intent.getStringExtra("CopyIntent")

        if (!urlIntentData.isNullOrBlank()) {


            mEtText.setText(urlIntentData)
        }

        mTvToolbarText.setText("Downloader for ShareChat")
        mIvAppIcon.setImageResource(R.drawable.sharechat)

        mTitle.setText("Sharechat")

        mDownloadBtn.setOnClickListener {


            startDownloading(mEtText.text.toString())

//            Utils.showInterstitialAds(interstitialAd, googleAds)

        }

        mLLOpenApp.setOnClickListener {


            Utils.openApp(context, "in.mohalla.sharechat")

        }

        mImBack.setOnClickListener {

            onBackPressed()
//            Utils.showInterstitialAds(interstitialAd, googleAds)

        }


    }


    fun initView() {
        mImBack = findViewById(R.id.imBack) as ImageView
        mTvToolbarText = findViewById(R.id.tv_toolbar_text) as TextView
        mHelp = findViewById(R.id.help) as ImageView
        mIvAppIcon = findViewById(R.id.app_logo) as ImageView
        mTitle = findViewById(R.id.title) as TextView
        mLLOpenApp = findViewById(R.id.LLOpenApp) as RelativeLayout
        mRLTopLayout = findViewById(R.id.RLTopLayout) as RelativeLayout
        mEtText = findViewById(R.id.et_text) as EditText
        mDownloadBtn = findViewById(R.id.downloadBtn) as Button
        mRLEdittextLayout = findViewById(R.id.RLEdittextLayout) as RelativeLayout
        mLnrMain = findViewById(R.id.lnr_main) as LinearLayout

//        adView = findViewById(R.id.adView) as AdView
        fbContainer = findViewById(R.id.fb_container) as LinearLayout
        unityBannerView = findViewById(R.id.unityBannerView) as RelativeLayout


    }


    fun startDownloading(url: String) {

        if (url.equals("") && Utils.checkURL(url)) {

            Utils.showToast(context, "Please enter a Valid Url")

        } else {

            var myurl = url
            try {
                myurl = myurl.substring(myurl.indexOf("http")).trim() ?: myurl
            } catch (e: Exception) {

                e.printStackTrace()
            }

            DownloadVideoFile.Start(context!!, myurl, false)
        }


    }


}