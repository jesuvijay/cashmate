package com.kasi.cashmate.collection;

import com.kasi.cashmate.common.CommonFun;
import com.kasi.cashmate.tables.SavedTransaction;

import java.text.ParseException;

/**
 * @author kasi
 */
public class SavedTransactionData {
    private int id;
    private String transactionName;
    private String transactionDt;
    private float tellerAmount;
    private float cashTotal;
    private String createdAt;
    private int hasDeleted;

    private String createdDate, createdTime;

    public SavedTransactionData (int id, String transaction_name, String transaction_dt, float teller_amount,
                            float cash_total, String created_at, int has_deleted) {
        this.id = id;
        this.transactionName = transaction_name;
        this.transactionDt = transaction_dt;
        this.tellerAmount = teller_amount;
        this.cashTotal = cash_total;
        this.createdAt = created_at;
        this.hasDeleted = has_deleted;
        this.setCreatedDate();
        this.setCreatedTime();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTellerAmount(float tellerAmount) {
        this.tellerAmount = tellerAmount;
    }

    public float getTellerAmount() {
        return tellerAmount;
    }

    public void setCashTotal(float cashTotal) {
        this.cashTotal = cashTotal;
    }

    public float getCashTotal() {
        return cashTotal;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setHasDeleted(int hasDeleted) {
        this.hasDeleted = hasDeleted;
    }

    public int getHasDeleted() {
        return hasDeleted;
    }

    public void setCreatedDate() {
        this.createdDate = CommonFun.getFormattedDate(this.createdAt, CommonFun.date_pattern);
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedTime() {
        this.createdTime = CommonFun.getFormattedDate(this.createdAt, CommonFun.time_pattern);
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedTime() {
        return createdTime;
    }
}
