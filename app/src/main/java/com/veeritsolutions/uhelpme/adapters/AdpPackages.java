package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;

import com.veeritsolutions.uhelpme.models.Packages;

import java.util.ArrayList;

/**
 * Created by vaishali on 7/10/2017.
 */

public class AdpPackages extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Packages> PackagesList;

    public AdpPackages(Context mContext, ArrayList<Packages> PackagesList) {
        this.mContext = mContext;
        this.PackagesList = PackagesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_packages, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            Packages packagesModel = PackagesList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.linPackges.setTag(packagesModel);

            myViewHolder.tvPackageName.setText(packagesModel.getPackageName());
            myViewHolder.tvPackageDesc.setText(packagesModel.getDescription());

            myViewHolder.tvPackagePost.setText(String.valueOf(packagesModel.getCreditPost()));
            myViewHolder.tvPackageCredits.setText(String.valueOf(packagesModel.getCreditPoint()));

            myViewHolder.tvPackageAmount.setText("$ " + String.valueOf(packagesModel.getAmount()));

            myViewHolder.imgPackageAmount.setColorFilter(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));

            GradientDrawable drawable = (GradientDrawable) myViewHolder.viewStrip.getBackground();
            drawable.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));
            //myViewHolder.viewStrip.setBackgroundColor(ResourcesCompat.getColor(mContext.getResources(), R.color.colorAccent, null));
        }

    }

    @Override
    public int getItemCount() {

        return PackagesList.size();
    }

    public void refreshList(ArrayList<Packages> PackagesList) {
        this.PackagesList = PackagesList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPackageName, tvPackagePost, tvPackageCredits, tvPackageDesc, tvPackageAmount;

        LinearLayout linPackges;
        ImageView imgPackageAmount;
        View viewStrip;

        MyViewHolder(View itemView) {
            super(itemView);

            viewStrip = itemView.findViewById(R.id.view_strip);

            imgPackageAmount = (ImageView) itemView.findViewById(R.id.img_packageAmount);

            tvPackageName = (TextView) itemView.findViewById(R.id.tv_packageName);
            tvPackageName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvPackageDesc = (TextView) itemView.findViewById(R.id.tv_packageDesc);
            tvPackageDesc.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvPackagePost = (TextView) itemView.findViewById(R.id.tv_packageHelpPost);
            tvPackagePost.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvPackageCredits = (TextView) itemView.findViewById(R.id.tv_packageHelpCredit);
            tvPackageCredits.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvPackageAmount = (TextView) itemView.findViewById(R.id.tv_packageAmount);
            tvPackageAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

//            tvPackageCredits = (TextView) itemView.findViewById(R.id.txv_post);
            linPackges = (LinearLayout) itemView.findViewById(R.id.lin_packages);
        }
    }
}
