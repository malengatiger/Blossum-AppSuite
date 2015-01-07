package com.boha.proximity.visitortrack;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.visitortrack.fragments.OneVisitorTrackListFragment;

import java.util.Timer;
import java.util.TimerTask;


public class OneVisitorActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_visitor);
        ovf = (OneVisitorTrackListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        VisitorDTO v = (VisitorDTO)getIntent().getSerializableExtra("visitor");
        ovf.setVisitor(v);
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ovf.animateCount();
                    }
                });

                t.cancel();
            }
        }, 500);

    }

    OneVisitorTrackListFragment ovf;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.one_visitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
}
