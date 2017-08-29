package com.veeritsolutions.uhelpme.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.enums.RegisterBy;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements OnClickEvent, DataObserver {

    // xml components
    private LinearLayout linParentView;
    private EditText edtFirstName, edtEmail, edtLastName, edtPassword;
    private Button btnSingUp;
    private Toolbar toolbar;
    private TextView tvHeader, tvTermsAndConditions;
    private ImageView imgBackHeader;

    //object or variable declaration
    private String firstName, mEmailAddress, lastName, mPassword;
    //private JSONObject params;
    private String lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (PrefHelper.getInstance().containKey(PrefHelper.LANGUAGE)) {
            lang = PrefHelper.getInstance().getString(PrefHelper.LANGUAGE, "en");
        }
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
                //profileActivity.getBaseContext().createConfigurationContext(config);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            } else {
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            }
            //  profileActivity.recreate();
        }
        init();
        Utils.setupOutSideTouchHideKeyboard(linParentView);
    }

    private void init() {

        //  imgBackHeader = (ImageView) findViewById(R.id.img_back_header);
        //  imgBackHeader.setVisibility(View.INVISIBLE);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

       /* @SuppressLint("PrivateResource")
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material, null);
        if (upArrow != null) {
            upArrow.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorTitle, null), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }*/

        // tvHeader = (TextView) findViewById(R.id.tv_headerTitle);
        // tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);
        // tvHeader.setText(getString(R.string.str_create_account));

        linParentView = (LinearLayout) findViewById(R.id.parentView);

        edtFirstName = (EditText) findViewById(R.id.edt_firstname);
        edtFirstName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtEmail.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtLastName = (EditText) findViewById(R.id.edt_lastname);
        edtLastName.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtPassword.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

        btnSingUp = (Button) findViewById(R.id.btn_sign_up);
        btnSingUp.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvTermsAndConditions = (TextView) findViewById(R.id.tv_termsAndConditions);
        tvTermsAndConditions.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

        tvTermsAndConditions.setLinkTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link), Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvTermsAndConditions.setText(Html.fromHtml(getString(R.string.general_conditions)
                    + getString(R.string.link)));
        }
        tvTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private boolean validateForm() {


        firstName = edtFirstName.getText().toString().trim();
        mEmailAddress = edtEmail.getText().toString().trim();
        lastName = edtLastName.getText().toString().trim();
        mPassword = edtPassword.getText().toString().trim();


        if (firstName.isEmpty()) {
            edtFirstName.requestFocus();
            edtFirstName.setError(getString(R.string.enter_first_name));
            return false;
        } else if (lastName.isEmpty()) {
            edtLastName.requestFocus();
            edtLastName.setError(getString(R.string.enter_last_name));
            return false;
        } else if (mEmailAddress.isEmpty()) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!mEmailAddress.matches(Patterns.EMAIL_ADDRESS.pattern())) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (mPassword.isEmpty()) {
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else {

            return true;
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case clientInsert:

                PrefHelper.getInstance().setBoolean(PrefHelper.IS_LOGIN, true);
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
                // signUpInFireBase();
                break;
        }
    }

    private void signUpInFireBase() {
        CustomDialog.getInstance().showProgress(this, "", false);
        LoginUserModel loginUserModel = LoginUserModel.getLoginUserModel();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(loginUserModel.getEmailId(), loginUserModel.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        CustomDialog.getInstance().dismiss();
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the stack of activities
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();
                        } else {
                            ToastHelper.getInstance().showMessage(task.getException().getLocalizedMessage());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CustomDialog.getInstance().dismiss();
                        ToastHelper.getInstance().showMessage(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_sign_up:
                Utils.buttonClickEffect(view);
                if (validateForm()) {
                    try {
                        //params = new JSONObject();
                        Map<String, String> params = new HashMap<>();
                        params.put("op", ApiList.CLIENT_INSERT);
                        params.put("AuthKey", ApiList.AUTH_KEY);
                        params.put("FirstName", firstName);
                        params.put("LastName", lastName);
                        params.put("EmailId", mEmailAddress);
                        params.put("Password", mPassword);
                        params.put("AcTokenId", "");
                        params.put("RegisteredBy", RegisterBy.APP.getRegisterBy());

                        RestClient.getInstance().post(this, Request.Method.POST, params, ApiList.CLIENT_INSERT,
                                true, RequestCode.clientInsert, this);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.img_back_header:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
