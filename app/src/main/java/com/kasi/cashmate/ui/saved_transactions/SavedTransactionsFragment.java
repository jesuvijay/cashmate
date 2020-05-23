package com.kasi.cashmate.ui.saved_transactions;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.SavedTransactionAdapter;
import com.kasi.cashmate.collection.SavedTransactionData;
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.interfaces.TransactionDataTransferInterface;
import com.kasi.cashmate.tables.SavedTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kasi
 */
public class SavedTransactionsFragment extends Fragment implements TransactionDataTransferInterface {

    SavedTransaction savedTransactionTbl = new SavedTransaction();
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

        dbManager = new DBManager(this.getContext(), savedTransactionTbl.tableName(), savedTransactionTbl.columnColumns(), savedTransactionTbl.columnTypes());
        dbManager.open();

        prepareListData();
        return root;
    }

    public void prepareListData() {
        String whereClause = savedTransactionTbl.columnColumns()[6] + " =?";
        String[] whereArgs = new String[]{"0"};
        Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, "_id DESC");

        final List<SavedTransactionData> items = new ArrayList<>();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                final int id = cursor.getInt(0);
                final String transaction_name = cursor.getString(1);
                final String transaction_dt = cursor.getString(2);
                final float teller_amount = cursor.getFloat(3);
                final float cash_total = cursor.getFloat(4);
                final String created_at = cursor.getString(5);
                final int has_deleted = cursor.getInt(6);
                items.add(new SavedTransactionData(id, transaction_name, transaction_dt, teller_amount, cash_total, created_at, has_deleted));
            } while (cursor.moveToNext());
        }

        int visibility = (cursor.getCount() > 0)  ? View.GONE : View.VISIBLE;
        empty.setVisibility(visibility);

        SavedTransactionAdapter adapter = new SavedTransactionAdapter(this.getActivity(), items, this);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setDividerHeight(5);
        listView.setDivider(new ColorDrawable(getActivity().getResources().getColor(R.color.grey)));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SavedTransactionData data = items.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("transaction_id", data.getId());
                Navigation.findNavController(view).navigate(R.id.nav_report, bundle);
            }
        });

    }

    @Override
    public void softDeleteData(SavedTransactionData data) {
        data.setHasDeleted(1);
        int t = dbManager.update(data.getId(), savedTransactionTbl.getContentValues(data.getId(), data.getTransactionName(),
                data.getTransactionDt(), data.getTellerAmount(), data.getCashTotal(), data.getCreatedAt(), data.getHasDeleted()));
        prepareListData();
    }
}