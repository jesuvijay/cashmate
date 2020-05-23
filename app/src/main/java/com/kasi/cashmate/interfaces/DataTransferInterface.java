package com.kasi.cashmate.interfaces;

import android.widget.TextView;
import com.kasi.cashmate.collection.GroupListData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kasi
 */
public interface DataTransferInterface {

    public void setNotesTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data);

    public void setCoinsTextViewValues(TextView pcsValue, TextView totalValue, ArrayList<GroupListData> data);

    public void setOthersTextViewValues(TextView totalValue, ArrayList<GroupListData> data);

    public void setNotesTextView(TextView pcsValue, TextView totalValue);

    public void setCoinsTextView(TextView pcsValue, TextView totalValue);

    public void setOthersTextView(TextView totalValue);

    public void onSetCoinsValues(ArrayList<GroupListData> data);

    public void onSetNotesValues(ArrayList<GroupListData> data);

    public void onSetOthersValues(ArrayList<GroupListData> data);

    public void updateListData(HashMap<String, ArrayList<GroupListData>> updatedListDataChildData);

}
