package com.infy.android.telstrafeeds.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
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
 * @author Murali_Arigala
 */
public class RecyclerViewFragment extends Fragment {
    private final String TAG = "RecyclerViewFragment";
    private final String FEEDS_URL = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    private OnFragmentInteractionListener mListener;
    private RecyclerView mFeedListView;
    private SwipeRefreshLayout mSwipeRrefreshLayout;
    private RecyclerFeedAdapter mFeedsAdapter;
    private RequestQueue mRequestrQueue;
    private ProgressDialog mProgressBar;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecyclerViewFragment.
     */
    public static RecyclerViewFragment newInstance() {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mRequestrQueue = Volley.newRequestQueue(getActivity());
        getLiveFeedsDetails(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mSwipeRrefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_view);
        mSwipeRrefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.GREEN,Color.DKGRAY);
        mSwipeRrefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, " SwipeRrefreshLayout OnRefresh is called");
                mSwipeRrefreshLayout.setRefreshing(true);
                refreshFeedsDetails(false);
            }
        });
        mFeedListView = (RecyclerView) view.findViewById(R.id.feeds_view);
        mFeedListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedListView.addItemDecoration(
                new DividerItemDecoration(ResourcesCompat.getDrawable(getActivity().getResources(),
                        R.drawable.divider, getActivity().getTheme()),
                        false, false));
        return view;
    }

    private void getLiveFeedsDetails(final boolean showProgressDialog) {
        if(showProgressDialog) {
            mProgressBar = ProgressDialog.show(getActivity(), null, getString(R.string.download_message), false, false);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                FEEDS_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(showProgressDialog) {
                    stopProgressDialog();
                }
                ObjectMapper mapper = new ObjectMapper();
                FeedListDetails details = null;
                try {
                    details = mapper.readValue(response.toString(), FeedListDetails.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG, "Error in mapping live feed list details intoobject : ");
                }
                if (details != null) {
                    getActivity().setTitle(details.getTitle());
                    Log.i(TAG, "Title : " + details.getTitle());
                    Log.i(TAG, "Toll feeds received: " + details.getRows().length);
                    if(mFeedsAdapter == null){
                        mFeedsAdapter = new RecyclerFeedAdapter(getActivity().getApplicationContext(),
                                Utility.getNonNullFeedList(details.getRows()));
                        mFeedListView.setAdapter(mFeedsAdapter);
                    } else {
                        mFeedsAdapter.setFeedItemList(Utility.getNonNullFeedList(details.getRows()));
                        mFeedsAdapter.notifyDataSetChanged();
                        mSwipeRrefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Feeds Details refreshed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(showProgressDialog) {
                    stopProgressDialog();
                }
                Log.i(TAG, getString(R.string.feed_list_retrieval_error_message));
                Toast.makeText(getActivity(), R.string.feed_list_retrieval_error_message, Toast.LENGTH_LONG);
            }
        });
        jsonObjectRequest.setTag(TAG);
        mRequestrQueue.add(jsonObjectRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mRequestrQueue != null){
            mRequestrQueue.cancelAll(TAG);
        }
        stopProgressDialog();
    }

    private void stopProgressDialog() {
        if(mProgressBar != null) {
            mProgressBar.dismiss();
            mProgressBar = null;
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        inflater.inflate(R.menu.app_controller_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_action_btn:
                refreshFeedsDetails(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void refreshFeedsDetails(boolean showProgressDialog) {
        Log.i(TAG, " Refresh Feeds is called");
        getLiveFeedsDetails(showProgressDialog);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
