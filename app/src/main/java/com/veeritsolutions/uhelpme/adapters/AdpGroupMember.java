package com.veeritsolutions.uhelpme.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;

import java.util.ArrayList;

/**
 * Created by VEER7 on 7/10/2017.
 */

public class AdpGroupMember extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<LoginUserModel> chatuserList;
    private Context context;
    protected boolean showLoader;
    private static final int TYPE_FOOTER = 0;
    private static final int TYPE_ITEM = 1;


    public AdpGroupMember(Context context, ArrayList<LoginUserModel> chatuserList, boolean showLoader) {
        this.chatuserList = chatuserList;
        this.context = context;
        this.showLoader = showLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            return new VHFooter(itemView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_contacts, parent, false);

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
            LoginUserModel loginUser = chatuserList.get(position);
            loginUser.setPosition(position);
            myViewHolder.linContactList.setTag(loginUser);
            myViewHolder.imgProfilePhoto.setTag(loginUser);

            myViewHolder.tvUserName.setText(loginUser.getFirstName() + " " + loginUser.getLastName());

            if (loginUser.getIsAdmin() == 1) {
                myViewHolder.tvLocation.setText(context.getString(R.string.admin));
                myViewHolder.tvLocation.setTextColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, null));
            }
        }

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
        if (chatuserList == null || chatuserList.size() == 0) {
            return 0;
        }

        return chatuserList.size() + 1;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProfilePhoto;
        TextView tvUserName, tvLocation;
        LinearLayout linContactList;
        CheckBox chkIsSelected;


        MyViewHolder(View itemView) {
            super(itemView);
            imgProfilePhoto = (ImageView) itemView.findViewById(R.id.img_profilePhoto);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_userName);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_userLocation);

            linContactList = (LinearLayout) itemView.findViewById(R.id.lin_contacts);

            chkIsSelected = (CheckBox) itemView.findViewById(R.id.chk_contactSelected);
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
    }

    public void refreshList(ArrayList<LoginUserModel> chatuserList) {
        this.chatuserList = chatuserList;
        notifyDataSetChanged();
    }
}


