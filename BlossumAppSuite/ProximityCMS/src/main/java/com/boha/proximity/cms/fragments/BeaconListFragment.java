package com.boha.proximity.cms.fragments;

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

import com.boha.proximity.cms.R;
import com.boha.proximity.cms.adapters.BeaconAdapter;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.util.SharedUtil;

import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class BeaconListFragment extends Fragment {
    public interface BeaconListFragmentListener {
        public void onBeaconPicked(BeaconDTO beacon);
        public void setBusy();
        public void setNotBusy();
    }

    BeaconListFragmentListener listener;
    BranchDTO branch;
    @Override
    public void onAttach(Activity a) {
        if (a instanceof BeaconListFragmentListener) {
            listener = (BeaconListFragmentListener) a;
        } else {
            throw new UnsupportedOperationException("This Host " + a.getLocalClassName() +
                    " has to implement BeaconListFragmentListener");
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
                .inflate(R.layout.fragment_beacon_list, container, false);
        setFields();
        company = SharedUtil.getCompany(ctx);
        setFields();
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.FBC_txtCount);
        txtBranch = (TextView) view.findViewById(R.id.FBC_txtBranch);
        listView = (ListView) view.findViewById(R.id.FBC_list);

    }

    public void beaconDeleted(BeaconDTO b) {
        int i = 0, index = -1;
        for (BeaconDTO beacon: beaconList) {
            if (beacon.getBeaconID() == b.getBeaconID()) {
                index = i;
                break;
            }
            i++;
        }
        if (index > -1) {
            beaconList.remove(index);
            adapter.notifyDataSetChanged();
            txtCount.setText("" + beaconList.size());
        }
    }

    private void setList() {
        adapter = new BeaconAdapter(ctx, R.layout.beacon_item, beaconList);
        listView.setAdapter(adapter);
        txtCount.setText("" + beaconList.size());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                beacon = beaconList.get(i);
                listener.onBeaconPicked(beacon);
            }
        });
    }

    public void setUpdatedBeacon(BeaconDTO b) {
        Log.i(LOG,"###### setUpdatedBeacon beacon images: " + b.getImageFileNameList().size());
        for (BeaconDTO beacon: beaconList) {
            if (b.getBeaconID() == beacon.getBeaconID()) {
                beacon.setImageFileNameList(b.getImageFileNameList());
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    public void setBranch(BranchDTO branch) {
        this.branch = branch;
        if (branch == null) throw new UnsupportedOperationException("Branch is NULL");
        if (branch.getBeaconList() == null) return;
        beaconList = branch.getBeaconList();
        txtBranch.setText(branch.getBranchName());
        setList();
    }

    View view;
    Context ctx;
    CompanyDTO company;
    List<BeaconDTO> beaconList;
    BeaconDTO beacon;
    BeaconAdapter adapter;

    ListView listView;
    TextView  txtCount, txtBranch;

    static final String LOG = "BeaconListFragment";
}
