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
import java.net.MalformedURLException;
import java.net.URL;

public class Group3Connection extends BaseConnection {

    private RadioTagsItem fmStationTags;
    private String link;

    public Group3Connection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {
        super(context, obj, strDate, strTimeFrom, strTimeTo);

        RadioTagEngine engine = new RadioTagEngine(context);

        fmStationTags = engine.getItemById(obj.getTagType());

        link = createLink(obj.getLink(),strDate);
    }


    public void createNewConnection(){

        try {
            URL url = new URL(link);
            CleanerProperties properties = new CleanerProperties();
            properties.setCharset(fmStationTags.getCharset());

            HtmlCleaner cleaner = new HtmlCleaner(properties);

            TagNode rootNode = cleaner.clean(url);

            String playList = fmStationTags.getPlayListItemTag();// 2 values to find PlayList items Elements
            String attName= playList.substring(0, playList.indexOf('*')); // attribute name to get playList Items Nodes
            String attValue=playList.substring(playList.indexOf('*')+1, playList.length()); //atribute Value to get playList Items Nodes

            TagNode[] listNodes = rootNode.getElementsByAttValue(attName, attValue, true, true);;    // getting all items of Songs in playlist by the Tag

            for(int i =0; i< listNodes.length; i++){

                playList = fmStationTags.getTimeTag();  //element Time with 2 value
                attName= playList.substring(0, playList.indexOf('*'));   // attribute name to get Time
                attValue=playList.substring(playList.indexOf('*')+1, playList.length());    //atribute Value to get Time

                TagNode[] timeNode = listNodes[i].getElementsByAttValue(attName, attValue, true, true); // getting Time Node for i item in Playlist by attribute name and attribute Value

                String time = timeNode[0].getText().toString().trim();      // get time in String format
                Log.d("Look", " "+time );

                if(isMyTime(time)){

                    playList = fmStationTags.getSingerTag();    //element Singer with 2 value
                    attName= playList.substring(0, playList.indexOf('*'));   // attribute name to get Singer
                    attValue=playList.substring(playList.indexOf('*')+1, playList.length());     //atribute Value to get Singer

                    TagNode[] singerNode = listNodes[i].getElementsByAttValue(attName, attValue, true, true);   //  getting Singer Node for i item in Playlist by attribute name and attribute Value
                    String singer = singerNode[0].getText().toString().trim();   // get Singer in String format

                    playList = fmStationTags.getSongTag();//element Song with 1 value
                    attName= playList.substring(0, playList.indexOf('*'));   // attribute name to get song
                    attValue=playList.substring(playList.indexOf('*')+1, playList.length());     //atribute Value to get song

                    TagNode[] songNode = listNodes[i].getElementsByAttValue(attName, attValue, true, true);  //getting  Song  by the Tag
                    String song = songNode[0].getText().toString().trim();  // get Song in String format  Error

                    saveItem(time,song,singer);
                }else {continue;}

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

        strLink = strLink+strDate+".html";

        Log.d("Look","Group0Connection-createLink: link is ->"+strLink);

        return strLink;
    }
}
