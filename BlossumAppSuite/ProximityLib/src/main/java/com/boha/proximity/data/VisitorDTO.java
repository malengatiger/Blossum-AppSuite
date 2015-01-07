package com.boha.proximity.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aubreyM on 2014/09/09.
 */
public class VisitorDTO implements Serializable {
    private int visitorID;
    private String firstName, lastName, email;
    private long dateRegistered;
    private List<VisitorTrackDTO> visitorTrackList;

    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
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

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public List<VisitorTrackDTO> getVisitorTrackList() {
        return visitorTrackList;
    }

    public void setVisitorTrackList(List<VisitorTrackDTO> visitorTrackList) {
        this.visitorTrackList = visitorTrackList;
    }

}
