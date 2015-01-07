package com.boha.proximity.cms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.boha.proximity.cms.R;
import com.boha.proximity.cms.adapters.ImageAdapter;
import com.boha.proximity.cms.adapters.ImageDeleteDialog;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class BeaconImageGridFragment extends Fragment {

    public interface BeaconImageGridListener {
        public void onImageRemoved(String fileName);
    }

    BeaconImageGridListener listener;
    BranchDTO branch;
    @Override
    public void onAttach(Activity a) {
        if (a instanceof BeaconImageGridListener) {
            listener = (BeaconImageGridListener)a;
        } else {
            throw new UnsupportedOperationException("Host " + a.getLocalClassName() +
            " must implement BeaconImageGridListener");
        }
        Log.d(LOG, "##### Fragment hosted by " + a.getLocalClassName());
        super.onAttach(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragment_picture_grid, container, false);
        setFields();
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.PIC_txtTotal);
        txtTitle = (TextView) view.findViewById(R.id.PIC_txtTitle);
        gridView = (GridView) view.findViewById(R.id.PIC_grid);

    }


    private void setGrid() {
        adapter = new ImageAdapter(ctx, R.layout.image_item, beacon, beaconImageList);
        gridView.setAdapter(adapter);
        txtCount.setText("" + beaconImageList.size());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int index = i;
                FragmentManager fm = getFragmentManager();
                ImageDeleteDialog diag = new ImageDeleteDialog();
                diag.setFileName(beaconImageList.get(i));
                diag.setBeacon(beacon);
                diag.setCtx(ctx);
                diag.setListener(new ImageDeleteDialog.ImageDeleteDialogListener() {
                    @Override
                    public void onImageDeleted() {
                        listener.onImageRemoved(beaconImageList.get(index));
                        beaconImageList.remove(index);
                        adapter.notifyDataSetChanged();
                        txtCount.setText("" + beaconImageList.size());

                    }
                });

                diag.show(fm, "fragment_grid");
            }
        });

    }

    public void imagesDeleted() {
        beacon.setImageFileNameList(new ArrayList<String>());
        beaconImageList = new ArrayList<String>();
        adapter.notifyDataSetChanged();
    }
    public void setBeacon(BeaconDTO beacon) {
        this.beacon = beacon;
        beaconImageList = beacon.getImageFileNameList();
        setGrid();
    }

    View view;
    Context ctx;
    List<String> beaconImageList;
    BeaconDTO beacon;
    ImageAdapter adapter;
    GridView gridView;
    String fileName;

    TextView  txtCount, txtTitle;

    static final String LOG = "BeaconImageGridFragment";
}
