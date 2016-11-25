package com.vkrakatitsa.radio.Services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.io.IOException;
import java.io.Serializable;

public class PlayMusicService extends Service implements Serializable{

    public static final int UPDATE_DATA_KEY=1121;

    private MediaPlayer m_Player;
    private Messenger m_ClientMessenger;
    private ThisBinder binder = new ThisBinder();
    private VKApiAudio m_currentSong;
    private VKList<VKApiAudio> m_vkList;

    public class ThisBinder extends Binder{

        /**
         *
         * @return link on this Service;
         */
        public PlayMusicService getService (){
            return PlayMusicService.this;
        }
    }

    //----------------------------------------------------------------------------------------------setMessenger
    //receive NavigationDrawer messenger. Send messages when music added in Player
    public void setMessenger(Messenger messenger){
        m_ClientMessenger = messenger;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    //----------------------------------------------------------------------------------------------onBind
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AudioManager mAudioManager =(AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
        return binder;
    }

    //----------------------------------------------------------------------------------------------onStartCommand
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //----------------------------------------------------------------------------------------------startMusic
    private void startMusic(String m_srtURL){

        if(m_Player != null){
            m_Player.stop();
            m_Player.release();
            m_Player=null;
        }
        try {

            if(m_srtURL==null){
                Log.e("Look","error:\n PlayMusicService m_url is null");
                return;
            }
            m_Player = new MediaPlayer();
            m_Player.setDataSource(m_srtURL);
            m_Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            m_Player.prepareAsync();

            m_Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    sendMessage(UPDATE_DATA_KEY);   // send message about player start play
                }
            });

        } catch (IOException e) {
            Log.e("Look", "Error in service : "+e.getMessage());
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------pauseMusic
    public void pauseMusic(){
        if(m_Player !=null && m_Player.isPlaying()){
            Log.d("Look","PlayMusicService - pauseMusic - work");
            m_Player.pause();
        }
    }

    //----------------------------------------------------------------------------------------------resumeMusic
    public boolean  resumeMusic(){
        if(m_Player !=null&&!m_Player.isPlaying()){

            Log.d("Look","PlayMusicService - resumeMusic - work");
            m_Player.start();
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------createURL

    /**
     *Method search song by title and artist and start playing song
     *
     * @param strSearchLine - title and artist of choosen song
     */
    public void createURL (String strSearchLine) {

        //start search request with params
        // q- search line
        // AUTO_COMPLETE- autocorrection of searchLine
        // Sort - 2(sort by population)
        VKRequest songFile = VKApi.audio().search(VKParameters.from(VKApiConst.Q, strSearchLine, VKApiConst.AUTO_COMPLETE,1, VKApiConst.SORT,2));

        Log.d("Look",this.getClass().getCanonicalName()+" searchLine: url is ->"+strSearchLine);
        //Listener receive Response
        songFile.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                //get List of Response songs
                m_vkList = (VKList<VKApiAudio>) response.parsedModel;

                //start playing first songof response if list not empty
                if(m_vkList.size()>0) {
                    //start play song
                    startPlaSongByAudioItem( m_vkList.get(0));
                }else{
                    // if Search response is empty
                    Toast.makeText(getApplicationContext(), "Song not found",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //----------------------------------------------------------------------------------------------getResultVKSearch

    /**
     *Return search result List
     */
    public VKList<VKApiAudio> getResultVKSearch (){
        return m_vkList;
    }

    //----------------------------------------------------------------------------------------------startPlaSongByAudioItem
    /**
     * Start playing song
     * @param audio - song item
     */
    public void startPlaSongByAudioItem(VKApiAudio audio){
            m_currentSong = audio;
            startMusic(m_currentSong.url);
    }

    //----------------------------------------------------------------------------------------------sendMessage
    //send message to navigation drawer
    public void sendMessage(int nWhat) {
        try {

            Message msg = Message.obtain(null, nWhat);
            m_ClientMessenger.send(msg);

        } catch (RemoteException e) {
            Log.e("Look", "Error \n PlayMusicService - sendMessage \n " + e.getMessage());
            e.printStackTrace();
        }
    }



    //----------------------------------------------------------------------------------------------getTitle;
    public String getSongTitle(){
        if(m_currentSong !=null){
            return m_currentSong.title;
        }
        return null;
    }

    //----------------------------------------------------------------------------------------------getAudioId
    public int getAudioId(){
        if(m_currentSong !=null){
            return m_currentSong.id;
        }
        return -1;
    }

    //----------------------------------------------------------------------------------------------isMusicAvalilable
    public boolean isMusicAvalilable(){

        return m_currentSong !=null;

    }

    //----------------------------------------------------------------------------------------------getOwnerID
    public int getOwnerID(){
        if(m_currentSong !=null){
            return m_currentSong.owner_id;
        }
        return -1;
    }

    //----------------------------------------------------------------------------------------------getArtist;
    public String getArtist(){
        if(m_currentSong !=null){
            return m_currentSong.artist;
        }
        return null;
    }
    //----------------------------------------------------------------------------------------------getDuration
    public int getDuration(){
        if(m_currentSong !=null){
            return m_currentSong.duration;
        }
        return-1;
    }

    //----------------------------------------------------------------------------------------------isPlaying
    public boolean isplaying (){

       return (m_Player !=null)&& m_Player.isPlaying() ;
    }
//--------------------------------------------------------------------------------------------------getCurentDuration

    /**
     * get current play song Time
     * @return time in MilliSeconds
     */
    public int getCurentPosition(){
        if(m_Player !=null){
            return m_Player.getCurrentPosition();
        }
        return-1;
    }
    //----------------------------------------------------------------------------------------------seekTo

    /**
     *  Rewind song to time
     * @param nSec - time to rewind in milliseconds
     */
    public void seekTo(int nSec){
        if(m_Player !=null){
            int ntime = nSec*1000;
            Log.d("Look","Service - seekTo - > int nSec->"+nSec);
            m_Player.seekTo(ntime);
        }
    }
    //----------------------------------------------------------------------------------------------getUrl

    /**
     * get URL current playing song for DownloadService
     * @return URL of current playing song
     */
    public String getUrl(){
        if (m_currentSong != null) {
         return m_currentSong.url;
        }
        return null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("Look", this.getClass().getCanonicalName()+" onUnbind ");
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onDestroy() {
        Log.d("Look", this.getClass().getCanonicalName()+" onDestroy ");
        super.onDestroy();
    }
}
