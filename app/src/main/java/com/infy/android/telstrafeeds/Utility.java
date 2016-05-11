package com.infy.android.telstrafeeds;

import com.infy.android.telstrafeeds.model.Feed;

import java.util.ArrayList;

/**
 * Created by Murali_Arigala on 5/10/2016.
 */
public class Utility {

    /**
     * Utility method for null and empty value check
     */
    public static boolean isNotEmpty(String value) {
        return (value != null && value.trim().length() > 0);
    }

    /**
     * Utility method for non empty Feed objects array list.Removes the feed when all feed details
     * like title,description and image are null or empty from list
     **/
    public static ArrayList<Feed> getNonNullFeedList(Feed[] feedsArray) {
        ArrayList<Feed> feedList = new ArrayList<Feed>();
        int iArraySize =  feedsArray != null ? feedsArray.length : 0;
        for (int i = 0; i < iArraySize ; i++) {
            if (isNotEmpty(feedsArray[i].getTitle()) ||
                    isNotEmpty(feedsArray[i].getDescription()) ||
                    isNotEmpty(feedsArray[i].getImageHref())) {
                feedList.add(new Feed(feedsArray[i].getTitle(), feedsArray[i].getDescription(),
                        feedsArray[i].getImageHref()));
            }
        }
        return feedList;
    }
}
