package com.kasi.cashmate.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kasi.cashmate.R;
import com.kasi.cashmate.collection.SavedTransactionData;
import com.kasi.cashmate.interfaces.TransactionDataTransferInterface;

import java.util.List;

/**
 * @author kasi
 */
public class SavedTransactionAdapter extends BaseAdapter {

    private final Activity context;
    private final List<SavedTransactionData> listData;
    TransactionDataTransferInterface dtInterface;

    public SavedTransactionAdapter(Activity context, List<SavedTransactionData> listData, TransactionDataTransferInterface dtInterface) {
        this.context=context;
        this.listData=listData;
        this.dtInterface = dtInterface;
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
        final SavedTransactionData item = listData.get(position);
        View rowView = inflater.inflate(R.layout.manage_items_list3, null, true);

        TextView nameTV = rowView.findViewById(R.id.name);
        TextView dateTV = rowView.findViewById(R.id.date);
        ImageView deleteIV = rowView.findViewById(R.id.delete);
        deleteIV.setVisibility(View.VISIBLE);

        String name = item.getTransactionName();
        String dateValue = item.getCreatedDate() + " @ " + item.getCreatedTime() + " hrs";

        nameTV.setText(name);
        dateTV.setText(dateValue);

        nameTV.setTypeface(nameTV.getTypeface(), Typeface.BOLD);

        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                Resources resources = context.getResources();

                builder.setTitle(resources.getString(R.string.confirm))
                        .setMessage(resources.getString(R.string.delete_confirmation))
                        .setPositiveButton(resources.getString(R.string.yes),  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dtInterface.softDeleteData(item);
                            }
                        })
                        .setNegativeButton(resources.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        return rowView;

    };
}