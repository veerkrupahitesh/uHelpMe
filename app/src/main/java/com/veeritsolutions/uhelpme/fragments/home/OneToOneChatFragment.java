package com.veeritsolutions.uhelpme.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.adapters.AdpOneToOneChat;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.api.ServerConfig;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ChatModel;
import com.veeritsolutions.uhelpme.models.ChatUsersListModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class OneToOneChatFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, DataObserver {

    private View rootView;
    private RecyclerView recyclerViewChat;
    private EditText inputText;
    private Button btnSend;
    private TextView tvHeader;
    private ImageView imgProfilePic;

    private HomeActivity homeActivity;
    private AdpOneToOneChat adpChat;
    private ChatUsersListModel specificCategoryChatListModel;
    private Bundle bundle;
    private LoginUserModel loginUserModel;

    // Firebase variables and objects
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceOne, databaseReferenceTwo;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();

        loginUserModel = LoginUserModel.getLoginUserModel();
        bundle = getArguments();

        if (bundle != null) {

            specificCategoryChatListModel = (ChatUsersListModel) bundle.getSerializable(Constants.CHAT_DATA);
        }

        firebaseDatabase = FirebaseDatabase.getInstance(ServerConfig.FCM_APP_URL);
        databaseReferenceOne = firebaseDatabase.getReference().child(String.valueOf(loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId()));
        databaseReferenceTwo = firebaseDatabase.getReference().child(String.valueOf(specificCategoryChatListModel.getId() + "_" + loginUserModel.getClientId()));

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_one_to_one_chat, container, false);

        tvHeader = (TextView) rootView.findViewById(R.id.tv_headerTitle);
        tvHeader.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);
        tvHeader.setText(specificCategoryChatListModel.getName());

        tvHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.buttonClickEffect(view);

                LoginUserModel loginUserModel = new LoginUserModel();
                loginUserModel.setClientId(specificCategoryChatListModel.getId());
                bundle = new Bundle();
                bundle.putSerializable(Constants.USER_DATA, loginUserModel);

                homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
            }
        });

        imgProfilePic = (ImageView) rootView.findViewById(R.id.img_profilePhoto);
        Utils.setImage(specificCategoryChatListModel.getProfilePic(), R.drawable.img_user_placeholder, imgProfilePic);

        recyclerViewChat = (RecyclerView) rootView.findViewById(R.id.recyclerView_chat);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(homeActivity, LinearLayoutManager.VERTICAL, false);
        recyclerViewChat.setLayoutManager(linearLayoutManager);
        // Setup our input methods. Enter key on the keyboard or pushing the send button
        inputText = (EditText) rootView.findViewById(R.id.edt_sendMsg);
        btnSend = (Button) rootView.findViewById(R.id.img_send);

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = inputText.getText().toString().trim();

                if (str.isEmpty()) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });

        adpChat = new AdpOneToOneChat(databaseReferenceOne, homeActivity);
        recyclerViewChat.setAdapter(adpChat);
        recyclerViewChat.setAddStatesFromChildren(true);

        adpChat.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                linearLayoutManager.scrollToPosition(adpChat.getItemCount() - 1);
            }
        });

        Utils.setupOutSideTouchHideKeyboard(rootView);
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

            case R.id.img_send:
                Utils.buttonClickEffect(view);
                sendMsg();
                break;
        }
    }

    private void sendMsg() {

        EditText inputText = (EditText) getActivity().findViewById(R.id.edt_sendMsg);
        String msg = inputText.getText().toString().trim();

        if (!msg.equals("")) {
            // Create our 'model', a Chat object
            ChatModel userChat = new ChatModel(loginUserModel.getClientId(), loginUserModel.getFirstName(),
                    msg, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A), 0);
            // ChatModel otherChat = new ChatModel(specificCategoryChatListModel.getClientId(), specificCategoryChatListModel.getFirstName(), input, Utils.dateFormat(System.currentTimeMillis(), Constants.MM_DD_YYYY_HH_MM_SS_A));
            // Create a new, auto-generated child of that chat location, and save our chat data there
            databaseReferenceOne.push().setValue(userChat);
            databaseReferenceTwo.push().setValue(userChat);
            inputText.setText("");


            String str = loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId();

            if (!PrefHelper.getInstance().containKey(str)) {

                Map<String, String> params = new HashMap<>();
                params.put("op", ApiList.CHAT_USER_INSERT);
                params.put("AuthKey", ApiList.AUTH_KEY);
                params.put("ClientId", String.valueOf(loginUserModel.getClientId()));
                params.put("ToClientId", String.valueOf(specificCategoryChatListModel.getId()));

                RestClient.getInstance().post(homeActivity, Request.Method.POST, params, ApiList.CHAT_USER_INSERT,
                        false, RequestCode.ChatUserInsert, this);
            }
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case ChatUserInsert:
                PrefHelper.getInstance().setString(
                        loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId(),
                        loginUserModel.getClientId() + "_" + specificCategoryChatListModel.getId());
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

    }
}
