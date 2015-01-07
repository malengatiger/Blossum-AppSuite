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
public class BeaconDataItemDTO implements Serializable {
  private int beaconDataItemID;
    private String imageUrl;
    private String html;
    private String text;
    private int beaconID;  

    public int getBeaconDataItemID() {
        return beaconDataItemID;
    }

    public void setBeaconDataItemID(int beaconDataItemID) {
        this.beaconDataItemID = beaconDataItemID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(int beaconID) {
        this.beaconID = beaconID;
    }
}
