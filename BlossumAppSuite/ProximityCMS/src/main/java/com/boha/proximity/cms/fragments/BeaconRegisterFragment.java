package com.boha.proximity.cms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.R;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.SharedUtil;
import com.boha.proximity.volley.BaseVolley;

import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class BeaconRegisterFragment extends Fragment {

    public interface BeaconRegisterListener {
        public void onBeaconRegistered(List<BeaconDTO> list);
    }
    BranchDTO branch;
    BeaconRegisterListener listener;

    @Override
    public void onAttach(Activity a) {
        if (a instanceof BeaconRegisterListener) {
            listener = (BeaconRegisterListener)a;
        } else {
            throw new UnsupportedOperationException("Host "
            + a.getLocalClassName() + " must implement BeaconRegisterListener");
        }
        Log.e(LOG, "##### Fragment hosted by " + a.getLocalClassName());
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragment_beacon_detail, container, false);
        setFields();
        company = SharedUtil.getCompany(ctx);
        setFields();

        return view;

    }


    private void setFields() {

        txtName = (TextView) view.findViewById(R.id.DET_txtName);
        txtMajor = (TextView) view.findViewById(R.id.DET_txtMajor);
        txtMinor = (TextView) view.findViewById(R.id.DET_txtMinor);
        txtMacAddress = (TextView) view.findViewById(R.id.DET_txtMac);
        txtUUID = (TextView) view.findViewById(R.id.DET_txtUUID);
        txtRSSI = (TextView) view.findViewById(R.id.DET_txtRSSI);
        txtPower = (TextView) view.findViewById(R.id.DET_txtPower);
        txtRegistered  = (TextView) view.findViewById(R.id.DET_txtRegistered);
        txtNotReg = (TextView) view.findViewById(R.id.DET_txtUnRegistered);

        editName = (EditText)view.findViewById(R.id.DET_editName);
        txtBranch = (TextView) view.findViewById(R.id.DET_txtBranch);
        btnRegister = (Button) view.findViewById(R.id.DET_btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerBeacon();
            }
        });

    }
    private void registerBeacon() {
        if (editName.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Please enter Beacon name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_BEACON);
        BeaconDTO d = new BeaconDTO();
        d.setBeaconName(editName.getText().toString());
        d.setBranchID(branch.getBranchID());
        d.setBranchName(branch.getBranchName());
        d.setMacAddress(beacon.getMacAddress());
        d.setMajor(beacon.getMajor());
        d.setMinor(beacon.getMinor());
        d.setProximityUUID(beacon.getProximityUUID());
        w.setBeacon(d);

        btnRegister.setEnabled(false);
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response.getStatusCode() > 0) {
                    btnRegister.setEnabled(true);
                    Toast.makeText(ctx,response.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                txtRegistered.setVisibility(View.VISIBLE);
                txtNotReg.setVisibility(View.GONE);

                Toast.makeText(ctx,"Beacon registered, branch has "
                                + response.getBeaconList().size(),
                        Toast.LENGTH_LONG).show();
                Log.e(LOG,"Beacon registered, branch has "
                        + response.getBeaconList().size());
                listener.onBeaconRegistered(response.getBeaconList());

            }

            @Override
            public void onVolleyError(VolleyError error) {
                btnRegister.setEnabled(true);
                Toast.makeText(ctx,"Communications Error",Toast.LENGTH_LONG).show();
            }
        });
    }
    private boolean isScanning;
    private List<BeaconDTO> beaconDTOList;

    public void setBranch(BranchDTO branch, BeaconDTO scannedBeacon, BeaconRegisterListener listener) {
        this.branch = branch;
        this.beacon = scannedBeacon;
        this.listener = listener;
        beaconDTOList = branch.getBeaconList();
        txtBranch.setText(branch.getBranchName());

        txtUUID.setText(scannedBeacon.getProximityUUID());
        txtMajor.setText("" + scannedBeacon.getMajor());
        txtMinor.setText(""+scannedBeacon.getMinor());

        txtMacAddress.setText(scannedBeacon.getMacAddress());
        txtName.setText(scannedBeacon.getBeaconName());

    }


    View view;
    Context ctx;
    CompanyDTO company;
    BeaconDTO beacon;
    TextView txtCount, txtBranch;
    TextView txtName, txtMacAddress,
            txtRegistered, txtNotReg,
            txtPower, txtRSSI, txtUUID, txtMajor, txtMinor;
    Button btnRegister;
    EditText editName;

    static final String LOG = "BeaconRegisterFragment";
}
