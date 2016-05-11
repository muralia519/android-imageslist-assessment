package com.infy.android.telstrafeeds.common;

import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.infy.android.telstrafeeds.R;

/**
 * @author Murali_Arigala
 */
public class AppControllerActivity extends AppCompatActivity implements RecyclerViewFragment.OnFragmentInteractionListener {


    protected Toolbar mAppToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_controller);
        mAppToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(mAppToolbar);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, RecyclerViewFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
