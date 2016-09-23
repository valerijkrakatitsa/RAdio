package com.vkrakatitsa.radio.ToolsAndConstants.ConnectRadioObject;

import android.util.Log;

public class BaseFmObject {

    private String link ="";
    private String tagForTimeFirst = "";
    private String tagForTimeSecond = "";
    private String tagForSongFirst="";
    private String tagForSongSecond ="";
    private String charset = "";
    private String tagLineTime="";
    private String tagLineSong="";


    public BaseFmObject(){
    }

    public String createtLink(String date){

        String digit = date.substring(0,2);
        Log.d("error", "link is --> "+link);
        return link+digit;
    }

    private int convertTime(String time){
        String digitTime = time.replace(":","");
        return Integer.getInteger(digitTime);
    }


    public String getTimeTagFrom(){
        return tagForTimeFirst;
    }

    public String getTimeTagTo(){
        return tagForTimeSecond;
    }

    public String getLink(String strDate){

        return createtLink(strDate);
    }

    public String getCharset(){
        return charset;
    }

    public String getTagLineTime(){
        return tagLineTime;
    }
    public String getTagLineSong(){
        return tagLineSong;
    }
    public String getTagForSongSecond() {
        return tagForSongSecond;
    }

    public String getTagForSongFirst() {
        return tagForSongFirst;
    }



}
