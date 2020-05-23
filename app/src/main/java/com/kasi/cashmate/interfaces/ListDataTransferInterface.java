package com.kasi.cashmate.interfaces;

import com.kasi.cashmate.collection.GroupListData;

/**
 * @author kasi
 */
public interface ListDataTransferInterface {

    public void updateData(GroupListData data);

    public void softDeleteData(GroupListData data);

}
