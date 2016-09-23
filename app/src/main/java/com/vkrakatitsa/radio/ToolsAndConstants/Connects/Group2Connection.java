package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.content.Context;
import android.util.Log;

import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.Model.RadioTagsItem;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Group2Connection extends BaseConnection {

    private RadioTagsItem fmStationTags;
    private String link;
    private String strSearchKey;

    public Group2Connection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {
        super(context, obj, strDate, strTimeFrom, strTimeTo);

        RadioTagEngine engine = new RadioTagEngine(context);

        fmStationTags = engine.getItemById(obj.getTagType());

        link = createLink(obj.getLink(),strDate);
    }

    public void createNewConnection(){

        try {
            URL url = new URL(link);

            //Create connectio with POSTData
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(getSearchKey());
            writer.flush();
            writer.close();
            BufferedReader read = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            while((line=read.readLine())!=null){

                if (line.contains("&mdash;")){

                    if(line.contains("<p")){
                        line = line.substring(line.indexOf("</p>")+4, line.length());
                    }

                    String time = line.substring(0,line.indexOf("&"));

                    if(isMyTime(time)){

                        line = line.replace(time, "");
                        line = line.replace("&","");
                        line = line.replace(";","");
                        line = line.replace("emsp","");
                        line = line.replace("mdash","");
                        line = line.replace("<br />","");
                        String singer = "";
                        String song="";

                        //Check if Song has only Singer or only Name
                        if(line.indexOf('-')!=-1){
                            singer = line.substring(0,line.indexOf('-'));
                            song = line.substring(line.indexOf('-')+1,line.length());
                        }else{
                            singer =line;
                            song = "";
                        }
                        saveItem(time,song,singer);
                    }
                }
            }

            read.close();


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

        return strLink;
    }

    private String getSearchKey(){

        strSearchKey = "SelectedDate="+strDate;
        return strSearchKey;
    }
}
