package com.vkrakatitsa.radio.ToolsAndConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        Log.d("Look", "I work DBHelper onCreate ");

        sqLiteDatabase.execSQL("CREATE TABLE "+DBConstants.TABLE_NAME_RADIO+" (" +
                "   _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+DBConstants.TABLE_RADIO_NAME+" text," +
                " "+DBConstants.TABLE_RADIO_PICTURE+" integer," +
                " "+DBConstants.TABLE_RADIO_POSITION+ " integer," +
                " "+DBConstants.TABLE_RADIO_SITE+" text," +
                " "+DBConstants.TABLE_RADIO_TAG_TYPE+" integer);");

        sqLiteDatabase.execSQL("CREATE TABLE "+DBConstants.TABLE_NAME_TAGS+" ( " +
                " _id integer PRIMARY KEY NOT NULL," +
                " "+DBConstants.TABLE_TAGS_PLAYLIST_TAG+" text," +
                " "+DBConstants.TABLE_TAGS_PLAYLIST_ITEM_TAG+" text," +
                " "+DBConstants.TABLE_TAGS_TIME_TAG+" text," +
                " "+DBConstants.TABLE_TAGS_SONG_TAG+" text," +
                " "+DBConstants.TABLE_TAGS_SINGER_TAG+" text,"+
                " "+DBConstants.TABLE_TAGS_CHARSET+" text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
