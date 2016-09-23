package com.vkrakatitsa.radio.Model.Engine;

import android.content.Context;
import android.util.Log;

import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.Model.Wrappers.RadioItemWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RadioItemEngine extends BaseEngine {


    public RadioItemEngine(Context context) {
        super(context);
    }

    public void insert(RadioStationItem item){
        RadioItemWrapper wrapper = new RadioItemWrapper(getContext());
        wrapper.insert(item);
    }

    public void update(RadioStationItem item ){
        RadioItemWrapper wrapper = new RadioItemWrapper(getContext());
        wrapper.update(item);
    }

    public ArrayList<RadioStationItem> getAll(){
        RadioItemWrapper wrapper = new RadioItemWrapper(getContext());
        //Log.d("Look","RadioItemEngine getAll ");
       // ArrayList<RadioStationItem> arrayList = arraySort(wrapper.getAll());    LastChanges
        //return arrayList
        return wrapper.getAll();
    }

    public RadioStationItem getItemById(long id ){
        RadioItemWrapper wrapper = new RadioItemWrapper(getContext());
        return wrapper.getItemById(id);
    }

    //Last Changes
   /* public ArrayList <RadioStationItem> arraySort(ArrayList <RadioStationItem> array){
        Collections.sort(array,new  Comparator<RadioStationItem>(){
            @Override
            public int compare(RadioStationItem radioStationListItem, RadioStationItem t1) {
                return (int) (radioStationListItem.getPosition()-t1.getPosition());
            }
        });
        return array;
    }*/

    public ArrayList<RadioStationItem> getItemsByString(String strSearcg){
        RadioItemWrapper wrapper = new RadioItemWrapper(getContext());
        return wrapper.getgetItemsByString(strSearcg);
    }
}
