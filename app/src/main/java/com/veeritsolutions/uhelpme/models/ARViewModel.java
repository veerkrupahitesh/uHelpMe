package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by VEER7 on 7/17/2017.
 */

public class ARViewModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private long dataId = 0;
    @SerializedName("ClientId")
    @Expose
    private long clientId = 0;
    @SerializedName("FirstName")
    @Expose
    private String firstName = "";
    @SerializedName("LastName")
    @Expose
    private String lastName = "";
    @SerializedName("Latitude")
    @Expose
    private double latitude = 0;
    @SerializedName("Longitude")
    @Expose
    private double longitude = 0;
    @SerializedName("Altitude")
    @Expose
    private double altitude = 0;
    @SerializedName("Rating")
    @Expose
    private float rating = 0;
    @SerializedName("CategoryId")
    @Expose
    private long categoryId = 0;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName = "";
    @SerializedName("CategoryIcon1")
    @Expose
    private String categoryIcon1 = "";
    @SerializedName("CategoryIcon2")
    @Expose
    private String categoryIcon2 = "";
    @SerializedName("CategoryColorCode")
    @Expose
    private String categoryColorCode = "";
    @SerializedName("Distance")
    @Expose
    private float distance = 0;

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon1() {
        return categoryIcon1;
    }

    public void setCategoryIcon1(String categoryIcon1) {
        this.categoryIcon1 = categoryIcon1;
    }

    public String getCategoryIcon2() {
        return categoryIcon2;
    }

    public void setCategoryIcon2(String categoryIcon2) {
        this.categoryIcon2 = categoryIcon2;
    }

    public String getCategoryColorCode() {
        return categoryColorCode;
    }

    public void setCategoryColorCode(String categoryColorCode) {
        this.categoryColorCode = categoryColorCode;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}