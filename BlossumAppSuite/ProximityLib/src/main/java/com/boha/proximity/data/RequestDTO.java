/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.proximity.data;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class RequestDTO implements Serializable {

    private int beaconID, branchID, companyID, requestType,visitorID;
    private CompanyDTO company;
    private BranchDTO branch;
    private BeaconDTO beacon;
    private BeaconDataItemDTO beaconDataItem;
    private String macAddress, fileName;
    private VisitorDTO visitor;
    private VisitorTrackDTO visitorTrack;

    public static final int REGISTER_COMPANY = 1;
    public static final int REGISTER_BRANCH = 2;
    public static final int REGISTER_BEACON = 3;
    public static final int REGISTER_DATA_ITEM = 4;
    public static final int UPDATE_DATA_ITEM = 5;
    public static final int UPDATE_BEACON = 6;

    public static final int REGISTER_VISITOR = 7;
    public static final int REGISTER_VISIT = 8;


    public static final int GET_BRANCH_BEACONS = 11;
    public static final int GET_BEACONS_BY_MAC_ADDRESS = 12;
    public static final int GET_COMPANY_BEACONS = 33;
    public static final int GET_BEACON_IMAGE_FILES = 14;
    public static final int GET_COMPANIES = 15;


    public static final int GET_VISITOR_LIST = 16;
    public static final int GET_VISITOR_TRACKS = 17;
    public static final int GET_VISITOR_TRACKS_SORTED_BY_BEACONS = 18;

    public static final int GET_ERROR_REPORTS = 73;
    //
    public static final int DELETE_ALL_BEACON_IMAGES = 20;
    public static final int DELETE_BEACON_IMAGE = 21;
    public static final int DELETE_BEACON = 22;
    public static final int DELETE_ALL_BEACONS = 23;
    //


    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
    }

    public VisitorDTO getVisitor() {
        return visitor;
    }

    public void setVisitor(VisitorDTO visitor) {
        this.visitor = visitor;
    }

    public VisitorTrackDTO getVisitorTrack() {
        return visitorTrack;
    }

    public void setVisitorTrack(VisitorTrackDTO visitorTrack) {
        this.visitorTrack = visitorTrack;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    
    public int getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(int beaconID) {
        this.beaconID = beaconID;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public BranchDTO getBranch() {
        return branch;
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
    }

    public BeaconDTO getBeacon() {
        return beacon;
    }

    public void setBeacon(BeaconDTO beacon) {
        this.beacon = beacon;
    }

    public BeaconDataItemDTO getBeaconDataItem() {
        return beaconDataItem;
    }

    public void setBeaconDataItem(BeaconDataItemDTO beaconDataItem) {
        this.beaconDataItem = beaconDataItem;
    }

}
