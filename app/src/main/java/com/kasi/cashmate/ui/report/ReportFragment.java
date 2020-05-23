package com.kasi.cashmate.ui.report;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kasi.cashmate.BuildConfig;
import com.kasi.cashmate.R;
import com.kasi.cashmate.adapter.ReportListAdapter;
import com.kasi.cashmate.collection.GroupListData;
import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.database.DBManager;
import com.kasi.cashmate.tables.SavedTransaction;
import com.kasi.cashmate.tables.Transactions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author kasi
 */
public class ReportFragment extends Fragment {

    String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private SavedTransaction savedTransactionTbl = new SavedTransaction();
    private Transactions transactionTbl = new Transactions();

    private DBManager savedTransactionDBManager, transactionDBManager;
    private File imagePath;
    private int transactionID = -1;
    private View root;

    private ListView notesListLV, coinsListLV, othersListLV;
    private TextView nameTV, titleTV, dtTimeTV, cashTotalValueTV, totalPcsNotesValueTV,
            totalPcsCoinsValueTV, totalPcsOthersValueTV;
    private RelativeLayout notesRelative, coinsRelative, othersRelative;

    List<GroupListData> notesData = new ArrayList<>();
    List<GroupListData> coinsData = new ArrayList<>();
    List<GroupListData> othersData = new ArrayList<>();
    float notesPcs = 0.0f, notesTotal = 0.0f;
    float coinsPcs = 0.0f, coinsTotal = 0.0f;
    float othersPcs = 0.0f, othersTotal = 0.0f;

