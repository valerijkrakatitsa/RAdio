package com.vkrakatitsa.radio.Activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedHashMap;

import com.vkrakatitsa.radio.Fragments.ChooseTimeAndDateFragment;
import com.vkrakatitsa.radio.Fragments.NavigationDrawer;
import com.vkrakatitsa.radio.Fragments.ShowMoreResults;
import com.vkrakatitsa.radio.Fragments.ShowResultFragment;
import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.Services.PlayMusicService;

public class ChooseTimeActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, ChooseTimeAndDateFragment.OnStartNewFragment {

    public static final String GET_ITEM_ID_FOR_INTENT = "GET_ITEM_ID_FOR_INTENT";

    private ChooseTimeAndDateFragment chooseTimeAndDateFragment;
    private String m_strRadioName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        Intent intent = getIntent();

        //Check is VkSearch Results or first start activity
        // if intent content START_MORE_RESULT_FRAGMENT_KEY - this is vkSearch Result
        if ((intent != null) && (intent.getIntExtra(NavigationDrawer.START_MORE_RESULT_FRAGMENT_KEY, -1) != -1)) {

            Log.d("Look", this.getClass().getCanonicalName() + " VK RESULTS BLOCK");
            ShowMoreResults showMore = new ShowMoreResults();      //New Fragment Show Vk results


            showMore.setArguments(intent.getExtras());      //Add Arguments from intent extras

            //-------------startFragment More Result---------
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ChooseTimeActivity_MainFragment, showMore).commit();

        } else {
            //Open chooseTime Fragment if intent don`t have MORE_RESULT_FRAGMENT key
            chooseTimeAndDateFragment = new ChooseTimeAndDateFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ChooseTimeActivity_MainFragment, chooseTimeAndDateFragment).commit();

            //Find title name by id
            long nID = getIntent().getLongExtra(GET_ITEM_ID_FOR_INTENT, -1L);
            RadioItemEngine engine = new RadioItemEngine(this);
            RadioStationItem item = engine.getItemById(nID);
            m_strRadioName = item.getStationName();         // setTitle
        }

        initToolBar();      //Create Acrion Bar
        initNavigationDrawer();      //Create NavigationDrawer
    }

    //---------------------------------------------------------------------------------------------------------Init ToolBar
    public void initToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.root_layout);

        // If string title is null - fragment set title
        if (m_strRadioName != null) {
            toolbar.setTitle(m_strRadioName);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //----------------------------------------------------------------------------------------------initNavigationDrawer
    public void initNavigationDrawer() {

        DrawerLayout drawerMainPage = (DrawerLayout) findViewById(R.id.drawBar);
        ActionBarDrawerToggle drawerTogle = new ActionBarDrawerToggle(this, drawerMainPage, null, R.string.app_name, R.string.app_name);
        drawerTogle.setDrawerIndicatorEnabled(false);
        drawerMainPage.addDrawerListener(drawerTogle);
        drawerTogle.syncState();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Log.d("Look", this.getClass().getCanonicalName() + "  onMenuItemClick ");

        if (item.getItemId() == android.support.v7.appcompat.R.id.homeAsUp) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Action back  on Toolbar "Back" button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------isNetworkavailable
    private boolean isNetworkavailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = connectivityManager.getActiveNetworkInfo();

        if (i != null && i.isConnected()) {
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------------------------------showConnectionDialog
    public void showConnectioDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(getResources().getString(R.string.no_internet_connection_dialog_message));
        builder.setTitle(getResources().getString(R.string.no_internet_connection_dialog_title));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PlayMusicService.class));
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------onStartNewFragment (Interface ChooseTimeAndDateFragment.OnStartNewFragment)
    @Override
    public void onStartNewFragment(LinkedHashMap<String, String> arrResult, String strDate, String strTimeFrom, String strTimeTo) {

        //Check Network connection
        if (!isNetworkavailable()) {
            showConnectioDialog();      // show "No connection" dialog if no network connection
            return;
        }

        if (arrResult != null) {

            //Create new fragment and send Result Collection (LinkedHashMap<String,String> arrResult) and pass like argument
            ShowResultFragment fragResult = new ShowResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ShowResultFragment.GET_ARRAY_RESULT_KEY, arrResult);          //add argument resultArray
            bundle.putString(ShowResultFragment.GET_STRING_TIME_FROM_KEY, strTimeFrom);          //add argument time from (String)
            bundle.putString(ShowResultFragment.GET_STRING_TIME_TO_KEY, strTimeTo);              //add argument time to (String)
            bundle.putString(ShowResultFragment.GET_STRING_DATE_KEY, strDate);                   //add argument date (String)
            bundle.putString(ShowResultFragment.GET_STRING_RADIO_NAME_KEY, m_strRadioName);      //add argument Radio station (String)
            fragResult.setArguments(bundle);

            Log.d("Look", "ChooseTimeActivity-Onclick: I work");

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.ChooseTimeActivity_MainFragment, fragResult);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}