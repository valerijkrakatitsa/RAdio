package com.vkrakatitsa.radio.Model.Wrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vkrakatitsa.radio.Model.RadioTagsItem;
import com.vkrakatitsa.radio.ToolsAndConstants.DBConstants;

import java.util.ArrayList;

public class RadioTagsWrappers extends BaseWrapper{

    public RadioTagsWrappers(Context context) {
        super(context, DBConstants.TABLE_NAME_TAGS);
    }
    public void insert (RadioTagsItem item){
        SQLiteDatabase db = getDatabase();
        db.insert(getTableName(),null,item.getContentValues());
        closeDatabase();
    }

    public RadioTagsItem getItemById(int id){
        SQLiteDatabase db =getDatabase();
        Log.d("Look","RadioTagsWrappers-getItemById: db is null - >"+(db==null));
        Cursor cursor = db.query(getTableName(),null, "_id=?",new String[]{Long.toString(id)},null,null,null);
        RadioTagsItem item = null;
        if(cursor!=null&&cursor.moveToFirst()){
            do{
                item = new RadioTagsItem(cursor);
            }while(cursor.moveToNext());
        }
        return item;
    }

    /*

    //На будущее Если понадобится - раскоментировать

    public void update(RadioTagsItem item){
        SQLiteDatabase db = getWritableDB();
        db.update(getTableName(),item.getContentValues(),"_id=?",new String[]{Long.toString(item.getnId())});
        db.close();strLink

    }

    public ArrayList<RadioTagsItem> getAll(){
        ArrayList<RadioTagsItem> arrItems = new ArrayList<RadioTagsItem>();
        SQLiteDatabase db = getReadableDB();
        Cursor cursor = db.query(getTableName(),null,null,null,null,null,null);

        if(cursor!=null&& cursor.moveToFirst()){

            do{
                arrItems.add(new RadioTagsItem(cursor));
            }while(cursor.moveToNext());
        }
        db.close();
        return arrItems;
    }

    */
}
