package com.allvideodownloader.quickdownloader2020.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.VideoPlayActivity;
import com.allvideodownloader.quickdownloader2020.interfaces.FileListClickInterface;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;


public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<WhatsappStatusModel> fileArrayList;
    private LayoutInflater layoutInflater;
    private final FileListClickInterface fileListClickInterface;

    public FileListAdapter(Context context, ArrayList<WhatsappStatusModel> files, FileListClickInterface fileListClickInterface) {
        this.context = context;
        this.fileArrayList = files;
        this.fileListClickInterface = fileListClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        View view = layoutInflater.inflate(R.layout.items_file_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        File fileItem = new File(fileArrayList.get(i).getPath());

        try {
            String extension = fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
            if (extension.equals(".mp4")) {
                viewHolder.ivPlay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ivPlay.setVisibility(View.GONE);
            }


            viewHolder.ivPlay.setOnClickListener(view ->  {



                Intent intent = new Intent(context, VideoPlayActivity.class);
                intent.putExtra("path", fileItem.getPath());
                intent.putExtra("name", fileItem.getName());
                context.startActivity(intent);


            });

            Glide.with(context)
                    .load(fileItem.getPath())
                    .into(viewHolder.ivImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        viewHolder.rlMain.setOnClickListener(v -> fileListClickInterface.getPosition(i, fileItem));
    }


    @Override
    public int getItemCount() {
        return fileArrayList == null ? 0 : fileArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout rlMain;
        private final ImageView ivImage;
        private final ImageView ivPlay;

        public ViewHolder(View view) {
            super(view);
            rlMain = view.findViewById(R.id.rl_main);
            ivImage = view.findViewById(R.id.iv_image);
            ivPlay = view.findViewById(R.id.iv_play);

        }
    }
}