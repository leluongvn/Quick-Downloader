package com.allvideodownloader.quickdownloader2020.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.FullViewActivity;
import com.allvideodownloader.quickdownloader2020.adapter.WhatsappStatusAdapter;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
import com.allvideodownloader.quickdownloader2020.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.allvideodownloader.quickdownloader2020.fragment.WhatsappSavedFragment.IS_STATUS;

public class WhatsappStatusFragment extends Fragment implements WhatsappStatusAdapter.FileListClickInterface {

    ArrayList<File> fileArrayList;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    private RecyclerView rvFileList;
    private SwipeRefreshLayout swiperefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);

        initViews();

        return view;
    }


    private void initViews() {

        fileArrayList = new ArrayList<>();
        statusModelArrayList = new ArrayList<>();
        getData();

        swiperefresh.setOnRefreshListener(() -> {

            fileArrayList.clear();
            statusModelArrayList.clear();
            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 10);

            swiperefresh.setRefreshing(false);
        });
    }

    private void getData() {
        WhatsappStatusModel whatsappStatusModel;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses";
        File targetDirector = new File(targetPath);
        File[] allfiles = targetDirector.listFiles();


        String targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/.Statuses";
        File targetDirectorBusiness = new File(targetPathBusiness);
        File[] allfilesBusiness = targetDirectorBusiness.listFiles();


        if (allfiles != null) {
            try {
                Arrays.sort(allfiles, (o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified()) {
                        return -1;
                    } else if (o1.lastModified() < o2.lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                });

                for (int i = 0; i < allfiles.length; i++) {
                    File file = allfiles[i];
                    if (Uri.fromFile(file).toString().endsWith(".mp4") || Uri.fromFile(file).toString().endsWith(".png") && Uri.fromFile(file).toString().endsWith(".jpg")) {
                        whatsappStatusModel = new WhatsappStatusModel(file.getName(),
                                Uri.fromFile(file),
                                allfiles[i].getAbsolutePath(),
                                file.getName());
                        statusModelArrayList.add(whatsappStatusModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Arrays.sort(allfilesBusiness, (o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified()) {
                        return -1;
                    } else if (o1.lastModified() < o2.lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                });

                for (int i = 0; i < allfilesBusiness.length; i++) {
                    File file = allfilesBusiness[i];
                    if (Uri.fromFile(file).toString().endsWith(".mp4") || Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                        whatsappStatusModel = new WhatsappStatusModel(file.getName(),
                                Uri.fromFile(file),
                                allfilesBusiness[i].getAbsolutePath(),
                                file.getName());
                        statusModelArrayList.add(whatsappStatusModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        WhatsappStatusAdapter whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList, this);
        Utils.setUpRecycler(rvFileList);
        rvFileList.setAdapter(whatsappStatusAdapter);

    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(getActivity(), FullViewActivity.class);
        inNext.putExtra("ImageDataFile", statusModelArrayList);
        inNext.putExtra(IS_STATUS, 0);
        inNext.putExtra("Position", position);
        startActivity(inNext);

    }

    private void initView(View view) {
        rvFileList = view.findViewById(R.id.rv_fileList);
        swiperefresh = view.findViewById(R.id.swiperefresh);
    }
}
