package com.kasi.cashmate.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kasi.cashmate.R;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.common.CommonFun;

import java.util.List;

/**
 * @author kasi
 */
public class ReportListAdapter extends BaseAdapter {

    private final Activity context;
    private final List<GroupListData> listData;

    public ReportListAdapter(Activity context, List<GroupListData> listData) {
        this.context=context;
        this.listData=listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        final GroupListData item = listData.get(position);
        View rowView = inflater.inflate(R.layout.manage_report_list, null, true);

        TextView nameTV = rowView.findViewById(R.id.name);
        TextView bundleTV = rowView.findViewById(R.id.bundle);
        TextView looseTV = rowView.findViewById(R.id.loose);
        TextView totalTV = rowView.findViewById(R.id.total);
        RelativeLayout relativeTotal = rowView.findViewById(R.id.relative1);

        if (item.getType() == "others") {
            bundleTV.setVisibility(View.GONE);
        }

        if (item.isIsHeader()) {
            nameTV.setText(item.getTitle());
            bundleTV.setText(item.getBags());
            looseTV.setText(item.getLoose());

            nameTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            bundleTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            looseTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            totalTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
        } else if(item.isLast()) {
            int _color = this.context.getResources().getColor(R.color.grey);

            nameTV.setText(item.getTitle());
            bundleTV.setText("");
            looseTV.setText("");
            String total = CommonFun.currencyValue(context, item.getTotal());
            totalTV.setText(total);

            nameTV.setBackgroundColor(_color);
            bundleTV.setBackgroundColor(_color);
            looseTV.setBackgroundColor(_color);
            relativeTotal.setBackgroundColor(_color);

            nameTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            bundleTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            looseTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
            totalTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);
        } else {
            switch (item.getType()) {
                case "coins":
                case "notes":
                    String bundle = item.getBags() +"(x " + item.getBundles() + ")";
                    nameTV.setText(item.getAmount() + "");
                    bundleTV.setText(bundle);
                    String loose = item.getLoose().replace(".0", "");
                    looseTV.setText(loose);
                    break;
                case "others":
                    nameTV.setText(item.getTitle());
                    bundleTV.setText("");
                    looseTV.setText("");
                    break;
            }
            String total = CommonFun.currencyValue(context, item.getTotal());
            totalTV.setText(total);
        }

        return rowView;

    };
}