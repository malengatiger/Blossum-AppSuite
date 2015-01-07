package com.boha.proximity.cms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.fragments.CompanyListFragment;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.CacheUtil;
import com.boha.proximity.volley.BaseVolley;

public class CompanyListActivity extends ActionBarActivity
        implements CompanyListFragment.CompanyListFragmentListener {

    static final String LOG = "CompanyListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        ctx = getApplicationContext();
//        ACRA.getErrorReporter().putCustomData("name", "CMS App");
        companyListFragment = (CompanyListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
    }

    @Override
    public void onResume() {
        Log.w(LOG, "########## onResume");
        super.onResume();
    }

    private void getCompanies() {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_COMPANIES);

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
                companyListFragment.setCompanyList(response.getCompanyList());
                CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_COMPANIES, new CacheUtil.CacheUtilListener() {
                    @Override
                    public void onFileDataDeserialized(ResponseDTO response) {

                    }

                    @Override
                    public void onDataCached() {

                    }
                });
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
        getMenuInflater().inflate(R.menu.company_list, menu);
        mMenu = menu;
        getCachedData();
        return true;
    }

    private void getCachedData() {
        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_COMPANIES, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    companyListFragment.setCompanyList(response.getCompanyList());
                }
                    getCompanies();

            }

            @Override
            public void onDataCached() {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getCompanies();
            return true;
        }
        if (id == R.id.action_add) {
            companyListFragment.showEditLayout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
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
    CompanyListFragment companyListFragment;
    Context ctx;

    @Override
    public void onCompanyPicked(CompanyDTO company) {

        Intent i = new Intent(this, BranchListActivity.class);
        i.putExtra("company", company);
        startActivityForResult(i, COMPANY_REQ);
    }

    @Override
    public void onActivityResult(int reqCode, int result, Intent data) {
        switch (reqCode) {
            case COMPANY_REQ:
                if (result == RESULT_OK) {
                    ResponseDTO r = (ResponseDTO)data.getSerializableExtra("response");
                    if (r != null) {
                        companyListFragment.setCompanyData(r.getBranchList());
                    }
                }

                break;
        }
    }
    static final int COMPANY_REQ = 731;
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
