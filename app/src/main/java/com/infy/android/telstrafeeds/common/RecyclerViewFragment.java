package com.infy.android.telstrafeeds.common;

import android.content.Context;
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
import android.widget.Toast;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.android.telstrafeeds.R;
import com.infy.android.telstrafeeds.model.Feed;
import com.infy.android.telstrafeeds.model.FeedListDetails;
import com.infy.android.telstrafeeds.view.DividerItemDecoration;
import com.infy.android.telstrafeeds.view.RecyclerFeedAdapter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Murali_Arigala
 */
public class RecyclerViewFragment extends Fragment {
    private final String TAG = "RecyclerViewFragment";
    private OnFragmentInteractionListener mListener;
    private RecyclerView mFeedListView;
    private SwipeRefreshLayout mSwipeRrefreshLayout;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mSwipeRrefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_to_refresh_view);

        mSwipeRrefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, " SwipeRrefreshLayout OnRefresh is called");
                mSwipeRrefreshLayout.setRefreshing(true);
                refreshFeeds();
            }
        });
        mFeedListView = (RecyclerView) view.findViewById(R.id.feeds_view);
        mFeedListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedListView.addItemDecoration(
                new DividerItemDecoration(ResourcesCompat.getDrawable(getActivity().getResources(),
                        R.drawable.divider, getActivity().getTheme()),
                        false, false));

        refreshFeeds();
        return view;
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
                refreshFeeds();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void refreshFeeds() {
        Log.i(TAG, " Refresh Feeds is called");
        Toast.makeText(getContext(), "Data refreshed", Toast.LENGTH_LONG).show();
        String testData = "{ \n" +
                "   \"title\":\"About Canada\",\n" +
                "   \"rows\":[ \n" +
                "      { \n" +
                "         \"description\":\"Beavers are second only to humans in their ability to manipulate and change their environment. They can measure up to 1.3 metres long. A group of beavers is called a colony\",\n" +
                "         \"title\":\"Beavers\",\n" +
                "         \"imageHref\":\"http:\\/\\/upload.wikimedia.org\\/wikipedia\\/commons\\/thumb\\/6\\/6b\\/American_Beaver.jpg\\/220px-American_Beaver.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":null,\n" +
                "         \"title\":\"Flag\",\n" +
                "         \"imageHref\":\"http:\\/\\/images.findicons.com\\/files\\/icons\\/662\\/world_flag\\/128\\/flag_of_canada.png\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"It is a well known fact that polar bears are the main mode of transportation in Canada. They consume far less gas and have the added benefit of being difficult to steal.\",\n" +
                "         \"title\":\"Transportation\",\n" +
                "         \"imageHref\":\"http:\\/\\/1.bp.blogspot.com\\/_VZVOmYVm68Q\\/SMkzZzkGXKI\\/AAAAAAAAADQ\\/U89miaCkcyo\\/s400\\/the_golden_compass_still.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"These Saturday night CBC broadcasts originally aired on radio in 1931. In 1952 they debuted on television and continue to unite (and divide) the nation each week.\",\n" +
                "         \"title\":\"Hockey Night in Canada\",\n" +
                "         \"imageHref\":\"http:\\/\\/fyimusic.ca\\/wp-content\\/uploads\\/2008\\/06\\/hockey-night-in-canada.thumbnail.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"A chiefly Canadian interrogative utterance, usually expressing surprise or doubt or seeking confirmation.\",\n" +
                "         \"title\":\"Eh\",\n" +
                "         \"imageHref\":null\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"Warmer than you might think.\",\n" +
                "         \"title\":\"Housing\",\n" +
                "         \"imageHref\":\"http:\\/\\/icons.iconarchive.com\\/icons\\/iconshock\\/alaska\\/256\\/Igloo-icon.png\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\" Sadly it's true.\",\n" +
                "         \"title\":\"Public Shame\",\n" +
                "         \"imageHref\":\"http:\\/\\/static.guim.co.uk\\/sys-images\\/Music\\/Pix\\/site_furniture\\/2007\\/04\\/19\\/avril_lavigne.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":null,\n" +
                "         \"title\":null,\n" +
                "         \"imageHref\":null\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"Canada hopes to soon launch a man to the moon.\",\n" +
                "         \"title\":\"Space Program\",\n" +
                "         \"imageHref\":\"http:\\/\\/files.turbosquid.com\\/Preview\\/Content_2009_07_14__10_25_15\\/trebucheta.jpgdf3f3bf4-935d-40ff-84b2-6ce718a327a9Larger.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"A moose is a common sight in Canada. Tall and majestic, they represent many of the values which Canadians imagine that they possess. They grow up to 2.7 metres long and can weigh over 700 kg. They swim at 10 km\\/h. Moose antlers weigh roughly 20 kg. The plural of moose is actually 'meese', despite what most dictionaries, encyclopedias, and experts will tell you.\",\n" +
                "         \"title\":\"Meese\",\n" +
                "         \"imageHref\":\"http:\\/\\/caroldeckerwildlifeartstudio.net\\/wp-content\\/uploads\\/2011\\/04\\/IMG_2418%20majestic%20moose%201%20copy%20(Small)-96x96.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"It's really big.\",\n" +
                "         \"title\":\"Geography\",\n" +
                "         \"imageHref\":null\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"Éare illegal. Cats are fine.\",\n" +
                "         \"title\":\"Kittens...\",\n" +
                "         \"imageHref\":\"http:\\/\\/www.donegalhimalayans.com\\/images\\/That%20fish%20was%20this%20big.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"They are the law. They are also Canada's foreign espionage service. Subtle.\",\n" +
                "         \"title\":\"Mounties\",\n" +
                "         \"imageHref\":\"http:\\/\\/3.bp.blogspot.com\\/__mokxbTmuJM\\/RnWuJ6cE9cI\\/AAAAAAAAATw\\/6z3m3w9JDiU\\/s400\\/019843_31.jpg\"\n" +
                "      },\n" +
                "      { \n" +
                "         \"description\":\"Nous parlons tous les langues importants.\",\n" +
                "         \"title\":\"Language\",\n" +
                "         \"imageHref\":null,\n" + "         \"imageHref\":null\n" +
                "      }\n" +
                "   ]\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        FeedListDetails details = null;
        try {
            details = mapper.readValue(testData, FeedListDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (details != null) {
            getActivity().setTitle(details.getTitle());
            Log.i(TAG, "Title : " + details.getTitle());
            Log.i(TAG, "No of feeds : " + details.getRows().length);
        }
        ArrayList<Feed> feeds = new ArrayList<Feed>();
        for (int i = 0; i < details.getRows().length; i++) {
            if (isNotEmpty(details.getRows()[i].getTitle()) ||
                    isNotEmpty(details.getRows()[i].getDescription()) || isNotEmpty(details.getRows()[i].getImageHref())) {
                feeds.add(new Feed(details.getRows()[i].getTitle(), details.getRows()[i].getDescription(), details.getRows()[i].getImageHref()));
            }
        }
        RecyclerFeedAdapter adapter = new RecyclerFeedAdapter(getActivity().getApplicationContext(), feeds);
        mFeedListView.setAdapter(adapter);
        mSwipeRrefreshLayout.setRefreshing(false);
    }

    public static boolean isNotEmpty(String value) {
        return (value != null && value.trim().length() > 0);
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
