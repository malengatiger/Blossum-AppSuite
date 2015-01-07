package com.boha.proximity.cms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.proximity.cms.R;
import com.boha.proximity.data.BeaconDTO;
import com.estimote.sdk.Beacon;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScannedBeaconAdapter extends ArrayAdapter<Beacon> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<Beacon> mList;
    private List<BeaconDTO> beaconList;
    private Context ctx;

    public ScannedBeaconAdapter(Context context, int textViewResourceId,
                                List<Beacon> list, List<BeaconDTO> bList) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        beaconList = bList;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtNumber, txtMacAddress,
        txtRegistered, txtNotReg,
                txtPower, txtRSSI, txtUUID, txtMajor, txtMinor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.SH_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.SH_txtNumber);
            item.txtPower = (TextView) convertView
                    .findViewById(R.id.SH_txtPower);
            item.txtRSSI = (TextView) convertView
                    .findViewById(R.id.SH_txtRSSI);

            item.txtUUID = (TextView) convertView
                    .findViewById(R.id.SH_txtUUID);
            item.txtMajor = (TextView) convertView
                    .findViewById(R.id.SH_txtMajor);
            item.txtMinor = (TextView) convertView
                    .findViewById(R.id.SH_txtMinor);
            item.txtMacAddress = (TextView) convertView
                    .findViewById(R.id.SH_txtMac);
            item.txtRegistered = (TextView) convertView
                    .findViewById(R.id.SH_txtRegistered);
            item.txtNotReg = (TextView) convertView
                    .findViewById(R.id.SH_txtUnRegistered);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        Beacon p = mList.get(position);

        item.txtNumber.setText("" + (position + 1));
        item.txtUUID.setText(p.getProximityUUID());
        item.txtMajor.setText(""+p.getMajor());
        item.txtMinor.setText(""+p.getMinor());

        item.txtMacAddress.setText(p.getMacAddress());
        item.txtRSSI.setText("" + p.getRssi());
        item.txtName.setText(p.getName());
        item.txtPower.setText("" + p.getMeasuredPower());
        if (isBeaconRegistered(p)) {
            item.txtRegistered.setVisibility(View.VISIBLE);
            item.txtNotReg.setVisibility(View.GONE);
            item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
        } else {
            item.txtRegistered.setVisibility(View.GONE);
            item.txtNotReg.setVisibility(View.VISIBLE);
        }

        animateView(convertView);
        return (convertView);
    }
    private boolean isBeaconRegistered(Beacon b) {
        boolean isReg = false;
        if (beaconList == null) return isReg;
        for (BeaconDTO x: beaconList) {
            if (x.getMacAddress().equalsIgnoreCase(b.getMacAddress())) {
                return true;
            }
        }
        return isReg;
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
