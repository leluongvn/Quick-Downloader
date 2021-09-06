package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;

import org.apache.commons.io.FileUtils;

import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    Dialog dialog;
    private Dialog dialogclear;
    private ImageView btnBack;
    private LinearLayout llRateUs;
    private LinearLayout llShare;
    private LinearLayout llPolicy;
    private LinearLayout llAppNotWorking;
    private LinearLayout disclaimer;
    private LinearLayout llFeedback;
    private LinearLayout clearApp;
    private Activity context;

    public void showDialog() {

        dialog = new Dialog(SettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_not_working);


        TextView textView = dialog.findViewById(R.id.tvOk);

        textView.setOnClickListener(view -> {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        });

        if (!dialog.isShowing())
            dialog.show();


    }


    public void showDialogDisclaimer() {

        Dialog dialog = new Dialog(SettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.disclaimer);

        TextView textView = dialog.findViewById(R.id.tvOk);

        textView.setOnClickListener(view -> {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        });

        if ( !dialog.isShowing())
            dialog.show();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        context = this;

        dialogclear = new Dialog(SettingActivity.this);
        dialogclear.setTitle("Clearing...");


        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(this);
        setLocale(appLangSessionManager.getLanguage(),this);

        btnBack.setOnClickListener(this);
        llRateUs.setOnClickListener(this);
        llShare.setOnClickListener(this);
        llPolicy.setOnClickListener(this);
        llAppNotWorking.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llRateUs.setOnClickListener(this);
        disclaimer.setOnClickListener(this);
        clearApp.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.llRateUs:
                Utils.rateApp(context);
                break;
            case R.id.llShare:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app_message) + getPackageName());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_image_via)));

                break;
            case R.id.llPolicy:

                startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(Utils.PRIVACY_POLICY_URL), "text/plain"));

                break;
            case R.id.llAppNotWorking:

                showDialog();

                break;


            case R.id.disclaimer:
                showDialogDisclaimer();
                break;

            case R.id.clearApp:

                clearData();

                break;

            case R.id.llFeedback:


                ShareCompat.IntentBuilder.from(this)
                        .setType("message/rfc822")
                        .addEmailTo(Utils.FEED_BACK_EMAIL)
                        .setSubject(getResources().getString(R.string.app_name) + " All Video Downloader Without Watermark")
                        .setChooserTitle("Email Us Via ")
                        .startChooser();

                break;

            default: break;
        }
    }


    public void clearData() {

        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                dialogclear.show();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                FileUtils.deleteQuietly(getCacheDir());
                FileUtils.deleteQuietly(getExternalCacheDir());
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                dialogclear.dismiss();
                Toast.makeText(SettingActivity.this, getString(R.string.cache_cleared), Toast.LENGTH_SHORT).show();
                super.onPostExecute(s);
            }
        }.execute();

    }

    private void initView() {
        btnBack = (ImageView) findViewById(R.id.btn_back);
        llRateUs = (LinearLayout) findViewById(R.id.llRateUs);
        llShare = (LinearLayout) findViewById(R.id.llShare);
        llPolicy = (LinearLayout) findViewById(R.id.llPolicy);
        llAppNotWorking = (LinearLayout) findViewById(R.id.llAppNotWorking);
        disclaimer = (LinearLayout) findViewById(R.id.disclaimer);
        llFeedback = (LinearLayout) findViewById(R.id.llFeedback);
        clearApp = (LinearLayout) findViewById(R.id.clearApp);
    }
}
