package com.kasi.cashmate.collection;

/**
 * @author kasi
 */
public class GroupHeaderData {
    int header_top_color;
    private String title, bags_count_title, value_title, type;

    public GroupHeaderData(String title, String bags_count_title, String value_title, int header_top_color, String type) {
        this.title = title;
        this.header_top_color = header_top_color;
        this.bags_count_title = bags_count_title;
        this.value_title = value_title;
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setBagsCount(String bags_count_title) {
        this.bags_count_title = bags_count_title;
    }

    public String getBagsCount() {
        return bags_count_title;
    }

    public void setValue(String value_title) {
        this.value_title = value_title;
    }

    public String getValue() {
        return value_title;
    }

    public void setHeaderTopColor(int header_top_color) {
        this.header_top_color = header_top_color;
    }

    public int getHeaderTopColor() {
        return this.header_top_color;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
