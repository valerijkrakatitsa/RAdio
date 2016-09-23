package com.vkrakatitsa.radio.ToolsAndConstants.ConnectRadioObject;

import com.vkrakatitsa.radio.ToolsAndConstants.Connects.BaseConnection;

public class RockFmObject extends BaseFmObject {

    public static final String ROCK_FM = "ROCK_FM";


    private static String link ="http://radio.i.ua/radio.roks/archive/";
    private static String tagForTimeFirst = ">";
    private static String tagForTimeSecond = "<";
    private static String tagForSongFirst=">";
    private static String tagGorSongSecond="</a>";
    private static String charset = "windows-1251";
    private static String tagLineTime="class=\"time\"";
    private static String tagLineSong="class=\"executor\"";
    BaseConnection baseConnection;


    public RockFmObject(){
        super();
    }


    public static String createLink(String date){
        String digit = date.substring(0,2);
        return link+digit;
    }

}
