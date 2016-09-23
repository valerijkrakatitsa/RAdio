package com.vkrakatitsa.radio.Model.Wrappers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vkrakatitsa.radio.ToolsAndConstants.DBHelper;
import com.vkrakatitsa.radio.ToolsAndConstants.DatabaseManager;

public class BaseWrapper {

    private Context context;
    //private DBHelper helper;                Todo  Delete
    private String tableName;
    private DatabaseManager m_DnManager;

    public BaseWrapper(Context context, String tableName){

       this.context = context;
        this.tableName=tableName;
        //Log.d("Look","BaseWrapper- context is null ->"+(context == null));

        //Init Singletonconnection to Database
        DatabaseManager.initializeInstance(new DBHelper(context));
        m_DnManager = DatabaseManager.getInstance();

        //helper = new DBHelper(context);       Todo DeleTE
    }

    public Context getContext(){
        return context;
    }

    public SQLiteDatabase getDatabase(){
        return m_DnManager.openDatabase();
    }

    public String getTableName(){
        return tableName;
    }

    public void closeDatabase (){
        m_DnManager.closeDatabase();
    }

}
