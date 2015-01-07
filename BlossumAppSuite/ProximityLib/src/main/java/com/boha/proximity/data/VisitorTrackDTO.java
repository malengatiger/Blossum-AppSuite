package com.boha.proximity.data;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by aubreyM on 2014/09/09.
 */
public class VisitorTrackDTO implements Serializable {
    private int visitorTrackID, visitorID, beaconID;
    private String beaconName;
    private long dateTracked;
    private String firstName, lastName, email;

    public int getVisitorTrackID() {
        return visitorTrackID;
    }

    public void setVisitorTrackID(int visitorTrackID) {
        this.visitorTrackID = visitorTrackID;
    }

    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
    }

    public int getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(int beaconID) {
        this.beaconID = beaconID;
    }

    public String getBeaconName() {
        return beaconName;
    }

    public void setBeaconName(String beaconName) {
        this.beaconName = beaconName;
    }

    public long getDateTracked() {
        return dateTracked;
    }

    public void setDateTracked(long dateTracked) {
        this.dateTracked = dateTracked;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public VisitorTrackDTO() {
    }

    private VisitorTrackDTO(Parcel in) {
        this.visitorTrackID = in.readInt();
        this.visitorID = in.readInt();
        this.beaconID = in.readInt();
        this.beaconName = in.readString();
        this.dateTracked = in.readLong();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.email = in.readString();
    }

}
