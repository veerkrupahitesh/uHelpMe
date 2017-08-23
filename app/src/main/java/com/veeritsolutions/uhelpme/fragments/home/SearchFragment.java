package com.veeritsolutions.uhelpme.fragments.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.activity.HomeActivity;
import com.veeritsolutions.uhelpme.api.ApiList;
import com.veeritsolutions.uhelpme.api.DataObserver;
import com.veeritsolutions.uhelpme.api.RequestCode;
import com.veeritsolutions.uhelpme.api.RestClient;
import com.veeritsolutions.uhelpme.fragments.profile.OtherPersonProfileFragment;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.helper.ToastHelper;
import com.veeritsolutions.uhelpme.listener.OnBackPressedEvent;
import com.veeritsolutions.uhelpme.listener.OnClickEvent;
import com.veeritsolutions.uhelpme.models.ARViewModel;
import com.veeritsolutions.uhelpme.models.LoginUserModel;
import com.veeritsolutions.uhelpme.utility.Constants;
import com.veeritsolutions.uhelpme.utility.PermissionClass;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VEER7 on 7/1/2017.
 */

public class SearchFragment extends Fragment implements OnClickEvent, OnBackPressedEvent, OnMapReadyCallback, DataObserver {

    int PLACE_PICKER_REQUEST = 1;
    private HomeActivity homeActivity;
    private View view;
    private View rootView;
    private RelativeLayout mapFrameLayout;
    private ImageView imgBackHeader, imgCamera;
    private FloatingActionButton fabRefresh;
    private MapView mMapView;

    private SupportMapFragment spFragment;
    private float latitude, longitude;
    private GoogleMap mGoogleMap;
    //  private PlacePicker.IntentBuilder builder;
    private LoginUserModel loginUserModel;
    private ArrayList<ARViewModel> arViewList;
    private List<String> permissionList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        homeActivity.imgHome.setImageResource(R.drawable.img_home_inactive);
        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_active);
        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_inactive);
        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_inactive);
        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme);

        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));

        // return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        imgBackHeader = (ImageView) rootView.findViewById(R.id.img_back_header);
        imgBackHeader.setVisibility(View.GONE);
        fabRefresh = (FloatingActionButton) rootView.findViewById(R.id.fab_refresh);
//        fabRefresh = new FloatingActionButton(homeActivity);
//        fabRefresh.setImageResource(R.drawable.drw_autorenew_black_24dp);
//        fabRefresh.setRippleColor(Color.WHITE);
//        //fabRefresh.setPadding(0, 80, 0, 0);
//        fabRefresh.setUseCompatPadding(true);
//        fabRefresh.setLeft(10);
//        fabRefresh.setTop(10);
//
//        // fabRefresh.setRotation(360);
//        fabRefresh.setSize(FloatingActionButton.SIZE_MINI);
        imgCamera = (ImageView) rootView.findViewById(R.id.img_camera);
        /*if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map, container, false);
        } catch (InflateException e) {
        *//* map is already there, just return dataView as it is *//*
        }*/
        mapFrameLayout = (RelativeLayout) rootView.findViewById(R.id.map_framelayout);
        // mapFrameLayout.setPadding(0, 80, 0, 0);
        // LinearLayout.LayoutParams relativeParams = (LinearLayout.LayoutParams) mapFrameLayout.getLayoutParams();
        // relativeParams.setMargins(0, 80, 0, 0);  // left, top, right, bottom
        //mapFrameLayout.setLayoutParams(relativeParams);

        //mapFrameLayout.setGravity(Gravity.BOTTOM);
