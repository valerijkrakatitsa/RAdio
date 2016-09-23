package com.vkrakatitsa.radio.ToolsAndConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.Model.RadioTagsItem;
import com.vkrakatitsa.radio.R;

import java.util.ArrayList;

public class RadioListSettings {

    public static final String FIRST_START="FIRST_START";

    Context context;

    private String hit;
    private String kiss;
    private String rocks;
    private String nrj;
    private String russkoe;
    private String retro;
    private String gti;
    private String dj;
    private String bumblebee;
    private String nashe;
    private String melodia;
    private String bisnes;
    private String mfm;
    private String shanson;
    private String lux;
    private String friday;
    private String relax;
    private String autoradio;

    //----------------------------------------------------------------------------------------------Constructor(Context)
    public RadioListSettings (Context context){



        Log.d("Error","I work  -  -  -  RadioList");

        if(isFirstStart(context)){
            setContext(context);
            initStrings();
            Log.d("Look", "This is First Start");
            createNewRadioList();
            createNewTagList();
        }else {
            Log.d("Look", "This isn`t the First Start");
        }

    }

    public void setContext(Context context){
        this.context=context;
    }

    public Context getContext(){
        return context;
    }

    //----------------------------------------------------------------------------------------------getSharedPreferences
    public static  SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // ---------------------------------------------------------------------------------------------isFirstStart
    private  boolean isFirstStart(Context context){

        SharedPreferences sPref = getSharedPreferences(context);
        SharedPreferences.Editor editor = sPref.edit();
        boolean bres = sPref.getBoolean(FIRST_START,true);

        if(bres){
            editor.putBoolean(FIRST_START,false);
            editor.apply();
        }
        return bres;
    }

    //----------------------------------------------------------------------------------------------initStrings
    private void  initStrings(){
         hit = getContext().getResources().getString(R.string.hit);
         kiss= context.getResources().getString(R.string.kiss);
         rocks= context.getResources().getString(R.string.rocks);
         nrj= context.getResources().getString(R.string.nrj);
         russkoe= context.getResources().getString(R.string.russkoe);
         retro= context.getResources().getString(R.string.retro);
         gti= context.getResources().getString(R.string.gti);
         dj= context.getResources().getString(R.string.dj);
         bumblebee= context.getResources().getString(R.string.bumblebee);
         nashe= context.getResources().getString(R.string.nashe);
         melodia= context.getResources().getString(R.string.melodia);
         bisnes= context.getResources().getString(R.string.bisnes);
         mfm= context.getResources().getString(R.string.mfm);
         shanson= context.getResources().getString(R.string.shanson);
         lux= context.getResources().getString(R.string.lux);
        friday= context.getResources().getString(R.string.pjatnizza);
        relax = getContext().getResources().getString(R.string.relax);
        autoradio = getContext().getResources().getString(R.string.avto);
    }

    //----------------------------------------------------------------------------------------------createNewRadioList
    // fill table with Radio stations
    private void createNewRadioList(){
        int i =0 ;
        RadioItemEngine engine  = new RadioItemEngine(getContext());
        engine.insert(new RadioStationItem(R.drawable.logo_hit,hit,i++,"http://radio.i.ua/hit.fm/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_kiss,kiss,i++,"http://radio.i.ua/kiss.fm/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_radio_rocks,rocks,i++,"http://radio.i.ua/radio.roks/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_nrj,nrj,i++,"http://radio.i.ua/nrj/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_russkoe_radio,russkoe,i++,"http://radio.i.ua/russkoe.radio/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_retro_fm,retro,i++,"http://radio.i.ua/retro.fm/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_gti,gti,i++,"http://radio.i.ua/gtiradio.ru/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_djfm,dj,i++,"http://radio.i.ua/dj.fm/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_bunmble_bee,bumblebee,i++,"http://radio.i.ua/bumblebeefm.ru/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_nashe_radio,nashe,i++,"http://radio.i.ua/nashe.radio/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_melodia,melodia,i++,"http://radio.i.ua/melodiya/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_business,bisnes,i++,"http://radio.i.ua/business.fm/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_mfm,mfm,i++,"http://gr.kh.ua/playlist//list.html",2));
        engine.insert(new RadioStationItem(R.drawable.logo_shanson,shanson,i++,"http://radioshanson.fm/info/plyei-list-dnya-",4));
        engine.insert(new RadioStationItem(R.drawable.logo_lux,lux,i++,"http://www.moreradio.org/playlist_radio/radio_lux_fm/",1));
        engine.insert(new RadioStationItem(R.drawable.logo_pyatnitsa,friday,i++,"http://radio.i.ua/radiopyatnica/archive/",0));
        engine.insert(new RadioStationItem(R.drawable.logo_relax,relax,i++,"http://www.radiorelax.ua/playlist/",3));
        engine.insert(new RadioStationItem(R.drawable.logo_avtoradio,autoradio,i++,"http://www.moreradio.org/playlist_radio/avtoradio_ukraine/",1));
    }

    //----------------------------------------------------------------------------------------------createNewTagList
    // fill table with Tags
    public void createNewTagList(){
        RadioTagEngine engine = new RadioTagEngine(getContext());
        engine.insert(new RadioTagsItem(0, "class*list_underlined radio_program", "li", "class*time" ,"a","class*executor",  "windows-1251"));
        engine.insert(new RadioTagsItem(1,"id*playlist","itemprop*track","class*ffDigits time","class*track shFloat shLineNowrap","itemprop*name","windows-1251"));
        engine.insert(new RadioTagsItem(2,"","","","","","UFF-8"));
        engine.insert(new RadioTagsItem(3,"","class*row song-list","class*time songTime","class*artist","class*song","UTF-8"));
        engine.insert(new RadioTagsItem(4,"td","td","","","","UTF-8"));
    }

    //----------------------------------------------------------------------------------------------saveSettings
    // save radio stations positions in thread
    public void saveSettings(final ArrayList<RadioStationItem> array,final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Look", "Save user Settings");
                RadioItemEngine engine = new RadioItemEngine(context);
                int i =0;   //station position in radio Collection array
                for (RadioStationItem item : array) {

                    item.setPosition(i);        //set position to each element
                    engine.update(item);
                    i++;
                }
            }
        }).start();

    }

}
