package com.boha.proximity.visitortrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.visitortrack.fragments.BeaconTrackListFragment;
import com.boha.proximity.visitortrack.fragments.OneVisitorTrackListFragment;
import com.boha.proximity.visitortrack.fragments.PageFragment;
import com.boha.proximity.visitortrack.fragments.VisitorListFragment;
import com.boha.proximity.visitortrack.fragments.VisitorTrackListFragment;
import com.boha.proximity.volley.BaseVolley;

import java.util.ArrayList;
import java.util.List;


public class MainPagerActivity extends FragmentActivity
        implements VisitorListFragment.VisitorListFragmentListener{

    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        setContentView(R.layout.activity_main);
        pager = (ViewPager)findViewById(R.id.pager);
    }


    private void getVisitors() {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_VISITOR_LIST);
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        setBusy();
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO r) {
                setNotBusy();
                if (r.getStatusCode() > 0) {
                    Toast.makeText(ctx,r.getMessage(),Toast.LENGTH_LONG).show();
                    return;
                }
                response = r;
                buildPages(r);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                setNotBusy();
                Toast.makeText(ctx,"Communications Error. Please try again",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void buildPages(ResponseDTO resp) {

        pageList = new ArrayList<>();

        visitorListFragment = new VisitorListFragment();
        Bundle b = new Bundle();
        b.putSerializable("response",resp);
        visitorListFragment.setArguments(b);
        pageList.add(visitorListFragment);

        visitorTrackListFragment = new VisitorTrackListFragment();
        visitorTrackListFragment.setArguments(b);
        pageList.add(visitorTrackListFragment);

        for (BeaconDTO v: resp.getBeaconList()) {
            BeaconTrackListFragment d = new BeaconTrackListFragment();
            Bundle bx = new Bundle();
            bx.putSerializable("beacon", v);
            bx.putSerializable("response",resp);
            d.setArguments(bx);
            pageList.add(d);
        }

        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PageFragment pf = pageList.get(position);
                if (pf instanceof VisitorListFragment) {
                    VisitorListFragment vlf = (VisitorListFragment)pf;
                    vlf.animateCount();
                    setReminder();
                }
                if (pf instanceof VisitorTrackListFragment) {
                    VisitorTrackListFragment vlf = (VisitorTrackListFragment)pf;
                    vlf.animateCount();
                }
                if (pf instanceof OneVisitorTrackListFragment) {
                    OneVisitorTrackListFragment vlf = (OneVisitorTrackListFragment)pf;
                    vlf.animateCount();
                }
                if (pf instanceof BeaconTrackListFragment) {
                    BeaconTrackListFragment vlf = (BeaconTrackListFragment)pf;
                    vlf.animateCount();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setReminder();

    }

    PagerAdapter adapter;
    ViewPager pager;
    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageList.get(i);
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Page " + (position + 1);



            return title;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        //getVisitors();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getVisitors();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    VisitorListFragment visitorListFragment;
    VisitorTrackListFragment visitorTrackListFragment;
    List<PageFragment> pageList;
    ResponseDTO response;

    @Override
    public void onVisitorPicked(VisitorDTO visitor) {

        Intent i = new Intent(this, OneVisitorActivity.class);
        i.putExtra("visitor",visitor);
        startActivity(i);
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
    public void onResume() {
        Log.i("MainPager","########## onResume");
        if (response == null) {
            Log.i("MainPager","########## getting visitor list...");
            getVisitors();
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
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


    private void setReminder() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        int cnt = sp.getInt(REMINDER_COUNTER,-1);
        if (cnt == -1) {
            cnt = 1;
        } else {
            cnt++;
        }

        if (cnt > REMINDS_LIMIT) {
            return;
        }
        reminder();
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(REMINDER_COUNTER,cnt);
        ed.commit();

    }

    private void reminder() {
        Toast.makeText(ctx,"Tap the visitor's card to see details. Swipe to see more!",
                Toast.LENGTH_LONG).show();
    }
    static final int REMINDS_LIMIT = 3;
    static final String REMINDER_COUNTER = "reminderCounter";
}
