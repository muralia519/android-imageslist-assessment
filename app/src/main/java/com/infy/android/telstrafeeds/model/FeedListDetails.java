package com.infy.android.telstrafeeds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is model class for FeedListDetails object and used Jackson for mapping JSON into POJO.
 *
 * Created by Murali_Arigala on 5/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedListDetails {
    private String title;
    private Feed[] rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Feed[] getRows() {
        return rows;
    }

    public void setRows(Feed[] rows) {
        this.rows = rows;
    }
}
