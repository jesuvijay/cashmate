package com.kasi.cashmate.ui.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.CustomExpandableListAdapter;
import com.kasi.cashmate.collection.GroupHeaderData;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.collection.TransactionsData;
import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.interfaces.DataTransferInterface;
import com.kasi.cashmate.tables.Coins;
import com.kasi.cashmate.tables.Notes;
import com.kasi.cashmate.tables.Others;
import com.kasi.cashmate.tables.SavedTransaction;
import com.kasi.cashmate.tables.Transactions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * @author kasi
 */
public class HomeFragment extends Fragment implements DataTransferInterface {

    public TextView notesPcsTitle, notesTotalTitle, coinsPcsTitle, coinsTotalTitle, othersTotalTitle;
    public TextView notesPcsValue, notesTotalValue, coinsPcsValue, coinsTotalValue, othersTotalValue;
    public static TextView notesPcsValue2, notesTotalValue2, coinsPcsValue2, coinsTotalValue2, othersTotalValue2;
    private TextView dtTimeTV, cashTotalTV, amtTotalTV;
    private TextInputEditText tellerAmountET;
    private LinearLayout linearLyt;

    public String strPcs = "Total no. pcs: ";
    public String strTotal = "Total: ";

    private float notesTotal = 0.00f;
    private float coinsTotal = 0.00f;
    private float othersTotal = 0.00f;
    private float cashTotal = 0.00f;
    private float tellerAmount = 0.00f;
    private ExpandableListView mExpandableView;
    private List<GroupHeaderData> listDataHeader;
    private HashMap<String, ArrayList<GroupListData>> listDataChild;
    private HashMap<String, ArrayList<GroupListData>> updatedListDataChildData;

    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mHour = 0;
    private int mMinute = 0;
    private int mSECOND = 0;
    private Calendar c;
    private Context ctx;

    private Notes notesTbl = new Notes();
    private Coins coinsTbl = new Coins();
    private Others othersTbl = new Others();
    private Transactions transactionsTbl = new Transactions();
    private SavedTransaction savedTransactionTbl = new SavedTransaction();
    private View view;

