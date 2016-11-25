package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.content.Context;
import android.util.Log;

import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.Model.RadioTagsItem;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class Group1Connection extends BaseConnection {

    private RadioTagsItem fmStationTags;
    private String link;

    public Group1Connection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {
        super(context, obj, strDate, strTimeFrom, strTimeTo);



        RadioTagEngine engine = new RadioTagEngine(context);

        fmStationTags = engine.getItemById(obj.getTagType());

        link = createLink(obj.getLink(),strDate);
    }

    public void createNewConnection(){

        try {
            URL url = new URL(link);

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie","AllTrackRadio=563");
            connection.setRequestProperty("User-Agent","\"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0\"");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            CleanerProperties properties = new CleanerProperties();
            properties.setCharset(fmStationTags.getCharset());
            HtmlCleaner cleaner = new HtmlCleaner(properties);
            TagNode rootNode = cleaner.clean(connection.getInputStream());

            String playList = fmStationTags.getPlayListTag();   // 2 values to find PlayList Element
            String attName= playList.substring(0, playList.indexOf('*'));    // attribute name to get playListNode
            String attValue=playList.substring(playList.indexOf('*')+1, playList.length());      //atribute Value to get playListNode

            TagNode playElements[] = rootNode.getElementsByAttValue(attName, attValue, true, true);

            playList = fmStationTags.getPlayListItemTag()   ;// 2 values to find Elements Rows
            attName= playList.substring(0, playList.indexOf('*'));   // attribute name to get playListNode
            attValue=playList.substring(playList.indexOf('*')+1, playList.length());    //atribute Value to get playListNode;

            TagNode [] itemElements  = playElements[0].getElementsByAttValue(attName, attValue, true, true);


            for (int i = 0 ; i<itemElements.length; i++){



                playList = fmStationTags.getTimeTag();  //element Time with 2 value
                attName= playList.substring(0, playList.indexOf('*')); // attribute name to get TimeNode
                attValue=playList.substring(playList.indexOf('*')+1, playList.length()); //atribute Value to get TimeNode

                TagNode[] timesNoodes = itemElements[i].getElementsByAttValue(attName, attValue, true, true);   // getting Time Node for i item in Playlist by attribute name and attribute Value
                String strTime = timesNoodes[0].getText().toString();   // get time in String format

                //Log.d("Look", "Group1Connection - newConnection(): time->"+strTime);

                if(isMyTime(strTime)) {

                    strTime = strTime.substring(0,2)+":"+strTime.substring(2,4);

                    playList = fmStationTags.getSongTag();  //element song with 2 value
                    attName= playList.substring(0, playList.indexOf('*')); // attribute name to get SongNode
                    attValue=playList.substring(playList.indexOf('*')+1, playList.length()); //atribute Value to get SongNode

                    TagNode[] songNodes = itemElements[i].getElementsByAttValue(attName, attValue, true, true); // get Singer and Song in one line

                    playList = fmStationTags.getSingerTag();  //element singer with 2 value
                    attName= playList.substring(0, playList.indexOf('*')); // attribute name to get SingerNode
                    attValue=playList.substring(playList.indexOf('*')+1, playList.length()); //atribute Value to get SingerNode

                    TagNode[] singerNode = songNodes[0].getElementsByAttValue(attName, attValue, true, true); // get Singer only Node
                    String strSinger = singerNode[0].getText().toString().trim(); // get singer in String format


                    String strSong = songNodes[0].getText().toString().trim();//get song and singer in one line, in  String format
                    strSong = strSong.replace(strSinger, "");    //get song  in String format

                    saveItem(strTime,strSong,strSinger);
                }
            }


        } catch (MalformedURLException e) {
            Log.e("Look","MalformedURLException in GroupConnection1  "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Look","IOException in GroupConnection1  "+e.getMessage());
            e.printStackTrace();
        }

    }

    public String createLink(String strLink,String strDate ){

        //Input date format dd-mm-yyyy


        String strDay = strDate.substring(0,strDate.indexOf("-"));
        String strMonth= strDate.substring(strDate.indexOf('-')+1,strDate.lastIndexOf('-'));
        String strYear = strDate.substring(strDate.lastIndexOf('-')+1, strDate.length());

       // Log.d("Look","Group1Connection-createLink: day ->"+strDay+"  month ->"+strMonth+"  year ->"+strYear);

        if(strMonth.equals("01")){
            strMonth = "january";
        }else if(strMonth.equals("02")){
            strMonth = "february";
        }else if(strMonth.equals("03")){
            strMonth = "march";
        }else if(strMonth.equals("04")){
            strMonth = "april";
        }else if(strMonth.equals("05")){
            strMonth = "may";
        }else if(strMonth.equals("06")){
            strMonth = "june";
        }else if(strMonth.equals("07")){
            strMonth = "july";
        }else if(strMonth.equals("08")){
            strMonth = "august";
        }else if(strMonth.equals("09")){
            strMonth = "september";
        }else if(strMonth.equals("10")){
            strMonth = "october";
        }else if(strMonth.equals("11")){
            strMonth = "november";
        }else if(strMonth.equals("12")) {
            strMonth = "december";
        }

        strLink = strLink+strDay+"_"+strMonth+"_"+strYear+"/";

        Log.d("Look","Group1Connection-createLink: link is ->"+strLink);

        return strLink;
    }
}
