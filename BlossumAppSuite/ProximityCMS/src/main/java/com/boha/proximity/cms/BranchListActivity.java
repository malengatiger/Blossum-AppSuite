package com.boha.proximity.cms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.fragments.BranchListFragment;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.volley.BaseVolley;

public class BranchListActivity extends ActionBarActivity
        implements BranchListFragment.BranchListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_list);
        ctx = getApplicationContext();
        company = (CompanyDTO)getIntent().getSerializableExtra("company");

        branchListFragment = (BranchListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        if (company != null) {
            branchListFragment.setBranchList(company.getBranchList(), company);
        }
    }


    private void getCompanyBeacons() {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_COMPANY_BEACONS);
        w.setCompanyID(company.getCompanyID());

        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        setRefreshActionButtonState(true);
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN, w, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                setRefreshActionButtonState(false);
                if (response.getStatusCode() > 0) {
                    Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                branchListFragment.setBranchList(response.getBranchList(), company);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                setRefreshActionButtonState(false);
                Toast.makeText(ctx, "Comms Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.branch_list, menu);
        mMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add) {
            branchListFragment.showEditLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_add);
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
    BranchListFragment branchListFragment;
    Context ctx;

    @Override
    public void onBranchPicked(BranchDTO branch) {

        if (branch.getBeaconList().size() == 0) {
            startDialog(branch);
            return;
        }
        Intent i = new Intent(this, BeaconListActivity.class);
        i.putExtra("branch", branch);
        startActivityForResult(i,BRANCH_REQ);
    }

    @Override
    public void onBackPressed() {
        if (response != null) {
            Intent w = new Intent();
            w.putExtra("response", response);
            setResult(RESULT_OK, w);
            finish();
        }
        super.onBackPressed();
    }

    ResponseDTO response;
    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case BRANCH_REQ:
                if (result == RESULT_OK) {
                    BeaconDTO d = (BeaconDTO)data.getSerializableExtra("beacon");
                    if (d != null) {
                        branchListFragment.updateBeacon(d);
                    }
                }
                break;
        }
    }
    static final int BRANCH_REQ = 913;
    private void startDialog(final BranchDTO branch) {
        AlertDialog.Builder diag = new AlertDialog.Builder(this);
        diag.setTitle("Beacon Search")
                .setMessage("Do you want to start scanning your beacons?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent p = new Intent(ctx,BeaconScanActivity.class);
                        p.putExtra("branch", branch);
                        startActivity(p);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

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

    CompanyDTO company;
}
