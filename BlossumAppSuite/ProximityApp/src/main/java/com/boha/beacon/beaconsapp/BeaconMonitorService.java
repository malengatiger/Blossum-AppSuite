package com.boha.beacon.beaconsapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.volley.BaseVolley;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BeaconMonitorService extends IntentService {

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startBeaconMonitor(Context context, BeaconManager bm,
                                          int delay, int period) {
        Log.e(LOG, "########################## startBeaconMonitor");
        ctx = context;
        beaconManager = bm;
        Intent intent = new Intent(context, BeaconMonitorService.class);
        intent.putExtra(STRING_DELAY, delay);
        intent.putExtra(STRING_PERIOD, period);
        context.startService(intent);
    }

    public BeaconMonitorService() {
        super("BeaconMonitorService");
        Log.d(LOG, "######### BeaconMonitorService constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG, "######### onHandleIntent");
        beaconManager.setBackgroundScanPeriod(PERIOD, DELAY);
        try {
            beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                @Override
                public void onEnteredRegion(Region region, List<Beacon> beacons) {
                    Log.w(LOG, "###### onEnteredRegion region: "
                            + region.getIdentifier() + " beacons: " + beacons.size());
                    //if (beacons.size() > 0)
                    //getBeaconsFromServer(beacons.get(0).getMacAddress());
                    //TODO - notify someone -
                }

                @Override
                public void onExitedRegion(Region region) {
                    Log.e(LOG, "###### onExitedRegion region: " + region.getIdentifier());
                }
            });
            beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private static void findStoreBeacons() {

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                beaconList = beacons;
                for (Beacon b : beacons) {
                    //log(b);
                }
                if (!beacons.isEmpty()) {
                    if (!serverBeaconsLoaded) {
                        try {
                            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
                            getBeaconsFromServer(beacons.get(0).getMacAddress());
                            Log.e(LOG, "***** RANGING STOPPED...maybe..at least I asked!");
                            return;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        for (BeaconDTO b : response.getBeaconList()) {
                            if (b.getMacAddress().equalsIgnoreCase(beacons.get(0).getMacAddress())) {
                                //buildPages(b);
                                break;
                            }
                        }

                    }
                }

            }
        });
    }

    private static void getBeaconsFromServer(String macAddress) {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_BEACONS_BY_MAC_ADDRESS);
        w.setMacAddress(macAddress);

        if (!BaseVolley.checkNetworkOnDevice(ctx)) return;
        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN, w, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO r) {
                if (r.getStatusCode() > 0) {
                    Toast.makeText(ctx, r.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                serverBeaconsLoaded = true;
                response = r;
                findStoreBeacons();

                CacheUtil.cacheData(ctx, response, CacheUtil.SERVER_BEACON_LIST, new CacheUtil.CacheUtilListener() {
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
                //TODO - use cached beacons?
                Toast.makeText(ctx, "Communications Error, please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static final Region ALL_ESTIMOTE_BEACONS_REGION =
            new Region("rid", null, null, null);
    static boolean serverBeaconsLoaded;
    static List<Beacon> beaconList;
    static ResponseDTO response;
    static final String LOG = BeaconMonitorService.class.getSimpleName(),
            STRING_DELAY = "delay", STRING_PERIOD = "period";
    static Context ctx;
    static final long DELAY = 1000, PERIOD = 10000;
    static BeaconManager beaconManager;
}
