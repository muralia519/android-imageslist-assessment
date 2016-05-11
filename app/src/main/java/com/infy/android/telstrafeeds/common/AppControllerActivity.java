package com.infy.android.telstrafeeds.common;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.infy.android.telstrafeeds.R;

/**
 * This is app home/central controller/dashboard activity.
 *
 * @author Murali_Arigala
 */
public class AppControllerActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener {

    // Application Bar/Action Bar instance
    protected Toolbar mAppToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_controller);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(mAppToolbar);

        //Adding RecyclerFeedListFragment into this activity
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RecyclerViewFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onRecyclerViewFragmentInteraction(String action) {
        //TODO::This is placeholder Feed Details Activity/Fragment view
    }
}
