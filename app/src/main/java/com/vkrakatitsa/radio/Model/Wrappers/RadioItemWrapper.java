package com.vkrakatitsa.radio.Model.Wrappers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.ToolsAndConstants.DBConstants;

import java.util.ArrayList;

public class RadioItemWrapper extends BaseWrapper{

    public RadioItemWrapper(Context context) {
        super(context, DBConstants.TABLE_NAME_RADIO);
       // Log.d("Look", "Table Name RadioItemWrapper constructor - > "+DBConstants.TABLE_NAME_RADIO);
    }

    public void insert (RadioStationItem item){
        SQLiteDatabase db = getDatabase();
        long rowID = db.insert(getTableName(),null,item.getContentValues());
      //  Log.d("Look", " RadioItemWrapper-insert:  Row Id ->"+rowID);
        closeDatabase();
    }

    public void update(RadioStationItem item){
        SQLiteDatabase db = getDatabase();
       long rowID =  db.update(getTableName(),item.getContentValues(),"_id=?",new String[]{Long.toString(item.getId())});
        //Log.d("Look","RadioItemWrapper-update: row iD ->"+rowID+"  |  Item ->"+item.getStationName()+"  |  item id "+ item.getId()+ "  |  Position ->"+ item.getPosition());
        closeDatabase();

    }

    public ArrayList<RadioStationItem> getAll(){
        ArrayList<RadioStationItem> arrItems = new ArrayList<RadioStationItem>();
        SQLiteDatabase db = getDatabase();
        String selectQuery = "SELECT  * FROM " + getTableName();
        //Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor cursor = db.query(getTableName(),null,null,null,null,null,DBConstants.TABLE_RADIO_POSITION+" ASC");//Last changes

      //  Log.d("Look","RadioItemWrapper getAll Cursor is null ->"+(cursor==null)+"  | Cursor is empty->"+!cursor.moveToFirst());

        if(cursor!=null && cursor.moveToFirst()){

            do{
                //Log.d("Look","RadioItemWrapper getAll add in array element");
                arrItems.add(new RadioStationItem(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();
        return arrItems;
    }

    public RadioStationItem getItemById(long id){
        SQLiteDatabase db =getDatabase();
       // Log.d("Look","RadioItemWrapper-getItemById: id ->"+id);
        Cursor cursor = db.query(getTableName(),null, "_id=?",new String[]{Long.toString(id)},null,null,null);
        RadioStationItem item=null;
        if(cursor!=null&&cursor.moveToFirst()){
          do{
            item =  new RadioStationItem(cursor);
          }while(cursor.moveToNext());
        }
        return item;
    }

    public ArrayList<RadioStationItem> getgetItemsByString (String strSearch){
        ArrayList<RadioStationItem> arrItems = new ArrayList<RadioStationItem>();
        SQLiteDatabase db = getDatabase();
        strSearch = strSearch + "%";
        String strSelectQuery = DBConstants.TABLE_RADIO_NAME+" LIKE ? ";

        String [] arrKey= new String[]{strSearch};
        Cursor cursor = db.query(getTableName(),null,strSelectQuery,arrKey,null,null,null);

        if(cursor!=null&&cursor.moveToFirst()){
            do{
                arrItems.add(new RadioStationItem(cursor));
            }while(cursor.moveToNext());
        }
        Log.d("Look","RadioItemWrapper - getItemsByString : array size"+ arrItems.size());
        cursor.close();
        closeDatabase();
        return arrItems;
    }
}
