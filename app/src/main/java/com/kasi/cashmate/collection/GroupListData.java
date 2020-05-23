package com.kasi.cashmate.collection;


/**
 * @author kasi
 */
public class GroupListData {
    private String title, loose, bags, type;
    private float total;
    private int _id = -1, bundles = 0, has_deleted = 0, amount = 0;
    private boolean is_last = false, is_header = false;

    public GroupListData(int _id, String title, int amount, int bundles, String bags, String loose, String type,
                         int has_deleted, boolean is_last) {
        this._id = _id;
        this.title = title;
        this.amount = amount;
        this.bundles = bundles;
        this.bags = bags;
        this.loose = loose;
        this.has_deleted = has_deleted;
        this.type = type;
        this.is_last = is_last;
        this.total = 0.00f;
    }

    public GroupListData(int _id, String title, int amount, int bundles, String bags, String loose, String type, float total,
                         int has_deleted, boolean is_last, boolean is_header) {
        this._id = _id;
        this.title = title;
        this.amount = amount;
        this.bundles = bundles;
        this.bags = bags;
        this.loose = loose;
        this.has_deleted = has_deleted;
        this.type = type;
        this.is_last = is_last;
        this.total = total;
        this.is_header = is_header;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getId() {
        return _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setBundles(int bundles) {
        this.bundles = bundles;
    }

    public int getBundles() {
        return bundles;
    }

    public void setLoose(String loose) {
        this.loose = loose;
    }

    public String getLoose() {
        return loose;
    }

    public void setIsLast(boolean is_last) {
        this.is_last = is_last;
    }

    public boolean isLast() {
        return is_last;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal() {
        return total;
    }

    public void setBags(String bags) {
        this.bags = bags;
    }

    public String getBags() {
        return bags;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setHasDeleted(int has_deleted) {
        this.has_deleted = has_deleted;
    }

    public int getHasDeleted() {
        return has_deleted;
    }

    public void setIsHeader(boolean is_header) {
        this.is_header = is_header;
    }

    public boolean isIsHeader() {
        return is_header;
    }
}
