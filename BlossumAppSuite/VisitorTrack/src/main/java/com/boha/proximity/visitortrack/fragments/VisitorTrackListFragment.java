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

import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.data.VisitorTrackDTO;
import com.boha.proximity.visitortrack.R;
import com.boha.proximity.visitortrack.adapters.VisitorTrackListAdapter;

import java.util.List;

/**
 * Created by aubreyM on 2014/09/11.
 */
public class VisitorTrackListFragment extends Fragment implements PageFragment{


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
                .inflate(R.layout.fragment_visitor_track_list, container, false);
        setFields();
        Bundle b = getArguments();
        if (b != null) {
            response = (ResponseDTO)b.getSerializable("response");
            visitorTrackList = response.getVisitorTrackList();
            setList();
        }
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.VL_count);
        listView = (ListView) view.findViewById(R.id.VL_list);
    }

    ResponseDTO response;

    private void setList() {
        adapter = new VisitorTrackListAdapter(ctx, R.layout.visitor_track_item, visitorTrackList, false);
        listView.setAdapter(adapter);
        txtCount.setText("" + visitorTrackList.size());

        //animateText(txtCount);
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
    List<VisitorTrackDTO> visitorTrackList;
    VisitorTrackDTO visitorTrack;
    VisitorTrackListAdapter adapter;

    ListView listView;
    TextView txtCount;

    static final String LOG = VisitorTrackListFragment.class.getSimpleName();
}
