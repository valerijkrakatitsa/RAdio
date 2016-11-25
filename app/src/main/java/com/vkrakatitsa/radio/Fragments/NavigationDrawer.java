package com.vkrakatitsa.radio.Fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.methods.VKApiAudio;
import com.vk.sdk.util.VKUtil;
import com.vkrakatitsa.radio.Activities.ChooseTimeActivity;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.Services.DownloadService;
import com.vkrakatitsa.radio.Services.PlayMusicService;

public class NavigationDrawer extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String START_MORE_RESULT_FRAGMENT_KEY="StartMoreResultFragment";

    private ImageButton m_buttonPlayStop;
    private TextView m_txtTime;
    private TextView m_txtSong;
    private SeekBar m_barProgress;
    private Messenger m_fromService = new Messenger(new NavigationHandler());
    private PlayMusicService m_playMusicService;
    private boolean m_bIsPlaying = false;
    private IBinder m_binder=null;
    private Handler m_Handler;      // handler for update musi play progress
    private View m_addToVKSound;
    private View m_downloadMusic;
    private View m_searchMore;
    private LinearLayout m_vkAuthorization;

    // Service Connection for Service Bind
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            Log.d("Look", this.getClass().getCanonicalName()+" OnService connected m_binder is null");

            m_playMusicService = ((PlayMusicService.ThisBinder)iBinder).getService();

            //pass to service link on messenger for receive messages about music play progress
            m_playMusicService.setMessenger(m_fromService);

            Log.d("Look", this.getClass().getCanonicalName()+"Service connection : onServiceConnected binder is null "+ (iBinder==null));
            updatePlayerData();
            m_binder = iBinder;
            updatePlayerData();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("Look", this.getClass().getCanonicalName()+"Service connection : onServiceDisconnected ");
            m_playMusicService=null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_navigation_drawer,container,false);

        m_buttonPlayStop = (ImageButton) view.findViewById(R.id.drawerButtonStopAndPlay);
        m_barProgress=(SeekBar)view.findViewById(R.id.drawerProgress);
        m_txtSong = (TextView)view.findViewById(R.id.drawerSongName);
        m_txtTime=(TextView)view.findViewById(R.id.drawerSongTimeLeft);

        m_vkAuthorization= (LinearLayout)view.findViewById(R.id.VKLog) ;
        m_vkAuthorization.setOnClickListener(this);

        LinearLayout senMail= (LinearLayout)view.findViewById(R.id.send_mail) ;
        senMail.setOnClickListener(this);

        m_downloadMusic=view.findViewById(R.id.downloadSongDrawerLayout);
        m_downloadMusic.setOnClickListener(this);

        m_addToVKSound=view.findViewById(R.id.AddtoFavoriteVk);
        m_addToVKSound.setOnClickListener(this);

        m_searchMore =view.findViewById(R.id.showMoreResults);
        m_searchMore.setOnClickListener(this);

        //set unnecessary buttons
        m_searchMore.setEnabled(false);
        m_addToVKSound.setEnabled(false);
        m_downloadMusic.setEnabled(false);

        m_buttonPlayStop.setOnClickListener(this);

        return view;
    }

    //----------------------------------------------------------------------------------------------onActivityCreated
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Look", this.getClass().getCanonicalName()+" onActivityCreated");

        Intent intent = new Intent(getContext(), PlayMusicService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        m_Handler = new Handler();
        m_barProgress.setOnSeekBarChangeListener(this);

    }

    //----------------------------------------------------------------------------------------------onStart
    @Override
    public void onStart() {
        Log.d("Look",this.getClass().getCanonicalName()+"  NavigationDrawer on start");
        super.onStart();
        if(m_playMusicService!=null) {
            updatePlayerData();
        }
    }

    //----------------------------------------------------------------------------------------------UpdatePlayerDAta
    //Display state of play song when Song is added to player
    public void updatePlayerData(){

        Log.d("Look","NavigationDrawer - update Drawer start");

        //Change state button  Play
        if(m_playMusicService!=null) {

            if (m_playMusicService.isplaying()) {
                m_bIsPlaying = true;
                m_buttonPlayStop.setImageResource(R.drawable.ic_pause);
            } else {
                m_buttonPlayStop.setImageResource(R.drawable.ic_play_arrow_black);
                m_bIsPlaying = false;
            }

            // Update song title line
            if (m_playMusicService.getSongTitle() != null) {
                m_txtSong.setText(m_playMusicService.getArtist()+" - "+m_playMusicService.getSongTitle());
            }

            //Set Song Duration
            m_barProgress.setMax(m_playMusicService.getDuration());
            updateProgressBar();

            //set Enable buttons for work with song
            if(m_playMusicService.isMusicAvalilable()) {
                m_downloadMusic.setEnabled(true);
                m_addToVKSound.setEnabled(true);
                m_searchMore.setEnabled(true);

            }
        }
    }

    //----------------------------------------------------------------------------------------------FormatTimeDuration
    /**
     * Format display song time progress. Update every second
     * @param ntimeInSec - time progress in seconds
     * @return String line - formated time (3:31)
     */
    public String formatTimeDuration(int ntimeInSec){

        int ntimeMinutes = 0;
        String strTimeDuration;

        while (ntimeInSec >= 60) {
            ntimeInSec = ntimeInSec - 60;
            ntimeMinutes++;
        }

        if (ntimeInSec == 0) {
            strTimeDuration = "" + ntimeMinutes + ":00";
        } else if (ntimeInSec < 10) {
            strTimeDuration = "" + ntimeMinutes + ":0" + ntimeInSec;
        } else {
            strTimeDuration = "" + ntimeMinutes + ":" + ntimeInSec;
        }

        return strTimeDuration;
    }

    //----------------------------------------------------------------------------------------------onClick
    @Override
    public void onClick(View view) {

        Animation animPressed ;
        Animation animReleased ;

        switch (view.getId()){

            case R.id.drawerButtonStopAndPlay:

                animPressed = loadAnimation(R.anim.button_play_pressed);
                animReleased = loadAnimation(R.anim.button_play_released);

                if(m_playMusicService.isplaying() ){

                    //pause music
                    Log.d("Look","NavigationDrawer - OnClick - isPlaying->(true ) "+m_playMusicService.isplaying());
                    m_playMusicService.pauseMusic();
                    view.startAnimation(animPressed); // start Animation button pressed
                    m_buttonPlayStop.setImageResource(R.drawable.ic_play_arrow_black);
                    view.startAnimation(animReleased);// start Animation button released
                    m_bIsPlaying = false;
                    m_txtSong.setText(m_playMusicService.getArtist()+"-"+m_playMusicService.getSongTitle());// set Song title

                }else if(!m_playMusicService.isplaying()){
                    if(m_playMusicService.resumeMusic()) {

                        //start Playing musicstart Playing music
                        view.startAnimation(animPressed);// start Animation button pressed
                        m_buttonPlayStop.setImageResource(R.drawable.ic_pause);
                        view.startAnimation(animReleased);// start Animation button released
                        m_bIsPlaying = true;
                        m_txtSong.setText(m_playMusicService.getArtist()+"-"+m_playMusicService.getSongTitle());// set Song title
                    }
                }

                break;
            case R.id.AddtoFavoriteVk:

                //Start Press Button Animation
                animPressed = loadAnimation(R.anim.navigation_item_animation_pressed);
                animReleased= loadAnimation(R.anim.navigation_item_animation_released);
                animPressed.start();
                animReleased.start();
                Log.d("Look",this.getClass().getCanonicalName()+"  AddtoFavoriteVk");

                startAddSongVKRequest();
                Toast.makeText(getContext(),"Add to VkList",Toast.LENGTH_SHORT).show();
                break;

            case R.id.downloadSongDrawerLayout:

                Intent intent=prepareDownloadService();
                //start DownloadMusic Service
                if(intent!=null){
                    getActivity().startService(intent);
                }

                break;

            // Buttonn ShowMore Results in Navigation Drawer
            case R.id.showMoreResults:

                Intent moreResultsIntent = new Intent(getContext(), ChooseTimeActivity.class);
                moreResultsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // start of new task

                //Prepare intent extra for show more Result
                String songTitle = m_playMusicService.getArtist()+" - "+m_playMusicService.getSongTitle();

                //Put data in intent ectra
                moreResultsIntent.putExtra(ShowMoreResults.RESULT_ARRAY_VK_SEARCH_TITLE_KEY,songTitle);
                moreResultsIntent.putExtra(ShowMoreResults.RESULT_ARRAY_VK_SEARCH_ARRAY_KEY,m_playMusicService.getResultVKSearch());
                moreResultsIntent.putExtra(START_MORE_RESULT_FRAGMENT_KEY,23); // Key for Start ShowMoreResults argument!=-1

                startActivity(moreResultsIntent);
                break;

            case R.id.VKLog:
                // Vk Authorization
                VKSdk.login(getActivity(),VKScope.AUDIO);

                //show Fingersckript
                String arrKey[] = VKUtil.getCertificateFingerprint(getContext(),"com.vkrakatitsa.radio");
                String fingerKey ="";
                for(String s : arrKey){
                    fingerKey+=s;
                }
                Log.d("Look","Fingerscript > "+fingerKey);
                break;

            case R.id.send_mail:

                Log.d("Look",this.getClass().getCanonicalName()+" Send Mail");
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setData(Uri.parse("mailto:"));
                mailIntent.setType("text/plain");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, "valerijkrakatitsa@gmail.com");
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Radio App");
                startActivity(Intent.createChooser(mailIntent, "Send Your Propose"));

                break;
        }
    }

    //----------------------------------------------------------------------------------------------updateProgressBar
    public void updateProgressBar(){
        m_Handler.postDelayed(m_UpdateTimeTask,100); // update progress every 100 milliseconds
    }
    //-------------------------------------------------------------------------- -------------------- mUpdateTimeTask Runnable;
    private Runnable m_UpdateTimeTask= new Runnable() {
        @Override
        public void run() {
            String strTime = formatTimeDuration(m_playMusicService.getCurentPosition()/1000);
            m_txtTime.setText(strTime);
            m_barProgress.setProgress(m_playMusicService.getCurentPosition()/1000);
            m_Handler.postDelayed(this, 100);
        }
    };

    //----------------------------------------------------------------------------------------------startAddSongVKRequest
    //Add song in VK playlist
    public void startAddSongVKRequest(){

        //get song id (now played)
        int audioID;
        if((audioID=m_playMusicService.getAudioId())==-1){
            return ;
        }
        //get played owner id (now played)
        int ownerID;
        if((ownerID=m_playMusicService.getOwnerID())==-1){
            return;
        }

        //send VK request to add song in playlist
        VKRequest request =new  VKApiAudio().add(VKParameters.from("audio_id",audioID, VKApiConst.OWNER_ID, ownerID));
        request.start();
    }

    // ---------------------------------------------------------------------------------------------prepareDownloadService
    /**
     *prepare data for Downloading song
     * @return  intetn-to start DownloadService
     */
    public Intent prepareDownloadService (){

        //get downloaded song title
        String title;
        if((title= m_playMusicService.getSongTitle())==null){
            return null;
        }

        //get downloaded song artist
        String artist;
        if((artist=m_playMusicService.getArtist())==null){
            return null;
        }

        //getSong url
        String url ;
        if((url=m_playMusicService.getUrl())==null){
            return null ;
        }

        Toast.makeText(getContext(), "Download "+title+ " start", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DownloadService.class);
        intent.putExtra(DownloadService.INTENT_EXTRA_LINK_KEY,url);
        intent.putExtra(DownloadService.INTENT_EXTRA_TITILE_KEY,title);
        intent.putExtra(DownloadService.INTENT_EXTRA_ARTIST_KEY,artist);

        return intent;
    }

    //----------------------------------------------------------------------------------------------loadAnimation
    public Animation loadAnimation( @AnimRes int nAnimId){
        Animation anim = new AnimationUtils().loadAnimation(getContext(),nAnimId);
        return anim;
    }

    //----------------------------------------------------------------------------------------------onProgressChanged
    // method from OnSeekBarChangeListener Interface
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    //----------------------------------------------------------------------------------------------onStartTrackingTouch
    // method from OnSeekBarChangeListener Interface
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        m_Handler.removeCallbacks(m_UpdateTimeTask);        //stop listen  change play music progress while progress changing
    }

    //----------------------------------------------------------------------------------------------onStopTrackingTouch
    // method from OnSeekBarChangeListener Interface
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        m_Handler.removeCallbacks(m_UpdateTimeTask);    //stop listen  change play music progress
        Log.d("SeekLook", "NavigationDrawer  -  onStopTracking : seekBar Progres->"+seekBar.getProgress());
        m_playMusicService.seekTo(seekBar.getProgress());   //rewind song to choosen time
        updateProgressBar();
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(serviceConnection);
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------class Navigation Handler
    // handler sign when song added in player
    public class NavigationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            if(msg.what==PlayMusicService.UPDATE_DATA_KEY){
                updatePlayerData();
            }
        }
    }
}
