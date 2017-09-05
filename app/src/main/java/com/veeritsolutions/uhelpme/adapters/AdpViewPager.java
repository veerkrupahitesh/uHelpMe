package com.veeritsolutions.uhelpme.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;

import java.util.ArrayList;

/**
 * Created by hitesh on 05-09-2017.
 */

public class AdpViewPager extends PagerAdapter {

    ArrayList<String> picList;

    public AdpViewPager(ArrayList<String> picList) {
        this.picList = picList;
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.list_item_image_viewpager, container, false);

        String path = picList.get(position);
        ImageView imgHelpPhoto = (ImageView) view.findViewById(R.id.img_help_ProfilePic);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pg_helpPhoto);
        Glide.with(MyApplication.getInstance())
                .load(path)
                .placeholder(R.color.colorHint)
                .error(R.color.colorHint)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgHelpPhoto);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}