package com.boha.proximity.cms.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.R;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.volley.BaseVolley;

/**
 * Created by aubreyM on 2014/08/13.
 */
public class BeaconDeleteDialog extends DialogFragment {
    public BeaconDeleteDialog() {
    }
    public interface BeaconDeleteListener {
        public void onImagesDeleted();
        public void onBeaconDeleted();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.remove_beacon_dialog, container);
        chkImages = (RadioButton)view.findViewById(R.id.DEL_chkImages);
        chkBeacon = (RadioButton)view.findViewById(R.id.DEL_chkBeacon);
        btnCan = (Button)view.findViewById(R.id.DEL_btnCancel);
        btnRemove= (Button)view.findViewById(R.id.DEL_btnRemove);
        txtBeacon = (TextView)view.findViewById(R.id.DEL_txtBeaconName);
        txtBeacon.setText(beacon.getBeaconName());

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkImages.isChecked()) {
                    removeImages();
                    return;
                }
                if (chkBeacon.isChecked()) {
                    removeBeacon();
                    return;
                }
                Toast.makeText(context, "Nothing selected fpr removal",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void removeImages() {
        RequestDTO w = new RequestDTO();
        w.setCompanyID(beacon.getCompanyID());
        w.setBranchID(beacon.getBranchID());
        w.setBeaconID(beacon.getBeaconID());
        w.setRequestType(RequestDTO.DELETE_ALL_BEACON_IMAGES);

        if (!BaseVolley.checkNetworkOnDevice(context)) return;
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,context,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response.getStatusCode() == 0) {
                    listener.onImagesDeleted();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });

    }
    private void removeBeacon() {
        RequestDTO w = new RequestDTO();
        w.setCompanyID(beacon.getCompanyID());
        w.setBranchID(beacon.getBranchID());
        w.setBeaconID(beacon.getBeaconID());
        w.setRequestType(RequestDTO.DELETE_BEACON);

        if (!BaseVolley.checkNetworkOnDevice(context)) return;
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,context,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response.getStatusCode() == 0) {
                    listener.onBeaconDeleted();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });
    }
    View view;
    Context context;
    RadioButton chkImages, chkBeacon;
    Button btnCan, btnRemove;
    TextView txtBeacon;
    BeaconDTO beacon;
    BeaconDeleteListener listener;

    public void setListener(BeaconDeleteListener listener) {
        this.listener = listener;
    }

    public void setBeacon(BeaconDTO beacon) {
        this.beacon = beacon;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}