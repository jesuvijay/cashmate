package com.kasi.cashmate.ui.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.DataAdapter;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.interfaces.ListDataTransferInterface;
import com.kasi.cashmate.tables.Notes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kasi
 */
public class NotesFragment extends Fragment implements ListDataTransferInterface {

    private Notes notesTbl = new Notes();
    private DBManager dbManager;
    private ListView listView;
    private TextView empty;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        listView = root.findViewById(R.id.list);
        empty = root.findViewById(R.id.empty);

        ViewStub stub = root.findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.manage_items_list);
        View inflated = stub.inflate();

        TextView denom = root.findViewById(R.id.denomination);
        TextView pcs = root.findViewById(R.id.pieces);
        denom.setText(getResources().getText(R.string.title_denomination));
        pcs.setText(getResources().getText(R.string.title_bundles));
        denom.setTypeface(denom.getTypeface(), Typeface.BOLD);
        pcs.setTypeface(denom.getTypeface(), Typeface.BOLD);

        dbManager = new DBManager(this.getContext(), notesTbl.tableName(), notesTbl.columnColumns(), notesTbl.columnTypes());
        dbManager.open();

        setHasOptionsMenu(true);
        prepareListData();

        return root;
    }

    public void prepareListData() {

        String whereClause = notesTbl.columnColumns()[3] + " =?";
        String[] whereArgs = new String[]{"0"};
        Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);

        List<GroupListData> datas = new ArrayList<>();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = cursor.getInt(0);
                final int amount = cursor.getInt(1);
                final int bundles = cursor.getInt(2);
                final int has_deleted = cursor.getInt(3);
                datas.add(new GroupListData(id, amount + " x", amount, bundles,"0", "0", "notes", has_deleted,false));
            } while (cursor.moveToNext());
        }

        int visibility = (cursor.getCount() > 0)  ? View.GONE : View.VISIBLE;
        empty.setVisibility(visibility);

        DataAdapter adapter = new DataAdapter(this.getActivity(), datas, this);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setDividerHeight(5);
        listView.setDivider(new ColorDrawable(getActivity().getResources().getColor(R.color.grey)));

    }

    public void showNotesDialog(final boolean is_edit, final GroupListData data) {
        Activity activity = this.getActivity();

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.add_new_item, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);

        TextView title = promptsView.findViewById(R.id.title);
        TextInputLayout titleDenom = promptsView.findViewById(R.id.title_denomination);
        final TextInputEditText valueDenom = promptsView.findViewById(R.id.value_denomination);
        TextInputLayout titleBundles = promptsView.findViewById(R.id.title_bundles);
        final TextInputEditText valueBundles = promptsView.findViewById(R.id.value_bundles);

        title.setText(getResources().getText(is_edit ? R.string.title_edit_coin : R.string.title_save_note));
        titleDenom.setHint(getResources().getString(R.string.title_denomination));
        titleBundles.setHint(getResources().getString(R.string.title_bundles));

        if (is_edit) {
            valueDenom.setText(data.getAmount()+"");
            valueBundles.setText(data.getBundles()+"");
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getText(R.string.save),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String denomStr = valueDenom.getText().toString().trim();
                                String bundlesStr = valueBundles.getText().toString().trim();

                                if (denomStr.length() > 0 && bundlesStr.length() > 0) {
                                    int denom = CommonFun.str2Int(denomStr);
                                    int bundles = CommonFun.str2Int(bundlesStr);
                                    checkData(denom, bundles, is_edit, data);
                                }
                            }
                        })
                .setNegativeButton(getResources().getText(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void checkData(int denom, int bundles, boolean is_edit, GroupListData data) {

        String whereClause = "";
        String[] whereArgs = new String[] {};

        if (is_edit) {
            int _id = data.getId();

            ContentValues contentValues = notesTbl.getContentValues(_id, denom, bundles, 0);
            dbManager.update(_id, contentValues);
            prepareListData();

//            whereClause = notesTbl.columnColumns()[1] + " =?";
//            whereArgs = new String[]{ denom + "" };
//
//            Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);
//
//            if (cursor.getCount() == 0) {
//                ContentValues contentValues = notesTbl.getContentValues(_id, denom, bundles, 0);
//                dbManager.update(_id, contentValues);
//                prepareListData();
//            } else if (cursor.getCount() == 1) {
//                int id = -1;
//                if (cursor.moveToFirst()) {
//                    do {
//                        // Read the values of a row in the table using the indexes acquired above
//                        id = cursor.getInt(0);
//                    } while (cursor.moveToNext());
//                }
//
//                ContentValues contentValues = notesTbl.getContentValues(id, denom, bundles, 0);
//                dbManager.update(id, contentValues);
//                prepareListData();
//            }
        } else {
            whereClause = notesTbl.columnColumns()[1] + " =?";// AND " + notesTbl.columnColumns()[3] + " =?";
            whereArgs = new String[]{ ( denom + "")};

            Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);

            if (cursor.getCount() == 0) {
                ContentValues contentValues = notesTbl.getContentValues(denom, bundles, 0);
                dbManager.insert(contentValues);
                prepareListData();
            } else if (cursor.getCount() == 1) {
                int id = -1;
                if (cursor.moveToFirst()) {
                    do {
                        // Read the values of a row in the table using the indexes acquired above
                        id = cursor.getInt(0);
                    } while (cursor.moveToNext());
                }

                ContentValues contentValues = notesTbl.getContentValues(id, denom, bundles, 0);
                dbManager.update(id, contentValues);
                prepareListData();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (inflater!=null) {
            inflater.inflate(R.menu.main, menu);
        }

        menu.findItem(R.id.action_new_notes).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_notes:
                showNotesDialog(false, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(GroupListData data) {
        showNotesDialog(true, data);
    }

    @Override
    public void softDeleteData(GroupListData data) {
        data.setHasDeleted(1);
        dbManager.update(data.getId(), notesTbl.getContentValues(data.getId(), data.getAmount(), data.getBundles(), data.getHasDeleted()));
        prepareListData();
    }
}