package com.vkrakatitsa.radio.Model.Engine;

import android.content.Context;

import com.vkrakatitsa.radio.Model.RadioTagsItem;
import com.vkrakatitsa.radio.Model.Wrappers.RadioTagsWrappers;

public class RadioTagEngine extends BaseEngine {
    public RadioTagEngine(Context context) {
        super(context);
    }

    public void insert(RadioTagsItem item){
        RadioTagsWrappers wrapper = new RadioTagsWrappers(getContext());
        wrapper.insert(item);
    }

    public RadioTagsItem getItemById(int id ){
        RadioTagsWrappers wrapper = new RadioTagsWrappers(getContext());
        return wrapper.getItemById(id);
    }

    /*
    public void update(RadioTagsItem item ){
        RadioTagsWrappers wrapper = new RadioTagsWrappers(getContext());
        wrapper.update(item);
    }

    public ArrayList<RadioTagsItem> getAll(){
        RadioTagsWrappers wrapper = new RadioTagsWrappers(getContext());
        return wrapper.getAll();
    }
    */
}
