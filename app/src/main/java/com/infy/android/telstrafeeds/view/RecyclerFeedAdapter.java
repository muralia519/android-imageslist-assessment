package com.infy.android.telstrafeeds.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infy.android.telstrafeeds.R;
import com.infy.android.telstrafeeds.Utility;
import com.infy.android.telstrafeeds.model.Feed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This is adapter implementation for recycler view(FeedListView).
 *
 * Created by Murali_Arigala on 5/10/2016.
 */
public class RecyclerFeedAdapter extends RecyclerView.Adapter<RecyclerFeedAdapter.ListItemViewHolder> {
    private final String TAG = "RecyclerFeedAdapter";
    private ArrayList<Feed> mFeedItemList;
    private Context mContext;

    public RecyclerFeedAdapter(Context p_context, ArrayList<Feed> p_feedItemList) {
        this.mFeedItemList = p_feedItemList;
        this.mContext = p_context;
    }

    /**
     * Sets FeedItems list.
     *
     * @param p_feedItemList FeedItems list
     */
    public void setFeedItemList(ArrayList<Feed> p_feedItemList){
        this.mFeedItemList = p_feedItemList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_layout, null);
        ListItemViewHolder viewHolder = new ListItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        Feed feedItem = mFeedItemList.get(position);
        //Sets feed view image thumbnail
        if (Utility.isNotEmpty(feedItem.getImageHref())) {
            Log.d(TAG, "Image URL "+ feedItem.getImageHref() +" at position : "+position);
            /**
             * Picasso library is used for lazing loading of images.
             *
             * Image loading is done on different worker threads.
             *
             * Image caching is supported by default for this library.This can be changed based on requirement.
             *
             * This library will handle multiple requests simultaneously.
             *
             * For more info on this library
             *
             * http://square.github.io/picasso/
             */

            Picasso.with(mContext).
                    load(feedItem.getImageHref()). // Image url
                    resize(mContext.getResources().getDimensionPixelSize(R.dimen.thumbnail_width), //Resizing image for unified look for all list times
                            mContext.getResources().getDimensionPixelSize(R.dimen.thumbnail_height))
                    .error(R.drawable.ic_download_error) // error image when unable to get/unavailable image from the url
                    .into(holder.mImageThumbnail); // Image view instance to which downloaded image is set.
            if (holder.mImageThumbnail.getVisibility() != View.VISIBLE) {
                holder.mImageThumbnail.setVisibility(View.VISIBLE);
            }
        } else {
            if (holder.mImageThumbnail.getVisibility() == View.VISIBLE) {
                holder.mImageThumbnail.setVisibility(View.GONE);
            }
        }
        //Sets feed view title
        holder.mFeedTitle.setText(feedItem.getTitle());

        //Sets feed view description
        holder.mFeedDescription.setText(feedItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return mFeedItemList != null ? mFeedItemList.size() : 0;
    }

    /**
     * This class is used as Holder for feed list item.This avoids repetitive
     * use of findViewId request for each list item
     */
    public static class ListItemViewHolder extends RecyclerView.ViewHolder {
        protected TextView mFeedTitle;
        protected TextView mFeedDescription;
        protected ImageView mImageThumbnail;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            mFeedTitle = (TextView) itemView.findViewById(R.id.feed_item_title);
            mFeedDescription = (TextView) itemView.findViewById(R.id.feed_item_description);
            mImageThumbnail = (ImageView) itemView.findViewById(R.id.feed_item_thumbail);
        }
    }
}
