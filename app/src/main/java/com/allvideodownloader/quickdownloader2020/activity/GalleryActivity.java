package com.allvideodownloader.quickdownloader2020.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.adapter.FileListAdapter;
import com.allvideodownloader.quickdownloader2020.interfaces.FileListClickInterface;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
import com.allvideodownloader.quickdownloader2020.util.AppLangSessionManager;
import com.allvideodownloader.quickdownloader2020.util.Utils;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.allvideodownloader.quickdownloader2020.fragment.WhatsappSavedFragment.IS_STATUS;
import static com.allvideodownloader.quickdownloader2020.util.Utils.ROOT_DOWNLOAD_DIRECTORY;
import static com.allvideodownloader.quickdownloader2020.util.Utils.setLocale;

public class GalleryActivity extends AppCompatActivity implements FileListClickInterface {
    Activity context;
    private ArrayList<File> fileArrayList;
    private GalleryActivity activity;
    private ImageView imBack;
    private TextView tvNoResult;
    private SwipeRefreshLayout swiperefresh;
    private RecyclerView rvFileList;
    private ArrayList<WhatsappStatusModel> statusModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initView();
        context = activity = this;
        initViews();

        AppLangSessionManager appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage(), this);

//        Utils.onlyInitializeAds(context);

        // Show banner Ads
//        Utils.showBannerAds(context);

        imBack.setOnClickListener(v -> onBackPressed());

    }

    private void initViews() {

        getAllFiles();

        swiperefresh.setOnRefreshListener(() -> {
            getAllFiles();
            swiperefresh.setRefreshing(false);
        });


    }

    private void getAllFiles() {

        WhatsappStatusModel whatsappStatusModel;
        fileArrayList = new ArrayList<>();
        File directory = ROOT_DOWNLOAD_DIRECTORY;
        if (!directory.exists())
            directory.mkdirs();

        File[] files = ROOT_DOWNLOAD_DIRECTORY.listFiles();

        if (files != null) {
            if (files.length > 0) {
                tvNoResult.setVisibility(View.GONE);
            } else {

                tvNoResult.setVisibility(View.VISIBLE);
            }

            Arrays.sort(files, (a, b) -> {
                if (a.lastModified() < b.lastModified())
                    return 1;
                if (a.lastModified() > b.lastModified())
                    return -1;
                return 0;
            });

            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                fileArrayList.add(file);

                whatsappStatusModel = new WhatsappStatusModel(file.getName(),
                        Uri.fromFile(file),
                        fileArrayList.get(i).getPath(),
                        file.getName());
                statusModelArrayList.add(whatsappStatusModel);


            }
            FileListAdapter fileListAdapter = new FileListAdapter(activity, statusModelArrayList, activity);
            rvFileList.setAdapter(fileListAdapter);

        }
    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(activity, FullViewActivity.class);
        inNext.putExtra("ImageDataFile", statusModelArrayList);
        inNext.putExtra(IS_STATUS, 1);
        inNext.putExtra("Position", position);
        startActivity(inNext);
    }


    private void initView() {
        imBack = findViewById(R.id.imBack);
        tvNoResult = findViewById(R.id.tv_NoResult);
        swiperefresh = findViewById(R.id.swiperefresh);
        rvFileList = findViewById(R.id.rv_fileList);
    }
}
