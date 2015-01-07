package com.boha.beacon.beaconsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aubreyM on 2014/07/09.
 */
public class MainFragment extends Fragment implements PageFragment{
    @Override
    public void onAttach(Activity a) {
//        if (a instanceof BusyListener) {
//            busyListener = (BusyListener) a;
//        } else {
//            throw new UnsupportedOperationException("Host activity "
//                    + a.getLocalClassName()
//                    + " must implement BusyListener");
//        }
//        if (a instanceof CameraRequestListener) {
//            cameraRequestListener = (CameraRequestListener) a;
//        } else {
//            throw new UnsupportedOperationException("Host activity "
//                    + a.getLocalClassName()
//                    + " must implement CameraRequestListener");
//        }
        super.onAttach(a);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_main, container, false);
        setFields();

        Bundle b = getArguments();

        return view;
    }

    private void setFields() {

    }
    Context ctx;
    View view;

}
