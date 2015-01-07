package com.boha.proximity.data;

import java.io.Serializable;

/**
 * Created by aubreyM on 2014/07/27.
 */
public class PhotoUploadDTO implements Serializable {
    public static final String BRANCH_PREFIX = "branch";
    public static final String COMPANY_PREFIX = "company";
    public static final String BEACON_PREFIX = "beacon";


    public static final int PICTURES_FULL_SIZE = 1;
    public static final int PICTURES_THUMBNAILS = 2;

    private int companyID, branchID, beaconID;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public int getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(int beaconID) {
        this.beaconID = beaconID;
    }

}
