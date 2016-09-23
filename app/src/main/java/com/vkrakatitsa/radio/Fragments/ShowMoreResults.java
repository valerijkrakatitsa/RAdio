package com.vkrakatitsa.radio.Fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
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

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;
import com.vkrakatitsa.radio.Adapters.MoreSearchResultAdapter;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.Services.PlayMusicService;

public class ShowMoreResults extends Fragment implements AdapterView.OnItemClickListener {

    public static final String RESULT_ARRAY_VK_SEARCH_ARRAY_KEY="ResultArrayVKSearchKey";
    public static final String RESULT_ARRAY_VK_SEARCH_TITLE_KEY="ResultTitleVKSearchKey";

    private VKList<VKApiAudio> m_arrAudio;
    private String m_strTitle;
    private ListView m_resultList;
    private PlayMusicService m_musicService;

    private ServiceConnection m_serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            m_musicService = ((PlayMusicService.ThisBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            m_musicService=null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_more_results,container,false);
        m_resultList= (ListView)view.findViewById(R.id.showMoreResultListView);
        TextView txtTitle= (TextView)view.findViewById(R.id.showSongTitle) ;

        // Restore saved instance if not null
        if(savedInstanceState!=null){
            m_arrAudio=(VKList<VKApiAudio>)savedInstanceState.getParcelable(RESULT_ARRAY_VK_SEARCH_ARRAY_KEY);
            m_strTitle=savedInstanceState.getString(RESULT_ARRAY_VK_SEARCH_TITLE_KEY);
            Log.d("Look",this.getClass().getCanonicalName()+" Saved instance !=null. m_arrAudio is null ->"+(m_arrAudio==null));
        }else{
            m_arrAudio=(VKList<VKApiAudio>)getArguments().getParcelable(RESULT_ARRAY_VK_SEARCH_ARRAY_KEY);
            m_strTitle=getArguments().getString(RESULT_ARRAY_VK_SEARCH_TITLE_KEY);
        }
        Log.d("Look",this.getClass().getCanonicalName()+" TitleText->"+m_strTitle);
        txtTitle.setText(m_strTitle);
        return view;
    }

    //----------------------------------------------------------------------------------------------onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set ACtion Bar Title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.actionBarTitleSearchMoreResults));

        if(m_arrAudio==null){
            Toast.makeText(getContext(), getContext().getResources().getString(R.string.strEmptySearchREsult),Toast.LENGTH_SHORT).show();
            return;
        }
        MoreSearchResultAdapter adapter = new MoreSearchResultAdapter(getContext(),m_arrAudio);
        m_resultList.setAdapter(adapter);
        m_resultList.setOnItemClickListener(this);

        //Bind Service
        Intent intent = new Intent(getContext(),PlayMusicService.class);
        if(m_musicService==null) {
            getActivity().bindService(intent, m_serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

//--------------------------------------------------------------------------------------------------onItemClick
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(m_musicService==null){
            return;
        }
        // start playing choosen song
        VKApiAudio audio = m_arrAudio.get(i);
        m_musicService.startPlaSongByAudioItem(audio);
    }

//--------------------------------------------------------------------------------------------------onSaveInstance
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RESULT_ARRAY_VK_SEARCH_ARRAY_KEY,m_arrAudio);
        outState.putString(RESULT_ARRAY_VK_SEARCH_TITLE_KEY,m_strTitle);
    }
}
