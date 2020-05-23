package com.kasi.cashmate.ui.others;

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
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.interfaces.ListDataTransferInterface;
import com.kasi.cashmate.tables.Others;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kasi
 */
public class OthersFragment extends Fragment implements ListDataTransferInterface {

    Others othersTbl = new Others();
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
        stub.setLayoutResource(R.layout.manage_items_list2);
        View inflated = stub.inflate();

        TextView denom = root.findViewById(R.id.denomination);
        denom.setText(getResources().getText(R.string.title_description));
        denom.setTypeface(denom.getTypeface(), Typeface.BOLD);

        dbManager = new DBManager(this.getContext(), othersTbl.tableName(), othersTbl.columnColumns(), othersTbl.columnTypes());
        dbManager.open();

        setHasOptionsMenu(true);
        prepareListData();

        return root;
    }

    public void prepareListData() {

        String whereClause = othersTbl.columnColumns()[2] + " =?";
        String[] whereArgs = new String[]{"0"};
        Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);

        List<GroupListData> datas = new ArrayList<>();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = cursor.getInt(0);
                final String name = cursor.getString(1);
                final int has_deleted = cursor.getInt(2);
                datas.add(new GroupListData(id, name, 1, 0,"0", "0", "others", has_deleted,false));
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

    public void showOthersDialog(final boolean is_edit, final GroupListData data) {
        Activity activity = this.getActivity();

        LayoutInflater li = LayoutInflater.from(activity);
        View promptsView = li.inflate(R.layout.add_new_item2, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setView(promptsView);

        TextView title = promptsView.findViewById(R.id.title);
        TextInputLayout titleDesc = promptsView.findViewById(R.id.title_description);
        final TextInputEditText valueDesc = promptsView.findViewById(R.id.value_description);

        title.setText(getResources().getText(is_edit ? R.string.title_edit_other_transaction : R.string.title_save_other_transaction));
        titleDesc.setHint(getResources().getString(R.string.title_description));

        if (is_edit) {
            valueDesc.setText(data.getTitle());
        }

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getText(R.string.save),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String desc = valueDesc.getText().toString().trim();

                                if (desc.length()>0) {
                                    checkData(desc, is_edit, data);
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

    private void checkData(String desc, boolean is_edit, GroupListData data) {

        String whereClause = "";
        String[] whereArgs = new String[] {};

        if (is_edit) {
            int _id = data.getId();

            ContentValues contentValues = othersTbl.getContentValues(_id, desc, 0);
            dbManager.update(_id, contentValues);
            prepareListData();

//            whereClause = othersTbl.columnColumns()[1] + " =?";
//            whereArgs = new String[]{ desc };
//
//            Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);
//
//            if (cursor.getCount() == 0) {
//                ContentValues contentValues = othersTbl.getContentValues(_id, desc, 0);
//                dbManager.update(_id, contentValues);
//                prepareListData();
//            }

        } else {
            whereClause = othersTbl.columnColumns()[1] + " =? AND " + othersTbl.columnColumns()[2] + " =?";
            whereArgs = new String[]{desc, "0"};

            Cursor cursor = dbManager.fetch(whereClause, whereArgs, null, null, null);

            if (cursor.getCount() == 0) {
                ContentValues contentValues = othersTbl.getContentValues(desc, 0);
                dbManager.insert(contentValues);
                prepareListData();
            }
        }
    }

    @Override
    public void updateData(GroupListData data) {
        showOthersDialog(true, data);
    }

    @Override
    public void softDeleteData(GroupListData data) {
        data.setHasDeleted(1);
        dbManager.update(data.getId(), othersTbl.getContentValues(data.getId(), data.getTitle(), data.getHasDeleted()));
        prepareListData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (inflater!=null) {
            inflater.inflate(R.menu.main, menu);
        }

        menu.findItem(R.id.action_others).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_others:
                showOthersDialog(false, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}