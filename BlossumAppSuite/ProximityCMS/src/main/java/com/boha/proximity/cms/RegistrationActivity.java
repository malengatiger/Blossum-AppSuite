package com.boha.proximity.cms;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.SharedUtil;
import com.boha.proximity.volley.BaseVolley;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.util.ArrayList;


public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = getApplicationContext();

        if (SharedUtil.getCompany(ctx) != null) {
            startBranchList();
            finish();
            return;
        }
        setFields();
        getEmail();
    }

    private void startBranchList() {
        Intent t = new Intent(getApplicationContext(), BranchListActivity.class);
        startActivity(t);
    }

    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am
                .getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        final ArrayList<String> tarList = new ArrayList<String>();
        if (accts != null) {
            for (int i = 0; i < accts.length; i++) {
                tarList.add(accts[i].name);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    R.layout.xxsimple_spinner_item, tarList);
            dataAdapter
                    .setDropDownViewResource(R.layout.xxsimple_spinner_dropdown_item);
            emailSpinner.setAdapter(dataAdapter);
            emailSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            email = tarList.get(arg2);
                            Log.d("Reg", "###### Email account selected is "
                                    + email);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    public void setFields() {
        eCompany = (EditText)findViewById(R.id.REG_editCompanyName);
        eBranch = (EditText)findViewById(R.id.REG_editBranchName);
        btnSave = (Button)findViewById(R.id.REG_btnSave);
        emailSpinner = (Spinner)findViewById(R.id.REG_spinner);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegistration();
            }
        });
    }
    private void sendRegistration() {
        if (eCompany.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Please enter the Company name", Toast.LENGTH_LONG).show();
            return;
        }
        if (eBranch.getText().toString().isEmpty()) {
            Toast.makeText(ctx, "Please enter the Branch name", Toast.LENGTH_LONG).show();
            return;
        }
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        setRefreshActionButtonState(true);
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_COMPANY);
        CompanyDTO c = new CompanyDTO();
        c.setCompanyName(eCompany.getText().toString());
        c.setEmail(email);
        w.setCompany(c);

        BranchDTO b = new BranchDTO();
        b.setBranchName(eBranch.getText().toString());
        b.setEmail(email);
        w.setBranch(b);

        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                setRefreshActionButtonState(false);
                if (response.getStatusCode() > 0) {
                    Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                SharedUtil.saveCompany(ctx, response.getCompany());
                Toast.makeText(ctx, "Registration is complete. Welcome aboard!", Toast.LENGTH_SHORT).show();
                startBranchList();
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Toast.makeText(ctx, "Communications Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    EditText eCompany, eBranch;
    Button btnSave;
    Spinner emailSpinner;
    String email;
    Menu mMenu;
    Context ctx;

}