    int id = transactionID;
    String transction_name = "";
    String transaction_dt = "";
    float teller_amt = 0.0f;
    float cash_total = 0.0f;
    String created_at = "";
    int has_deleted = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_report, container, false);

        nameTV = root.findViewById(R.id.name);
        titleTV = root.findViewById(R.id.title);
        dtTimeTV = root.findViewById(R.id.date_time);
        cashTotalValueTV = root.findViewById(R.id.cash_total_value);
        totalPcsNotesValueTV = root.findViewById(R.id.total_pcs_notes_value);
        notesListLV = root.findViewById(R.id.notesList);
        totalPcsCoinsValueTV = root.findViewById(R.id.total_pcs_coins_value);
        coinsListLV = root.findViewById(R.id.coinsList);
        totalPcsOthersValueTV = root.findViewById(R.id.total_pcs_others_value);
        othersListLV = root.findViewById(R.id.othersList);
        notesRelative = root.findViewById(R.id.notesRelative);
        coinsRelative = root.findViewById(R.id.coinsRelative);
        othersRelative = root.findViewById(R.id.othersRelative);

        transactionID = getArguments().getInt("transaction_id");

        savedTransactionDBManager = new DBManager(this.getContext(), savedTransactionTbl.tableName(), savedTransactionTbl.columnColumns(), savedTransactionTbl.columnTypes());
        savedTransactionDBManager.open();

        transactionDBManager = new DBManager(this.getContext(), transactionTbl.tableName(), transactionTbl.columnColumns(), transactionTbl.columnTypes());
        transactionDBManager.open();

        setHasOptionsMenu(true);

        fetchData(transactionID);

        return root;
    }

    private void fetchData(int transactionID) {
        final Activity activity = this.getActivity();
        Resources resources = activity.getResources();
        String whereClause1 = "transaction_id =? AND has_deleted =?";
        String whereClause2 = "_id =? AND has_deleted =?";
        String[] whereArgs = new String[]{transactionID + "", "0"};
        Cursor transactionCursor = transactionDBManager.fetch(whereClause1, whereArgs, null, null, null);
        Cursor savedTransactionCursor = savedTransactionDBManager.fetch(whereClause2, whereArgs, null, null, null);

        notesData = new ArrayList<>();
        coinsData = new ArrayList<>();
        othersData = new ArrayList<>();

        notesPcs = 0.0f;
        coinsPcs = 0.0f;
        othersPcs = 0.0f;
        notesTotal = 0.0f;
        coinsTotal = 0.0f;
        othersTotal = 0.0f;

        if (savedTransactionCursor.moveToFirst() && savedTransactionCursor.getCount() > 0) {
            do {
                id = savedTransactionCursor.getInt(0);
                transction_name = savedTransactionCursor.getString(1);
                transaction_dt = savedTransactionCursor.getString(2);
                teller_amt = savedTransactionCursor.getFloat(3);
                cash_total = savedTransactionCursor.getFloat(4);
                created_at = savedTransactionCursor.getString(5);
                has_deleted = savedTransactionCursor.getInt(6);
            } while (savedTransactionCursor.moveToNext());
        }

        if (transactionCursor.moveToFirst() && transactionCursor.getCount() > 0) {
            do {
                // Read the values of a row in the table using the indexes acquired above
                final int id = transactionCursor.getInt(0);
                final int transaction_id = transactionCursor.getInt(1);
                final String description = transactionCursor.getString(2);
                final int denomination = transactionCursor.getInt(3);
                final int pieces = transactionCursor.getInt(4);
                final int bags = transactionCursor.getInt(5);
                final float loose = transactionCursor.getFloat(6);
                final String type = transactionCursor.getString(7);
                final String createdAt = transactionCursor.getString(8);
                final int has_deleted = transactionCursor.getInt(9);

                float totalPcs = (pieces * bags) + loose;
                float totalValue = (denomination * pieces * bags);
                GroupListData item = new GroupListData(id, description, denomination, pieces, bags + "",loose + "",
                                                        type, totalValue, has_deleted, false, false);

                switch (type) {
                    case "notes":
                        if (notesData.size() == 0) {
                            notesData.add(new GroupListData(-1, resources.getString(R.string.denom), 0, 0,
                                    resources.getString(R.string.bundles), resources.getString(R.string.loose), type,
                                    0.0f, 0, false, true));
                        }


                        notesPcs += totalPcs;
                        notesTotal += (totalValue + (loose * item.getAmount()));
                        item.setTotal(totalValue + (loose * item.getAmount()));
                        notesData.add(item);
                        break;
                    case "coins":
                        if (coinsData.size() == 0) {
                            coinsData.add(new GroupListData(-1, resources.getString(R.string.denom), 0, 0,
                                    resources.getString(R.string.bags), resources.getString(R.string.loose), type,
                                    0.0f, 0, false, true));
                        }

                        coinsPcs += totalPcs;
                        coinsTotal += (totalValue + (loose * item.getAmount()));
                        item.setTotal(totalValue + (loose * item.getAmount()));
                        coinsData.add(item);
                        break;
                    case "others":
                        if (othersData.size() == 0) {
                            othersData.add(new GroupListData(-1, resources.getString(R.string.desc), 0, 0,
                                    "", "", type, 0.0f, 0, false, true));
                        }
                        othersPcs += totalPcs;
                        othersTotal += (totalValue + loose);
                        item.setTotal(totalValue + loose);
                        othersData.add(item);
                        break;
                }
            } while (transactionCursor.moveToNext());


            if (notesData.size() > 0) {
                notesData.add(new GroupListData(-1, resources.getString(R.string.total), 0, 0,
                        "", "", "notes", notesTotal, 0, true, false));
            } else {
                notesRelative.setVisibility(View.GONE);
            }

            if (coinsData.size() > 0) {
                coinsData.add(new GroupListData(-1, resources.getString(R.string.total), 0, 0,
                        "", "", "coins", coinsTotal, 0, true, false));
            } else {
                coinsRelative.setVisibility(View.GONE);
            }

            if (othersData.size() > 0) {
                othersData.add(new GroupListData(-1, resources.getString(R.string.total), 0, 0,
                        "", "", "others", othersTotal, 0, true, false));
            } else {
                othersRelative.setVisibility(View.GONE);
            }
        }

        int titleRes = (transction_name != null && transction_name.length() > 0) ? R.string.title_cash_tally_Report : R.string.title_cash_Report;
        float cashTotal = notesTotal + coinsTotal + othersTotal - teller_amt;

        nameTV.setText(transction_name);
        titleTV.setText(resources.getText(titleRes));
        dtTimeTV.setText(transaction_dt);

        cashTotalValueTV.setText(CommonFun.currencyFormat(this.getContext(), cashTotal));
        totalPcsNotesValueTV.setText(CommonFun.currencyValue(this.getContext(), notesPcs));
        totalPcsCoinsValueTV.setText(CommonFun.currencyValue(this.getContext(), coinsPcs));
        totalPcsOthersValueTV.setText(CommonFun.currencyValue(this.getContext(), othersPcs));

        ReportListAdapter notesAdapter = new ReportListAdapter(this.getActivity(), notesData);
        ReportListAdapter coinsAdapter = new ReportListAdapter(this.getActivity(), coinsData);
        ReportListAdapter othersAdapter = new ReportListAdapter(this.getActivity(), othersData);

        notesListLV.setAdapter(notesAdapter);
        coinsListLV.setAdapter(coinsAdapter);
        othersListLV.setAdapter(othersAdapter);

        getListViewSize(notesListLV);
        getListViewSize(coinsListLV);
        getListViewSize(othersListLV);

    }

    public void changeDivider(ListView listView) {
        listView.setDivider(null);
        listView.setDividerHeight(0);
    }

    public void getListViewSize(ListView myListView) {
        changeDivider(myListView);

        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            try {
                View listItem = myListAdapter.getView(size, null, myListView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            } catch (Exception ex) {}
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (inflater != null) {
            inflater.inflate(R.menu.main, menu);
        }

        menu.findItem(R.id.action_share).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareView();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareView() {
        if (checkPermission()) {
            View view = root.findViewById(R.id.relative0);
            RelativeLayout layout1 = root.findViewById(R.id.relative0);
            int width = view.getHeight();
            int height = view.getWidth();
            Bitmap bitmap = takeScreenshot();
//            saveBitmap(bitmap);
            if (bitmap != null && saveImageToExternalStorage(bitmap)) {
                layout1.setMinimumWidth(width);
                layout1.setMinimumHeight(height);
                shareIt();
            }
        }
    }

    private boolean checkPermission() {
        List arrayList = new ArrayList();
        for (String str : this.permissions) {
            if (ContextCompat.checkSelfPermission(this.getContext(), str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this.getActivity(), (String[]) arrayList.toArray(new String[arrayList.size()]), 100);
        return false;
    }

    public Bitmap takeScreenshot() {
//        View rootView = this.getActivity().findViewById(android.R.id.content).getRootView();
        View view = root.findViewById(R.id.relative0);
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();

        if (bitmap == null) {
            bitmap = loadBitmapFromView(view);
        }

        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        View rootview = v;
        int height = v.getHeight();
        int width = v.getWidth();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    /*public static Bitmap loadBitmapFromView2(View v) {
        View view2 = v;
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        v = view2;
        return b;
    }

    public static Bitmap loadBitmapFromView3(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }*/

    public boolean saveImageToExternalStorage(Bitmap image) {
        String APP_PATH_SD_CARD = "/Cashmate/";
        String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

        File storage = Environment.getExternalStorageDirectory() != null ? Environment.getExternalStorageDirectory() :
                Environment.getDataDirectory();

        String fullPath = storage.getAbsolutePath() + APP_PATH_SD_CARD + APP_THUMBNAIL_PATH_SD_CARD;

        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath, "CashMate.png");
            imagePath = file;
            file.createNewFile();
            fOut = new FileOutputStream(file);

            // 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(this.getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    /*public static File StoreScreenShot(Bitmap picture)
    {
        String folder = Environment.getExternalStorageDirectory() + File.separator + "MyFolderName";
        String extFileName = Environment.getExternalStorageDirectory() + File.separator +
                CommonFun.getCurrentDateTime().replaceAll(" ", "-") + ".jpeg";
        try {

            File file = new File(extFileName);

            using (var fs = new FileStream(extFileName, FileMode.OpenOrCreate))
            {
                try
                {
                    picture.Compress(Bitmap.CompressFormat.Jpeg, 100, fs);
                }
                finally
                {
                    fs.Flush();
                    fs.Close();
                }
                return file;
            }
        }
        catch (UnauthorizedAccessException ex)
        {
            Log.Error(LogPriority.Error.ToString(), "-------------------" + ex.Message.ToString());
            return null;
        }
        catch (Exception ex)
        {
            Log.Error(LogPriority.Error.ToString(), "-------------------" + ex.Message.ToString());
            return null;
        }
    }*/

    private void shareIt() {
//        Uri uri = Uri.fromFile(imagePath);
        Uri uri = FileProvider.getUriForFile(this.getContext(), BuildConfig.APPLICATION_ID + ".provider", imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}