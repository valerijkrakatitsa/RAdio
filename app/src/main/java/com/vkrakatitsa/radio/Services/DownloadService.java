package com.vkrakatitsa.radio.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.vkrakatitsa.radio.Model.VKSong;
import com.vkrakatitsa.radio.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

    public static final String INTENT_EXTRA_LINK_KEY="GetIntentExtraLinkKey";
    public static final String INTENT_EXTRA_TITILE_KEY="GetIntentExtraTitleKey";
    public static final String INTENT_EXTRA_ARTIST_KEY="GetIntentExtraArtistKey";

    public ExecutorService m_executorService;
    private String m_strPath;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent==null){
            return -1;
        }

        Bundle extra = intent.getExtras();
        if(extra==null){
            return-1;
        }

        //Check External Storage access
        if (!isExternalStorageWritable()){
            showToast(getResources().getString(R.string.toast_externalStorage_not_available));
            return 0;
        }
        m_strPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        // Obtain download data
        String url =extra.getString(INTENT_EXTRA_LINK_KEY);
        String title= extra.getString(INTENT_EXTRA_TITILE_KEY);
        String artist = extra.getString(INTENT_EXTRA_ARTIST_KEY);
        Log.d("Look", this.getClass().getCanonicalName()+ " | url -"+url+"  | title -"+title);
        VKSong songItem = new VKSong(title, url, artist);

        //Create new Runnable object
        Runnable runnable = new DownLoadSong(songItem);

        //Start dowload queue with 1 runned thread
        if(m_executorService==null){
            m_executorService = Executors.newSingleThreadExecutor();
        }

        m_executorService.execute(runnable);


        return 0;
    }

    public class DownLoadSong implements Runnable{

        private VKSong m_songData;

        /**
         * @param songData - object with url,song title and song Artist
         */
        public  DownLoadSong(VKSong songData){
            m_songData=songData;
        }

        @Override
        public void run() {
            URL url;
            try{
                url=new URL(m_songData.getSongURL());
                HttpURLConnection connection =(HttpURLConnection) url.openConnection();
                connection.setReadTimeout(1000);
                connection.setConnectTimeout(1000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                if(connection.getResponseCode()!=200){
                    Log.d("Look",this.getClass().getCanonicalName()+ " run:  Response code is "+ connection.getResponseCode());
                    return;
                }
                InputStream inputStream = connection.getInputStream();

                //Output stream in download song
                FileOutputStream output = new FileOutputStream(m_strPath+m_songData.getSongArtist() + " - "+m_songData.getSongTitle()+".mp3");

                int byteRead = -1;

                byte[] buffer = new byte[4096];  // byte buffer wih 4mb

                while((byteRead=inputStream.read(buffer))!=-1){
                    output.write(buffer,0,byteRead);
                }

                //Close theads
                output.flush();
                inputStream.close();
                connection.disconnect();
                output.close();

                //Sign to user "Download Complete"
                String strMessage = "Download "+m_songData.getSongTitle()+ " complete";
                showToast(strMessage);

            } catch (MalformedURLException e) {
                Log.e("Look",this.getClass().getCanonicalName()+"\n \n Error: \n "+e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Look",this.getClass().getCanonicalName()+"\n \n Error: \n "+e.getMessage());
                e.printStackTrace();
            }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //----------------------------------------------------------------------------------------------showToast
    /**
     * Show Toast message in UI Thread and stop service
     * @param strMessage - message to show
     */
    private void showToast(final String strMessage ){
        //Show "Dowbload complete" toast in ui thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(DownloadService.this.getApplicationContext(),strMessage,Toast.LENGTH_SHORT).show();
            }
        });
        //stop Service
        stopSelf();
    }

    //----------------------------------------------------------------------------------------------isExternalStorageWritable

    /**
     * Checks if external storage is available for read and write
     * @return  true if external storage available. false if not.
     */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
