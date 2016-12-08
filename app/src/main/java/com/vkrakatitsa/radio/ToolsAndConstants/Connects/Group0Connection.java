package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.Model.RadioTagsItem;
import com.vkrakatitsa.radio.R;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Group0Connection extends BaseConnection {

    private RadioTagsItem fmStationTags;
    private String link;

    public Group0Connection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {
        super(context, obj, strDate, strTimeFrom, strTimeTo);

        Log.d("date ","Group0Connection - Group0Connection(): strdate->"+strDate );

        RadioTagEngine engine = new RadioTagEngine(context);

        fmStationTags = engine.getItemById(obj.getTagType());

        link = createLink(obj.getLink(),strDate);
        //Log.d("Look","Group0Connection - Group0Connection(): link->"+link);
    }

    public void createNewConnection(){

        try {
            URL url = new URL(link);
            CleanerProperties properties = new CleanerProperties();
            properties.setCharset(fmStationTags.getCharset());

            HtmlCleaner cleaner = new HtmlCleaner(properties);

            TagNode rootNode = cleaner.clean(url);

            String playList = fmStationTags.getPlayListTag();// 2 values to find PlayList Element
            String attName= playList.substring(0, playList.indexOf('*')); // attribute name to get playListNode
            String attValue=playList.substring(playList.indexOf('*')+1, playList.length()); //atribute Value to get playListNode

            Log.d("Look","Group0Connection-createNewConnection(): attName ->"+attName+ "   attValue->"+attValue);

            TagNode[] playNodes = rootNode.getElementsByAttValue(attName, attValue, true, true); // getting PlayList Node by attribute name and attribute Value

            playList = fmStationTags.getPlayListItemTag();      // element Row with 1 value
            TagNode[] listNodes = playNodes[0].getElementsByName(playList, true);    // getting all items of Songs in playlist by the Tag

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

                    if(singerNode.length ==0) {
                        continue;}

                    String singer = singerNode[0].getText().toString().trim();// get Singer in String format

                    //Clear song theme (@Идеальный дует: )
                    if(singer.contains("@" ) && (singer.contains(":"))){
                        String sub = singer.substring(singer.indexOf('@'), singer.indexOf(':')+1);// get substring with song theme
                        singer = singer.replace(sub, "");
                    }

                    playList = fmStationTags.getSongTag();//element Song with 1 value

                    TagNode[] songNode = listNodes[i].getElementsByName(playList, true);    //getting  Song  by the Tag
                    String song = songNode[1].getText().toString().trim();  // get Song in String format

                    song = song.replace("&amp;"," & ");
                    singer = singer.replace("&amp;"," & ");

                    Log.d("Look", " "+time +"  "+ song+"  -  "+singer);
                    saveItem(time,song,singer);
                }else {continue;}

            }



        } catch (MalformedURLException e) {
            Log.e("Look","MalformedURLException in GroupConnection0 "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Look","IOException in GroupConnection0  "+e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            Log.e("Look","ArrayIndexOutOfBoundsException in GroupConnection0  " +
                    "\n Check internet connection"+e.getMessage());
            Toast.makeText(getContext(), getContext().getString(R.string.toast_CheckInternetException),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

}

    /**
     * Create link for group0Connection radio types
     * @param strLink - link on  radio archive
     * @param strDate - date to search in format dd-MM-yyyy
     * @return Link in format: Link+date. ("http://radio.i.ua/hit.fm/archive/14")
     */
    public String createLink(String strLink,String strDate ){

        strDate=strDate.substring(0,strDate.indexOf('-'));
        strLink = strLink+strDate;

        Log.d("Look","Group0Connection-createLink: link is ->"+strLink);

        return strLink;
    }
}
