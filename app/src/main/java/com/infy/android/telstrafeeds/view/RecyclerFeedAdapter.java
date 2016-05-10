package com.infy.android.telstrafeeds.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infy.android.telstrafeeds.R;
import com.infy.android.telstrafeeds.common.RecyclerViewFragment;
import com.infy.android.telstrafeeds.model.Feed;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Murali_Arigala on 5/10/2016.
 */
public class RecyclerFeedAdapter extends RecyclerView.Adapter<RecyclerFeedAdapter.ListItemViewHolder> {

    private ArrayList<Feed> mFeedItemList;
    private Context mContext;

    public RecyclerFeedAdapter(Context p_context, ArrayList<Feed> p_feedItemList) {
        this.mFeedItemList = p_feedItemList;
        this.mContext = p_context;
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
        if (RecyclerViewFragment.isNotEmpty(feedItem.getImageHref())) {
            //Gets network image using picasso library
            Picasso.with(mContext).load(feedItem.getImageHref()).networkPolicy(NetworkPolicy.OFFLINE).resize(120, 120).error(R.drawable.ic_download_error)
                    .into(holder.mImageThumbnail);
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
