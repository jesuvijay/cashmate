package com.kasi.cashmate.ui.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.SavedTransactionAdapter;
import com.kasi.cashmate.collection.SavedTransactionData;
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.interfaces.TransactionDataTransferInterface;
import com.kasi.cashmate.tables.Transactions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kasi
 */
public class SettingsFragment extends Fragment implements TransactionDataTransferInterface {


    Transactions transactionTbl = new Transactions();
    private DBManager dbManager;
    private ListView listView;
    private TextView empty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        listView = root.findViewById(R.id.list);
        empty = root.findViewById(R.id.empty);

        root.findViewById(R.id.relative1).setVisibility(View.GONE);

        dbManager = new DBManager(this.getContext(), transactionTbl.tableName(), transactionTbl.columnColumns(), transactionTbl.columnTypes());
        dbManager.open();

//        prepareListData();
        return root;
    }

    public void prepareListData() {
        String whereClause = transactionTbl.columnColumns()[6] + " =?";
        String[] whereArgs = new String[]{"0"};
        Cursor cursor = dbManager.fetch("", new String[] {}, null, null, null);

        List<SavedTransactionData> items = new ArrayList<>();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                final int id = cursor.getInt(0);
                final String transaction_name = "T-id: " + cursor.getInt(1);
                final String transaction_dt =  "Desc : " + cursor.getString(2) +
                                                "\nDenom : " + cursor.getInt(3) +
                                                "\nPieces : " + cursor.getInt(4) +
                                                "\nBags : " + cursor.getInt(5) +
                                                "\nLoose : " + cursor.getString(6);
                final float teller_amount = 0.00f;
                final float cash_total = cursor.getFloat(6);
                final String created_at = "\nType : " + cursor.getString(7);
                final int has_deleted = cursor.getInt(8);
                items.add(new SavedTransactionData(id, transaction_name, transaction_dt, teller_amount, cash_total, created_at, has_deleted));
//                public ContentValues getContentValues(int transaction_id, String description, int denomination, int pieces,
//                                        int bags, float loose, String type, int has_deleted) {
            } while (cursor.moveToNext());
        }

        int visibility = (cursor.getCount() > 0)  ? View.GONE : View.VISIBLE;
        empty.setVisibility(visibility);


        SavedTransactionAdapter adapter = new SavedTransactionAdapter(this.getActivity(), items, this);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

    }

    @Override
    public void softDeleteData(SavedTransactionData data) {

    }
}