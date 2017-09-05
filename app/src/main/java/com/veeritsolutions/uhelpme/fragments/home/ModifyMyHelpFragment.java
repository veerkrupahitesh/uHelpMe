package com.veeritsolutions.uhelpme.fragments.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpCategory;
import com.veeritsolutions.uhelpme.adapters.SpinnerAdapter;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.CalenderDateSelection;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VEER7 on 7/14/2017.
 */

public class ModifyMyHelpFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private EditText edtTitle, edtDescription, edtAmount;
    private RecyclerView recyclerViewCategory;
    private TextView tvUhelpMe, tvHelpPostDate, tvHelpPostHours;
    private Spinner spinner;
    private LinearLayout linSelectDate, linSelectHours;
    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 1) {

                //tvHelpPostHours.setVisibility(View.VISIBLE);
                linSelectHours.setVisibility(View.VISIBLE);
                linSelectDate.setVisibility(View.GONE);
                tvHelpPostHours.setText("");
                tvHelpPostDate.setText("");
                //CustomDialog.getInstance().showTimePickerDialog(homeActivity, tvHelpPostHours);

            } else if (position == 2) {
                linSelectHours.setVisibility(View.VISIBLE);
                linSelectDate.setVisibility(View.VISIBLE);
                tvHelpPostHours.setText("");
                tvHelpPostDate.setText("");
                // tvHelpPostHours.setVisibility(View.VISIBLE);
                // tvHelpPostDate.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private ImageView imgHelpPhoto;
    private HomeActivity homeActivity;
    private PostedJobModel postedJobModel;
    private Map<String, String> params;
    private AdpCategory adpCategory;
    private ArrayList<CategoryModel> categoryModelsList;
    private CategoryModel categoryModel;
    private List<String> dateSelectionList;
    private LoginUserModel loginUserModel;
    private String title = "", description = "", base64Image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUserModel = LoginUserModel.getLoginUserModel();
        homeActivity = (HomeActivity) getActivity();
        postedJobModel = (PostedJobModel) getArguments().getSerializable(Constants.HELP_DATA);
        dateSelectionList = new ArrayList<>();
        dateSelectionList.add(getString(R.string.select_time_limits));
        dateSelectionList.add(getString(R.string.today));
        dateSelectionList.add(getString(R.string.other_day));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_modify_help_offer, container, false);

        imgHelpPhoto = (ImageView) rootView.findViewById(R.id.img_help_ProfilePic);
        //imgHelpPhoto.setVisibility(View.GONE);

        edtTitle = (EditText) rootView.findViewById(R.id.edt_title);
        edtTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtDescription = (EditText) rootView.findViewById(R.id.edt_description);
        edtDescription.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        recyclerViewCategory = (RecyclerView) rootView.findViewById(R.id.recyclerView_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        tvHelpPostDate = (TextView) rootView.findViewById(R.id.tv_helpPostDate);
        tvHelpPostDate.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvHelpPostHours = (TextView) rootView.findViewById(R.id.tv_helpPostHours);
        tvHelpPostHours.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtAmount = (EditText) rootView.findViewById(R.id.edt_amount);
        edtAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        linSelectDate = (LinearLayout) rootView.findViewById(R.id.lin_selectDate);
        linSelectHours = (LinearLayout) rootView.findViewById(R.id.lin_selectHours);

        spinner = (Spinner) rootView.findViewById(R.id.sp_timeLimits);
        spinner.setAdapter(new SpinnerAdapter(homeActivity, R.layout.spinner_row_list, dateSelectionList));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getCategoryData();

        edtTitle.setText(postedJobModel.getJobTitle());
        edtDescription.setText(postedJobModel.getJobDescription());
        edtAmount.setText(String.valueOf(postedJobModel.getJobAmount()));

        if (postedJobModel.getJobHour() > 0) {
            spinner.setSelection(1);
            tvHelpPostHours.setText(String.valueOf(postedJobModel.getJobHour()));
            tvHelpPostDate.setText("");
        } else {
            spinner.setSelection(2);
            String str = postedJobModel.getJobDoneTime();

            String dateString = str.substring(0, 10);
            String timeString = str.substring(11, postedJobModel.getJobDoneTime().length());

            tvHelpPostDate.setText(dateString);
            tvHelpPostHours.setText(String.valueOf(timeString));
        }

        Utils.setImage(postedJobModel.getJobPhoto(), R.drawable.img_user_placeholder, imgHelpPhoto);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetCategory:

                categoryModelsList = (ArrayList<CategoryModel>) mObject;

                if (!categoryModelsList.isEmpty()) {

                    for (int i = 0; i < categoryModelsList.size(); i++) {
                        if (postedJobModel.getCategoryId() == categoryModelsList.get(i).getCategoryId()) {
                            categoryModel = categoryModelsList.get(i);
                            categoryModelsList.get(i).setSelected(true);
                            break;
                        }
                    }
                    adpCategory = (AdpCategory) recyclerViewCategory.getAdapter();
                    if (adpCategory != null && adpCategory.getItemCount() > 0) {
                        adpCategory.notifyDataSetChanged();

                    } else {
                        adpCategory = new AdpCategory(homeActivity, categoryModelsList);
                        recyclerViewCategory.setAdapter(adpCategory);
                    }
                }
                new ImageTask().execute();
                spinner.setOnItemSelectedListener(listener);
                break;

            case JobPostUpdate:
                ToastHelper.getInstance().showMessage(getString(R.string.updated));
                homeActivity.popBackFragment();
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {
        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onBackPressed() {
        homeActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                homeActivity.popBackFragment();
                break;

            case R.id.lin_categoryList:
                Utils.buttonClickEffect(view);
                categoryModel = (CategoryModel) view.getTag();
                if (categoryModel.isSelected()) {

                    for (int i = 0; i < categoryModelsList.size(); i++) {
                        categoryModelsList.get(i).setSelected(false);
                        categoryModelsList.set(i, categoryModelsList.get(i));
                    }
                    categoryModel.setSelected(false);
                } else {

                    for (int i = 0; i < categoryModelsList.size(); i++) {
                        categoryModelsList.get(i).setSelected(false);
                        categoryModelsList.set(i, categoryModelsList.get(i));
                    }
                    categoryModel.setSelected(true);
                }
                categoryModelsList.set(categoryModel.getPosition(), categoryModel);

                adpCategory.refreshList(categoryModelsList);
                break;

            case R.id.tv_helpPostDate:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().showDatePickerDialog(homeActivity, tvHelpPostDate,
                        CalenderDateSelection.CALENDER_WITH_FUTURE_DATE, 2050, 12, 30);
                spinner.setOnItemSelectedListener(listener);
                break;

            case R.id.tv_helpPostHours:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().showTimePickerDialog(homeActivity, tvHelpPostHours, spinner.getSelectedItemPosition());
                spinner.setOnItemSelectedListener(listener);
                break;

            case R.id.btn_update:
                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    updateHelp();
                }
                break;
        }
    }

    private void updateHelp() {

        float lat = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
        float longi = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
        float alti = PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0);
        String postAmount = edtAmount.getText().toString().trim();

        Map<String, String> params = new HashMap<>();

        params.put("op", "JobPostUpdate");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("JobTitle", title);
        params.put("JobDescription", description);
        params.put("JobPhoto", base64Image);
        params.put("CategoryId", String.valueOf(categoryModel.getCategoryId()));
        params.put("JobPostingPoints", String.valueOf(postedJobModel.getJobPostingPoints()));
        params.put("JobPostingAmount", String.valueOf(postedJobModel.getJobPostingAmount()));
        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude", String.valueOf(longi));
        params.put("Altitude", String.valueOf(alti));

        params.put("Latitude_1", String.valueOf(0));
        params.put("Longitude_1", String.valueOf(0));
        params.put("Altitude_1", String.valueOf(0));

        if (spinner.getSelectedItemPosition() == 1) {
            params.put("JobHour", tvHelpPostHours.getText().toString());
            params.put("JobDoneTime", "");
        } else if (spinner.getSelectedItemPosition() == 2) {
            params.put("JobHour", String.valueOf(0));
            params.put("JobDoneTime", String.valueOf(tvHelpPostDate.getText().toString() + " " + tvHelpPostHours.getText().toString()));
        }

        params.put("JobAmount", postAmount);
        params.put("PaymentTime", postedJobModel.getPaymentTime());
        params.put("PaymentId", postedJobModel.getPaymentId());
        params.put("PaymentStatus", postedJobModel.getPaymentStatus());
        params.put("PaymentResponse", postedJobModel.getPaymentResponse());

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.JOB_POST_UPDATE, true, RequestCode.JobPostUpdate, this);
    }

    private void getCategoryData() {

        params = new HashMap<>();
        params.put("op", "GetCategory");
        params.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CATEGORY,
                true, RequestCode.GetCategory, this);
    }

    private boolean validateForm() {

        title = edtTitle.getText().toString().trim();
        description = edtDescription.getText().toString();
        if (title.isEmpty()) {
            edtTitle.setError(getString(R.string.enter_help_title));
            return false;
        }

        if (description.isEmpty()) {
            edtDescription.setError(getString(R.string.enter_help_description));
            return false;
        }

        if (categoryModel == null) {
            ToastHelper.getInstance().showMessage(getString(R.string.select_category));
            return false;
        }

        if (spinner.getSelectedItemPosition() == 0) {
            ToastHelper.getInstance().showMessage(getString(R.string.select_time_limits));
            return false;
        } else if (spinner.getSelectedItemPosition() == 1) {
            if (tvHelpPostHours.getText().toString().trim().isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.select_hours_time_liimit));
                return false;
            } else if (edtAmount.getText().toString().isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.enter_amount_time_limits));
                return false;
            } else {
                return true;
            }

        } else if (spinner.getSelectedItemPosition() == 2) {
            if (tvHelpPostDate.getText().toString().isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.select_required_date_limit));
                return false;
            } else if (tvHelpPostHours.getText().toString().trim().isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.select_hours_time_liimit));
                return false;
            } else if (edtAmount.getText().toString().isEmpty()) {
                ToastHelper.getInstance().showMessage(getString(R.string.enter_amount_time_limits));
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    private class ImageTask extends AsyncTask<Void, Void, Void> {

        Bitmap mIcon1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomDialog.getInstance().showProgress(homeActivity, "", false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL imgValue;
            Debug.trace("taking", "2");
            try {
                imgValue = new URL(postedJobModel.getJobPhoto());
                //img_value = new URL("http://graph.facebook.com/"+ userProfileID +"/picture?type=square");
                BitmapFactory.Options options = new BitmapFactory.Options();
                mIcon1 = BitmapFactory.decodeStream(imgValue.openConnection().getInputStream(), null, options);
                Debug.trace("taking", "3" + imgValue);
                Debug.trace("taking", "3" + mIcon1);
                Debug.trace("taking", String.valueOf(mIcon1));

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                mIcon1.compress(Bitmap.CompressFormat.PNG, 100, bao);
                byte[] ba = bao.toByteArray();
                base64Image = Base64.encodeToString(ba, Base64.DEFAULT);

                Debug.trace("Encoded image is : ===== " + base64Image);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            CustomDialog.getInstance().dismiss();
        }
    }
}

