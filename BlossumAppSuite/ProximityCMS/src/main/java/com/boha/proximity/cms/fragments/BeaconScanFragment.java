package com.boha.proximity.cms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.proximity.cms.R;
import com.boha.proximity.cms.adapters.ScannedBeaconAdapter;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.util.SharedUtil;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;

import static com.estimote.sdk.Utils.computeAccuracy;
import static com.estimote.sdk.Utils.computeProximity;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class BeaconScanFragment extends Fragment {

    public interface BeaconScanListener {
        public void onBeaconRegistrationRequested(BeaconDTO beacon);
        public void onBeaconManageRequested(BeaconDTO beacon);
    }

    BeaconScanListener listener;
    BranchDTO branch;

    @Override
    public void onAttach(Activity a) {
        if (a instanceof BeaconScanListener) {
            listener = (BeaconScanListener) a;
        } else {
            throw new UnsupportedOperationException("Host "
                    + a.getLocalClassName() + " must implement BeaconScanListener");
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
                .inflate(R.layout.fragment_scan_beacon, container, false);
        setFields();
        company = SharedUtil.getCompany(ctx);

        setFields();

        return view;

    }

    public void setBeaconManager(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
    }

    private void monitor() {
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> beacons) {
                Log.w(LOG, "&&&&&&&&&&&&&&&&&& onEnteredRegion, beacons: " + beacons.size()
                        + " \nregion: " + region.getIdentifier() + " proxID:" + region.getProximityUUID()
                        + "\nMajor: " + region.getMajor() + " Minor: " + region.getMinor());

                if (!beacons.isEmpty()) {

                }
                //start ranging to find other beacons in the store
                findStoreBeacons();
            }

            @Override
            public void onExitedRegion(Region region) {

            }
        });

    }

    private void findStoreBeacons() {

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                beaconList = beacons;
                Log.e(LOG, "***** Beacons discovered .... Yeah! " + beacons.size()
                        + "  region: " + region.getIdentifier());
                for (Beacon b : beacons) {
                    log(b);
                }
                setList();
            }
        });
        try {
            beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
            isScanning = true;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.FBC_txtCount);
        txtBranch = (TextView) view.findViewById(R.id.FBC_txtBranch);
        listView = (ListView) view.findViewById(R.id.FBC_list);
        btnScan = (Button) view.findViewById(R.id.FBC_btn);

        btnScan.setText("Stop Beacon Scanning");

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScanning) {
                    try {
                        btnScan.setText("Start Beacon Scanning");
                        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
                        isScanning = false;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    btnScan.setText("Stop Beacon Scanning");
                    findStoreBeacons();
                }
            }
        });

    }

    private boolean isScanning;
    private List<BeaconDTO> beaconDTOList;

    private void setList() {
        adapter = new ScannedBeaconAdapter(ctx, R.layout.scanned_beacon_item, beaconList, beaconDTOList);
        listView.setAdapter(adapter);
        txtCount.setText("" + beaconList.size());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BeaconDTO d = new BeaconDTO();
                d.setProximityUUID(beaconList.get(i).getProximityUUID());
                d.setMacAddress(beaconList.get(i).getMacAddress());
                d.setBranchID(branch.getBranchID());
                d.setMinor(beaconList.get(i).getMinor());
                d.setMajor(beaconList.get(i).getMajor());
                d.setBeaconName(beaconList.get(i).getName());
                BeaconDTO x = getRegisteredBeacon(d);
                if (x != null) {
                    listener.onBeaconManageRequested(d);
                }else {
                    listener.onBeaconRegistrationRequested(d);
                }
            }
        });
    }

    private BeaconDTO getRegisteredBeacon(BeaconDTO dto) {
        for (BeaconDTO b: beaconDTOList) {
            if (dto.getMacAddress().equalsIgnoreCase(b.getMacAddress())) {
                return b;
            }
        }

        return null;
    }
    public void setBranch(BranchDTO branch) {
        this.branch = branch;
        beaconDTOList = branch.getBeaconList();
        txtBranch.setText(branch.getBranchName());

        if (listView != null) {
            findStoreBeacons();
        }

    }


    public void connectToService() {
        Log.e(LOG, "################# connectToService - start ranging...");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                monitor();
                findStoreBeacons();
            }
        });
    }

    private void log(Beacon b) {
        double accuracy = computeAccuracy(b);
        Utils.Proximity prox = computeProximity(b);

        double distance = Math.min(accuracy, 10.0);
        Log.w(LOG, "Beacon name: " + b.getName() +
                " \nmacAddress: " + b.getMacAddress()
                + "\n getProximityUUID: " + b.getProximityUUID()
                + "\nmajor: " + b.getMajor() + " minor: " + b.getMinor()
                + "\nmeasuredPower: " + b.getMeasuredPower()
                + "\nDistance: " + distance
                + "\nRSSI: " + b.getRssi());


    }

    View view;
    Context ctx;
    CompanyDTO company;
    List<Beacon> beaconList;
    Beacon beacon;
    private Region region;
    ScannedBeaconAdapter adapter;
    BeaconManager beaconManager;
    ListView listView;
    TextView txtCount, txtBranch;
    Button btnScan;
    int rangingCount;
    static final int RANGING_LIMIT = 3;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION =
            new Region("rid", null, null, null);
    static final String LOG = "BeaconScanFragment";
}
