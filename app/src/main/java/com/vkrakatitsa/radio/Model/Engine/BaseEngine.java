package com.vkrakatitsa.radio.Model.Engine;

import android.content.Context;

public class BaseEngine {

    private Context context;

    public BaseEngine(Context context ){
        this.context=context;
    }

    public Context getContext(){
        return context;
    }

}
