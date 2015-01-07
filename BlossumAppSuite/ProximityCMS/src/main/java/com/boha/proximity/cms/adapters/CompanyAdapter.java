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
import com.boha.proximity.data.BranchDTO;
import com.boha.proximity.data.CompanyDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompanyAdapter extends ArrayAdapter<CompanyDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<CompanyDTO> mList;
    private Context ctx;

    public CompanyAdapter(Context context, int textViewResourceId,
                          List<CompanyDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtNumber,txtCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.BI_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.BI_txtNumber);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.BI_txtCount);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        CompanyDTO p = mList.get(position);
        item.txtName.setText(p.getCompanyName());
        item.txtNumber.setText(""+(position + 1));
        if (p.getBranchList() == null) p.setBranchList(new ArrayList<BranchDTO>());
        item.txtCount.setText("" + p.getBranchList().size());

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
