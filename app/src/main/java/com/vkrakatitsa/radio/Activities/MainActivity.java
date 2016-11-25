package com.vkrakatitsa.radio.Activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vkrakatitsa.radio.Adapters.RadioStationListAdapter;
import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.Services.PlayMusicService;
import com.vkrakatitsa.radio.ToolsAndConstants.RadioListSettings;
import com.vkrakatitsa.radio.ToolsAndConstants.RecyclerItemCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RadioStationListAdapter.OnRecycleItemClickListener{

    private Toolbar toolbar;
    private RadioListSettings m_radioSettings;
    private ArrayList<RadioStationItem> m_arrayItems;
    private RadioStationListAdapter m_adapterItem;
    private RadioItemEngine m_RadioEngine;
    private PlayMusicService m_playMusicService;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            m_playMusicService = ((PlayMusicService.ThisBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activy_main);

        m_radioSettings = new RadioListSettings(this);

        // init Radio List
        RecyclerView mainListView = (RecyclerView) findViewById(R.id.main_list_view);
        mainListView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mainListView.setLayoutManager(layoutManager);

        m_RadioEngine = new RadioItemEngine(this);
        m_arrayItems = m_RadioEngine.getAll();

        m_adapterItem = new RadioStationListAdapter(m_arrayItems);
        m_adapterItem.setOnRecycleItemClickListener(this);

        Intent intent = new Intent(this, PlayMusicService.class );
        bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);

        mainListView.setAdapter(m_adapterItem);

        initToolBar();          // Create Action Bar
        initNavigationDrawer();  // Create NavigationDrawer


        //-----------Drag and Drop with Item Touch Listener------------------------------------
        RecyclerItemCallback callback = new RecyclerItemCallback(m_adapterItem);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mainListView);
    }

    @Override
    public void onRecycleItemClick(long id) {

        // ArrayList<RadioStationItem> m_arrayItems
        Intent intent = new Intent(this, ChooseTimeActivity.class);
        Log.d("Look", "MainActivity-onRecycleItemClick: item id ->" + id);
        intent.putExtra(ChooseTimeActivity.GET_ITEM_ID_FOR_INTENT, id);
        startActivity(intent);

        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out); // set Activity Animation
    }

    //----------------------------------------------------------------------------------------------Init ToolBar
    public void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.root_layout);
        toolbar.setTitle(R.string.choose_title);

        //overflow Icon
        Drawable rightMenuIcon = ContextCompat.getDrawable(this,R.drawable.icon_right_menu);
        toolbar.setOverflowIcon(rightMenuIcon);

        //--------set Action Bar and  button  back---------
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    //----------------------------------------------------------------------------------------------initNavigationDrawer
    public void initNavigationDrawer(){

         DrawerLayout drawerMainPage = (DrawerLayout) findViewById(R.id.drawBar);
        ActionBarDrawerToggle drowerTogle = new ActionBarDrawerToggle(this, drawerMainPage, toolbar, R.string.app_name, R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.icon_action_bar_navigation_menu);
        drawerMainPage.addDrawerListener(drowerTogle);
        drowerTogle.syncState();
    }

    //----------------------------------------------------------------------------------------------onCreateOptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu,menu);

        //get menu item "Search"
        MenuItem menuItem = menu.findItem(R.id.action_search);
        if(menuItem == null){
            Log.e("Look","Error: MainActivity-onCreateOptionsMenu: menuItem is null (Search Menu item not found)");
            return false;
        }
        //set Search Line
        SearchView search= (SearchView) menuItem.getActionView();
        initSearch(search);

        return true;
    }

    //----------------------------------------------------------------------------------------------initSearch
    public void initSearch(SearchView search){

        if(search==null){
            return;
        }

        search.setMaxWidth(2129960);

        String strHintint = getResources().getString(R.string.search_in_action_bar);
        search.setQueryHint(strHintint);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                m_arrayItems.clear();
                m_arrayItems.addAll(m_RadioEngine.getItemsByString(newText));
                m_adapterItem.notifyDataSetChanged();
                Log.d("Look","MainActivity - onQueryTextSubmit ");
                return true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Look", "I work --> onStop");

        //Save user Radio stations position settings
        m_radioSettings.saveSettings(m_adapterItem.getUserListSettings(), this);

    }

    @Override
    protected void onDestroy() {
        Log.d("Look", "MainActivity onDestroy");
        m_playMusicService.pauseMusic();
        unbindService(serviceConnection);
        stopService(new Intent(this, PlayMusicService.class));

        super.onDestroy();
    }
}


