package com.veeritsolutions.uhelpme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by VEER7 on 6/19/2017.
 */

public class AdpHelpNeeded extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostedJobModel> postedJobList;
    private Context context;
    protected boolean showLoader;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;
    private View itemView;

    public AdpHelpNeeded(Context context, ArrayList<PostedJobModel> postedJobList, boolean showLoader) {
        this.postedJobList = postedJobList;
        this.context = context;
        this.showLoader = showLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new VHFooter(itemView);
        } else if (viewType == TYPE_ITEM) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_needed_help, parent, false);

            return new MyViewHolder(itemView);
        }
        throw new RuntimeException("there is no type matches" + viewType + "\n make sure you are using the correct type");

    }

    @SuppressLint("SetTextI18n")
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

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            PostedJobModel postedJobModel = postedJobList.get(position);
            postedJobModel.setPosition(position);
            myViewHolder.btnDecline.setTag(postedJobModel);
            myViewHolder.tvOfferTitle.setTag(postedJobModel);
            myViewHolder.imgOfferSearch.setTag(postedJobModel);
            myViewHolder.imgOfferDp.setTag(R.id.img_offer, postedJobModel);
            // StateListDrawable stateListDrawable = (StateListDrawable) myViewHolder.dataView.getBackground();
            // stateListDrawable.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null), PorterDuff.Mode.SRC_ATOP);

            GradientDrawable bgShape0 = (GradientDrawable) myViewHolder.view.getBackground();
            bgShape0.setColor(Color.parseColor(postedJobModel.getColorCode()));

            // StateListDrawable stateListDrawable = (StateListDrawable) myViewHolder.dataView.getBackground();
            // stateListDrawable.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null), PorterDuff.Mode.SRC_ATOP);
            GradientDrawable gd1 = new GradientDrawable();
            //gd.setColor(Color.RED);
            gd1.setCornerRadius(50);
            gd1.setColor(Color.parseColor(postedJobModel.getColorCode()));
            myViewHolder.btnMoreDetails.setBackground(gd1);
//            StateListDrawable bgShape = (StateListDrawable) myViewHolder.btnMoreDetails.getBackground();
//            bgShape.setColorFilter(Color.parseColor(postedJobModel.getColorCode()), PorterDuff.Mode.SRC_ATOP);
            // bgShape.setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null));
            GradientDrawable gd = new GradientDrawable();
            //gd.setColor(Color.RED);
            gd.setCornerRadius(50);
            gd.setStroke(2, Color.parseColor(postedJobModel.getColorCode()));
            myViewHolder.btnDecline.setBackground(gd);
            myViewHolder.btnDecline.setTextColor(Color.parseColor(postedJobModel.getColorCode()));

            //String str = "Displayed less than " + 1 + " minutes ago " + "<b>" + postedJobList.get(position) + "</b>";

            //bgShape1.setColor(ResourcesCompat.getColor(context.getResources(), R.color.colorViolet, null));


            myViewHolder.btnMoreDetails.setTag(postedJobModel);

            myViewHolder.tvOfferTitle.setText(postedJobModel.getJobTitle());
            myViewHolder.tvOfferDetails.setText(postedJobModel.getJobDescription());
            myViewHolder.tvOfferTimeLocation.setText(postedJobModel.getJobPostTimeDiff());
            myViewHolder.tvOfferAmount.setText("$ " + postedJobModel.getJobAmount());
            Debug.trace("photoUrl", postedJobModel.getJobPhoto());
            Utils.setImage(postedJobModel.getJobPhoto(), R.drawable.img_launcher_icon, myViewHolder.imgOfferDp);

            myViewHolder.imgOfferSearch.setColorFilter(Color.parseColor(postedJobModel.getColorCode()));
        }

    }

    public void animateView() {

        ViewCompat.animate(itemView)
                .translationY(-itemView.getHeight() * 0.3f)
                .alpha(0)
                .setDuration(1000)
                //.setListener(listener)
                .start();
    }

    @Override
    public int getItemViewType(int position) {

       /* if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }*/
        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;

    }

    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (postedJobList == null || postedJobList.size() == 0) {
            return 0;
        }

        return postedJobList.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        ImageView imgOfferDp, imgOfferSearch;
        TextView tvOfferTitle, tvOfferTimeLocation, tvOfferDetails, tvOfferAmount;
        Button btnMoreDetails, btnDecline;


        MyViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view_strip);
            imgOfferDp = (ImageView) itemView.findViewById(R.id.img_offer);

            imgOfferSearch = (ImageView) itemView.findViewById(R.id.img_offer_search);

            tvOfferTitle = (TextView) itemView.findViewById(R.id.tv_offer_title);
            tvOfferTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvOfferTimeLocation = (TextView) itemView.findViewById(R.id.tv_offer_time_location);
            tvOfferTimeLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);

            tvOfferAmount = (TextView) itemView.findViewById(R.id.tv_offer_Amount);
            tvOfferAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_LIGHT);


            tvOfferDetails = (TextView) itemView.findViewById(R.id.tv_offer_detail);
            tvOfferDetails.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            btnMoreDetails = (Button) itemView.findViewById(R.id.btn_offer_more_detail);
            btnMoreDetails.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            btnDecline = (Button) itemView.findViewById(R.id.btn_help_decline);
            btnDecline.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        }
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        CardView footerView;

        VHFooter(View itemView) {
            super(itemView);
            this.footerView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    public void showLoading(boolean status) {
        showLoader = status;
//        notifyDataSetChanged();
    }

    public void refreshList(ArrayList<PostedJobModel> postedJobList) {
        this.postedJobList = postedJobList;
        notifyDataSetChanged();

    }
}
