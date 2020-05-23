package com.kasi.cashmate.adapter;

import android.app.Activity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kasi.cashmate.R;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.interfaces.ListDataTransferInterface;

import java.util.List;

/**
 * @author kasi
 */
public class DataAdapter extends BaseAdapter {

    private final Activity context;
    private final List<GroupListData> listData;
    ListDataTransferInterface dtInterface;

    public DataAdapter(Activity context, List<GroupListData> listData, ListDataTransferInterface dtInterface) {
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
        LayoutInflater inflater = context.getLayoutInflater();
        final GroupListData data = listData.get(position);
        View rowView = null;
        String title = "";

        if (data.getType() == "others") {
            rowView = inflater.inflate(R.layout.manage_items_list2, null, true);
            title = data.getTitle();
        } else {
            rowView = inflater.inflate(R.layout.manage_items_list, null, true);
            TextView piecesTV = rowView.findViewById(R.id.pieces);
            piecesTV.setText(data.getBundles() + "");
            title = data.getAmount() + "";
        }

        LinearLayout linear1 = rowView.findViewById(R.id.linear1);
        TextView denominationTV = rowView.findViewById(R.id.denomination);
        ImageView editIV = rowView.findViewById(R.id.edit);
        ImageView deleteIV = rowView.findViewById(R.id.delete);

        editIV.setVisibility(View.VISIBLE);
        deleteIV.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams params = linear1.getLayoutParams();
        params.height = 150;
        linear1.setLayoutParams(params);

        denominationTV.setText(title);

        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtInterface.updateData(data);
            }
        });

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
                                dtInterface.softDeleteData(data);
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