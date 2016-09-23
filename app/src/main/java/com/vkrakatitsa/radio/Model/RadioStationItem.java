package com.vkrakatitsa.radio.Model;

import android.content.ContentValues;
import android.database.Cursor;

import com.vkrakatitsa.radio.ToolsAndConstants.DBConstants;

public class RadioStationItem {

    private int nIcon =-1;
    private String strStationName=null;
    private int nPosition=-1;
    private String strLink="";
    private int nTagType = 0 ;
    private long id = -1;

    public RadioStationItem(){}

    public RadioStationItem(int icon, String strStationName, int position, String link, int tagType){
        setIcon(icon);
        setStationName(strStationName);
        setLink(link);
        setPosition(position);
        setTagType(tagType);
    }

    public RadioStationItem(Cursor cursor){
                setId(cursor.getLong(0));
                setStationName(cursor.getString(1));
                setIcon(cursor.getInt(2));
                setPosition(cursor.getInt(3));
                setLink(cursor.getString(4));
                setTagType(cursor.getInt(5));

    }


    public int getIcon() {
        return nIcon;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setIcon(int icon) {
        this.nIcon = icon;
    }

    public String getStationName() {
        return strStationName;
    }

    public void setStationName(String strStationName) {
        this.strStationName = strStationName;
    }

    public int getPosition() {
        return nPosition;
    }

    public void setPosition(int position) {
        nPosition = position;
    }

    public void setLink(String link){
        strLink = link;
    }

    public void setTagType(int tagType){
        nTagType = tagType;
    }

    public int getTagType(){
        return  nTagType;
    }

    public String getLink(){
        return strLink;
    }

    public ContentValues getContentValues (){
        ContentValues values  = new ContentValues();
        values.put(DBConstants.TABLE_RADIO_NAME,getStationName());
        values.put(DBConstants.TABLE_RADIO_PICTURE,getIcon());
        values.put(DBConstants.TABLE_RADIO_POSITION,getPosition());
        values.put(DBConstants.TABLE_RADIO_SITE,getLink());
        values.put(DBConstants.TABLE_RADIO_TAG_TYPE,getTagType());
      return values;
    }

}
