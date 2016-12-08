package com.vkrakatitsa.radio.ToolsAndConstants.Connects;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class Group4Connection extends BaseConnection {

    private RadioTagsItem fmStationTags;
    private String link;

    public Group4Connection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {
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

            String tag = fmStationTags.getPlayListTag();    // get tag fo playlist
            TagNode linkElements[] = rootNode.getElementsByName(tag, true); // get Playlist Node by Tag

            tag = fmStationTags.getPlayListItemTag();   // get tag fo playlist items
            TagNode singAndSongsElements[] = rootNode.getElementsByName(tag, true);    // get Playlist items Nodes by Tag

            for (int i = 0 ; i<linkElements.length-1; i+=3){

                String strTime = linkElements[i].getText().toString().trim();
                strTime = strTime.substring(0,strTime.lastIndexOf(':'));

                if(isMyTime(strTime)) {

                    String strSinger = linkElements[i + 1].getText().toString().trim();
                    String strSong = linkElements[i + 2].getText().toString().trim();

                    saveItem(strTime,strSong,strSinger);
                }


            }

        } catch (MalformedURLException e) {
            Log.e("Look","MalformedURLException in GroupConnection1  "+e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Look","IOException in GroupConnection1  "+e.getMessage());
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e){
            Log.e("Look","ArrayIndexOutOfBoundsException in GroupConnection0  " +
                    "\n Check internet connection"+e.getMessage());
            Toast.makeText(getContext(), getContext().getString(R.string.toast_CheckInternetException),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    /**
     * Create link for group4Connection radio types
     * @param strLink - link on  radio archive
     * @param strDate - date to search in format dd-MM-yyyy
     * @return Link in format: Link+date. ("http://radioshanson.fm/info/plyei-list-dnya-14-03-2016")
     */
    private String createLink(String strLink,String strDate ){

        strLink = strLink+strDate;

        Log.d("Look","Group0Connection-createLink: link is ->"+strLink);

        return strLink;
    }
}
