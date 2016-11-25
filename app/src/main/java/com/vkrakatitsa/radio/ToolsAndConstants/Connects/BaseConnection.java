package com.vkrakatitsa.radio.ToolsAndConstants.Connects;

import android.content.Context;
import android.util.Log;

import com.vkrakatitsa.radio.Model.Engine.RadioItemEngine;
import com.vkrakatitsa.radio.Model.Engine.RadioTagEngine;
import com.vkrakatitsa.radio.Model.RadioStationItem;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseConnection {

    private RadioStationItem fmStation;
    private boolean bFlag = false;
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
        //Log.d("Look", "BaseConnection-constructor: strdate is null ->" + (strDate == null));
        //this.strDate = strDate.substring(0, 2);
        //Log.d("Look", "BaseConnection-constructor: strDate ->" + this.strDate);
        strTimeFrom = strTimeFrom.replace(":", "");
        strTimeTo = strTimeTo.replace(":", "");

        nTimeFrom = Integer.parseInt(strTimeFrom);   //Время начала поиска
        nTimeTo = Integer.parseInt(strTimeTo);        //Время конца поиска

        //strLink = obj.getLink() + this.strDate;
        //Log.d("Look", "BaseConnection-constructor: time from -> " + nTimeFrom + "  |  timeTo-> " + nTimeTo);
        // createNewconnection();
    }

    public boolean isMyTime(String strTime) {

        strTime = strTime.replace(":", "");
        int nTime = Integer.parseInt(strTime);

        Log.d("RadioLook","BaseConnection - isMyTime " +  nTime+ "  nTimeFrom -"+nTimeFrom+"  nTime to ->"+nTimeTo);

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
        Log.d("RadioLook",this.getClass().getCanonicalName()+" - saveItem : Saved line->"+strSinger + " ^ "+ strSong);


    }

    public Context getContext() {
        return context;
    }

    public void createNewConnection() {
    }
}


/* RadioTagsItem fmStation = null;
    private boolean bFlag=false;
    private HashMap<String,String> arrResult;
    private String strDate = "";
    private int nTimeFrom=-1;
    private int nTimeTo=-1;
    private RadioTagEngine engine= null;
    private Context context = null;
    private String strLink;




    public BaseConnection (Context context, RadioStationItem obj, String strDate, String strTimeFrom, String strTimeTo){


        engine = new RadioTagEngine(context);
        Log.d("Look","BaseConnection constructor: engine is null ->"+(engine==null));
        Log.d("Look","BaseConnection constructor: obj is null ->"+(obj==null));
        fmStation= engine.getItemById(obj.getTagType());

        arrResult = new HashMap<>();
        Log.d("Look", "BaseConnection-constructor: strdate is null ->"+(strDate==null));
        this.strDate = strDate.substring(0,2);
        Log.d("Look","BaseConnection-constructor: strDate ->"+this.strDate);
        strTimeFrom = strTimeFrom.replace(":","");
        strTimeTo = strTimeTo.replace(":","");
        nTimeFrom =Integer.parseInt(strTimeFrom);   //Время начала поиска
        nTimeTo=Integer.parseInt(strTimeTo);        //Время конца поиска
        strLink = obj.getLink()+this.strDate;
        Log.d("Look","BaseConnection-constructor: time from -> "+nTimeFrom+"  |  timeTo-> "+nTimeTo);
        createNewconnection();
    }


    public void createNewconnection(){
        try {
            //Create HTTP Connection
            URL url = new URL(strLink);
            Log.d("Look","BaseConnection-createNewConnectionL: link - >"+strLink);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-agent", "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:45.0) Gecko/20100101 Firefox/45.0");

            //Create BufferdReader for Html Page
            String strCharset = fmStation.getCharset();
           // Log.d("Look","BaseConnection-createNewConnectionL: charset - >"+strCharset);
            BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream(),strCharset));

            //Read Response HTML Page
            String line ="";    //local line
            String strTime=null;
            String strSong=null;
            while ((line = read.readLine())!=null){
                //Поиск по Времени

                if(line.contains(fmStation.getTagLineTime())){
                    strTime=readTime(line);
                }

                if((line.contains(fmStation.getTagLineSong()))&&(bFlag)){
                    strSong=readAuthorAndSong(line);
                }

                //Добавление Верного Результата
                if(strTime!=null){
                    arrResult.put(strTime,strSong);
                }
            }

        } catch (MalformedURLException e) {
            Log.e("Look","BaseConnection-createNewConnectionL: Error ->"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Look","BaseConnection-createNewConnectionL: Error ->"+e.getMessage());
            e.printStackTrace();
        }
    }

    //---Поиск времени в строке
    private String readTime(String line){

        int nBegin = line.indexOf(fmStation.getTagForTimeFirst())+1; // Позиция для начала чтения времени
        int nEnd = line.lastIndexOf(fmStation.getTagForTimeSecond());// Позиция для окончания чтения времени

        line = line.substring(nBegin,nEnd);//Время в строке в виде строки
        line=line.replace(":","");

        int nTime = Integer.parseInt(line); // Время в строке в виде числа


       // Log.d("Look","BaseConnection-readTime: nTime -> "+nTime+"  |  timeFrom -> "+nTimeFrom+"  |  time to ->"+ nTimeTo);
        if((nTime<=nTimeTo)&&(nTime>=nTimeFrom)) {
            bFlag = true;
            Log.d("Look","BaseConnection-readTime: Подходящее Время ->"+nTime);
            // Преобразование времени в строку, добавление ":" и возврат результата
            String strTime = ""+nTime;
            int len = strTime.length();
            return strTime.substring(0,(len-2))+":"+strTime.substring(len-2);// Error lang.StringIndexOutOfBoundsException: length=1; regionStart=0; regionLength=-1
        }

        return null;
    }

    private String readAuthorAndSong(String line){
        int nBegin = line.indexOf(fmStation.getTagForSingerFirst())+1;//Позиция для начала чтения названия песни
        int nEnd = line.indexOf(fmStation.getTagForSingerSecond());//Позиция для завершения чтения названия песни

        String strSinger = line.substring(nBegin,nEnd); // Название песни

        nBegin = line.lastIndexOf(fmStation.getTagForSongFirst())+2;//Позиция для начала чтения автора песни
        nEnd  = line.lastIndexOf(fmStation.getTagForSongSecond());//Позиция для завершения чтения автора песни

        String strSong = line.substring(nBegin,nEnd);//Автор песни
        bFlag = false;//Переключение флага

        Log.d("Look","BaseConnection-readAuthorAndSong:  author ->"+strSinger+ "  |  song -> "+strSong);
        return strSinger+" - ^ "+ strSong;
    }

    public HashMap<String,String> getArray(){

        Log.d("Look","BaseConnection-getArray:  I work");

        return arrResult;
    }*/