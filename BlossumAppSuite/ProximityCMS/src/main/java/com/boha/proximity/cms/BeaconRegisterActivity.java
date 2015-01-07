package com.boha.proximity.cms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.proximity.cms.fragments.BeaconRegisterFragment;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;

import java.util.List;

public class BeaconRegisterActivity extends FragmentActivity
        implements BeaconRegisterFragment.BeaconRegisterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_register);
        ctx = getApplicationContext();
        branch = (BranchDTO)getIntent().getSerializableExtra("branch");
        beacon = (BeaconDTO)getIntent().getSerializableExtra("beacon");
        beaconRegisterFragment = (BeaconRegisterFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        beaconRegisterFragment.setBranch(branch, beacon, this);
    }


    BranchDTO branch;
    BeaconDTO beacon;
    boolean beaconRegistered;
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

    Menu mMenu;
    BeaconRegisterFragment beaconRegisterFragment;
    Context ctx;


    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onBeaconRegistered(final List<BeaconDTO> list) {
        Log.d(LOG, "onBeaconRegistered, list: " + list.size());
        beaconRegistered = true;
        branch.setBeaconList(list);
        onBackPressed();

    }
    @Override
    public void onBackPressed() {
        if (beaconRegistered = true) {
            Intent i = new Intent();
            i.putExtra("branch", branch);
            setResult(RESULT_OK, i);
        }

        finish();
    }
    static final String LOG = BeaconRegisterActivity.class.getName();
}
