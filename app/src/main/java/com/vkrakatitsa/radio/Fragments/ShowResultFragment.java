package com.vkrakatitsa.radio.Fragments;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vkrakatitsa.radio.Adapters.ListAdapter;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.Services.PlayMusicService;

import java.util.LinkedHashMap;

public class ShowResultFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String GET_ARRAY_RESULT_KEY = "GetArrayResultKey";
    public static final String GET_STRING_TIME_FROM_KEY="FlagTagStringTimeFrom";
    public static final String GET_STRING_TIME_TO_KEY="FlagTagStringTimeTo";
    public static final String GET_STRING_DATE_KEY="FlagTagStringDate";
    public static final String GET_STRING_RADIO_NAME_KEY="GET_STRING_RADIO_NAME_KEY";
    public static final String GET_BROADCAST_INTENT_KEY=" com.vkrakatitsa.radio.Fragments.GET_BROADCAST_INTENT_KEY_SHOW_SNACKBAR";

    LinkedHashMap<String, String> arrResult;
    private ListView listView;
    private ListAdapter adapter;
   // private boolean m_isBound = false;
    private TextView m_txtDate;
    private TextView m_txtTimeFrom;
    private TextView m_txtTimeTo;
    private String m_strRadioName;
    private Snackbar snackbar;
    private PlayMusicService m_playMusicService;

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            m_playMusicService = ((PlayMusicService.ThisBinder) iBinder).getService();
            //m_isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            m_playMusicService = null;
            // m_isBound=false;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_result, container, false);


        m_txtDate =(TextView) view.findViewById(R.id.showDateInResultFragment); // TextView Show choose date and time
        m_txtTimeFrom =(TextView) view.findViewById(R.id.showTimeFromInResultFragment); // TextView Show choose date and time
        m_txtTimeTo =(TextView) view.findViewById(R.id.showTimeToInResultFragment); // TextView Show choose date and time


        //Получение результата Поиска (LinkedHashMap<String, String>)
        if(savedInstanceState!=null){
            arrResult = (LinkedHashMap<String, String>)savedInstanceState.get(GET_ARRAY_RESULT_KEY);
            m_strRadioName = savedInstanceState.getString(GET_STRING_RADIO_NAME_KEY);
            initHeader(savedInstanceState);

        }else if(getArguments()!=null){
            arrResult = (LinkedHashMap<String, String>)getArguments().get(GET_ARRAY_RESULT_KEY);
            m_strRadioName = getArguments().getString(GET_STRING_RADIO_NAME_KEY);
            initHeader(getArguments());
        }else
            Log.e("Look","ShowResultFragments-OnCreateView : savedInstanceState  and Arguments  are null ");

        listView = (ListView) view.findViewById(R.id.showResultListView);
        adapter = new ListAdapter(getContext(), arrResult);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start Service
        Intent intent = new Intent(getContext(), PlayMusicService.class);
        getActivity().startService(intent);
        if(m_playMusicService==null) {
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    //-----------------------------------------------------------------------------------------------------onActivityResult
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Change Toolbar Title
        Log.d("Look", this.getClass().getCanonicalName() + " getActionBar is null ->" + (getActivity().getActionBar() == null) + "  | RadioName is null ->" + (m_strRadioName == null));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(m_strRadioName);
    }

    //----------------------------------------------------------------------------------------------initHeader
    public void initHeader(Bundle bundle){

        String strDate = bundle.getString(GET_STRING_DATE_KEY);
        String strTimeFrom = bundle.getString(GET_STRING_TIME_FROM_KEY);
        String strTimeTo = bundle.getString(GET_STRING_TIME_TO_KEY);
        m_txtDate.setText(strDate);
        m_txtTimeFrom.setText(strTimeFrom);
        m_txtTimeTo.setText(strTimeTo);

    }

    //----------------------------------------------------------------------------------------------OnItemClick
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //create search line
        TextView searchSinger = (TextView) view.findViewById(R.id.resultItemSinger);
        TextView searchSong = (TextView) view.findViewById(R.id.resultItemSong);
        String searchLine = searchSinger.getText().toString()+ " - "+searchSong.getText().toString();
        searchLine=searchLine.replaceAll("^","");

        //start PlayMusic service
        Intent intent = new Intent(getContext(), PlayMusicService.class);
        getActivity().startService(intent);

        //Start play music if LogIn-true
        if(checkVkLogin()) {
            m_playMusicService.createURL(searchLine);
        }
        //show Snackbar with search in browser
        showSnackbar(searchLine);
    }

    //----------------------------------------------------------------------------------------------onSaveInstanceState
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(GET_ARRAY_RESULT_KEY,arrResult);
        outState.putString(GET_STRING_DATE_KEY, m_txtDate.getText().toString());
        outState.putString(GET_STRING_TIME_FROM_KEY, m_txtTimeFrom.getText().toString());
        outState.putString(GET_STRING_TIME_TO_KEY, m_txtTimeTo.getText().toString());
        outState.putString(GET_STRING_RADIO_NAME_KEY, m_strRadioName);
    }

    //----------------------------------------------------------------------------------------------showSnackbar
    public void showSnackbar (final String searchLine){

        snackbar=Snackbar.make(listView, "Google", Snackbar.LENGTH_SHORT);
        snackbar.setAction("Search", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, searchLine); // query contains search string
                startActivity(intent);
            }
        });
        snackbar.show();

    }
   // ----------------------------------------------------------------------------------------------checkVkLogin

    /**
     *  Check can play song
     * @return true - if Login , false - is not Login
     */
    public boolean checkVkLogin(){

        boolean isLogin = VKSdk.wakeUpSession(getContext(), new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {

                switch (res) {
                    case LoggedOut:
                        Toast.makeText(getContext(), "You need to Login Vk To listen music.Sory(", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onError(VKError error) {

            }
        });
        return isLogin;
    }

    //----------------------------------------------------------------------------------------------onDestroyView
    @Override
    public void onDestroyView() {
        if(snackbar!=null){
            snackbar.dismiss();
        }
        getActivity().unbindService(serviceConnection);
        super.onDestroyView();
    }
}
