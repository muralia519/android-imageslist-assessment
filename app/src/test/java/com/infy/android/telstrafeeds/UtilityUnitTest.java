package com.infy.android.telstrafeeds;

import com.infy.android.telstrafeeds.model.Feed;
import com.infy.android.telstrafeeds.model.FeedListDetails;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Utility Unit test class
 *
 * @author Murali_Arigala .
 */
public class UtilityUnitTest {
    @Test
    public void isNotEmpty_ValidInput() throws Exception {
        String strNotEmpty = "TestDemo";
        ;
        assertEquals("Not and Non empty string test:", true, Utility.isNotEmpty(strNotEmpty));
    }

    @Test
    public void isNotEmpty_InvalidInput() throws Exception {
        String strNull = null;
        String strEmpty = "";
        assertEquals("Null string test: ", false, Utility.isNotEmpty(strNull));
        assertEquals("Empty string test: ", false, Utility.isNotEmpty(strEmpty));
    }

    @Test
    public void getNonNullFeedListTest() throws Exception {
        Feed[] nullfeeds = null;
        assertEquals("Null Input test: ", 0, Utility.getNonNullFeedList(nullfeeds).size());

        Feed[] emptyfeeds = {};
        assertEquals("Null Input test: ", 0, Utility.getNonNullFeedList(emptyfeeds).size());

        Feed[] feedswith5Entries = {new Feed(), new Feed(),
                new Feed("Title1", "Description1", "http://www.google.com/test.png"),
                new Feed("Title2", "", ""),
                new Feed("", "Description3", "http://www.google.com/test2.png")};

        assertEquals("Feed Array size 5, only 3 Valid Feed Objects test: ", 3,
                Utility.getNonNullFeedList(feedswith5Entries).size());
    }
}