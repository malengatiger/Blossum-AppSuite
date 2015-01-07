package com.boha.proximity.cms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.boha.proximity.cms.fragments.BeaconDeleteDialog;
import com.boha.proximity.cms.fragments.BeaconImageGridFragment;
import com.boha.proximity.data.BeaconDTO;

import java.util.ArrayList;
import java.util.List;

public class BeaconImageGridActivity extends ActionBarActivity implements BeaconImageGridFragment.BeaconImageGridListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        ctx = getApplicationContext();
        beacon = (BeaconDTO) getIntent().getSerializableExtra("beacon");
        fragment = (BeaconImageGridFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        fragment.setBeacon(beacon);
        setTitle(beacon.getBeaconName());
    }


    BeaconDTO beacon;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beacon_image_grid, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            case BEACON_PIC_REQ:
                if (res == RESULT_OK) {
                    FileNames f = (FileNames) data.getSerializableExtra("fileNames");
                    beacon.getImageFileNameList().addAll(f.getFileNames());
                    fragment.setBeacon(beacon);
                }
                break;
        }
    }

    static final int BEACON_PIC_REQ = 1155;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent t = new Intent(this, PictureActivity.class);
            t.putExtra("beacon", beacon);
            startActivityForResult(t, BEACON_PIC_REQ);
            return true;
        }
        if (id == R.id.action_delete) {
            final BeaconDeleteDialog diag = new BeaconDeleteDialog();
            diag.setContext(ctx);
            diag.setBeacon(beacon);
            diag.setListener(new BeaconDeleteDialog.BeaconDeleteListener() {
                @Override
                public void onImagesDeleted() {
                    Toast.makeText(ctx, "Beacon images removed from list", Toast.LENGTH_SHORT).show();
                    fragment.imagesDeleted();
                    diag.dismiss();
                }

                @Override
                public void onBeaconDeleted() {
                    Toast.makeText(ctx, "Beacon removed from list", Toast.LENGTH_SHORT).show();
                    beaconDeleted = true;
                    onBackPressed();
                }
            });
            diag.show(getSupportFragmentManager(), "BEACON_DELETE");


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
    BeaconImageGridFragment fragment;
    Context ctx;
    boolean beaconDeleted;

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onImageRemoved(String fileName) {
        removedFileNames.add(fileName);
    }

    private List<String> removedFileNames = new ArrayList<String>();

    @Override
    public void onBackPressed() {
        Intent w = new Intent();
        w.putExtra("beacon", beacon);
        if (beaconDeleted) {
            w.putExtra("beaconDeleteed", beaconDeleted);
        }
        if (removedFileNames.size() > 0) {
            FileNames fn = new FileNames(removedFileNames);
            w.putExtra("fileNames", fn);
        }


        setResult(RESULT_OK, w);
        finish();
    }
}
