package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.fragment.WhatsappSavedFragment;
import com.allvideodownloader.quickdownloader2020.fragment.WhatsappStatusFragment;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.allvideodownloader.quickdownloader2020.util.Utils.createFileFolder;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;

public class WhatsappActivity extends AppCompatActivity {

    Activity context;
    private WhatsappActivity activity;
    private ImageView imBack;
    private ImageView ivOpenWp;
    private TabLayout tabs;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whatsapp);
        initView();

        context = activity = this;


        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(this);
        setLocale(appLangSessionManager.getLanguage(),this);
//        Utils.onlyInitializeAds(context);
//
//        // Show banner Ads
//        Utils.showBannerAds(context);


        createFileFolder();
        initViews();

        ivOpenWp.setOnClickListener(v ->

                Utils.openApp(context, "com.whatsapp"));

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    private void initViews() {
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_sd_status);
        tabs.getTabAt(1).setIcon(R.drawable.ic_sd_saved);
        imBack.setOnClickListener(view -> onBackPressed());


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new WhatsappStatusFragment(), "Status");
        adapter.addFragment(new WhatsappSavedFragment(), "Saved");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }

    private void initView() {
        imBack = (ImageView) findViewById(R.id.imBack);
        ivOpenWp = (ImageView) findViewById(R.id.iv_open_wp);
        tabs = (TabLayout) findViewById(R.id.tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
