package com.boha.proximity.cms.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.R;
import com.boha.proximity.cms.adapters.BranchAdapter;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.CacheUtil;
import com.boha.proximity.volley.BaseVolley;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class BranchListFragment extends Fragment {
    public interface BranchListFragmentListener {
        public void onBranchPicked(BranchDTO branch);

        public void setBusy();

        public void setNotBusy();
    }

    BranchListFragmentListener listener;

    @Override
    public void onAttach(Activity a) {
        if (a instanceof BranchListFragmentListener) {
            listener = (BranchListFragmentListener) a;
        } else {
            throw new UnsupportedOperationException("Host " + a.getLocalClassName() +
                    " must implement BranchListFragmentListener");
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
                .inflate(R.layout.fragment_branch_list, container, false);
        setFields();
        return view;

    }

    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.FBL_txtCount);
        txtCompany = (TextView) view.findViewById(R.id.FBL_txtCompany);
        listView = (ListView) view.findViewById(R.id.FBL_list);
        editName = (EditText) view.findViewById(R.id.FBL_editBranch);
        editLayout = view.findViewById(R.id.FBL_branchLayout);
        btnSave = (Button) view.findViewById(R.id.FBL_btnSave);
        btnCancel = (Button) view.findViewById(R.id.FBL_btnCancel);
        editLayout.setVisibility(View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                hideEditLayout();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                saveBranch();
            }
        });
    }
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
    }
    private void saveBranch() {

        if (editName.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Please enter branch name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_BRANCH);
        BranchDTO c = new BranchDTO();
        c.setBranchName(editName.getText().toString());
        c.setCompanyID(company.getCompanyID());
        w.setBranch(c);

        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN, w, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO resp) {
                if (resp.getStatusCode() > 0) {
                    Toast.makeText(ctx, resp.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                response = resp;
                branchList.add(0, resp.getBranch());
                hideEditLayout();
                adapter.notifyDataSetChanged();
                txtCount.setText("" + branchList.size());
                //TODO - cache this new branch - 
                CacheUtil.getCachedData(ctx, CacheUtil.CACHE_COMPANIES, new CacheUtil.CacheUtilListener() {
                    @Override
                    public void onFileDataDeserialized(ResponseDTO r) {
                        if (r != null) {
                            for (CompanyDTO c : r.getCompanyList()) {
                                if (c.getCompanyName().equalsIgnoreCase(editName.getText().toString())) {
                                    if (c.getBranchList() == null)
                                        c.setBranchList(new ArrayList<BranchDTO>());
                                    c.getBranchList().add(0, r.getBranch());
                                    break;
                                }
                            }
                            CacheUtil.cacheData(ctx, r, CacheUtil.CACHE_COMPANIES, new CacheUtil.CacheUtilListener() {
                                @Override
                                public void onFileDataDeserialized(ResponseDTO response) {

                                }

                                @Override
                                public void onDataCached() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onDataCached() {

                    }
                });
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });
    }

    ResponseDTO response;

    public void updateBeacon(BeaconDTO b) {
        for (BranchDTO branch: branchList) {
            for (BeaconDTO beacon: branch.getBeaconList()) {
                if (beacon.getBeaconID() == b.getBeaconID()) {
                    beacon.setImageFileNameList(b.getImageFileNameList());
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
    private void setList() {
        adapter = new BranchAdapter(ctx, R.layout.branch_item, branchList);
        listView.setAdapter(adapter);
        txtCount.setText("" + branchList.size());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                branch = branchList.get(i);
                listener.onBranchPicked(branch);
            }
        });
    }

    public void setBranchList(List<BranchDTO> branchList, CompanyDTO company) {
        this.branchList = branchList;
        this.company = company;
        setList();
        txtCompany.setText(company.getCompanyName());
        if (branchList == null || branchList.isEmpty()) {
            showEditLayout();
        }
    }

    EditText editName;
    Button btnSave, btnCancel;
    View editLayout;

    public void showEditLayout() {
        editLayout.setVisibility(View.VISIBLE);
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.abc_slide_in_top);
        a.setDuration(500);
        editLayout.startAnimation(a);
    }

    private void hideEditLayout() {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.abc_slide_out_top);
        a.setDuration(500);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                editLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        editLayout.startAnimation(a);
    }

    View view;
    Context ctx;
    CompanyDTO company;
    List<BranchDTO> branchList;
    BranchDTO branch;
    BranchAdapter adapter;

    ListView listView;
    TextView txtCount, txtCompany;

    static final String LOG = "BranchListFragment";
}
