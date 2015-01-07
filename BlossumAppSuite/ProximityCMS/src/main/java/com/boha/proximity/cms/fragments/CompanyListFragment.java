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
import com.boha.proximity.cms.adapters.CompanyAdapter;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.CacheUtil;
import com.boha.proximity.volley.BaseVolley;

import java.util.List;

/**
 * Created by aubreyM on 2014/06/13.
 */
public class CompanyListFragment extends Fragment {
    public interface CompanyListFragmentListener {
        public void onCompanyPicked(CompanyDTO branch);
        public void setBusy();
        public void setNotBusy();
    }

    CompanyListFragmentListener listener;

    @Override
    public void onAttach(Activity a) {
        if (a instanceof CompanyListFragmentListener) {
            listener = (CompanyListFragmentListener) a;
        } else {
            throw new UnsupportedOperationException("Host " + a.getLocalClassName() +
                    " must implement CompanyListFragmentListener");
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
                .inflate(R.layout.fragment_company_list, container, false);
        setFields();
        return view;

    }


    public void setCompanyData(List<BranchDTO> list) {

        if (list != null && !list.isEmpty()) {
            for (CompanyDTO c : companyList) {
                if (list.get(0).getCompanyID() == c.getCompanyID()) {
                    c.setBranchList(list);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void setFields() {

        txtCount = (TextView) view.findViewById(R.id.FCL_txtCount);
        listView = (ListView) view.findViewById(R.id.FCL_list);

        editName = (EditText)view.findViewById(R.id.FCL_editCompany);
        editLayout = view.findViewById(R.id.FCL_companyLayout);
        btnSave = (Button)view.findViewById(R.id.FCL_btnSave);
        btnCancel = (Button)view.findViewById(R.id.FCL_btnCancel);
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
                saveCompany();
            }
        });
    }

    private void saveCompany() {

        if (editName.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Please enter company name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_COMPANY);
        CompanyDTO c = new CompanyDTO();
        c.setCompanyName(editName.getText().toString());
        w.setCompany(c);

        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response.getStatusCode() > 0) {
                    Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                companyList.add(0, response.getCompany());
                hideEditLayout();
                adapter.notifyDataSetChanged();
                txtCount.setText(""+companyList.size());
                ResponseDTO r = new ResponseDTO();
                r.setCompanyList(companyList);
                CacheUtil.cacheData(ctx,r,CacheUtil.CACHE_COMPANIES,new CacheUtil.CacheUtilListener() {
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

            }
        });
    }
    private void setList() {
        adapter = new CompanyAdapter(ctx, R.layout.company_item, companyList);
        listView.setAdapter(adapter);
        txtCount.setText("" + companyList.size());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                company = companyList.get(i);
                listener.onCompanyPicked(company);
            }
        });
    }

    public void setCompanyList(List<CompanyDTO> companyList) {
        this.companyList = companyList;
        setList();
        if (companyList == null || companyList.isEmpty()) {
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
    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
    }
    View view;
    Context ctx;
    CompanyDTO company;
    List<CompanyDTO> companyList;
    CompanyAdapter adapter;

    ListView listView;
    TextView  txtCount, txtCompany;

    static final String LOG = "BranchListFragment";
}
