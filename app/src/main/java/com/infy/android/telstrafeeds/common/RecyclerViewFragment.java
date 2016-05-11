package com.infy.android.telstrafeeds.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.android.telstrafeeds.R;
import com.infy.android.telstrafeeds.Utility;
import com.infy.android.telstrafeeds.model.FeedListDetails;
import com.infy.android.telstrafeeds.view.DividerItemDecoration;
import com.infy.android.telstrafeeds.view.RecyclerFeedAdapter;

import org.json.JSONObject;

import java.io.IOException;

/**
 * This fragment will displays live feed details in list view like layout.
 *
 * @author Murali_Arigala
 */
public class RecyclerViewFragment extends Fragment {
    private final String TAG = "RecyclerViewFragment";

    // Live feeds source url
    private final String FEEDS_URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    // Fragment hosting Activity interaction lister, future implementation
    private OnFragmentInteractionListener mListener;

    // Feed list view instance
    private RecyclerView mFeedListView;

    // Swipe to Refresh handle view for FeedListView
    private SwipeRefreshLayout mSwipeRrefreshView;

    // Feed list view adapter instance
    private RecyclerFeedAdapter mFeedsAdapter;

    // Volley library RequestQueue instance for fetching feeds from network.
    private RequestQueue mRequestrQueue;

    // Progress dialog instance.This is used during feed details fetch from network
    private ProgressDialog mProgressBar;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //For adding fragment specific action buttons into activity app bar
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        //Initialize SwipeRrefreshView,Sets scheme colors and refresh lister
        mSwipeRrefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_view);
        mSwipeRrefreshView.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN,Color.DKGRAY);
        mSwipeRrefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, " SwipeRrefreshLayout OnRefresh is called");
                //Sets Refreshing State true when refreshing started
                mSwipeRrefreshView.setRefreshing(true);
                getLiveFeedsDetails(false);
            }
        });

        //Initialize RecyclerView/FeedListView
        mFeedListView = (RecyclerView) view.findViewById(R.id.feeds_view);

        //Sets Layout manager for RecyclerView
        mFeedListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Sets list item dividers for recycler view/feed list view
        mFeedListView.addItemDecoration(
                new DividerItemDecoration(ResourcesCompat.getDrawable(getActivity().getResources(),
                        R.drawable.divider, getActivity().getTheme()),
                        false, false));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Initialize RequestQueue
        mRequestrQueue = Volley.newRequestQueue(getActivity());

        //Gets feeds details first time (when fragment is added to activity.)
        getLiveFeedsDetails(true);
    }

    /**
     * Retrieves live feed details from network.
     *
     * Volley library (Http) is used for getting feed details json.
     *
     * Volley's RequestQueue uses worker threads for running the network operations,
     * reading from and writing to the cache, and parsing responses.
     *
     * For more details on Volley: http://developer.android.com/training/volley/simple.html
     *
     * @param showProgressDialog
     */
    private void getLiveFeedsDetails(final boolean showProgressDialog) {

        // Progress Dialog is shown When get live feeds request is coming initial
        // fragment displayed or from refresh action button click
        if(showProgressDialog) {
            mProgressBar = ProgressDialog.show(getActivity(), null, getString(R.string.download_message), false, false);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                FEEDS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Live Feeds JSON response :" + response.toString());

                JSONToObjectMapping(response);
                if(showProgressDialog) {
                    stopProgressDialog();
                }
           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(showProgressDialog) {
                    stopProgressDialog();
                }

                Log.i(TAG, " Volley error : "+error.toString());
                Toast.makeText(getActivity(), R.string.feed_list_retrieval_error_message, Toast.LENGTH_LONG).show();
            }
        });
        jsonObjectRequest.setTag(TAG);
        mRequestrQueue.add(jsonObjectRequest);
    }

    private void JSONToObjectMapping(final JSONObject response ){

        // Using an AsyncTask to JSONObject into POJO in a background thread
        new AsyncTask<JSONObject, Void, FeedListDetails>() {
            @Override
            protected FeedListDetails doInBackground(JSONObject... params) {
                ObjectMapper mapper = new ObjectMapper();
                FeedListDetails feedsDetails = null;
                try {
                    feedsDetails = mapper.readValue(params[0].toString(), FeedListDetails.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "Error in converting json feed list details into java object");
                }
                return feedsDetails;
            }

            @Override
            protected void onPostExecute(FeedListDetails p_feedsDetails) {
                super.onPostExecute(p_feedsDetails);
                if (p_feedsDetails != null) {
                    getActivity().setTitle(p_feedsDetails.getTitle());
                    Log.i(TAG, "Title : " + p_feedsDetails.getTitle());
                    Log.i(TAG, "Toll feeds received: " + p_feedsDetails.getRows().length);
                    if(mFeedsAdapter == null){
                        mFeedsAdapter = new RecyclerFeedAdapter(getActivity().getApplicationContext(),
                                Utility.getNonNullFeedList(p_feedsDetails.getRows()));
                        mFeedListView.setAdapter(mFeedsAdapter);
                    } else {
                        mFeedsAdapter.setFeedItemList(Utility.getNonNullFeedList(p_feedsDetails.getRows()));
                        mFeedsAdapter.notifyDataSetChanged();
                        mSwipeRrefreshView.setRefreshing(false);
                        Toast.makeText(getContext(),R.string.refresh_completed_message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_feed_details_message, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(response);
    }
    @Override
    public void onStop() {
        super.onStop();

        //Cancel all pending json requests for feeds
        if(mRequestrQueue != null){
            mRequestrQueue.cancelAll(TAG);
        }
        stopProgressDialog();
    }

    /**
     * Dismisses progress dialog
     */
    private void stopProgressDialog() {
        if(mProgressBar != null) {
            mProgressBar.dismiss();
            mProgressBar = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //Gets activity instance for activity/fragment interaction
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Adds Refresh Data Action Button to App/Action Bar
        inflater.inflate(R.menu.app_controller_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_action_btn:
                Log.i(TAG, " Refresh Feeds Action Button is called");
                getLiveFeedsDetails(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onRecyclerViewFragmentInteraction(String action);
    }
}
