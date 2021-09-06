package com.allvideodownloader.quickdownloader2020.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.fragment.AppsFragment;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocaleMain;

public class MainActivity extends AppCompatActivity {
    MainActivity activity;
    boolean doubleBackToExitPressedOnce = false;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    String copyKey = "";
    String copyValue = "";
    Activity context;
    AppLangSessionManager appLangSessionManager;
    private BottomNavigationView navigationView;
    private String copyIntent = "CopyIntent";
    private ImageView translate;
    private BottomSheetDialog dialogLanguage;

    public void languageDialog() {
        dialogLanguage = new BottomSheetDialog(MainActivity.this, R.style.SheetDialog);
        dialogLanguage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLanguage.setContentView(R.layout.dialog_language);
        final TextView tvEnglish = dialogLanguage.findViewById(R.id.tv_english);
        final TextView tvHindi = dialogLanguage.findViewById(R.id.tv_hindi);
        final TextView tvCancel = dialogLanguage.findViewById(R.id.tv_cancel);
        tvEnglish.setOnClickListener(view -> {
            setLocaleMain("en", context);
            appLangSessionManager.setLanguage("en");
        });
        tvHindi.setOnClickListener(view -> {
            setLocaleMain("hi", context);
            appLangSessionManager.setLanguage("hi");
        });

        tvCancel.setOnClickListener(view -> dialogLanguage.dismiss());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        context = this;

        initView();
        showPermission();
        languageDialog();
        initViews();

        appLangSessionManager = new AppLangSessionManager(activity);


        if (appLangSessionManager.getLanguage() != null) {

            setLocale(appLangSessionManager.getLanguage(), this);

        }


        translate.setOnClickListener(view -> dialogLanguage.show());


        loadFragment(new AppsFragment());

        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.navigation_apps:
                    loadFragment(new AppsFragment());
                    return true;
                case R.id.navigation_downloads:
                    startActivity(new Intent(getApplicationContext(), GalleryActivity.class));
                    return true;
                case R.id.navigation_settings:

                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    return true;

                default:
                    loadFragment(new AppsFragment());

            }

            return false;
        });


    }

    private void showPermission() {

        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(activity, "Permission not Granted. Please Give us the Permission.", Toast.LENGTH_SHORT).show();

                    }
                }).check();
    }

    public void loadFragment(Fragment fragment) {


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

    }


    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
    }

    public void initViews() {
        if (activity.getIntent().getExtras() != null) {
            for (String key : activity.getIntent().getExtras().keySet()) {
                copyKey = key;
                String value = activity.getIntent().getExtras().getString(copyKey);
                if (copyKey.equals("android.intent.extra.TEXT")) {
                    copyValue = activity.getIntent().getExtras().getString(copyKey);
                    copyValue = Utils.extractLinks(copyValue);
                    callText(copyValue);
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

    @Override
    public void onBackPressed() {


        if (navigationView.getSelectedItemId() == R.id.navigation_apps) {

            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            this.doubleBackToExitPressedOnce = true;
            Utils.setToast(activity, "Please click BACK again to exit");
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            navigationView.setSelectedItemId(R.id.navigation_apps);
        }


    }

    public void startAppsActivity(String url) {

        if (url.contains("sharechat")) {

            startActivity(new Intent(context, SharechatActivity.class)
                    .putExtra(copyIntent, url));
        } else if (url.contains("instagram")) {
            startActivity(new Intent(context, InstagramActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("facebook")) {
            startActivity(new Intent(context, FacebookActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("tiktok")) {
            startActivity(new Intent(context, TikTokActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("twitter")) {
            startActivity(new Intent(context, TwitterActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("myjosh")) {
            startActivity(new Intent(context, JoshActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("chingari")) {
            startActivity(new Intent(context, ChingariActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("roposo.com")) {
            startActivity(new Intent(context, RoposoActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("mxtakatak")) {
            startActivity(new Intent(context, MxTakatakActivity.class)
                    .putExtra(copyIntent, url));

        } else if (url.contains("moj")) {
            startActivity(new Intent(context, MojActivity.class)
                    .putExtra(copyIntent, url));

        } else {

            Toast.makeText(context, "Url is not supported", Toast.LENGTH_SHORT).show();

        }


    }


    private void initView() {

        translate = findViewById(R.id.translate);
    }
}
