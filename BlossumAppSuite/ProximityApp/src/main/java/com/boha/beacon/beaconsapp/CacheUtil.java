package com.boha.beacon.beaconsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.boha.proximity.data.ResponseDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aubreyM on 2014/06/30.
 */
public class CacheUtil {

    public interface CacheUtilListener {
        public void onFileDataDeserialized(ResponseDTO response);

        public void onDataCached();
    }

    static CacheUtilListener listener;
    public static final int SERVER_BEACON_LIST = 1;
    static int dataType, classID;
    static ResponseDTO response;
    static Context ctx;
    static final String BEACON_DATA = "beacondata.json";

    public static void cacheData(Context context, ResponseDTO r, int type, CacheUtilListener cacheUtilListener) {
        dataType = type;
        response = r;
        listener = cacheUtilListener;
        ctx = context;
        new CacheTask().execute();
    }


    public static void getCachedData(Context context, int type, CacheUtilListener cacheUtilListener) {
        Log.d(LOG, "################ getting cached data ..................");
        dataType = type;
        listener = cacheUtilListener;
        ctx = context;
        new CacheRetrieveTask().execute();
    }public static void getCachedData(Context context, int type, int id, CacheUtilListener cacheUtilListener) {
        Log.e(LOG, "################ getting cached data ..................");
        dataType = type;
        listener = cacheUtilListener;
        ctx = context;
        classID = id;
        new CacheRetrieveTask().execute();
    }


    static class CacheTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String json = null;
            File file = null;
            FileOutputStream outputStream;
            try {
                switch (dataType) {

                    case SERVER_BEACON_LIST:
                        json = gson.toJson(response);
                        outputStream = ctx.openFileOutput(BEACON_DATA, Context.MODE_PRIVATE);
                        write(outputStream, json);
                        file = ctx.getFileStreamPath(BEACON_DATA);
                        if (file != null) {
                            Log.w(LOG, "......Data cache json written to disk,  - path: " + file.getAbsolutePath() +
                                    " - length: " + file.length());
                        }
                        break;

                    default:
                        Log.e(LOG, "######### NOTHING done ...");
                        break;

                }

            } catch (IOException e) {
                Log.w(LOG, "Failed to cache data", e);
            }
            return null;
        }

        private void write(FileOutputStream outputStream, String json) throws IOException {
            outputStream.write(json.getBytes());
            outputStream.close();
        }

        @Override
        protected void onPostExecute(Void v) {
            listener.onDataCached();
        }
    }

    static class CacheRetrieveTask extends AsyncTask<Void, Void, ResponseDTO> {

        private ResponseDTO getData(FileInputStream stream) throws IOException {
            String json = getStringFromInputStream(stream);
            ResponseDTO response = gson.fromJson(json, ResponseDTO.class);
            if (response == null) {
                response = new ResponseDTO();
            }
            return response;
        }

        @Override
        protected ResponseDTO doInBackground(Void... voids) {
            ResponseDTO response = null;
            FileInputStream stream;
            Log.e(LOG, "########### doInBackground: getting cached data ....");
            try {
                switch (dataType) {
                    case SERVER_BEACON_LIST:
                        stream = ctx.openFileInput(BEACON_DATA);
                        response = getData(stream);
                        break;

                }

            } catch (IOException e) {
                Log.e(LOG, "Failed to retrieve cache", e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(ResponseDTO v) {
            if (v != null) {
                Log.w(LOG, "$$$$$$$$$$$$ cached data retrieved");
            }
            listener.onFileDataDeserialized(v);
        }
    }


    private static String getStringFromInputStream(InputStream is) throws IOException {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } finally {
            if (br != null) {
                br.close();
            }
        }
        String json = sb.toString();
        return json;

    }

    static final String LOG = "CacheUtil";
    static final Gson gson = new Gson();
}
