package com.boha.proximity.visitortrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.proximity.data.VisitorTrackDTO;
import com.boha.proximity.visitortrack.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitorTrackListAdapter extends ArrayAdapter<VisitorTrackDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<VisitorTrackDTO> mList;
    private Context ctx;
    private boolean hideBeaconName;

    public VisitorTrackListAdapter(Context context, int textViewResourceId,
                                   List<VisitorTrackDTO> list, boolean hideBeaconName) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.hideBeaconName = hideBeaconName;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtNumber, txtCount, txtBeaconName, txtTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.VI_visitorName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.VI_number);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.VI_count);
            item.txtBeaconName = (TextView) convertView
                    .findViewById(R.id.VI_beaconName);
            item.txtTime = (TextView) convertView
                    .findViewById(R.id.VI_time);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        VisitorTrackDTO p = mList.get(position);
        if (p.getFirstName() != null) {
            item.txtName.setText(p.getFirstName() + " " + p.getLastName());
        } else {
            item.txtName.setText("#### No name");
        }
        item.txtNumber.setText("" + (position + 1));
        item.txtCount.setVisibility(View.GONE);

        if (hideBeaconName) {
            item.txtBeaconName.setVisibility(View.GONE);
        } else {
            item.txtBeaconName.setVisibility(View.VISIBLE);
            item.txtBeaconName.setText(p.getBeaconName());
        }
        item.txtTime.setText(y.format(new Date(p.getDateTracked())));


        animateView(convertView);
        return (convertView);
    }

    public void animateView(final View view) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.grow_fade_in_center);
        a.setDuration(500);
        view.startAnimation(a);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy HH:mm", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
