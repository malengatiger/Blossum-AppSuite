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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.visitortrack.R;
import com.boha.proximity.visitortrack.adapters.VisitorListAdapter;

import java.util.List;

/**
 * Created by aubreyM on 2014/09/11.
 */
public class VisitorListFragment extends Fragment implements PageFragment{
    public interface VisitorListFragmentListener {
        public void onVisitorPicked(VisitorDTO visitor);
        public void setBusy();
        public void setNotBusy();
    }

    VisitorListFragmentListener listener;

    @Override
    public void onAttach(Activity a) {
        if (a instanceof VisitorListFragmentListener) {
            listener = (VisitorListFragmentListener) a;
        } else {
            throw new UnsupportedOperationException("Host " + a.getLocalClassName() +
                    " must implement VisitorListFragmentListener");
        }
        Log.e(LOG, "##### Fragment hosted by " + a.getLocalClassName());
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragment_visitor_list, container, false);
        setFields();
        Bundle b = getArguments();
        if (b != null) {
            response = (ResponseDTO)b.getSerializable("response");
            visitorList = response.getVisitorList();
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
        adapter = new VisitorListAdapter(ctx, R.layout.visitor_item, visitorList);
        listView.setAdapter(adapter);
        txtCount.setText("" + visitorList.size());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                visitor = visitorList.get(i);
                listener.onVisitorPicked(visitor);
            }
        });
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
    List<VisitorDTO> visitorList;
    VisitorDTO visitor;
    VisitorListAdapter adapter;

    ListView listView;
    TextView txtCount;

    static final String LOG = VisitorListFragment.class.getSimpleName();
}