//        mapFrameLayout.addView(view);
//        mapFrameLayout.addView(fabRefresh);

       /* spFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        spFragment.getMapAsync(this);*/
        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(this);

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getARViewData(true);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  builder = new PlacePicker.IntentBuilder();
        loginUserModel = LoginUserModel.getLoginUserModel();
    }

    @Override
    public void onBackPressed() {

//        homeActivity.imgHome.setImageResource(R.drawable.img_home_active);
//        homeActivity.imgSearch.setImageResource(R.drawable.img_search_tabbar_inactive);
//        homeActivity.imgDashbord.setImageResource(R.drawable.img_dashboard_inactive);
//        homeActivity.imgChatRoom.setImageResource(R.drawable.img_chat_room_inactive);
//        homeActivity.imgHelpMe.setImageResource(R.drawable.img_helpme);
//
//        homeActivity.tvHome.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
//        homeActivity.tvSearch.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
//        homeActivity.tvDashBoard.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));
//        homeActivity.tvChatRoom.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorHint, null));

        homeActivity.popBackFragment();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.edt_searchUser:
                Utils.buttonClickEffect(view);
               /* try {
                    startActivityForResult(builder.build(homeActivity), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.img_camera:
                Utils.buttonClickEffect(view);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    permissionList = new ArrayList<>();
                    permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permissionList.add(Manifest.permission.CAMERA);

                    if (PermissionClass.checkPermission(homeActivity, PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, permissionList)) {


                    }

                } else {

                }
                break;

            /*case R.id.fab_refresh:

                getARViewData(true);
                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            /*if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(homeActivity, data);
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        place.getLatLng(), 5);
                mGoogleMap.animateCamera(cameraUpdate);

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(homeActivity, toastMsg, Toast.LENGTH_LONG).show();
            }*/
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (PrefHelper.getInstance().containKey(PrefHelper.LATITUDE)) {
            latitude = PrefHelper.getInstance().getFloat(PrefHelper.LATITUDE, 0);
        }
        if (PrefHelper.getInstance().containKey(PrefHelper.LONGITUDE)) {
            longitude = PrefHelper.getInstance().getFloat(PrefHelper.LONGITUDE, 0);
        }
        // mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(getString(R.string.your_location)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitude, longitude), 15);
        mGoogleMap.animateCamera(cameraUpdate);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        mGoogleMap.setBuildingsEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(1000)
                .strokeColor(Color.RED));

        List<String> permission = new ArrayList<>();
        permission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        permission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permission.add(Manifest.permission.ACCESS_NETWORK_STATE);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (PermissionClass.checkPermission(getActivity(), PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION, permission)) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                getARViewData(false);
            }
        });

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ARViewModel arViewModel = (ARViewModel) marker.getTag();
                if (arViewModel != null) {
                    LoginUserModel loginUserModel = new LoginUserModel();
                    loginUserModel.setClientId((int) arViewModel.getClientId());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.USER_DATA, loginUserModel);
                    homeActivity.pushFragment(new OtherPersonProfileFragment(), true, false, bundle);
                }
                return true;
            }
        });
    }

    private void getARViewData(boolean requiredDialog) {
        Map<String, String> params = new HashMap<>();
        params.put("op", ApiList.GET_AR_VIEW);
        params.put("AuthKey", ApiList.AUTH_KEY);
        params.put("ClientId", String.valueOf(loginUserModel.getClientId()));

        RestClient.getInstance().post(homeActivity, Request.Method.POST, params,
                ApiList.GET_AR_VIEW, requiredDialog, RequestCode.GetARView, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> shouldPermit = new ArrayList<>();

        if (requestCode == PermissionClass.REQUEST_CODE_RUNTIME_PERMISSION) {

            if (grantResults.length > 0 || grantResults.length != PackageManager.PERMISSION_GRANTED) {

                for (int i = 0; i < grantResults.length; i++) {
                    //  permissions[i] = Manifest.permission.CAMERA; //for specific permission check
                    grantResults[i] = PackageManager.PERMISSION_DENIED;
                    shouldPermit.add(permissions[i]);
                    if (permissions[i] == Manifest.permission.ACCESS_COARSE_LOCATION && permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    public void onSuccess(RequestCode mRequestCode, Object mObject) {

        switch (mRequestCode) {

            case GetARView:

                arViewList = (ArrayList<ARViewModel>) mObject;
                if (arViewList != null && !arViewList.isEmpty()) {
                    for (int i = 0; i < arViewList.size(); i++) {
                        new TheTask().execute(arViewList.get(i));
                    }
                }
                break;
        }
    }

    @Override
    public void onFailure(RequestCode mRequestCode, String mError) {

        ToastHelper.getInstance().showMessage(mError);
    }

    private class TheTask extends AsyncTask<ARViewModel, Void, Bitmap> {

        ARViewModel[] arViewModel;

        @Override
        protected Bitmap doInBackground(ARViewModel... params) {
            arViewModel = params;
            URL url;
            Bitmap bmp = null;
            try {
                url = new URL(params[0].getCategoryIcon1());

                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);

            if (result != null) {
                result = Bitmap.createScaledBitmap(result, 70, 90, true);
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(arViewModel[0].getLatitude(), arViewModel[0].getLongitude()))
                        .title("Distance " + arViewModel[0].getDistance() + "Km")
                        .snippet(arViewModel[0].getFirstName() + " " + arViewModel[0].getLastName())
                        .icon(BitmapDescriptorFactory.fromBitmap(result)))
                        .setTag(arViewModel[0]);
            } else {
                result = BitmapFactory.decodeResource(getResources(), R.drawable.img_pin_blue);
                result = Bitmap.createScaledBitmap(result, 70, 90, true);
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(arViewModel[0].getLatitude(), arViewModel[0].getLongitude()))
                        .title("Distance " + arViewModel[0].getDistance() + "Km")
                        .snippet(arViewModel[0].getFirstName() + " " + arViewModel[0].getLastName())
                        .icon(BitmapDescriptorFactory.fromBitmap(result)))
                        .setTag(arViewModel[0]);
            }

        }
    }
}
