package com.kasi.cashmate.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kasi.cashmate.R;
import com.kasi.cashmate.collection.GroupHeaderData;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.interfaces.DataTransferInterface;
import com.kasi.cashmate.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author kasi
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private AppCompatActivity mContext;
    private List<GroupHeaderData> mListDataHeader;
    private HashMap<String, ArrayList<GroupListData>> mListDataChild;
    private LayoutInflater mInflater;
    DataTransferInterface dtInterface;

    public String strPcs = "Total no. pcs: ";
    public String strTotal = "Total: ";

    public CustomExpandableListAdapter(AppCompatActivity context, DataTransferInterface dtInterface,
                                        List<GroupHeaderData> listDataHeader, HashMap<String, ArrayList<GroupListData>> listChildData) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.mInflater = LayoutInflater.from(context);
        this.dtInterface = dtInterface;
    }

    @Override
    public int getGroupCount() {
        return mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition).getTitle()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHeaderData header = mListDataHeader.get(groupPosition);
        convertView = mInflater.inflate(R.layout.exlv_list_group, null);

        TextView listTitle = convertView.findViewById(R.id.name);
        TextView bags = convertView.findViewById(R.id.bags);
        TextView value = convertView.findViewById(R.id.value);
        TextView headerTopColor = convertView.findViewById(R.id.topColor);

        listTitle.setText(header.getTitle());
        bags.setText(header.getBagsCount());
        value.setText(header.getValue());

        if (header.getType() == "others") {
            bags.setVisibility(View.GONE);
        }

        headerTopColor.setBackgroundColor(mContext.getResources().getColor(header.getHeaderTopColor()));

        ImageView arrow = convertView.findViewById(R.id.arrow);

        if (isExpanded) {
            arrow.setImageResource(R.drawable.up);
        } else {
            arrow.setImageResource(R.drawable.down);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ArrayList<GroupListData> items = mListDataChild.get(mListDataHeader.get(groupPosition).getTitle());
        final GroupListData item = items.get(childPosition);

        if (items.size() > 0) {
            if (!item.isLast()) {
                convertView = mInflater.inflate(R.layout.exlv_list_item, null);
                List<String> keySets = new ArrayList<>(mListDataChild.keySet());
                final String key = keySets.get(groupPosition);

                final TextView title = convertView.findViewById(R.id.title);
                final TextView total = convertView.findViewById(R.id.total);
                final EditText bags = convertView.findViewById(R.id.bags);
                EditText loose = convertView.findViewById(R.id.loose);
                RelativeLayout bagsLyt = convertView.findViewById(R.id.bagsLyt);

                if (item.getType().equals("others")) {
                    loose.setVisibility(View.GONE);
                    loose = convertView.findViewById(R.id.loose2);
                    loose.setVisibility(View.VISIBLE);
                }

                title.setText(item.getTitle());
                total.setText(CommonFun.currencyValue(mContext, item.getTotal()));
                bags.setText(item.getBags());
                loose.setText(item.getLoose());
                bags.setEnabled(item.getBundles() > 0 );

                updateTotal(groupPosition, childPosition, item, total, item.getBags(), item.getLoose());

                int view = (item.getType().equals("others")) ? View.GONE : View.VISIBLE;
                bags.setVisibility(view);
                bagsLyt.setVisibility(view);

                bags.addTextChangedListener(
                    new TextWatcher() {

                      @Override
                      public void afterTextChanged(Editable s) {}

                      @Override
                      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                      @Override
                      public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateTotal(groupPosition, childPosition, item, total, s.toString(), item.getLoose());
                        updateValue(items);
                      }
                    });

                loose.addTextChangedListener(
                    new TextWatcher() {

                      @Override
                      public void afterTextChanged(Editable s) {}

                      @Override
                      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                      @Override
                      public void onTextChanged(CharSequence s, int start, int before, int count) {
                        updateTotal(groupPosition, childPosition, item, total, item.getBags() + "", s.toString());
                        updateValue(items);
                      }
                    });

            } else {
                convertView = mInflater.inflate(R.layout.exlv_list_last_item, null);
                TextView pcsTotalTitle = convertView.findViewById(R.id.pcsTotalTitle);
                TextView amtTotalTitle = convertView.findViewById(R.id.amtTotalTitla);
                TextView pcsTotalValue = convertView.findViewById(R.id.pcsTotalValue);
                TextView amtTotalValue = convertView.findViewById(R.id.amtTotalValue);
                String type = "";

                switch (item.getType()) {
                    case "coins":
                        type = "Coins ";
                        HomeFragment.notesPcsValue2 = pcsTotalValue;
                        HomeFragment.notesTotalValue2 = amtTotalValue;
                        dtInterface.setCoinsTextViewValues(pcsTotalValue, amtTotalValue, items);
//                        dtInterface.onSetCoinsValues(items);
                        float total = getUpdatedTotal(items);
                        float pcs = getUpdatedPcs(items);
                        pcsTotalValue.setText(pcs + "");
                        amtTotalValue.setText(total + "");
                        break;
                    case "notes":
                        type = "Notes ";
                        HomeFragment.coinsPcsValue2 = pcsTotalValue;
                        HomeFragment.coinsTotalValue2 = amtTotalValue;
                        dtInterface.setNotesTextViewValues(pcsTotalValue, amtTotalValue, items);
//                        dtInterface.onSetNotesValues(items);
                        total = getUpdatedTotal(items);
                        pcs = getUpdatedPcs(items);
                        pcsTotalValue.setText(pcs + "");
                        amtTotalValue.setText(total + "");
                        break;
                    case "others":
                        type = "Others ";
                        HomeFragment.othersTotalValue2 = amtTotalValue;
                        dtInterface.setOthersTextViewValues(amtTotalValue, items);
//                        dtInterface.onSetOthersValues(items);
                        total = getUpdatedTotal(items);
                        amtTotalValue.setText(total + "");
                        pcsTotalTitle.setVisibility(View.GONE);
                        pcsTotalValue.setVisibility(View.GONE);
                        break;
                }
                pcsTotalTitle.setText(strPcs);
                amtTotalTitle.setText(type + strTotal);
            }

        } else {
            convertView = mInflater.inflate(R.layout.exlv_layout_blank, null);
        }
        return convertView;
    }

    public void updateValue(ArrayList<GroupListData> items) {
        float total = getUpdatedTotal(items);
        float pcs = getUpdatedPcs(items);
        HomeFragment.notesPcsValue2.setText(pcs + "");
        HomeFragment.notesTotalValue2.setText(total + "");
    }

    public void updateTotal(int groupPosition, int childPosition, GroupListData item, TextView total, String bags, String looses) {
        ArrayList<GroupListData> items = mListDataChild.get(mListDataHeader.get(groupPosition).getTitle());
        float bundle = item.getAmount() * item.getBundles() * CommonFun.str2Float(bags);
        float loose = item.getAmount() * CommonFun.str2Float(looses.replace(",", ""));
        item.setBags(bags);
        item.setLoose(looses);
        item.setTotal(bundle + loose);
        items.set(childPosition, item);
        total.setText(CommonFun.currencyValue(mContext, item.getTotal()));

        switch (item.getType()) {
            case "coins":
                dtInterface.onSetCoinsValues(items);
                break;
            case "notes":
                dtInterface.onSetNotesValues(items);
                break;
            case "others":
                dtInterface.onSetOthersValues(items);
                break;
        }

        dtInterface.updateListData(mListDataChild);

    }

    public float getUpdatedTotal(ArrayList<GroupListData> data) {
        float total = 0.00f;

        for (int pos=0; pos<data.size(); pos++) {
            total += data.get(pos).getTotal();
        }

        return total;
    }

    public float getUpdatedPcs(ArrayList<GroupListData> data) {
        float pcs = 0.00f;

        for (int pos=0; pos<data.size(); pos++) {
            GroupListData item = data.get(pos);
            pcs += (CommonFun.str2Float(item.getBags()) * item.getBundles()) + CommonFun.str2Float(item.getLoose());
        }

        return Math.round(pcs);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
