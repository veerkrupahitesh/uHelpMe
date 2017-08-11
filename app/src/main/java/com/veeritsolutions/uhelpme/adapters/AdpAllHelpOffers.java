package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;

import java.util.ArrayList;

/**
 * Created by VEER7 on 7/10/2017.
 */

public class AdpAllHelpOffers extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<AllHelpOfferModel> allHelpOffersList;

    public AdpAllHelpOffers(Context mContext, ArrayList<AllHelpOfferModel> allHelpOffersList) {
        this.mContext = mContext;
        this.allHelpOffersList = allHelpOffersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_all_help_offer, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            AllHelpOfferModel allHelpOfferModel = allHelpOffersList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.linCategoryList.setTag(allHelpOfferModel);

            if (position == 0) {

                myViewHolder.linCategoryList.setBackgroundColor(Color.parseColor("#f4f4f4"));
            } else if (position % 2 == 0) {
                myViewHolder.linCategoryList.setBackgroundColor(Color.parseColor("#f4f4f4"));
            } else {
                myViewHolder.linCategoryList.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            if (allHelpOfferModel.getIsMyOffer()==1){
                myViewHolder.tvAmount.setTextColor(ResourcesCompat.getColor(mContext.getResources(),R.color.colorAccent,null));
                myViewHolder.tvDateAndTime.setTextColor(ResourcesCompat.getColor(mContext.getResources(),R.color.colorAccent,null));
            }
            myViewHolder.tvAmount.setText(String.valueOf(allHelpOfferModel.getOfferAmount()));
            myViewHolder.tvDateAndTime.setText(allHelpOfferModel.getCreatedOn());

            allHelpOfferModel.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return allHelpOffersList.size();
    }

    public void refreshList(ArrayList<AllHelpOfferModel> categoryList) {
        this.allHelpOffersList = categoryList;
        notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        //ImageView imgCategory;
        TextView tvAmount, tvDateAndTime;
        LinearLayout linCategoryList;

        MyViewHolder(View itemView) {
            super(itemView);

            //imgCategory = (ImageView) itemView.findViewById(R.id.img_categoryIcon);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            tvAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvDateAndTime = (TextView) itemView.findViewById(R.id.tv_dateTime);
            tvDateAndTime.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            linCategoryList = (LinearLayout) itemView.findViewById(R.id.lin_allHelpOffer);
        }
    }
}

