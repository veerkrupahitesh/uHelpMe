package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by Admin on 7/1/2017.
 */

public class AdpMyHelpMe extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<PostedJobModel> jobPostSeekerList;
    private boolean showLoader;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    public AdpMyHelpMe(Context mContext, ArrayList<PostedJobModel> jobPostSeekerList, boolean showLoader) {
        this.context = mContext;
        this.jobPostSeekerList = jobPostSeekerList;
        this.showLoader = showLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new VHFooter(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_dashboard, parent, false);

            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("there is no type matches" + viewType + "\n make sure you are using the correct type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHFooter) {
            final VHFooter myViewHolder1 = (VHFooter) holder;

            if (showLoader) {
                myViewHolder1.footerView.setVisibility(View.VISIBLE);
            } else {
                myViewHolder1.footerView.setVisibility(View.GONE);
            }
        } else if (holder instanceof MyViewHolder) {

            PostedJobModel postedJobModel = jobPostSeekerList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;

            GradientDrawable bgShape0 = (GradientDrawable) myViewHolder.view.getBackground();
            bgShape0.setColor(Color.parseColor(postedJobModel.getColorCode()));

            myViewHolder.imgHelpChat.setTag(postedJobModel);
            myViewHolder.imgHelpEdit.setTag(postedJobModel);
            myViewHolder.imgHelpInfo.setTag(postedJobModel);

            myViewHolder.txvName.setText(postedJobModel.getJobTitle());
            myViewHolder.txvDescription.setText(postedJobModel.getJobDescription());
            // myViewHolder.txvDescription.setText("Applicants are ");

            Utils.setImage(postedJobModel.getJobPhoto(), R.drawable.img_launcher_icon, myViewHolder.imgHelpPhoto);

            String str = postedJobModel.getColorCode();
            String colorCode = new StringBuffer(str).insert(1, "0D").toString();
            Debug.trace("colorCode", colorCode);
            myViewHolder.reCategoryList.setBackgroundColor(Color.parseColor(colorCode));

        }
    }


    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (jobPostSeekerList == null || jobPostSeekerList.size() == 0) {
            return 0;
        }

        return jobPostSeekerList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;

    }

    public void refreshList(ArrayList<PostedJobModel> jobPostSeekerList) {
        this.jobPostSeekerList = jobPostSeekerList;
        notifyDataSetChanged();
    }

    public void showLoading(boolean showloader) {
        this.showLoader = showloader;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView txvName, txvDescription;
        ImageView imgHelpPhoto, imgHelpInfo, imgHelpEdit, imgHelpChat;
        RelativeLayout reCategoryList;

        MyViewHolder(View itemView) {
            super(itemView);

            imgHelpPhoto = (ImageView) itemView.findViewById(R.id.img_HeaderProfilePhoto);
            imgHelpInfo = (ImageView) itemView.findViewById(R.id.img_InfoIcon);
            imgHelpEdit = (ImageView) itemView.findViewById(R.id.img_EditIcon);
            imgHelpChat = (ImageView) itemView.findViewById(R.id.img_ChatIcon);

            view = itemView.findViewById(R.id.view_strip);

            txvName = (TextView) itemView.findViewById(R.id.txv_offer_title);
            txvName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            txvDescription = (TextView) itemView.findViewById(R.id.txv_offer_title1);
            txvDescription.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            reCategoryList = (RelativeLayout) itemView.findViewById(R.id.rel_categoryList);
        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        CardView footerView;

        VHFooter(View itemView) {
            super(itemView);
            this.footerView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
