package com.boha.proximity.cms.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.boha.proximity.cms.R;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.PhotoUploadDTO;
import com.boha.proximity.data.RequestDTO;
import com.boha.proximity.data.ResponseDTO;
import com.boha.proximity.library.Statics;
import com.boha.proximity.volley.BaseVolley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by aubreyM on 2014/08/12.
 */
public class ImageDeleteDialog extends DialogFragment {
    public ImageDeleteDialog() {
        // Empty constructor required for DialogFragment
    }


    public interface ImageDeleteDialogListener {
        public void onImageDeleted();
    }
    private ImageDeleteDialogListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_delete_dialog, container);
        image = (ImageView) view.findViewById(R.id.image);
        btnNo = (Button) view.findViewById(R.id.no);
        btnYes = (Button)view.findViewById(R.id.yes);
        getDialog().setTitle(beacon.getBeaconName());

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onImageDeleted();
                removeImage();
                dismiss();
            }
        });
        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL).append(PhotoUploadDTO.COMPANY_PREFIX).append(beacon.getCompanyID());
        sb.append("/").append(PhotoUploadDTO.BRANCH_PREFIX).append(beacon.getBranchID()).append("/");
        sb.append(PhotoUploadDTO.BEACON_PREFIX).append(beacon.getBeaconID()).append("/");
        sb.append(fileName);

        //Log.e("ImageAdapter", "imageURL: " + sb.toString());
        Picasso.with(ctx).load(sb.toString()).into(image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.error48));
            }
        });
        return view;
    }
    private void removeImage() {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.DELETE_BEACON_IMAGE);
        w.setCompanyID(beacon.getCompanyID());
        w.setBranchID(beacon.getBranchID());
        w.setBeaconID(beacon.getBeaconID());
        w.setFileName(fileName);

        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }

        BaseVolley.getRemoteData(Statics.SERVLET_ADMIN,w,ctx,new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response.getStatusCode() == 0) {
                    Log.e("ImageDeleteDialog",response.getMessage());
                    Toast.makeText(ctx, "Image removed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }
        });

    }
    private String fileName;
    private BeaconDTO beacon;
    private ImageView image;
    private Button btnNo, btnYes;
    private Context ctx;

    public void setListener(ImageDeleteDialogListener listener) {
        this.listener = listener;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public void setBeacon(BeaconDTO beacon) {
        this.beacon = beacon;
    }


    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
}
