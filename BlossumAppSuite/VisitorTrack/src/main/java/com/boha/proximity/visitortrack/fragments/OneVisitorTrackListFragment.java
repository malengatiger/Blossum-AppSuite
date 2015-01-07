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

import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.data.VisitorTrackDTO;
import com.boha.proximity.visitortrack.R;
import com.boha.proximity.visitortrack.adapters.OneVisitorTrackListAdapter;

import java.util.List;

/**
 * Created by aubreyM on 2014/09/11.
 */
public class OneVisitorTrackListFragment extends Fragment implements PageFragment{


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
            visitor = (VisitorDTO)b.getSerializable("visitor");
            if (visitor == null) throw new UnsupportedOperationException("Did not get valid visitor");
            visitorTrackList = visitor.getVisitorTrackList();
            txtLabel.setText(visitor.getFirstName() + " " + visitor.getLastName());

            setList();
        }
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.VL_count);
        txtLabel = (TextView) view.findViewById(R.id.VL_label);
        listView = (ListView) view.findViewById(R.id.VL_list);
        txtLabel.setTextColor(ctx.getResources().getColor(R.color.black));
    }

    private void setList() {
        adapter = new OneVisitorTrackListAdapter(ctx, R.layout.track_item, visitorTrackList);
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
    public void setVisitor(VisitorDTO visitor) {
        this.visitor = visitor;
        visitorTrackList = visitor.getVisitorTrackList();
        txtLabel.setText(visitor.getFirstName() + " " + visitor.getLastName());
        setList();
    }

    public VisitorDTO getVisitor() {
        return visitor;
    }

    View view;
    Context ctx;
    List<VisitorTrackDTO> visitorTrackList;
    VisitorDTO visitor;
    OneVisitorTrackListAdapter adapter;

    ListView listView;
    TextView txtCount, txtLabel;

    static final String LOG = OneVisitorTrackListFragment.class.getSimpleName();
}
