package com.veeritsolutions.uhelpme.fragments.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.ProfileActivity;
import com.veeritsolutions.uhelpme.adapters.AdpLocation;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.enums.ImageUpload;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.CityModel;
import com.veeritsolutions.uhelpme.models.CountryModel;
import com.veeritsolutions.uhelpme.models.GenderModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.models.StateModel;
import com.veeritsolutions.uhelpme.utility.BlurTransformation;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Admin on 6/28/2017.
 */

public class MyProfileFragment extends Fragment implements OnBackPressedEvent, OnClickEvent, DataObserver {


    private View rootView;
    private EditText edtFirstName, edtLastName, edtEmail, /*edtPassword,*/
            edtTelephone, edtPostalCode;
    private Button btnSubmit;
    private LoginUserModel loginUserModel;
    // private Spinner spgender;
    private ImageView imgProfilePhoto, imgBannerPhoto;
    private TextView tvProfileInfo, tvPersonalInfo, edtCity,
            edtState, edtCountry, tvGender;
    private ProgressBar prgBanner, prgProfile;

    private ProfileActivity profileActivity;
    private List<String> genderList;
    private ArrayList<GenderModel> genderModelList;
    private ArrayList<CountryModel> countryList;
    private ArrayList<StateModel> stateList;
    private ArrayList<CityModel> cityList;
    private Dialog mDialog;
    private String countryId = "", stateId = "", cityId = "";
    private List<String> permissionList;
    private String image64Base = "";
    private int genderId = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileActivity = (ProfileActivity) getActivity();
        loginUserModel = LoginUserModel.getLoginUserModel();
        genderList = new ArrayList<>();
        genderList.add(getString(R.string.select_gender));
        genderList.add(getString(R.string.male));
        genderList.add(getString(R.string.female));

        genderModelList = new ArrayList<>();
        genderModelList.add(new GenderModel(1, "Male"));
        genderModelList.add(new GenderModel(2, "Female"));
        permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.CAMERA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfilePhoto = (ImageView) rootView.findViewById(R.id.img_profilePhoto);
        imgBannerPhoto = (ImageView) rootView.findViewById(R.id.img_help_bannerPic);

        tvPersonalInfo = (TextView) rootView.findViewById(R.id.tv_profileInfo);
        tvPersonalInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvProfileInfo = (TextView) rootView.findViewById(R.id.tv_personalInfo);
        tvProfileInfo.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtFirstName = (EditText) rootView.findViewById(R.id.edt_firstName);
        edtFirstName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtLastName = (EditText) rootView.findViewById(R.id.edt_lastName);
        edtLastName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtEmail = (EditText) rootView.findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        // edtPassword = (EditText) rootView.findViewById(R.id.edt_password);
        // edtPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtTelephone = (EditText) rootView.findViewById(R.id.edt_telephone);
        edtTelephone.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtCity = (TextView) rootView.findViewById(R.id.edt_city);
        edtCity.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtState = (TextView) rootView.findViewById(R.id.edt_state);
        edtState.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtCountry = (TextView) rootView.findViewById(R.id.edt_country);
        edtCountry.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        tvGender = (TextView) rootView.findViewById(R.id.tv_gender);
        tvGender.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtPostalCode = (EditText) rootView.findViewById(R.id.edt_postalCode);
        edtPostalCode.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        btnSubmit = (Button) rootView.findViewById(R.id.btn_update);
        btnSubmit.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        //spgender = (Spinner) rootView.findViewById(R.id.sp_gender);
        //spgender.setAdapter(new SpinnerAdapter(profileActivity, R.layout.spinner_row_list, genderList));

