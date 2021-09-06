package com.allvideodownloader.quickdownloader2020.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.FullViewActivity;
import com.allvideodownloader.quickdownloader2020.adapter.WhatsappStatusAdapter;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.allvideodownloader.quickdownloader2020.util.Utils.ROOT_DOWNLOAD_DIRECTORY;

public class WhatsappSavedFragment extends Fragment implements WhatsappStatusAdapter.FileListClickInterface {

    public static final String IS_STATUS = "isStatus";
    ArrayList<File> fileArrayList;
    Activity context;
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    private TextView tvNoResult;
    private SwipeRefreshLayout swiperefresh;
    private RecyclerView rvFileList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initView(view);
        initViews();

        context = getActivity();


        return view;
    }

    private void initViews() {

        statusModelArrayList = new ArrayList<>();
        fileArrayList = new ArrayList<>();
        getData();

        swiperefresh.setOnRefreshListener(() -> {

            fileArrayList.clear();
            statusModelArrayList.clear();

            new Handler(Looper.getMainLooper()).postDelayed(this::getData, 100);

            swiperefresh.setRefreshing(false);
        });
    }

    private void getData() {
        WhatsappStatusModel whatsappStatusModel;
        String saveFilePath = ROOT_DOWNLOAD_DIRECTORY + "/Wp";
        File targetDirector = new File(saveFilePath);
        File[] allfiles = targetDirector.listFiles();

        if (allfiles != null)
            try {

                if (allfiles.length > 0) {
                    tvNoResult.setVisibility(View.GONE);
                } else {

                    tvNoResult.setVisibility(View.VISIBLE);
                }


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
                        if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                            whatsappStatusModel = new WhatsappStatusModel(file.getName(),
                                    Uri.fromFile(file),
                                    allfiles[i].getAbsolutePath(),
                                    file.getName());
                            statusModelArrayList.add(whatsappStatusModel);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    swiperefresh.setRefreshing(false);
                }


            } catch (Exception e) {
                e.printStackTrace();
                swiperefresh.setRefreshing(false);
            }

        WhatsappStatusAdapter whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList, this);
        rvFileList.setAdapter(whatsappStatusAdapter);

    }

    @Override
    public void getPosition(int position, File file) {
        Intent inNext = new Intent(getActivity(), FullViewActivity.class);
        inNext.putExtra("ImageDataFile", statusModelArrayList);
        inNext.putExtra(IS_STATUS, 1);
        inNext.putExtra("Position", position);
        startActivity(inNext);

    }

    private void initView(View view) {
        tvNoResult = view.findViewById(R.id.tv_NoResult);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        rvFileList = view.findViewById(R.id.rv_fileList);
    }
}
