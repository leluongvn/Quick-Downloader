package com.allvideodownloader.quickdownloader2020.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.VideoPlayActivity;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;


public class WhatsappStatusAdapter extends RecyclerView.Adapter<WhatsappStatusAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<WhatsappStatusModel> fileArrayList;
    private final FileListClickInterface fileListClickInterface;
    private LayoutInflater layoutInflater;
    private ImageView ivImage;
    private ImageView ivPlay;

    public WhatsappStatusAdapter(Context context, ArrayList<WhatsappStatusModel> files, FileListClickInterface fileListClickInterface) {
        this.context = context;
        this.fileListClickInterface = fileListClickInterface;
        this.fileArrayList = files;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }

        View view = layoutInflater.inflate(R.layout.items_whatsapp_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        File fileItem = new File(fileArrayList.get(i).getPath());

        if (fileItem.getAbsolutePath().endsWith(".mp4")) {
            ivPlay.setVisibility(View.VISIBLE);
        } else {
            ivPlay.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(fileItem.getAbsolutePath())
                .into(ivImage);

        ivPlay.setOnClickListener(view -> {

            Intent intent = new Intent(context, VideoPlayActivity.class);
            intent.putExtra("path", fileItem.getAbsolutePath());
            intent.putExtra("name", fileItem.getName());
            context.startActivity(intent);


        });

        viewHolder.itemView.setOnClickListener(view -> fileListClickInterface.getPosition(i, fileItem));

    }

    @Override
    public int getItemCount() {
        return fileArrayList == null ? 0 : fileArrayList.size();
    }

    private void initView(View view) {
        ivImage = view.findViewById(R.id.iv_image);
        ivPlay = view.findViewById(R.id.iv_play);
    }

    public interface FileListClickInterface {
        void getPosition(int position, File file);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);

        }
    }
}