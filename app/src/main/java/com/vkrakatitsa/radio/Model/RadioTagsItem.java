package com.vkrakatitsa.radio.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.vkrakatitsa.radio.ToolsAndConstants.DBConstants;

public class RadioTagsItem {

    private int nId = -1;
    private String strPlayListTag="";
    private String strPlayListItemTag="";
    private String strTimeTag="";
    private String strSongTag="";
    private String strSingerTag="";
    private String strCharset = "";

    public RadioTagsItem(){}

    public RadioTagsItem(Cursor cursor){
        setId(cursor.getInt(0));
        setPlayListTag(cursor.getString(1));
        setPlayListItemTag(cursor.getString(2));
        setTimeTag(cursor.getString(3));
        setSongTag(cursor.getString(4));
        setSingerTag(cursor.getString(5));
        setCharset(cursor.getString(6));
    }

    public RadioTagsItem(int id,String playListTag,String playListItemTag,String timeTag,String songTag,String singerTag,String charset){
        setId(id);
        setPlayListTag(playListTag);
        setPlayListItemTag(playListItemTag);
        setTimeTag(timeTag);
        setSongTag(songTag);
        setSingerTag(singerTag);
        setCharset(charset);
    }


    public String getCharset() {
        return strCharset;
    }

    public void setCharset(String charset) {
        strCharset = charset;
    }

    public int getId() {
        return nId;
    }

    public void setId(int id){
        nId = id;
    }

    public String getPlayListTag() {
        return strPlayListTag;
    }

    public void setPlayListTag(String playListTag) {
        strPlayListTag = playListTag;
    }

    public String getPlayListItemTag() {
        return strPlayListItemTag;
    }

    public void setPlayListItemTag(String playListItemTag) {
        strPlayListItemTag = playListItemTag;
    }

    public String getTimeTag() {
        return strTimeTag;
    }

    public void setTimeTag(String timeTag) {
        strTimeTag = timeTag;
    }

    public String getSongTag() {
        return strSongTag;
    }

    public void setSongTag(String songTag) {
        strSongTag = songTag;
    }

    public String getSingerTag() {
        return strSingerTag;
    }

    public void setSingerTag(String singerTag) {
        strSingerTag = singerTag;
    }



    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("_id", getId());
        values.put(DBConstants.TABLE_TAGS_PLAYLIST_TAG, getPlayListTag());
        values.put(DBConstants.TABLE_TAGS_PLAYLIST_ITEM_TAG, getPlayListItemTag());
        values.put(DBConstants.TABLE_TAGS_TIME_TAG, getTimeTag());
        values.put(DBConstants.TABLE_TAGS_SONG_TAG, getSongTag());
        values.put(DBConstants.TABLE_TAGS_SINGER_TAG, getSingerTag());
        values.put(DBConstants.TABLE_TAGS_CHARSET, getCharset());
        return values;
    }
}
