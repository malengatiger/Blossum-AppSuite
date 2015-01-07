package com.boha.proximity.visitortrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.proximity.data.VisitorDTO;
import com.boha.proximity.data.VisitorTrackDTO;
import com.boha.proximity.visitortrack.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitorListAdapter extends ArrayAdapter<VisitorDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<VisitorDTO> mList;
    private Context ctx;

    public VisitorListAdapter(Context context, int textViewResourceId,
                              List<VisitorDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
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

        VisitorDTO p = mList.get(position);
        item.txtName.setText(p.getFirstName() + " " + p.getLastName());
        item.txtNumber.setText("" + (position + 1));
        if (p.getVisitorTrackList() == null)
            p.setVisitorTrackList(new ArrayList<VisitorTrackDTO>());
        item.txtCount.setText("" + p.getVisitorTrackList().size());
        if (!p.getVisitorTrackList().isEmpty()) {
            item.txtBeaconName.setText(p.getVisitorTrackList().get(0).getBeaconName());
            item.txtTime.setText(y.format(new Date(p.getVisitorTrackList().get(0).getDateTracked())));
        }


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
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy HH:mm", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
