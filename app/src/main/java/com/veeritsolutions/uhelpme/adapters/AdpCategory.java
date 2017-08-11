package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.CategoryModel;

import java.util.ArrayList;

/**
 * Created by Admin on 6/26/2017.
 */

public class AdpCategory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<CategoryModel> categoryList;

    public AdpCategory(Context mContext, ArrayList<CategoryModel> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {

            CategoryModel categoryModel = categoryList.get(position);

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.linCategoryList.setTag(categoryModel);

            String str = categoryModel.getCategoryName();

            myViewHolder.tvCategory.setText(checkTextToDisplay(str));

            if (categoryModel.isSelected()) {
                Glide.with(mContext).load(categoryModel.getIcon1()).into(myViewHolder.imgCategory);
                //myViewHolder.imgCategory.setImageResource(R.drawable.img_map_pin_pink);
            } else {
                Glide.with(mContext).load(categoryModel.getIcon2()).into(myViewHolder.imgCategory);
                //myViewHolder.imgCategory.setImageResource(R.drawable.img_map_pin_blue);
            }
            categoryModel.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void refreshList(ArrayList<CategoryModel> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory;
        TextView tvCategory;
        LinearLayout linCategoryList;

        MyViewHolder(View itemView) {
            super(itemView);

            imgCategory = (ImageView) itemView.findViewById(R.id.img_categoryIcon);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvCategory.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            linCategoryList = (LinearLayout) itemView.findViewById(R.id.lin_categoryList);
        }
    }

    private String checkTextToDisplay(String str) {

        if (str.length() > 15) {
            str = str.substring(0, 15) + "\n" + str.substring(16);
        }
        return str;

    }
}
