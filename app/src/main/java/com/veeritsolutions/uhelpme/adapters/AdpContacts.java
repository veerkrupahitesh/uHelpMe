package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;

/**
 * Created by VEER7 on 7/3/2017.
 */

public class AdpContacts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<LoginUserModel> userList;

    public AdpContacts(Context mContext, ArrayList<LoginUserModel> userList, boolean showLoader) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_contacts, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            LoginUserModel loginUser = userList.get(position);
            loginUser.setPosition(position);
            myViewHolder.linContactList.setTag(loginUser);
            myViewHolder.imgProfilePhoto.setTag(R.id.img_profilePhoto, loginUser);

            myViewHolder.tvUserName.setText(loginUser.getFirstName() + " " + loginUser.getLastName());
            myViewHolder.tvLocation.setText(loginUser.getState() + " " + loginUser.getCountry());

            Utils.setImage(loginUser.getProfilePic(), R.drawable.img_user_placeholder, myViewHolder.imgProfilePhoto);

            if (loginUser.isSelected()) {
                myViewHolder.chkIsSelected.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.chkIsSelected.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void refreshList(ArrayList<LoginUserModel> categoryList) {
        this.userList = categoryList;
        notifyDataSetChanged();
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
}
