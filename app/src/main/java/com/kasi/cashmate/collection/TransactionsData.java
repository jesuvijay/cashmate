package com.kasi.cashmate.collection;

/**
 * @author kasi
 */
public class TransactionsData {
    private int id;
    int transactionID;
    String description;
    int denomination;
    int pieces;
    int bags;
    float loose;
    String type;
    String transactionDt;
    private int hasDeleted;

    public TransactionsData (int id, int transaction_id, String description, int denomination, int pieces,
                            int bags, float loose, String type, String transactionDt, int has_deleted) {
        this.id = id;
        this.transactionID = transaction_id;
        this.description = description;
        this.denomination = denomination;
        this.pieces = pieces;
        this.bags = bags;
        this.loose = loose;
        this.type = type;
        this.transactionDt = transactionDt;
        this.hasDeleted = has_deleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getPieces() {
        return pieces;
    }

    public void setBags(int bags) {
        this.bags = bags;
    }

    public int getBags() {
        return bags;
    }

    public void setLoose(float loose) {
        this.loose = loose;
    }

    public float getLoose() {
        return loose;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setHasDeleted(int hasDeleted) {
        this.hasDeleted = hasDeleted;
    }

    public int getHasDeleted() {
        return hasDeleted;
    }
}