    private DBManager notesDBManager, coinsDBManager, othersDBManager, savedTransactionDBManager, transactionsDBManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.exlv_content_main_page, container, false);
        view = root;
        mExpandableView = root.findViewById(R.id.expandable_listview);
        cashTotalTV = root.findViewById(R.id.totalValue);
        tellerAmountET = root.findViewById(R.id.tellerAmt);
        amtTotalTV = root.findViewById(R.id.amtTotalValue);
        linearLyt = root.findViewById(R.id.linear3);

        notesDBManager = new DBManager(this.getContext(), notesTbl.tableName(), notesTbl.columnColumns(), notesTbl.columnTypes());
        notesDBManager.open();

        coinsDBManager = new DBManager(this.getContext(), coinsTbl.tableName(), coinsTbl.columnColumns(), coinsTbl.columnTypes());
        coinsDBManager.open();

        othersDBManager = new DBManager(this.getContext(), othersTbl.tableName(), othersTbl.columnColumns(), othersTbl.columnTypes());
        othersDBManager.open();

        savedTransactionDBManager = new DBManager(this.getContext(), savedTransactionTbl.tableName(), savedTransactionTbl.columnColumns(), savedTransactionTbl.columnTypes());
        savedTransactionDBManager.open();

        transactionsDBManager = new DBManager(this.getContext(), transactionsTbl.tableName(), transactionsTbl.columnColumns(), transactionsTbl.columnTypes());
        transactionsDBManager.open();

        setHasOptionsMenu(true);
        prepareListData();

        ctx = this.getContext();

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);
        mSECOND = Calendar.getInstance().get(Calendar.SECOND);

        return root;
    }

    private void prepareListData() {

        notesTotal = 0.00f;
        coinsTotal = 0.00f;
        othersTotal = 0.00f;
        cashTotal = 0.00f;
        tellerAmount = 0.00f;

        linearLyt.setVisibility(View.GONE);
        tellerAmountET.setText("");

        listDataHeader = new ArrayList<>();
        listDataHeader.add(new GroupHeaderData ("Notes", "No. of Bundles", "Loose (Pcs)", R.color.red, "notes"));
        listDataHeader.add(new GroupHeaderData ("Coins", "No. of Bags", "Loose (Pcs)", R.color.blue, "coins"));
        listDataHeader.add(new GroupHeaderData ("Others", "", "Value", R.color.green, "others"));
        listDataChild = new HashMap<>();

        ArrayList<GroupListData> notesData = new ArrayList<>();
        ArrayList<GroupListData> coinsData = new ArrayList<>();
        ArrayList<GroupListData> othersData = new ArrayList<>();
        ArrayList<TransactionsData> transactionsData = new ArrayList<>();
        // fill the data as per your requirements

        String whereClause =  "has_deleted =?";
        String[] whereArgs = new String[]{"0"};
        Cursor notesCursor = notesDBManager.fetch(whereClause, whereArgs, null, null, null);
        Cursor coinsCursor = coinsDBManager.fetch(whereClause, whereArgs, null, null, null);
        Cursor othersCursor = othersDBManager.fetch(whereClause, whereArgs, null, null, null);

        whereClause =  "";
        whereArgs = new String[]{};
        Cursor savedTransactionCursor = savedTransactionDBManager.fetch(whereClause, whereArgs, null, null, " _id DESC ", " 1 ");
        int transactionID = -1;

        if (savedTransactionCursor.moveToFirst() && savedTransactionCursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                transactionID = savedTransactionCursor.getInt(0);
            } while (savedTransactionCursor.moveToNext());
        }
        
        if (transactionID > -1) {
            whereClause =  "transaction_id =?";
            whereArgs = new String[] { transactionID + "" };
            Cursor transactionsCursor = transactionsDBManager.fetch(whereClause, whereArgs, null, null, null);

            if (transactionsCursor.moveToFirst() && transactionsCursor.getCount() > 0) {
                do {
//                    int id, int transaction_id, String description, int denomination, int pieces,
//                    int bags, float loose, String type, String transactionDt, int has_deleted)
                    // Read the values of a row in the table using the indexes acquired above
                    final int id = transactionsCursor.getInt(0);
                    final int transaction_id = transactionsCursor.getInt(1);
                    final String description = transactionsCursor.getString(2);
                    final int denomination = transactionsCursor.getInt(3);
                    final int pieces = transactionsCursor.getInt(4);
                    final int bags = transactionsCursor.getInt(5);
                    final float loose = transactionsCursor.getFloat(6);
                    final String type = transactionsCursor.getString(7);
                    final String transaction_dt = transactionsCursor.getString(8);
                    final int has_deleted = transactionsCursor.getInt(9);

                    if (type.equals("others")) {
                        transactionsData.add(new TransactionsData(id, transaction_id, description,
                                denomination, pieces, bags, loose, type, transaction_dt, has_deleted));
                    }

                } while (transactionsCursor.moveToNext());
            }
        }

        if (notesCursor.moveToFirst() && notesCursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = notesCursor.getInt(0);
                final int amount = notesCursor.getInt(1);
                final int bundles = notesCursor.getInt(2);
                final int has_deleted = notesCursor.getInt(3);
                notesData.add(new GroupListData(id, amount + " x", amount, bundles,"", "", "notes", has_deleted,false));
            } while (notesCursor.moveToNext());
        }

        if (coinsCursor.moveToFirst() && coinsCursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = coinsCursor.getInt(0);
                final int amount = coinsCursor.getInt(1);
                final int bundles = coinsCursor.getInt(2);
                final int has_deleted = coinsCursor.getInt(3);
                coinsData.add(new GroupListData(id, amount + " x", amount, bundles,"", "", "coins", has_deleted,false));
            } while (coinsCursor.moveToNext());
        }

        if (othersCursor.moveToFirst() && othersCursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = othersCursor.getInt(0);
                final String name = othersCursor.getString(1);
                final int has_deleted = othersCursor.getInt(2);
                othersData.add(new GroupListData(id, name, 1, 0,"", "", "others", has_deleted,false));
            } while (othersCursor.moveToNext());
        }

        for (int pos1=0; pos1<othersData.size(); pos1++) {
            GroupListData data = othersData.get(pos1);
            for (int pos2=0; pos2<transactionsData.size(); pos2++) {
                TransactionsData _data = transactionsData.get(pos2);
                if (_data.getDescription().equals(data.getTitle())) {
                    data.setLoose(_data.getLoose()+"");
                    othersData.set(pos1, data);
                }
            }
        }

        // notes
        notesData.add(new GroupListData(0,"", 0, 0,"", "", "notes", 0,true));
        // coins
        coinsData.add(new GroupListData(0,"", 0, 0,"", "", "coins", 0, true));
        // others
        othersData.add(new GroupListData(0,"", 0, 0,"", "", "others", 0,true));

        listDataChild.put(listDataHeader.get(0).getTitle(), notesData);
        listDataChild.put(listDataHeader.get(1).getTitle(), coinsData);
        listDataChild.put(listDataHeader.get(2).getTitle(), othersData);
        // just to show null item view


        CustomExpandableListAdapter mExpandableListAdapter = new CustomExpandableListAdapter((AppCompatActivity) this.getActivity(), this, listDataHeader, listDataChild);
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

        tellerAmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tellerAmount = CommonFun.str2Float(tellerAmountET.getText().toString());
                int visibility = (tellerAmount==0.00f) ? View.GONE : View.VISIBLE;
                linearLyt.setVisibility(visibility);
                updateTransactionTotal();
            }
        });

    }

    private void checkTransaction() {
        HashMap<String, ArrayList<GroupListData>> updatedListDataChildData = this.updatedListDataChildData;

        if (updatedListDataChildData != null ) {
            final List<GroupListData> notesData = checkValues(updatedListDataChildData.get("Notes"));
            final List<GroupListData> coinsData = checkValues(updatedListDataChildData.get("Coins"));
            final List<GroupListData> othersData = checkValues(updatedListDataChildData.get("Others"));

            final Activity activity = this.getActivity();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            Resources resources = activity.getResources();

            if (notesData != null && coinsData != null && othersData != null &&
                    notesData.size() > 0 || coinsData.size() > 0 || othersData.size() > 0) {

                LayoutInflater li = LayoutInflater.from(activity);
                View promptsView = li.inflate(R.layout.save_record, null);
                builder.setView(promptsView);

                TextInputLayout titleDesc = promptsView.findViewById(R.id.title_description);
                final TextInputEditText valueDesc = promptsView.findViewById(R.id.value_description);
                dtTimeTV = promptsView.findViewById(R.id.value_dt_time);

                dtTimeTV.setText(getDateString());
                titleDesc.setCounterEnabled(true);
                titleDesc.setCounterMaxLength(30);

                dtTimeTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonFun.hideKeyboard(activity);
                        showTimepicker();
                        showDatepicker();
                    }
                });

                builder.setCancelable(false)
                        .setPositiveButton(getResources().getText(R.string.save),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        String name = valueDesc.getText().toString();
                                        String dtTime = dtTimeTV.getText().toString();
                                        processUpdate(name, dtTime, notesData, coinsData, othersData);
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
                AlertDialog alertDialog = builder.create();
                // show it
                alertDialog.show();
            } else {
                builder.setTitle(resources.getString(R.string.alert))
                        .setMessage(resources.getString(R.string.nothing_to_save))
                        .setNegativeButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
    }

    private void showDatepicker() {
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth = monthOfYear + 1;
                        mYear=year;
                        mDay=dayOfMonth;

                        dtTimeTV.setText(getDateString());

                    }
                }, mYearParam, mMonthParam, mDayParam);

        datePickerDialog.show();
    }

    public String getDoubleText (int value) {
        return ("" + ((value > 9) ? value : "0" + value));
    }

    public String getDateString() {

        String dateTime = getDoubleText(mDay) + "-" + getDoubleText(mMonth) + "-" + mYear +
                " " + getDoubleText(mHour) + ":" + getDoubleText(mMinute) + ":" + getDoubleText(mSECOND);

        return dateTime;
    }

    private void showTimepicker() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {

                        mHour = pHour;
                        mMinute = pMinute;

                        dtTimeTV.setText(getDateString());
                        mSECOND = Calendar.getInstance().get(Calendar.SECOND);
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();
    }

    public List<GroupListData> checkValues(ArrayList<GroupListData> data) {
        List<GroupListData> result = null;

        if (data != null) {
            result = new ArrayList<>();
            for (int pos=0; pos<data.size(); pos++) {
                GroupListData item = data.get(pos);

                if ( item.getTotal() > 0 || item.getTotal() < 0) {
                    result.add(item);
                }

            }
        }

        return result;
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
        coinsTotal = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);
        updateTransactionTotal();

        if (coinsPcsValue != null) {
            coinsPcsValue.setText(pcs + "");
        }

        if (coinsTotalValue != null) {
            System.out.println("currencyValue: " + CommonFun.currencyValue(this.getActivity(), coinsTotal));
            coinsTotalValue.setText(CommonFun.currencyValue(this.getActivity(), coinsTotal));
        }

    }

    @Override
    public void onSetNotesValues(ArrayList<GroupListData> data) {
        notesTotal = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);
        updateTransactionTotal();

        if (notesPcsValue != null) {
            notesPcsValue.setText(pcs + "");
        }

        if (notesTotalValue != null) {
            System.out.println("currencyValue: " + CommonFun.currencyValue(this.getActivity(), notesTotal));
            notesTotalValue.setText(CommonFun.currencyValue(this.getActivity(), notesTotal));
        }

    }

    @Override
    public void onSetOthersValues(ArrayList<GroupListData> data) {
        othersTotal = getUpdatedTotal(data);
        updateTransactionTotal();

        if (othersTotalValue != null) {
            System.out.println("currencyValue: " + CommonFun.currencyValue(this.getActivity(), othersTotal));
            othersTotalValue.setText(CommonFun.currencyValue(this.getActivity(), othersTotal));
        }

    }

    @Override
    public void setNotesTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data) {

        notesTotal = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);
        updateTransactionTotal();

        if (pcsValue != null) {
            notesPcsValue = pcsValue;
            pcsValue.setText(pcs + "");
        }

        if (totalValue != null) {
            notesTotalValue = totalValue;
            totalValue.setText(CommonFun.currencyValue(this.getActivity(), notesTotal));
        }

    }

    @Override
    public void setCoinsTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data) {
        coinsTotal = getUpdatedTotal(data);
        float pcs = getUpdatedPcs(data);
        updateTransactionTotal();

        if (pcsValue != null) {
            coinsPcsValue = pcsValue;
            pcsValue.setText(pcs + "");
        }

        if (totalValue != null) {
            coinsTotalValue = totalValue;
            totalValue.setText(CommonFun.currencyValue(this.getActivity(), coinsTotal));
        }

    }

    @Override
    public void setOthersTextViewValues(TextView totalValue, ArrayList<GroupListData> data) {

        othersTotal = getUpdatedTotal(data);
        updateTransactionTotal();

        if (totalValue != null) {
            othersTotalValue = totalValue;
            System.out.println("othersTotal" + CommonFun.currencyValue(this.getActivity(), othersTotal));
            totalValue.setText(CommonFun.currencyValue(this.getActivity(), othersTotal));
        }
    }

    @Override
    public void updateListData(HashMap<String, ArrayList<GroupListData>> updatedListDataChildData) {
        this.updatedListDataChildData = updatedListDataChildData;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (inflater!=null) {
            inflater.inflate(R.menu.main, menu);
        }

        menu.findItem(R.id.action_reset).setVisible(true);
        menu.findItem(R.id.action_save).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset:
                resetAllData();
                break;
            case R.id.action_save:
                checkTransaction();
                break;
        }

        CommonFun.hideKeyboard(this.getActivity());
        return super.onOptionsItemSelected(item);
    }

    private void resetAllData() {

        final Activity activity = this.getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        Resources resources = activity.getResources();

        builder.setTitle(resources.getString(R.string.alert))
                .setMessage(resources.getString(R.string.confirm_reset))
                .setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prepareListData();
                    }
                })
                .setNegativeButton(resources.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public float getUpdatedTotal(ArrayList<GroupListData> data) {
        float total = 0.00f;

        for (int pos=0; pos<data.size(); pos++) {
            total += data.get(pos).getTotal();
        }

        return total;
    }

    public void updateTransactionTotal() {
        cashTotal = 0.00f;
        cashTotal += ( notesTotal + coinsTotal + othersTotal );
        cashTotalTV.setText(CommonFun.currencyValue(this.getActivity(), cashTotal));
        amtTotalTV.setText(CommonFun.currencyValue(this.getActivity(), cashTotal - tellerAmount));
    }

    public float getUpdatedPcs(ArrayList<GroupListData> data) {
        float pcs = 0.00f;

        for (int pos=0; pos<data.size(); pos++) {
            GroupListData item = data.get(pos);
            pcs += (CommonFun.str2Float(item.getBags()) * item.getBundles()) + CommonFun.str2Float(item.getLoose());
        }

        return Math.round(pcs);
    }

    public void processUpdate(String name, String dtTime, List<GroupListData> notesData, List<GroupListData> coinsData, List<GroupListData> othersData) {
        ContentValues contentValues = savedTransactionTbl.getContentValues(name, dtTime, tellerAmount, cashTotal, 0);
        long id = savedTransactionDBManager.insert(contentValues);

        if (id > -1) {
            // notes
            insertTransactions(notesData, id);
            // coins
            insertTransactions(coinsData, id);
            // others
            insertTransactions(othersData, id);

//            prepareListData();

            final Activity activity = this.getActivity();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            Resources resources = activity.getResources();

            builder.setTitle("")
                    .setMessage(resources.getString(R.string.transaction_saved))
                    .setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            prepareListData();
                            Navigation.findNavController(view).navigate(R.id.nav_saved);
                        }
                    })
                    .show();
        }
    }

    public void insertTransactions(List<GroupListData> items, long transaction_id) {
        int transactionId = (int) transaction_id;
        for (int pos = 0; pos < items.size(); pos++) {
            GroupListData item = items.get(pos);
            long id = 0;
            ContentValues contentValues;
            switch (item.getType()) {
                case "notes":
                case "coins":
                    contentValues = transactionsTbl.getContentValues(
                            transactionId,
                            item.getTitle(),
                            item.getAmount(),
                            item.getBundles(),
                            CommonFun.str2Int(item.getBags()),
                            CommonFun.str2Int(item.getLoose()),
                            item.getType(),
                            0
                            );
                    id = transactionsDBManager.insert(contentValues);
                    break;
                case "others":
                    contentValues = transactionsTbl.getContentValues(
                            transactionId,
                            item.getTitle(),
                            0,
                            0,
                            0,
                            CommonFun.str2Float(item.getLoose()),
                            item.getType(),
                            0
                    );
                    id = transactionsDBManager.insert(contentValues);
                    break;
            }
        }
    }
}