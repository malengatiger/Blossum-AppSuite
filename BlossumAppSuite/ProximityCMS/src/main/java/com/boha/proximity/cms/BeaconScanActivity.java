package com.boha.proximity.cms;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.boha.proximity.cms.fragments.BeaconScanFragment;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.ProximityApplication;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.List;

public class BeaconScanActivity extends FragmentActivity
        implements BeaconScanFragment.BeaconScanListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG, "############# onCreate");
        setContentView(R.layout.activity_beacon_scan);
        ctx = getApplicationContext();
        branch = (BranchDTO) getIntent().getSerializableExtra("branch");
        ProximityApplication app = (ProximityApplication) getApplication();
        beaconManager = app.getBeaconManager();
        beaconScanFragment = (BeaconScanFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        beaconScanFragment.setBeaconManager(beaconManager);
        beaconScanFragment.setBranch(branch);
    }


    BranchDTO branch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beacon_list, menu);
        mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            //TODO - do we need arefresh??
            return true;
        }
        if (id == R.id.action_range) {
            //TODO - start ranging the beacons ....
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_range);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG, "############# onStart - checking for bluetooth support");
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(ctx, "Device does not have Bluetooth Low Energy",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else {
            beaconScanFragment.connectToService();
        }
    }

    @Override
    public void onStop() {
        Log.e(LOG, "############# onStop - stop ranging");
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(LOG, "Error while stopping ranging", e);
        }

        super.onStop();
    }

    Menu mMenu;
    BeaconScanFragment beaconScanFragment;
    Context ctx;
    BeaconManager beaconManager;
    private static final int REQUEST_ENABLE_BT = 1234;
    private static final Region ALL_ESTIMOTE_BEACONS_REGION =
            new Region("rid", null, null, null);

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    static final String LOG = "BeaconScanActivity";

    @Override
    public void onBeaconRegistrationRequested(BeaconDTO beacon) {
        Intent i = new Intent(this, BeaconRegisterActivity.class);
        i.putExtra("branch", branch);
        i.putExtra("beacon", beacon);
        startActivityForResult(i, REGISTER_BEACON);
    }
    @Override
    public void onBeaconManageRequested(BeaconDTO beacon) {
       //TODO - start activity to manage data items
    }
    static final int REGISTER_BEACON = 5734;
    @Override
    public void onBackPressed() {
        if (registeredBeacons != null) {
            Intent t = new Intent();
            ResponseDTO r = new ResponseDTO();
            r.setBeaconList(registeredBeacons);
            t.putExtra("beacons", r);
            setResult(RESULT_OK,t);
        }
        finish();
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case REGISTER_BEACON:
                if (result == RESULT_OK) {
                    BranchDTO branchDTO = (BranchDTO)data.getSerializableExtra("branch");
                    registeredBeacons = branchDTO.getBeaconList();
                    branch.setBeaconList(branchDTO.getBeaconList());
                    beaconScanFragment.setBranch(branch);
                }
                break;

        }
    }
    private List<BeaconDTO> registeredBeacons = new ArrayList<BeaconDTO>();
}
