package com.boha.proximity.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.boha.proximity.data.CompanyDTO;
import com.boha.proximity.data.VisitorDTO;
import com.google.gson.Gson;

/**
 * Created by aubreyM on 2014/07/26.
 */
public class SharedUtil {

    public static void saveCompany(Context ctx, CompanyDTO dto) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(COMPANY, x);
        ed.commit();
    }
    public static CompanyDTO getCompany(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(COMPANY, null);
        CompanyDTO cls = null;
        if (adm != null) {
            cls = gson.fromJson(adm, CompanyDTO.class);

        }
        return cls;
    }

    public static void saveVisitor(Context ctx, VisitorDTO dto) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String x = gson.toJson(dto);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(VISITOR, x);
        ed.commit();
    }
    public static VisitorDTO getVisitor(Context ctx) {

        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(ctx);
        String adm = sp.getString(VISITOR, null);
        VisitorDTO cls = null;
        if (adm != null) {
            cls = gson.fromJson(adm, VisitorDTO.class);

        }
        return cls;
    }

    static final Gson gson = new Gson();
    public static final String COMPANY = "company", BRANCH = "branch", BEACON = "beacon", VISITOR = "visitor";
}


