package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Created by Admin on 7/4/2017.
 */

public class AdpMyOffers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<PostedJobModel> list;
    //private ArrayList<String> Time;
    // private ArrayList<String> Amount;
    private boolean showLoader;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;

    public AdpMyOffers(Context mContext, ArrayList<PostedJobModel> list, boolean showLoader) {
        this.context = mContext;
        this.list = list;
        this.showLoader = showLoader;
        //this.Time = time;
        //this.Amount = amount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new VHFooter(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_dashboard_as_helper, parent, false);

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

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            PostedJobModel myOfferModel = list.get(position);

            myViewHolder.imgHelpChat.setTag(myOfferModel);
            myViewHolder.imgHelpEdit.setTag(myOfferModel);
            myViewHolder.imgHelpInfo.setTag(myOfferModel);
            myViewHolder.imgHelpPhoto.setTag(R.id.img_HeaderProfilePhoto, myOfferModel);

            GradientDrawable bgShape0 = (GradientDrawable) myViewHolder.view.getBackground();
            bgShape0.setColor(Color.parseColor(myOfferModel.getColorCode()));

            myViewHolder.tvHelpName.setText(myOfferModel.getJobTitle());
            // holder1.tvTimeLabel.setText(Time.get(0));
            myViewHolder.tvTimeRemaining.setText(myOfferModel.getCreatedOn());
            //holder1.btnAmount.setText(Amount.get(0));
            myViewHolder.btnAmount.setText("$ " + myOfferModel.getMyOfferAmount());

            Utils.setImage(myOfferModel.getJobPhoto(), R.drawable.img_launcher_icon, myViewHolder.imgHelpPhoto);

            String str = myOfferModel.getColorCode();
            String colorCode = new StringBuffer(str).insert(1, "0D").toString();
            Debug.trace("colorCode", colorCode);
            myViewHolder.linCategory.setBackgroundColor(Color.parseColor(colorCode));
        }

    }

    @Override
    public int getItemCount() {

        // If no items are present, there's no need for loader
        if (list == null || list.size() == 0) {
            return 0;
        }

        return list.size() + 1;
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

    public void refreshList(ArrayList<PostedJobModel> jobPostSeekerArrayList) {
        this.list = jobPostSeekerArrayList;
        notifyDataSetChanged();
    }

    public void showLoading(boolean showloader) {
        this.showLoader = showloader;
    }

    private class VHFooter extends RecyclerView.ViewHolder {
        CardView footerView;

        VHFooter(View itemView) {
            super(itemView);
            this.footerView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        View view;

        TextView tvHelpName, tvTimeLabel, tvTimeRemaining, tvOfferLabel;
        Button btnAmount;
        ImageView imgHelpPhoto, imgHelpInfo, imgHelpEdit, imgHelpChat;
        RelativeLayout linCategory;

        MyViewHolder(View itemView) {
            super(itemView);

            view = itemView.findViewById(R.id.view_strip);

            imgHelpPhoto = (ImageView) itemView.findViewById(R.id.img_HeaderProfilePhoto);
            imgHelpInfo = (ImageView) itemView.findViewById(R.id.img_InfoIcon);
            imgHelpEdit = (ImageView) itemView.findViewById(R.id.img_EditIcon);
            imgHelpChat = (ImageView) itemView.findViewById(R.id.img_ChatIcon);

            tvHelpName = (TextView) itemView.findViewById(R.id.txv_offer_title);
            tvHelpName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvTimeLabel = (TextView) itemView.findViewById(R.id.tv_timeLabel);
            tvTimeLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvTimeRemaining = (TextView) itemView.findViewById(R.id.tv_timeRemaining);
            tvTimeRemaining.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvOfferLabel = (TextView) itemView.findViewById(R.id.tv_my_offer);
            tvOfferLabel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            btnAmount = (Button) itemView.findViewById(R.id.btn_money);
            btnAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            linCategory = (RelativeLayout) itemView.findViewById(R.id.lin_categoryList);
        }
    }
}
