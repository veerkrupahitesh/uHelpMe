package com.veeritsolutions.uhelpme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.models.ChatModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class AdpOneToOneChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //List<ChatModel> chatList;
    Context context;
    private DatabaseReference mRef;
    // private Class<ChatModel> mModelClass;

    private List<ChatModel> mModels;
    private List<String> mKeys;
    private ChildEventListener mListener;
    private LoginUserModel loginUserModel;

    public AdpOneToOneChat(DatabaseReference mRef, Context context) {
        //this.chatList = chatList;
        this.context = context;
        this.mRef = mRef;
        // this.mModelClass = chatList
        // this.mLayout = mLayout;
        //mInflater = activity.getLayoutInflater();
        mModels = new ArrayList<>();
        mKeys = new ArrayList<>();
        CustomDialog.getInstance().showProgress(context, "", true);
        loginUserModel = LoginUserModel.getLoginUserModel();
// Look for all child events. We will then map them to our own internal ArrayList, which backs ListView

        mListener = this.mRef.limitToLast(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                CustomDialog.getInstance().dismiss();
                ChatModel model = dataSnapshot.getValue(ChatModel.class);
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {

                    mModels.add(0, model);
                    mKeys.add(0, key);

                } else {

                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(model);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CustomDialog.getInstance().dismiss();
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                ChatModel newModel = dataSnapshot.getValue(ChatModel.class);
                int index = mKeys.indexOf(key);

                mModels.set(index, newModel);

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CustomDialog.getInstance().dismiss();
                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                mModels.remove(index);

                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                CustomDialog.getInstance().dismiss();
                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                ChatModel newModel = dataSnapshot.getValue(ChatModel.class);
                int index = mKeys.indexOf(key);
                mModels.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    mModels.add(0, newModel);
                    mKeys.add(0, key);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(newModel);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                CustomDialog.getInstance().dismiss();
                Debug.trace("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_chat_message, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            ChatModel chatModel = mModels.get(position);
            myViewHolder.tvAuthor.setTag(chatModel);

            if (chatModel.getClientId() == loginUserModel.getClientId()) {
                myViewHolder.linParent.setGravity(Gravity.END);
                myViewHolder.linChat.setGravity(Gravity.END);
                myViewHolder.linChat.setBackgroundResource(R.drawable.chatright);
                myViewHolder.tvAuthor.setVisibility(View.GONE);
            } else {
                myViewHolder.linParent.setGravity(Gravity.START);
                myViewHolder.linChat.setGravity(Gravity.START);
                myViewHolder.linChat.setBackgroundResource(R.drawable.chat);
                if (chatModel.getIsChatGroup() == 1) {
                    myViewHolder.tvAuthor.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.tvAuthor.setVisibility(View.GONE);
                }
            }


            myViewHolder.tvAuthor.setText(chatModel.getClientName());
            myViewHolder.tvMsg.setText(chatModel.getMessage());
            myViewHolder.tvDateAndTime.setText(chatModel.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mKeys.clear();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        // View dataView;
        // ImageView imgOfferDp/*, imgOfferSearch*/;
        TextView tvMsg, tvDateAndTime, tvAuthor;
        //  Button btnMoreDetails, btnDecline;
        LinearLayout linChat, linParent;

        MyViewHolder(View itemView) {
            super(itemView);

            // dataView = itemView.findViewById(R.id.view_strip);
            //imgOfferDp = (ImageView) itemView.findViewById(R.id.img_profilePhoto);
            // imgOfferSearch = (ImageView) itemView.findViewById(R.id.img_offer_search);

            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvAuthor.setTypeface(MyApplication.getInstance().FONT_WORKSANS_MEDIUM);

            tvMsg = (TextView) itemView.findViewById(R.id.tv_message);
            tvMsg.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            tvDateAndTime = (TextView) itemView.findViewById(R.id.tv_dateTime);
            tvDateAndTime.setTypeface(MyApplication.getInstance().FONT_WORKSANS_REGULAR);

            linChat = (LinearLayout) itemView.findViewById(R.id.lin_chat);
            linParent = (LinearLayout) itemView.findViewById(R.id.lin_parentChat);
        }
    }
}
