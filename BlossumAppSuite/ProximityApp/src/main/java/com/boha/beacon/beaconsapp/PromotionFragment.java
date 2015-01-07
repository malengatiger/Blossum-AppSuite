package com.boha.beacon.beaconsapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.PhotoUploadDTO;
import com.boha.proximity.library.Statics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by aubreyM on 2014/07/09.
 */
public class PromotionFragment extends Fragment implements PageFragment{
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
        view = inflater.inflate(R.layout.fragment_promotion, container, false);
        Bundle b = getArguments();
        if (b != null) {
            fileName = b.getString("fileName");
            beacon = (BeaconDTO) b.getSerializable("beacon");
        }
        setDrawables();
        setFields();

        return view;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    private void setFields() {
        image = (ImageView)view.findViewById(R.id.image);
        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL).append(PhotoUploadDTO.COMPANY_PREFIX).append(beacon.getCompanyID());
        sb.append("/").append(PhotoUploadDTO.BRANCH_PREFIX).append(beacon.getBranchID()).append("/");
        sb.append(PhotoUploadDTO.BEACON_PREFIX).append(beacon.getBeaconID()).append("/");
        sb.append(fileName);

        Log.e(LOG, "imageURL: " + sb.toString());
        //image.setImageUrl(sb.toString(), imageLoader);
        Picasso.with(ctx).load(sb.toString()).into(image, new Callback() {
            @Override
            public void onSuccess() {
               Log.i(LOG,"Picasso downloaded image OK");
            }

            @Override
            public void onError() {
                image.setImageDrawable(getRandomImage());
            }
        });
    }
    public void animateImage() {
        final ObjectAnimator an = ObjectAnimator.ofFloat(image, View.SCALE_X, 0);
        an.setRepeatCount(1);
        an.setDuration(200);
        an.setRepeatMode(ValueAnimator.REVERSE);
        an.start();
    }
    private Drawable getRandomImage() {

        int index = rand.nextInt(drawables.size() - 1);
        return drawables.get(index);
    }
    BeaconDTO beacon;
    Random rand = new Random(System.currentTimeMillis());
    Context ctx;
    View view;
    ImageLoader imageLoader;
    ImageView image;
    static final String LOG = PromotionFragment.class.getName();
    String fileName;
    BranchDTO branch;


    private List<Drawable> drawables = new ArrayList<Drawable>();
    private void setDrawables() {
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic1));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic2));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic3));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic4));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic5));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic6));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic7));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic8));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic9));
        drawables.add(ctx.getResources().getDrawable(R.drawable.golf_pic10));
    }
}
