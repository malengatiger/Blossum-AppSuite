package com.boha.proximity.visitortrack.fragments;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.data.VisitorTrackDTO;
import com.boha.proximity.visitortrack.R;
import com.boha.proximity.visitortrack.adapters.VisitorTrackListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/09/11.
 */
public class BeaconTrackListFragment extends Fragment implements PageFragment{


    @Override
    public void onAttach(Activity a) {

        Log.e(LOG, "##### Fragment hosted by " + a.getLocalClassName());
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragment_beacon_track_list, container, false);
        setFields();
        Bundle b = getArguments();
        if (b != null) {
            beaconDTO = (BeaconDTO)b.getSerializable("beacon");
            ResponseDTO r = (ResponseDTO)b.getSerializable("response");
            visitorTrackList = new ArrayList<>();
            for (VisitorTrackDTO v: r.getVisitorTrackListSortedByBeacon()) {
                if (v.getBeaconID() == beaconDTO.getBeaconID()) {
                    visitorTrackList.add(v);
                }
            }

            txtLabel.setText(beaconDTO.getBeaconName());
            setList();
        }
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.BEAC_count);
        txtLabel = (TextView) view.findViewById(R.id.BEAC_beaconName);
        listView = (ListView) view.findViewById(R.id.BEAC_list);
    }

    private void setList() {
        adapter = new VisitorTrackListAdapter(ctx, R.layout.visitor_track_item, visitorTrackList, true);
        listView.setAdapter(adapter);
        txtCount.setText("" + visitorTrackList.size());

    }
    public void animateCount() {
        animateText(txtCount);
    }
    private void animateText(TextView txt) {
        final ObjectAnimator an = ObjectAnimator.ofFloat(txt, View.SCALE_X, 0);
        an.setRepeatCount(1);
        an.setDuration(300);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }



    View view;
    Context ctx;
    List<VisitorTrackDTO> visitorTrackList = new ArrayList<>();
    BeaconDTO beaconDTO;
    VisitorTrackListAdapter adapter;
    ListView listView;
    TextView txtCount, txtLabel;

    static final String LOG = BeaconTrackListFragment.class.getSimpleName();
}
