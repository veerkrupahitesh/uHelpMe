package com.veeritsolutions.uhelpme.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.HelpPicsModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ${Hitesh} on 4/8/2017.
 */

public class AdpHelpPics extends RecyclerView.Adapter<AdpHelpPics.MyViewHolder> {

    private Activity context;
    private List<HelpPicsModel> helpPicsList;


    public AdpHelpPics(Activity context, List<HelpPicsModel> helpPicsList) {
        this.context = context;
        this.helpPicsList = helpPicsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_pet_pics, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        HelpPicsModel helpPicsModel = helpPicsList.get(position);
        String petPicPath = helpPicsModel.getPicPath();
        holder.imgDelete.setTag(helpPicsModel);

        Utils.setImage(petPicPath, R.drawable.img_launcher_icon,
                holder.imgPet, holder.progressBar);
    }


    @Override
    public int getItemCount() {
        return helpPicsList.size();
    }

    public void refreshList(ArrayList<HelpPicsModel> petPicsList) {
        this.helpPicsList = petPicsList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPet, imgDelete;
        LinearLayout linearLayout;
        ProgressBar progressBar;

        private MyViewHolder(View view) {
            super(view);

            imgPet = (ImageView) view.findViewById(R.id.img_petPic);
            imgDelete = (ImageView) view.findViewById(R.id.img_petPicDelete);
            progressBar = (ProgressBar) view.findViewById(R.id.prg_petPicLoading);
            //linearLayout = (LinearLayout) view.findViewById(R.id.lin_package_settings);
        }
    }
}

