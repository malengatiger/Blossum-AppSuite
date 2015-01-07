package com.boha.proximity.cms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.proximity.cms.R;
import com.boha.proximity.data.BeaconDTO;
import com.boha.proximity.data.PhotoUploadDTO;
import com.boha.proximity.library.Statics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private int companyID, branchID, beaconID;
    private Context ctx;

    public ImageAdapter(Context context, int textViewResourceId,
                        BeaconDTO beacon, List<String> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.companyID = beacon.getCompanyID();
        this.branchID = beacon.getBranchID();
        this.beaconID = beacon.getBeaconID();

        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        ImageView image;
        TextView number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.image = (ImageView) convertView
                    .findViewById(R.id.image);
            item.number = (TextView) convertView
                    .findViewById(R.id.number);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        String fileName = mList.get(position);
        item.number.setText("" + (position + 1));

        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL).append(PhotoUploadDTO.COMPANY_PREFIX).append(companyID);
        sb.append("/").append(PhotoUploadDTO.BRANCH_PREFIX).append(branchID).append("/");
        sb.append(PhotoUploadDTO.BEACON_PREFIX).append(beaconID).append("/");
        sb.append(fileName);

        //Log.e("ImageAdapter", "imageURL: " + sb.toString());
        Picasso.with(ctx).load(sb.toString()).into(item.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.d("ImageAdapter", "------------ onError");
                item.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.error48));
            }
        });

        animateView(convertView);
        return (convertView);
    }

    public void animateView(final View view) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.grow_fade_in_center);
        a.setDuration(1000);
        if (view == null)
            return;
        view.startAnimation(a);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
