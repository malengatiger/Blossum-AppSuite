package com.boha.beacon.beaconsapp;

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
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.util.SharedUtil;
import com.boha.proximity.volley.BaseVolley;

import java.util.ArrayList;

public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = getApplicationContext();
        if (SharedUtil.getVisitor(ctx) != null) {
            startMain();
        }
        setFields();
        getEmail();
        //TODO - send email to contacts, including Koekemoer at Junk mail
        //TODO Candice Goodman - fan club (IMPORTANT)
        //sd
    }


    private void startMain() {
        Intent i = new Intent(this,MainPagerActivity.class);
        startActivity(i);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration, menu);
        mMenu = menu;
        return true;
    }
    Menu mMenu;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Toast.makeText(ctx,"There is no further information at this time",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccounts();
        final ArrayList<String> tarList = new ArrayList<String>();
        if (accts != null) {
            for (int i = 0; i < accts.length; i++) {
                tarList.add(accts[i].name);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    R.layout.xsimple_spinner_item, tarList);
            dataAdapter
                    .setDropDownViewResource(R.layout.xsimple_spinner_dropdown_item);
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

    private void setFields() {
        editFirst = (EditText)findViewById(R.id.REG_editFirstName);
        editLast = (EditText)findViewById(R.id.REG_editLastName);
        emailSpinner = (Spinner)findViewById(R.id.REG_spinner);
        btn = (Button)findViewById(R.id.REG_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegistration();
            }
        });
    }
    private void sendRegistration() {
        if (editFirst.getText().toString().isEmpty()) {
            Toast.makeText(ctx, ctx.getResources().getString(R.string.enter_first), Toast.LENGTH_SHORT).show();
            return;
        }
        if (editLast.getText().toString().isEmpty()) {
            Toast.makeText(ctx, ctx.getResources().getString(R.string.enter_last), Toast.LENGTH_SHORT).show();
            return;
        }
        if (email == null) {
            Toast.makeText(ctx, ctx.getResources().getString(R.string.email_notfound), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_VISITOR);
        VisitorDTO v = new VisitorDTO();
        v.setEmail(email);
        v.setFirstName(editFirst.getText().toString());
        v.setLastName(editLast.getText().toString());
        w.setVisitor(v);

        setRefreshActionButtonState(true);
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                setRefreshActionButtonState(false);
                if (response.getStatusCode() > 0) {
                    Toast.makeText(ctx, response.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                SharedUtil.saveVisitor(ctx, response.getVisitor());
                startMain();
            }

            @Override
            public void onVolleyError(VolleyError error) {
                setRefreshActionButtonState(false);
                Toast.makeText(ctx, ctx.getResources().getString(R.string.comms_error),Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }
    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_info);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
    EditText editFirst, editLast;
    Spinner emailSpinner;
    String email;
    Button btn;
    Context ctx;
}
