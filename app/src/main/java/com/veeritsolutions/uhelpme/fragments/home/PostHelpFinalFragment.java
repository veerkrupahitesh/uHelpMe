package com.veeritsolutions.uhelpme.fragments.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.SpinnerAdapter;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.api.ServerConfig;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.CalenderDateSelection;
import com.veeritsolutions.uhelpme.fragments.profile.PackagesFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CategoryModel;
import com.veeritsolutions.uhelpme.models.ChatModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 6/24/2017.
 */

public class PostHelpFinalFragment extends Fragment implements OnClickEvent, OnBackPressedEvent,
        DataObserver, RewardedVideoAdListener {

    private View rootView;
    private TextView tvUhelpMe, tvHelpPostDate, tvHelpPostHours, tvFinally;
    private Spinner spinner;
    private LinearLayout linSelectDate, linSelectHours;
    private EditText edtAmount;
    private Button btnNextStep;

    private HomeActivity homeActivity;
    private Bundle bundle;
    private List<String> dateSelectionList;
    // Firebase variables and objects
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceGroup;
    private LoginUserModel loginUserModel;
    final CharSequence[] options = {"Watch Video", "Buy Points", "Cancel"};
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();
        dateSelectionList = new ArrayList<>();
        dateSelectionList.add(getString(R.string.select_time_limits));
        dateSelectionList.add(getString(R.string.today));
        dateSelectionList.add(getString(R.string.other_day));
        firebaseDatabase = FirebaseDatabase.getInstance(ServerConfig.FCM_APP_URL);
        loginUserModel = LoginUserModel.getLoginUserModel();
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(homeActivity, Constants.APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(homeActivity);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_post_help_and_finally, container, false);

        tvUhelpMe = (TextView) rootView.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        String str = getString(R.string.view_a_u) + getString(R.string.font_color) + getString(R.string.helpme) + getString(R.string.font);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvUhelpMe.setText(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvUhelpMe.setText(Html.fromHtml(str));
            //tvUhelpMe.setText(Html.fromHtml("By creating your account, you accept the "
            //        + "<a color=\"#0095d7\"href=\"http://www.anivethub.com/terms-and-conditions\">general conditions</a> of <b>UHelpMe</b>"));
            //  tvUhelpMe.setText(Html.fromHtml("View a U" + "<a color=\"#0095d7\">HelpMe</a>"));
        }

        tvFinally = (TextView) rootView.findViewById(R.id.tv_finally);
        tvFinally.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvHelpPostHours = (TextView) rootView.findViewById(R.id.tv_helpPostHours);
        tvHelpPostHours.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvHelpPostDate = (TextView) rootView.findViewById(R.id.tv_helpPostDate);
        tvHelpPostDate.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtAmount = (EditText) rootView.findViewById(R.id.edt_amount);
        edtAmount.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        linSelectDate = (LinearLayout) rootView.findViewById(R.id.lin_selectDate);
        linSelectHours = (LinearLayout) rootView.findViewById(R.id.lin_selectHours);

        spinner = (Spinner) rootView.findViewById(R.id.sp_timeLimits);
        spinner.setAdapter(new SpinnerAdapter(homeActivity, R.layout.spinner_row_list, dateSelectionList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });

        btnNextStep = (Button) rootView.findViewById(R.id.btn_next_help);
        btnNextStep.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        return rootView;
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

            case R.id.tv_helpPostDate:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().showDatePickerDialog(homeActivity, tvHelpPostDate,
                        CalenderDateSelection.CALENDER_WITH_FUTURE_DATE, 2050, 12, 30);
                break;

            case R.id.tv_helpPostHours:
                Utils.buttonClickEffect(view);
                CustomDialog.getInstance().showTimePickerDialog(homeActivity, tvHelpPostHours, /*spinner.getSelectedItemPosition()*/2);
                break;

            case R.id.btn_next_help:
                Utils.buttonClickEffect(view);
                if (validateForm()) {

                    insertJobPost();
                }
                break;
        }
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case JobPostInsert:

                PostedJobModel postedJobModel = (PostedJobModel) mObject;
                if (postedJobModel != null) {
                    if (postedJobModel.getChatGroupId().length() > 0) {
                        String chatId = postedJobModel.getChatGroupId();
                        String[] chatIds = chatId.split(",");
                        for (String chatId1 : chatIds) {
                            databaseReferenceGroup = firebaseDatabase.getReference().child(String.valueOf(chatId1));

                            SpannableString content = new SpannableString(postedJobModel.getJobTitle());
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

                            // Create our 'model', a Chat object
                            ChatModel userChat = new ChatModel(loginUserModel.getClientId(), loginUserModel.getFirstName(),
                                    "Need Help : " + content.toString() + "\n" + "Details : " + postedJobModel.getJobDescription(),
                                    Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A), 1);
                            // ChatModel otherChat = new ChatModel(specificCategoryChatListModel.getClientId(), specificCategoryChatListModel.getFirstName(), input, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A));
                            // Create a new, auto-generated child of that chat location, and save our chat data there
                            databaseReferenceGroup.push().setValue(userChat);
                        }
                        homeActivity.removeAllFragment();
                        homeActivity.pushFragment(new HomeFragment(), true, false, null);

                    } else {
                        homeActivity.removeAllFragment();
                        homeActivity.pushFragment(new HomeFragment(), true, false, null);
                    }
                }
                break;

            case ClientWatchVideo:

                ToastHelper.getInstance().showMessage(getString(R.string.can_help_post));

                break;

        }
        //ToastHelper.getInstance().showMessage("Success");
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);

        switch (mRequestCode) {

            case JobPostInsert:

                if (mError.equalsIgnoreCase("You Dont have enough credit points.")) {
                    showConfirmationDialog();
                }
                Debug.trace("mError", mError);
                //showConfirmationDialog();
                break;
        }

    }

    private boolean validateForm() {

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

    private void insertJobPost() {

        CategoryModel categoryModel = (CategoryModel) bundle.getSerializable(Constants.CATEGORY_DATA);
        LoginUserModel loginUser = LoginUserModel.getLoginUserModel();
        float lat = bundle.getFloat(PrefHelper.LATITUDE);
        float longi = bundle.getFloat(PrefHelper.LONGITUDE);
        float alti = PrefHelper.getInstance().getFloat(PrefHelper.ALTITUDE, 0);
        String postAmount = edtAmount.getText().toString().trim();

        Map<String, String> params = new HashMap<>();

        params.put("op", "JobPostInsert");
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUser.getClientId()));
        params.put("JobTitle", bundle.getString(Constants.TITLE));
        params.put("JobDescription", bundle.getString(Constants.DESCRIPTION));
        params.put("JobPhoto", bundle.getString(Constants.BASE_64_IMAGE));
        params.put("CategoryId", String.valueOf(categoryModel.getCategoryId()));
        params.put("JobPostingPoints", String.valueOf(0));
        params.put("JobPostingAmount", String.valueOf(0));
        params.put("Latitude", String.valueOf(lat));
        params.put("Longitude", String.valueOf(longi));
        params.put("Altitude", String.valueOf(alti));

        params.put("Latitude_1", String.valueOf(0));
        params.put("Longitude_1", String.valueOf(0));
        params.put("Altitude_1", String.valueOf(0));

        if (spinner.getSelectedItemPosition() == 1) {
            params.put("JobHour", String.valueOf(0));
            params.put("JobDoneTime", String.valueOf(Utils.dateFormat(System.currentTimeMillis(), Constants.DATE_MM_DD_YYYY) + " " + tvHelpPostHours.getText().toString()));
        } else if (spinner.getSelectedItemPosition() == 2) {
            params.put("JobHour", String.valueOf(0));
            params.put("JobDoneTime", String.valueOf(tvHelpPostDate.getText().toString() + " " + tvHelpPostHours.getText().toString()));
        }

        params.put("JobAmount", postAmount);
        params.put("PaymentTime", "");
        params.put("PaymentId", "");
        params.put("PaymentStatus", "");
        params.put("PaymentResponse", "");

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.JOB_POST_INSERT, true, RequestCode.JobPostInsert, this);
    }

    private void showConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Watch Video")) {
                    dialog.dismiss();
                    showRewardedVideo();

                } else if (options[item].equals("Buy Points")) {
                    dialog.dismiss();
                    homeActivity.pushFragment(new PackagesFragment(), true, false, null);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

        /*final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.purchase_credit_points));
        builder[0].setMessage(R.string.are_you_want_to_but_credit_points);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                homeActivity.pushFragment(new PackagesFragment(), true, false, null);
            }
        });
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();*/
    }

    private void showRewardedVideo() {
        CustomDialog.getInstance().showProgress(homeActivity, "", true);
        //mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        } else {
            loadRewardedVideoAd();
        }
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(Constants.AD_UNIT_ID, new AdRequest.Builder().build());

        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Debug.trace("MyVideo", "loaded");
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Debug.trace("MyVideo", "Opened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Debug.trace("MyVideo", "started");
        CustomDialog.getInstance().dismiss();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Debug.trace("MyVideo", "closed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Debug.trace("MyVideo", "rewarded");
        ToastHelper.getInstance().showMessage(getString(R.string.you_will_rewarded_video_points));

        insertVideoPoints();
    }

    private void insertVideoPoints() {

        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.CLIENT_WATCH_VIDEO_POINTS);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.CLIENT_WATCH_VIDEO_POINTS, true, RequestCode.ClientWatchVideo, this);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Debug.trace("MyVideo", "Left app");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Debug.trace("MyVideo", "failed");
        ToastHelper.getInstance().showMessage("failed " + errorCode);
        CustomDialog.getInstance().dismiss();
    }
}
