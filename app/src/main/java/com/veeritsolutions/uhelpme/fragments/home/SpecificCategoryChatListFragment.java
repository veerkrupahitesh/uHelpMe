package com.veeritsolutions.uhelpme.fragments.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpSpecificCategoryChatList;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.PostedJobModel;
import com.veeritsolutions.uhelpme.models.AllHelpOfferModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class SpecificCategoryChatListFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerView;

    private HomeActivity homeActivity;
    private AdpSpecificCategoryChatList adpChatList;
    private ArrayList<AllHelpOfferModel> chatListModels;
    private Bundle bundle;
    private PostedJobModel postedJobModel;
    private Map<String, String> params;
    private LoginUserModel loginUserModel;
    private AllHelpOfferModel allHelpOfferModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        bundle = getArguments();

        loginUserModel = LoginUserModel.getLoginUserModel();
        if (bundle != null) {
            postedJobModel = (PostedJobModel) bundle.getSerializable(Constants.HELP_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_specific_category_chat_list, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_categoryChat);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chatListModels = new ArrayList<>();

        getCategoryChatData();
    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetSpecificCategoryChat:

                ArrayList<AllHelpOfferModel> list = (ArrayList<AllHelpOfferModel>) mObject;

                if (!list.isEmpty()) {
                    chatListModels.addAll(list);
                    if (!chatListModels.isEmpty()) {

                        adpChatList = (AdpSpecificCategoryChatList) recyclerView.getAdapter();

                        if (adpChatList != null && adpChatList.getItemCount() > 0) {
                            adpChatList.refreshList(chatListModels);

                        } else {
                            adpChatList = new AdpSpecificCategoryChatList(homeActivity, chatListModels, false);
                            recyclerView.setAdapter(adpChatList);
                        }
                    }
                }
                break;

            case AcceptOffer:

                String s = (String) mObject;
                chatListModels.clear();
                getCategoryChatData();
                break;

            case FinishOffer:

                s = (String) mObject;
                chatListModels.clear();
                getCategoryChatData();
                showReviewRatingDialog(allHelpOfferModel);
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

            case R.id.lin_contacts:
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();
                ChatUsersListModel chatUsersListModel = new ChatUsersListModel();
                chatUsersListModel.setId(allHelpOfferModel.getClientId());
                chatUsersListModel.setName(allHelpOfferModel.getFirstName() + " " + allHelpOfferModel.getLastName());

                bundle = new Bundle();
                bundle.putSerializable(Constants.CHAT_DATA, chatUsersListModel);

                homeActivity.pushFragment(new OneToOneChatFragment(), true, false, bundle);
                break;

            case R.id.tv_acceptOffer:
                // showReviewRatingDialog();
                Utils.buttonClickEffect(view);
                allHelpOfferModel = (AllHelpOfferModel) view.getTag();

                if (allHelpOfferModel != null) {
                    if (allHelpOfferModel.getIsHire() == 0) {
                        showConfirmationDialog(allHelpOfferModel);
                    } else if (allHelpOfferModel.getIsHire() == 1) {
                        showFinishJobDialog(allHelpOfferModel);
                    }

                }
                break;
        }
    }

    private void showFinishJobDialog(final AllHelpOfferModel allHelpOfferModel) {

        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.finish_job));
        builder[0].setMessage(R.string.are_you_want_to_finish_help_post);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                insertFinishJobOffer(allHelpOfferModel);
            }
        });
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();

    }

    private void insertFinishJobOffer(AllHelpOfferModel allHelpOfferModel) {

        params = new HashMap<>();
        params.put("op", ApiList.FINISH_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.FINISH_OFFER,
                true, RequestCode.FinishOffer, this);

    }

    private void getCategoryChatData() {

        params = new HashMap<>();
        params.put("op", ApiList.GET_CATEGORY_CHAT_DATA);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(postedJobModel.getJobPostId()));
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.GET_CATEGORY_CHAT_DATA,
                true, RequestCode.GetSpecificCategoryChat, this);
    }

    private void showConfirmationDialog(final AllHelpOfferModel allHelpOfferModel) {
        final AlertDialog.Builder[] builder = new AlertDialog.Builder[1];
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder[0] = new AlertDialog.Builder(homeActivity, R.style.dialogStyle);
        //  } else {
        //      builder = new AlertDialog.Builder(profileActivity);
        //  }
        // builder.create();
        builder[0].setTitle(getString(R.string.accept_post_offer_for_help));
        builder[0].setMessage(R.string.are_you_want_to_accept_offer);
        builder[0].setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                insertAcceptOffer(allHelpOfferModel);
            }
        });
        builder[0].setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder[0].show();
    }

    private void insertAcceptOffer(AllHelpOfferModel allHelpOfferModel) {

        params = new HashMap<>();
        params.put("op", ApiList.JOB_POST_ACCEPT_OFFER);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.JOB_POST_ACCEPT_OFFER, true, RequestCode.AcceptOffer, this);

    }

    private void showReviewRatingDialog(final AllHelpOfferModel allHelpOfferModel) {

        final Dialog mDialog;
        mDialog = new Dialog(homeActivity, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_alert, null, false);
        mDialog.setContentView(R.layout.custom_dialog_review_rating);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        //mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogTitle = (TextView) mDialog.findViewById(R.id.tv_dialogHeader);
        mDialogTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        // mDialogTitle.setText(mTitle);
        TextView tvUhelpMe = (TextView) mDialog.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        final float[] ratings = new float[1];
        RatingBar ratingBar = (RatingBar) mDialog.findViewById(R.id.rb_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratings[0] = rating;
            }
        });
        final EditText editText = (EditText) mDialog.findViewById(R.id.edt_review);

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

        ImageView tvOk = (ImageView) mDialog.findViewById(R.id.img_close);
        //  tvOk.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        Button btnSubmit = (Button) mDialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().trim().isEmpty()) {
                    ToastHelper.getInstance().showMessage(getString(R.string.enter_review));
                } else {
                    mDialog.dismiss();
                    insertReviewAndRating(allHelpOfferModel, ratings[0], editText.getText().toString().trim());
                }
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void insertReviewAndRating(AllHelpOfferModel allHelpOfferModel, float rating, String reviews) {

        params = new HashMap<>();
        params.put("op", ApiList.REVIEW_INSERT);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(allHelpOfferModel.getClientId()));
        params.put("JobPostId", String.valueOf(allHelpOfferModel.getJobPostId()));
        params.put("Rating", String.valueOf(rating));
        params.put("ReviewData", reviews);

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.REVIEW_INSERT, true, RequestCode.ReviewInsert, this);

    }
}
