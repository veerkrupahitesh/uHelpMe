package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.models.CityModel;
import com.veeritsolutions.uhelpme.models.CountryModel;
import com.veeritsolutions.uhelpme.models.GenderModel;
import com.veeritsolutions.uhelpme.models.StateModel;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This is adapter class manage the list of locations
 * e.g countryList, stateList, cityList
 */

public class AdpLocation extends BaseAdapter {

    private Context context;
    private ArrayList<?> listCountry;
    private String filter = "", strLoading = "";
   /* private LocationType locationType;*/


    public AdpLocation(Context context, ArrayList<?> listCountry, String filter/*, LocationType locationType*/) {
        this.context = context;
        this.listCountry = listCountry;
        this.filter = filter;
        /*this.locationType = locationType;*/
        this.strLoading = "Loading";
    }

    @Override
    public int getCount() {
        return listCountry.size();
    }

    @Override
    public Object getItem(int position) {
        return listCountry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_location, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // holder.footerView.setVisibility(View.GONE);

            String itemName = null;

            Object object = listCountry.get(position);

            if (object instanceof CountryModel) {

                CountryModel countryModel = (CountryModel) object;

                holder.txtLocationName.setTag(countryModel);
                countryModel.setPosition(position);
                itemName = countryModel.getCountryName();

            } else if (object instanceof StateModel) {

                StateModel stateModel = (StateModel) object;

                holder.txtLocationName.setTag(stateModel);
                stateModel.setPosition(position);
                itemName = stateModel.getStateName();

            } else if (object instanceof CityModel) {

                CityModel cityModel = (CityModel) object;

                holder.txtLocationName.setTag(cityModel);
                cityModel.setPosition(position);
                itemName = cityModel.getCityName();
            } else if (object instanceof GenderModel) {
                GenderModel genderModel = (GenderModel) object;
                holder.txtLocationName.setTag(genderModel);
                genderModel.setPosition(position);
                itemName = genderModel.getGender();
            }


//			Change color on search
            int startPos = itemName.toLowerCase(Locale.US).indexOf(filter.toLowerCase(Locale.US));
            int endPos = startPos + filter.length();
            if (startPos != -1) // This should always be true, just a sanity check
            {
                ColorStateList searchedTextColour;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    searchedTextColour = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getColor(R.color.colorAccent)});
                } else {
                    searchedTextColour = new ColorStateList(new int[][]{new int[]{}}, new int[]{context.getResources().getColor(R.color.colorAccent)});
                }
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, searchedTextColour, null);

                Spannable spannable = new SpannableString(itemName);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txtLocationName.setText(spannable);
            } else
                holder.txtLocationName.setText(itemName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView txtLocationName/*, txtLoading*/;
        // View footerView;
        LinearLayout linLocation;

        ViewHolder(View convertView) {

            txtLocationName = (TextView) convertView.findViewById(R.id.txtLocationName);
            // txtLoading = (TextView) convertView.findViewById(R.id.txtLoading);
            linLocation = (LinearLayout) convertView.findViewById(R.id.lin_location);
            //  footerView = convertView.findViewById(R.id.footerview);

            //  Set Font Type
            txtLocationName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            // txtLoading.setTypeface(MyApplication.getInstance().FONT_ROBOTO_REGULAR);

        }
    }
}
