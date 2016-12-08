package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.content.Context;
import android.util.Log;

import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseConnection {

    private LinkedHashMap<String, String> arrResult;
    public String strDate = "";
    public int nTimeFrom = -1;
    public int nTimeTo = -1;

    private Context context = null;
    //private String strLink;

    public static BaseConnection getConnection(Context context, long nObjId, String strDate, String strTimeFrom, String strTimeTo) {

        RadioItemEngine engine = new RadioItemEngine(context);
        RadioStationItem obj = engine.getItemById(nObjId);
        Log.d("Look","BaseConnection-getConnection: obj is null->"+(obj==null));
        int nTagTypeId = obj.getTagType();

        if (nTagTypeId == 0) {
            return new Group0Connection(context, obj, strDate, strTimeFrom, strTimeTo);
        } else if (nTagTypeId == 1) {
            return new Group1Connection(context, obj, strDate, strTimeFrom, strTimeTo);
        } else if (nTagTypeId == 2) {
            return new Group2Connection(context, obj, strDate, strTimeFrom, strTimeTo);
        } else if (nTagTypeId == 3) {
            return new Group3Connection(context, obj, strDate, strTimeFrom, strTimeTo);
        } else if (nTagTypeId == 4) {
            return new Group4Connection(context, obj, strDate, strTimeFrom, strTimeTo);
        }

        return null;
    }

    public BaseConnection(Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo) {

        Log.d("Look", "BaseConnection constructor: obj is null ->" + (obj == null));
        // fmStation= engine.getItemById(obj.getTagType());

        arrResult = new LinkedHashMap<>();
        strTimeFrom = strTimeFrom.replace(":", "");
        strTimeTo = strTimeTo.replace(":", "");

        nTimeFrom = Integer.parseInt(strTimeFrom);   //Время начала поиска
        nTimeTo = Integer.parseInt(strTimeTo);        //Время конца поиска
    }

    public boolean isMyTime(String strTime) {

        strTime = strTime.replace(":", "");
        int nTime = Integer.parseInt(strTime);

        Log.v("RadioLook","BaseConnection - isMyTime " +  nTime+ "  nTimeFrom -"+nTimeFrom+"  nTime to ->"+nTimeTo);

        if ((nTime <= nTimeTo) && (nTime >= nTimeFrom)) {
            return true;
        }

        return false;
    }

    public LinkedHashMap<String, String> getArray() {

        Log.d("Look", "BaseConnection-getArray:  I work, array size - >"+arrResult.size());



        return arrResult;
    }

    public String createLink() {
        return "";
    }

    public void saveItem(String strTime, String strSong, String strSinger) {

        arrResult.put(strTime, strSinger + " ^ "+ strSong);
        Log.v("RadioLook",this.getClass().getCanonicalName()+" - saveItem : Saved line->"+strSinger + " ^ "+ strSong);


    }

    public Context getContext() {
        return context;
    }

    public void createNewConnection() {
    }
}