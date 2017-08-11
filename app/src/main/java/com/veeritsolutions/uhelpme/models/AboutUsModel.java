package com.veeritsolutions.uhelpme.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 7/22/2017.
 */

public class AboutUsModel implements Serializable {

    @SerializedName("DataId")
    @Expose
    private int dataId = 1;

    @SerializedName("AboutUsId")
    @Expose
    private int AboutUsId = 1;

    @SerializedName("Remarks")
    @Expose
    private String Remarks;

    @SerializedName("CreatedOn")
    @Expose
    private String CreatedOn;

    @SerializedName("EndDate")
    @Expose
    private String EndDate;

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getAboutUsId() {
        return AboutUsId;
    }

    public void setAboutUsId(int aboutUsId) {
        AboutUsId = aboutUsId;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
