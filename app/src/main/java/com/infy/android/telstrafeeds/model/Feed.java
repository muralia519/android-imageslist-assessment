package com.infy.android.telstrafeeds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This is model class for Feed object and used Jackson for mapping JSON into POJO.
 *
 * Created by Murali_Arigala on 5/10/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Feed {
    private String title;
    private String description;

    public Feed() {
    }

    public Feed(String title, String description, String imageHref) {
        this.title = title;
        this.description = description;
        this.imageHref = imageHref;
    }

    private String imageHref;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }
}
