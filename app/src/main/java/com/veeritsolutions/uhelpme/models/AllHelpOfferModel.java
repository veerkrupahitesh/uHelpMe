package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/7/2017.
 */

public class AllHelpOfferModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId = 0;
    @SerializedName("JobPostOfferId")
    @Expose
    private int jobPostOfferId = 0;
    @SerializedName("JobPostId")
    @Expose
    private int jobPostId = 0;
    @SerializedName("ClientId")
    @Expose
    private int clientId = 0;
    @SerializedName("IsMyOffer")
    @Expose
    private int isMyOffer = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("OfferAmount")
    @Expose
    private int offerAmount = 0;
    @SerializedName("Latitude")
    @Expose
    private float latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private float longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private float altitude = 0;
    @SerializedName("CreatedOn")
    @Expose
    private String createdOn = "";
    @SerializedName("EndDate")
    @Expose
    private String endDate = "";

    private int position = 0;
    @SerializedName("IsHire")
    @Expose
    private int IsHire = 0;
    @SerializedName("ProfilePic")
    @Expose
    private String ProfilePic = "";

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getJobPostOfferId() {
        return jobPostOfferId;
    }

    public void setJobPostOfferId(int jobPostOfferId) {
        this.jobPostOfferId = jobPostOfferId;
    }

    public int getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(int jobPostId) {
        this.jobPostId = jobPostId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(int offerAmount) {
        this.offerAmount = offerAmount;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getIsMyOffer() {
        return isMyOffer;
    }

    public void setIsMyOffer(int isMyOffer) {
        this.isMyOffer = isMyOffer;
    }

    public int getIsHire() {
        return IsHire;
    }

    public void setIsHire(int isHire) {
        IsHire = isHire;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}
