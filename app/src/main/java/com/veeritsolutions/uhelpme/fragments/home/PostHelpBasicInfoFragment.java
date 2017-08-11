package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 6/23/2017.
 */

public class PostHelpBasicInfoFragment extends Fragment implements DataObserver, OnBackPressedEvent, OnClickEvent {

    private View rootView;
    private TextView tvUhelpMe, tvBasicInfo, tvAddHelpPhoto;
    private EditText edtTitle, edtDescription;
    private Button btnNextStep;
    private ImageView imgBackHeader, imgHelpPhoto;

    private Bundle bundle;
    private HomeActivity homeActivity;
    private String title, description;
    private Dialog mDialog;
    private List<String> permissionList;
    private String image64Base = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeActivity.imgHome.setImageResource(R.drawable.img_home_inactive);
        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_inactive);
        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_inactive);
        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_inactive);
        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme_gray);

        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));

        rootView = inflater.inflate(R.layout.fragment_post_help_basic_info, container, false);

        imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        imgBackHeader.setVisibility(View.GONE);

        tvUhelpMe = (TextView) rootView.findViewById(R.id.txv_show);
        tvUhelpMe.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvBasicInfo = (TextView) rootView.findViewById(R.id.tv_basic_info);
        tvBasicInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvAddHelpPhoto = (TextView) rootView.findViewById(R.id.tv_add_picture);
        tvAddHelpPhoto.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtTitle = (EditText) rootView.findViewById(R.id.edt_title);
        edtTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        edtDescription = (EditText) rootView.findViewById(R.id.edt_description);
        edtDescription.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        btnNextStep = (Button) rootView.findViewById(R.id.btn_next_help);
        btnNextStep.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        imgHelpPhoto = (ImageView) rootView.findViewById(R.id.img_helpPhoto);

        try {
            if (image64Base.length() > 0) {
                imgHelpPhoto.setVisibility(View.VISIBLE);
                imgHelpPhoto.setImageBitmap(decodeBase64(image64Base));
            } else {
                imgHelpPhoto.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return rootView;

    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }

    @Override
    public void onBackPressed() {

        homeActivity.popBackFragment();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_next_help:
                Utils.buttonClickEffect(view);
                getHelpBasicData();
                break;

            case R.id.tv_add_picture:

                Utils.buttonClickEffect(view);
                //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    // ContextCompat.checkSelfPermission(getContext(), permissionList.get(0));
                    if (PermissionClass.checkPermission(getContext(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList)) {
                        showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);

                    } /*else {
                        PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                    }*/
                } else {
                    showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;

        }
    }

    private void getHelpBasicData() {

        title = edtTitle.getText().toString().trim();
        description = edtDescription.getText().toString().trim();
        if (title.isEmpty()) {
            edtTitle.setError("Enter help title");
            return;
        }

        if (description.isEmpty()) {
            edtDescription.setError("Enter help description");
            return;
        }

        bundle = new Bundle();
        bundle.putInt(Constants.IS_FROM_HOME_ACTIVITY, 0);
        bundle.putString(Constants.TITLE, title);
        bundle.putString(Constants.DESCRIPTION, description);
        bundle.putString(Constants.BASE_64_IMAGE, image64Base);
        homeActivity.pushFragment(new PostHelpCategoryFragment(), true, false, bundle);
    }

    public void showImageSelect(Context mContext, String mTitle, boolean mIsCancelable) {

        mDialog = new Dialog(mContext, R.style.dialogStyle);
        //  @SuppressLint("InflateParams")
        //  View dataView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_select_image, null, false);
        mDialog.setContentView(R.layout.custom_dialog_select_image);

         /* Set Dialog width match parent */
        mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(mIsCancelable);

        TextView tvTitle, tvCamera, tvGallery;

        tvTitle = (TextView) mDialog.findViewById(R.id.tv_selectImageTitle);
        tvTitle.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        tvTitle.setText(mTitle);

        tvCamera = (TextView) mDialog.findViewById(R.id.tv_camera);
        tvCamera.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvGallery = (TextView) mDialog.findViewById(R.id.tv_gallery);
        tvGallery.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        LinearLayout linCamera, linGallery;

        linCamera = (LinearLayout) mDialog.findViewById(R.id.lin_camera);
        linGallery = (LinearLayout) mDialog.findViewById(R.id.lin_gallery);

        linCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.REQUEST_CAMERA_PROFILE);
            }
        });

        linGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        Constants.REQUEST_FILE_PROFILE);
            }
        });
        ImageView imgCancel = (ImageView) mDialog.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        try {
            if (mDialog != null) {
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case Constants.REQUEST_CAMERA_PROFILE:

                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    Uri selectedImageUri = Utils.getImageUri(getActivity(), thumbnail);
                    beginCrop(selectedImageUri);
                    break;

                case Constants.REQUEST_FILE_PROFILE:

                    selectedImageUri = data.getData();
                    beginCrop(selectedImageUri);
                    break;

                case Crop.REQUEST_CROP:

                    handleCrop(resultCode, data);
                    break;
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination)
                .withAspect(imgHelpPhoto.getWidth(), imgHelpPhoto.getHeight())
                .start(getActivity(), this);
    }

    private void handleCrop(int resultCode, Intent data) {

        image64Base = Utils.getStringImage(Crop.getOutput(data).getPath(), ImageUpload.ClientProfile);
        imgHelpPhoto.setVisibility(View.VISIBLE);
        imgHelpPhoto.setImageURI(null);
        imgHelpPhoto.setImageURI(Crop.getOutput(data));
        //Utils.setImage(Crop.getOutput(data).getPath(), R.drawable.img_user_placeholder, imgHelpPhoto);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA) {

            if (grantResults.length > 0 || grantResults.length != 0) {

                for (int i = 0; i < grantResults.length; i++) {
                    //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                    shouldPermit.add(permissions[i]);
                }
                if (PermissionClass.verifyPermission(grantResults)) {
                    showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                }
            }
        }
    }
}