        prgBanner = (ProgressBar) rootView.findViewById(R.id.prg_banner);
        prgBanner.setVisibility(View.GONE);
        prgProfile = (ProgressBar) rootView.findViewById(R.id.prg_profile);
        prgProfile.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Utils.setupOutSideTouchHideKeyboard(rootView);
        GetClientInfo();
    }

    @Override
    public void onBackPressed() {

        profileActivity.popBackFragment();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.img_helpMe:
                Utils.buttonClickEffect(view);

                break;

            case R.id.btn_update:
                Utils.buttonClickEffect(view);
                if (validateForm()) {

                    clientUpdate();
                }

                break;

            case R.id.img_back_header:
                Utils.buttonClickEffect(view);
                profileActivity.popBackFragment();
                break;

            case R.id.edt_country:
                Utils.buttonClickEffect(view);
                getCountryInfo(true);
                break;

            case R.id.edt_state:
                Utils.buttonClickEffect(view);
                if (!countryId.equals("")) {
                    getStateInfo(countryId);
                } else {
                    if (loginUserModel.getCountryId() != 0) {
                        getStateInfo(String.valueOf(loginUserModel.getCountryId()));
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.select_country));
                    }
                }
                break;

            case R.id.edt_city:
                Utils.buttonClickEffect(view);
                if (!stateId.equals("")) {
                    getCityInfo(stateId);
                } else {

                    if (loginUserModel.getStateId() != 0) {
                        getCityInfo(String.valueOf(loginUserModel.getStateId()));
                    } else {
                        ToastHelper.getInstance().showMessage(getString(R.string.select_state));
                    }
                }
                break;

            case R.id.img_profilePhoto:
                Utils.buttonClickEffect(view);
                //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), false);

                if (Build.VERSION.SDK_INT > 22) {
                    // ContextCompat.checkSelfPermission(getContext(), permissionList.get(0));
                    if (PermissionClass.checkPermission(getContext(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList)) {
                        // start cropping activity for pre-acquired image saved on the device
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle("Crop")
                                .setRequestedSize(400, 400)
                                .start(getContext(), this);
                        //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);

                    } /*else {
                        PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                    }*/
                } else {
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                }
                break;

            case R.id.tv_gender:

                showLocationListDialog(profileActivity, genderModelList, tvGender, "Select Gender");
                break;
        }

    }


    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetClientInfo:

                if (mObject instanceof LoginUserModel) {

                    loginUserModel = (LoginUserModel) mObject;
                    edtFirstName.setText(loginUserModel.getFirstName());
                    edtLastName.setText(loginUserModel.getLastName());
                    edtEmail.setText(loginUserModel.getEmailId());
                    // edtPassword.setText(loginUserModel.getPassword());
                    edtTelephone.setText(loginUserModel.getPhoneNo());
                    edtCity.setText(loginUserModel.getCity());
                    edtState.setText(loginUserModel.getState());
                    edtCountry.setText(loginUserModel.getCountry());
                    tvGender.setText(loginUserModel.getGenderDisp());
                    genderId = loginUserModel.getGender();
                    //spgender.setSelection(loginUserModel.getGender());
                    edtPostalCode.setText(loginUserModel.getpOBox());
                    Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto, prgProfile);
                    prgBanner.setVisibility(View.VISIBLE);
                    Glide.with(profileActivity)
                            .load(loginUserModel.getProfilePic())
                            .transform(new BlurTransformation(profileActivity))
                            .signature(new StringSignature(String.valueOf(PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG, 0))))
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    prgBanner.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    prgBanner.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imgBannerPhoto);
                    //new DownloadFilesTask().execute(loginUserModel.getProfilePic());
                }
                break;

            case ClientUpdate:

                ToastHelper.getInstance().showMessage(getString(R.string.profile_updated));
                profileActivity.popBackFragment();

                break;

            case GetCountry:

                try {
                    countryList = (ArrayList<CountryModel>) mObject;
                    showLocationListDialog(getActivity(), countryList, edtCountry, getString(R.string.select_country_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetState:
                try {
                    stateList = (ArrayList<StateModel>) mObject;
                    showLocationListDialog(getActivity(), stateList, edtState, getString(R.string.select_state_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GetCity:
                try {
                    cityList = (ArrayList<CityModel>) mObject;
                    showLocationListDialog(getActivity(), cityList, edtCity, getString(R.string.select_city_title));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case ClientProfilePicUpdate:

                PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG, System.currentTimeMillis());
                LoginUserModel loginUserModel = (LoginUserModel) mObject;
                Utils.setImage(loginUserModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePhoto, prgProfile);
                prgBanner.setVisibility(View.VISIBLE);
                Glide.with(profileActivity)
                        .load(this.loginUserModel.getProfilePic())
                        .transform(new BlurTransformation(profileActivity))
                        .signature(new StringSignature(String.valueOf(PrefHelper.getInstance().getLong(PrefHelper.IMAGE_CACHE_FLAG, 0))))
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                prgBanner.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                prgBanner.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(imgBannerPhoto);
                LoginUserModel.setLoginCredentials(RestClient.getGsonInstance().toJson(loginUserModel));
                //new DownloadFilesTask().execute(loginUserModel.getProfilePic());
                //PrefHelper.getInstance().setString(PrefHelper.CLIENT_CREDENTIALS, Utils.objectToString((Serializable) mObject));
                break;
        }


    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    private boolean validateForm() {

        if (edtFirstName.getText().toString().trim().isEmpty()) {
            edtFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (edtLastName.getText().toString().trim().isEmpty()) {
            edtLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (edtEmail.getText().toString().trim().isEmpty()) {
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!edtEmail.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }/* else if (edtPassword.getText().toString().trim().isEmpty()) {
            edtPassword.setError("Password can not be empty");
            return false;

        }*/ else {
            return true;
        }
    }


    private void getCountryInfo(boolean requireDialog) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("op", ApiList.GET_COUNTRY_INFO);
        params1.put("AuthKey", ApiList.AUTH_KEY);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1,
                ApiList.GET_COUNTRY_INFO, requireDialog, RequestCode.GetCountry, this);

    }

    private void getStateInfo(String countryId) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("AuthKey", ApiList.AUTH_KEY);
        params1.put("op", ApiList.GET_STATE_INFO);
        params1.put("CountryId", countryId);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1, ApiList.GET_STATE_INFO,
                true, RequestCode.GetState,
                this);
    }

    private void getCityInfo(String stateId) {

        Map<String, String> params1 = new HashMap<>();
        params1.put("op", ApiList.GET_CITY_INFO);
        params1.put("AuthKey", ApiList.AUTH_KEY);
        params1.put("StateId", stateId);

        RestClient.getInstance().post(getActivity(), Request.Method.POST, params1, ApiList.GET_CITY_INFO,
                true, RequestCode.GetCity,
                this);
    }

    /**
     * This method show the dialog with list of locations and having functionality of search
     *
     * @param context(Context)        : context
     * @param listLocation(ArrayList) : list of locations with countries, states and cities
     * @param textView(TextView)      : to show selected location e.g country-United Kingdom,
     *                                state- new york, city- london
     */
    public void showLocationListDialog(final Context context, final ArrayList<?> listLocation,
                                       final TextView textView, String dialogTitle) {

        try {

            mDialog = new Dialog(context, R.style.dialogStyle);
            // View dataView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_location_list, null);
            mDialog.setContentView(R.layout.custom_dialog_location_list);
            /* Set Dialog width match parent */
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            /*Set out side touch hide keyboard*/
            Utils.setupOutSideTouchHideKeyboard(mDialog.findViewById(R.id.parentDialog));

            TextView txtTitleDialog = (TextView) mDialog.findViewById(R.id.tv_titleDialog);
            txtTitleDialog.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
            // Set Dialog title
            txtTitleDialog.setText(dialogTitle);
            final ListView listViewLocation = (ListView) mDialog.findViewById(R.id.lv_location);
            final TextView txtNoRecord = (TextView) mDialog.findViewById(R.id.tv_noRecord);
            txtNoRecord.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            // txtNoRecord.setText("No Records Found");
            LinearLayout linSearch = (LinearLayout) mDialog.findViewById(R.id.lin_fab_search);
            Button btnCancel = (Button) mDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
            btnCancel.setText(getString(R.string.cancel));
            final EditText edtSearchLocation = (EditText) mDialog.findViewById(R.id.edt_searchLocation);
            edtSearchLocation.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            if (listLocation != null && listLocation.size() > 0) {

                listViewLocation.setVisibility(View.VISIBLE);
                AdpLocation adpLocationList = new AdpLocation(context, listLocation, "");
                listViewLocation.setAdapter(adpLocationList);
            } else {

                listViewLocation.setVisibility(View.GONE);
                txtNoRecord.setVisibility(View.VISIBLE);
            }

            listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    mDialog.dismiss();
                    TextView txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);
                    Object object = txtLocationName.getTag();

                    if (object != null) {

                        if (object instanceof CountryModel) {
                            CountryModel countryModel = (CountryModel) object;
                            countryId = String.valueOf(countryModel.getCountryId());
                            textView.setText(countryModel.getCountryName());
                            textView.setTag(countryModel);
                            edtState.setText("");
                            edtState.performClick();

                        } else if (object instanceof StateModel) {

                            StateModel stateModel = (StateModel) object;
                            stateId = String.valueOf(stateModel.getStateId());
                            textView.setText(stateModel.getStateName());
                            textView.setTag(stateModel);
                            edtCity.setText("");
                            edtCity.performClick();

                        } else if (object instanceof CityModel) {

                            CityModel cityModel = (CityModel) object;
                            textView.setText(cityModel.getCityName());
                            textView.setTag(cityModel);
                            cityId = String.valueOf(cityModel.getCityId());
                        } else if (object instanceof GenderModel) {
                            GenderModel genderModel = (GenderModel) object;
                            textView.setText(genderModel.getGender());
                            genderId = genderModel.getId();
                        }
                    }

                }
            });

            edtSearchLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    /* Show clear icon on editext length greater than zero */
                    String searchStr = edtSearchLocation.getText().toString().trim();

                    ArrayList<Object> filterList = new ArrayList<>();

                    for (int i = 0; i < listLocation.size(); i++) {
                        try {

                            Object object = listLocation.get(i);

                            if (object instanceof CountryModel) {

                                CountryModel countryModel = (CountryModel) object;

                                if (countryModel.getCountryName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                                textView.setText(countryModel.getCountryName());
                                textView.setTag(countryModel);

                            } else if (object instanceof StateModel) {

                                StateModel stateModel = (StateModel) object;

                                if (stateModel.getStateName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }

                            } else if (object instanceof CityModel) {

                                CityModel cityModel = (CityModel) object;

                                if (cityModel.getCityName().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            } else if (object instanceof GenderModel) {

                                GenderModel genderModel = (GenderModel) object;

                                if (genderModel.getGender().toLowerCase().startsWith(searchStr.toLowerCase().trim())) {
                                    filterList.add(object);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (filterList.size() > 0) {
                        txtNoRecord.setVisibility(View.GONE);
                        listViewLocation.setVisibility(View.VISIBLE);

                        AdpLocation adpLocationList = new AdpLocation(context, filterList, searchStr/*, locationType*/);
                        listViewLocation.setAdapter(adpLocationList);

                    } else {
                        txtNoRecord.setVisibility(View.VISIBLE);
                        listViewLocation.setVisibility(View.GONE);
                    }

                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            if (mDialog != null && !mDialog.isShowing()) {
                mDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    handleCrop(0, result.getUri().getPath());
                    break;
            }
        }
    }

    private void beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        /*Crop.of(source, destination)
                .withAspect(imgProfilePhoto.getWidth(), imgProfilePhoto.getHeight())
                .start(getActivity(), this);*/
    }

    private void handleCrop(int resultCode, String data) {

        image64Base = Utils.getStringImage(data, ImageUpload.ClientProfile);
        // imgHelpPhoto.setVisibility(View.VISIBLE);
        //imgProfilePhoto.setImageURI(null);
        // imgProfilePhoto.setImageURI(Crop.getOutput(data));
        //Utils.setImage(Crop.getOutput(data).getPath(), R.drawable.img_user_placeholder, imgHelpPhoto);
        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.CLIENT_PROFILE_PIC_UPDATE);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
        params.put("ProfilePic", image64Base);

        RestClient.getInstance().post(profileActivity, Request.Method.POST, params,
                ApiList.CLIENT_PROFILE_PIC_UPDATE, true, RequestCode.ClientProfilePicUpdate, this);
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
                    // start cropping activity for pre-acquired image saved on the device
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setActivityTitle("Crop")
                            .setRequestedSize(400, 400)
                            .start(getContext(), this);
                    //showImageSelect(getActivity(), getString(R.string.str_select_profile_photo), true);
                } else {
                    PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION_STORAGE_CAMERA, permissionList);
                }
            }
        }
    }

    private void GetClientInfo() {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.GET_CLIENT_INFO);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("AuthKey", ApiList.AUTH_KEY);

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.GET_CLIENT_INFO,
                    true, RequestCode.GetClientInfo, this);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void clientUpdate() {

        try {

            Map<String, String> params = new HashMap<>();
            params.put("op", ApiList.CLIENT_UPDATE);
            params.put("AuthKey", ApiList.AUTH_KEY);
            params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
            params.put("FirstName", edtFirstName.getText().toString());
            params.put("LastName", edtLastName.getText().toString());
            params.put("Gender", String.valueOf(genderId));
            params.put("Address1", "");
            params.put("Address2", "");
            //params.put("City", edtCity.getText().toString());
            params.put("POBox", edtPostalCode.getText().toString());
            if (cityId.equals("")) {
                params.put("City", String.valueOf(loginUserModel.getCityId()));
            } else {
                params.put("City", cityId);
            }

            if (stateId.equals("")) {
                params.put("State", String.valueOf(loginUserModel.getStateId()));
            } else {
                params.put("State", stateId);
            }

            if (countryId.equals("")) {
                params.put("Country", String.valueOf(loginUserModel.getCountryId()));
            } else {
                params.put("Country", countryId);
            }

            //params.put("State", edtState.getText().toString());
            //params.put("Country", edtCountry.getText().toString());
            params.put("PhoneNo", edtTelephone.getText().toString());
            params.put("EmailId", edtEmail.getText().toString());

            RestClient.getInstance().post(getActivity(), Request.Method.POST, params, ApiList.CLIENT_UPDATE,
                    true, RequestCode.ClientUpdate, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
