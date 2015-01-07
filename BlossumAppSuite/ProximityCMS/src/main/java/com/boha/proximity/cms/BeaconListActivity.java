package com.boha.proximity.cms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.proximity.cms.fragments.BeaconListFragment;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.ResponseDTO;

public class BeaconListActivity extends ActionBarActivity
        implements BeaconListFragment.BeaconListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_list);
        ctx = getApplicationContext();
        branch = (BranchDTO) getIntent().getSerializableExtra("branch");
        beaconListFragment = (BeaconListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        beaconListFragment.setBranch(branch);
        setTitle("Registered Beacons");
    }


    BranchDTO branch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.branch_beacon_list, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            return true;
        }
        if (id == R.id.action_add) {
            Intent q = new Intent(this, BeaconScanActivity.class);
            q.putExtra("branch", branch);
            startActivityForResult(q, SCAN_REQ);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static final int SCAN_REQ = 8765;

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

    Menu mMenu;
    BeaconListFragment beaconListFragment;
    Context ctx;

    @Override
    public void onBeaconPicked(BeaconDTO beacon) {
        selectedBeacon = beacon;
        Intent w = new Intent(this, BeaconImageGridActivity.class);
        w.putExtra("beacon", beacon);
        startActivityForResult(w, BEACON_IMAGES);
    }

    @Override
    public void onBackPressed() {
        if (updatedbBeacon != null) {
            Intent w = new Intent();
            w.putExtra("beacon", updatedbBeacon);
            setResult(RESULT_OK, w);
        }


        finish();

    }

    private BeaconDTO updatedbBeacon, selectedBeacon;

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case BEACON_IMAGES:
                if (result == RESULT_OK) {
                    FileNames f = (FileNames) data.getSerializableExtra("fileNames");
                    if (f != null) {
                        selectedBeacon.getImageFileNameList().addAll(f.getFileNames());
                        beaconListFragment.setUpdatedBeacon(selectedBeacon);
                        updatedbBeacon = selectedBeacon;
                    }
                    boolean deleted = data.getBooleanExtra("beaconDeleted", false);
                    BeaconDTO b = (BeaconDTO) data.getSerializableExtra("beacon");
                    if (deleted) {
                        beaconListFragment.beaconDeleted(b);
                    } else {
                        beaconListFragment.setUpdatedBeacon(b);
                    }
                }
                break;
            case SCAN_REQ:
                if (result == RESULT_OK) {
                    ResponseDTO r = (ResponseDTO) data.getSerializableExtra("beacons");
                    branch.setBeaconList(r.getBeaconList());
                    beaconListFragment.setBranch(branch);
                }

                break;
        }
    }

    static final int BEACON_IMAGES = 313;

    @Override
    public void setBusy() {
        setRefreshActionButtonState(true);
    }

    @Override
    public void setNotBusy() {
        setRefreshActionButtonState(false);
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
}
