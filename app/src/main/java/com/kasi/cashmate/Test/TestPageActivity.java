package com.kasi.cashmate.Test;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.CustomExpandableListAdapter;
import com.kasi.cashmate.collection.GroupHeaderData;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.interfaces.DataTransferInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author kasi
 */
public class TestPageActivity extends AppCompatActivity implements DataTransferInterface {

    private List<GroupHeaderData> listDataHeader;
    private HashMap<String, ArrayList<GroupListData>> listDataChild;
    public TextView notesPcsTitle, notesPcsValue, notesTotalTitle, notesTotalValue,
            coinsPcsTitle, coinsPcsValue, coinsTotalTitle, coinsTotalValue,
            othersTotalTitle, othersTotalValue;

    public String strPcs = "Total no. pcs: ";
    public String strTotal = "Total: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exlv_content_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prepareListData();


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataHeader.add(new GroupHeaderData ("Notes", "No. of Bundles", "Loose (Pcs)", R.color.red, "notes"));
        listDataHeader.add(new GroupHeaderData ("Coins", "No. of Bags", "Loose (Pcs)", R.color.blue, "coins"));
        listDataHeader.add(new GroupHeaderData ("Others", "", "Value", R.color.green, "others"));
        listDataChild = new HashMap<>();

        ArrayList<GroupListData> details = new ArrayList<>();
        ArrayList<GroupListData> details2 = new ArrayList<>();
        ArrayList<GroupListData> details3 = new ArrayList<>();
        // fill the data as per your requirements

        // notes
        details.add(new GroupListData(1,"2000 x", 2000, 100, "1", "1", "notes", 0,false));
        details.add(new GroupListData(2,"500 x", 500, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(3,"200 x", 200, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(4,"100 x", 100, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(5,"50 x", 50, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(6,"20 x", 20, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(7,"10 x", 10, 100,"1", "1", "notes", 0,false));
        details.add(new GroupListData(0,"", 0, 0,"", "", "notes", 0,true));
        // coins
        details2.add(new GroupListData(1,"10 x", 10, 50,"1", "1", "coins", 0,false));
        details2.add(new GroupListData(2,"5 x", 5, 50,"1", "1", "coins", 0,false));
        details2.add(new GroupListData(3,"2 x", 2, 50,"1", "1", "coins", 0,false));
        details2.add(new GroupListData(4,"1 x", 1, 50,"1", "1", "coins", 0,false));
        details2.add(new GroupListData(0,"", 0, 0,"", "", "coins", 0, true));
        // others
        details3.add(new GroupListData(1,"Solid Notes", 1, 0,"1", "1", "others", 0,false));
        details3.add(new GroupListData(2,"Petty Cash", 1, 0,"1", "1", "others", 0,false));
        details3.add(new GroupListData(3,"Voucher", 1, 0,"1", "1", "others", 0,false));
        details3.add(new GroupListData(0,"", 0, 0,"", "", "others", 0,true));

        listDataChild.put(listDataHeader.get(0).getTitle(), details);
        listDataChild.put(listDataHeader.get(1).getTitle(), details2);
        listDataChild.put(listDataHeader.get(2).getTitle(), details3);
        // just to show null item view


        ExpandableListView mExpandableView = findViewById(R.id.expandable_listview);
        CustomExpandableListAdapter mExpandableListAdapter = new CustomExpandableListAdapter(TestPageActivity.this, this, listDataHeader, listDataChild);
        mExpandableView.setAdapter(mExpandableListAdapter);
        mExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // This way the expander cannot be collapsed
                // on click event of group item
                return false;
            }
        });

        for (int i = 0; i < mExpandableListAdapter.getGroupCount(); i++) {
            mExpandableView.expandGroup(i);
        }
    }

    @Override
    public void setNotesTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data) {

    }

    @Override
    public void setCoinsTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data) {

    }

    @Override
    public void setOthersTextViewValues(TextView totalValue, ArrayList<GroupListData> data) {

    }

    @Override
    public void setNotesTextView(TextView pcsValue, TextView totalValue) {

        if (pcsValue != null) {
            notesPcsValue = pcsValue;
        }

        if (totalValue != null) {
            notesTotalValue = totalValue;
        }
    }

    @Override
    public void setCoinsTextView(TextView pcsValue, TextView totalValue) {
        if (pcsValue != null) {
            coinsPcsValue = pcsValue;
        }

        if (totalValue != null) {
            coinsTotalValue = totalValue;
        }
    }

    @Override
    public void setOthersTextView(TextView totalValue) {

        if (totalValue != null) {
            othersTotalValue = totalValue;
        }
    }

    @Override
    public void onSetCoinsValues(ArrayList<GroupListData> data) {
        float value = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);

        if (coinsPcsTitle != null) {
            coinsPcsTitle.setText(strPcs);
        }

        if (coinsPcsValue != null) {
            coinsPcsValue.setText(pcs + "");
        }

        if (coinsTotalTitle != null) {
            coinsTotalTitle.setText("Coins " + strTotal);
        }

        if (coinsTotalValue != null) {
            coinsTotalValue.setText(value + "");
        }

    }

    @Override
    public void onSetNotesValues(ArrayList<GroupListData> data) {
        float value = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);

        if (notesPcsTitle != null) {
            notesPcsTitle.setText(strPcs);
        }

        if (notesPcsValue != null) {
            notesPcsValue.setText(pcs + "");
        }

        if (notesTotalTitle != null) {
            notesTotalTitle.setText("Notes " + strTotal);
        }

        if (notesTotalValue != null) {
            notesTotalValue.setText(value + "");
        }

    }

    @Override
    public void onSetOthersValues(ArrayList<GroupListData> data) {
        float value = getUpdatedTotal(data);

        if (othersTotalTitle != null) {
            othersTotalTitle.setText("Others " + strTotal);
        }

        if (othersTotalValue != null) {
            othersTotalValue.setText(value + "");
        }

    }

    @Override
    public void updateListData(HashMap<String, ArrayList<GroupListData>> updatedListDataChildData) {

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
}
