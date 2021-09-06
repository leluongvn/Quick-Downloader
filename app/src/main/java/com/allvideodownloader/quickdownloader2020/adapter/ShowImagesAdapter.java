package com.allvideodownloader.quickdownloader2020.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.allvideodownloader.quickdownloader2020.R;
import com.allvideodownloader.quickdownloader2020.activity.FullViewActivity;
import com.allvideodownloader.quickdownloader2020.activity.VideoPlayActivity;
import com.allvideodownloader.quickdownloader2020.model.WhatsappStatusModel;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ShowImagesAdapter extends PagerAdapter {
    FullViewActivity fullViewActivity;
    private Context context;
    private ArrayList<WhatsappStatusModel> imageList;
    private LayoutInflater inflater;

    public ShowImagesAdapter(Context context, ArrayList<WhatsappStatusModel> imageList1, FullViewActivity fullViewActivity) {
        this.context = context;
        this.imageList = imageList1;
        this.fullViewActivity = fullViewActivity;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.item_image_status_view, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.im_fullViewImage);
        final ImageView imVpPlay = imageLayout.findViewById(R.id.im_vpPlay);

        if (imageList != null) {

            if (imageList.get(position).getPath() != null) {

                Glide.with(context).load(imageList.get(position).getPath()).into(imageView);
                String extension = imageList.get(position).getName().substring(imageList.get(position).getName().lastIndexOf("."));

                if (extension.equals(".mp4")) {

                    imVpPlay.setVisibility(View.VISIBLE);

                } else {

                    imVpPlay.setVisibility(View.GONE);

                }


                imVpPlay.setOnClickListener(v -> {

                    Intent intent = new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("path", imageList.get(position).getPath());
                    intent.putExtra("name", imageList.get(position).getName());
                    context.startActivity(intent);


                });

            }

        }

        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }


}