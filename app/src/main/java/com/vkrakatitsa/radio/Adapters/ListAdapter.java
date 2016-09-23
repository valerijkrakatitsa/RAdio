package com.vkrakatitsa.radio.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vkrakatitsa.radio.R;

import java.util.LinkedHashMap;
import java.util.Set;

public class ListAdapter extends BaseAdapter {

    LinkedHashMap<String,String> array;
    String [] arrKey;
    LayoutInflater linflater;

    public ListAdapter(Context context, LinkedHashMap<String, String> array){
        this.array=array;
        Log.d("Look","ListtAdapter-ListAdapter: array is null - >"+(array==null));
        Set<String> keySet = array.keySet();
        arrKey = new String[keySet.size()];
        keySet.toArray(arrKey);
        linflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return arrKey[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = linflater.inflate(R.layout.adapter_item_result_show_results_list,null,false);
        }

        TextView txtTime=(TextView)view.findViewById(R.id.resultItemTime);
        TextView txtSinger=(TextView)view.findViewById(R.id.resultItemSinger);
        TextView txtSong=(TextView)view.findViewById(R.id.resultItemSong);

        txtTime.setText(arrKey[i]);

        Log.d("Look", this.getClass().getCanonicalName()+" -getView: array Result Singer and song ->"+array.get(arrKey[i])/*+ "  | region end-> "+ array.get(arrKey[i]).indexOf('^')*/);

        String temp = array.get(arrKey[i]);
        String strSinger = temp.substring(0,(temp.indexOf('^'))); // Parse String to find Singer  substring from begin to *
        String strSong = temp.substring(temp.indexOf('^')+1,temp.length()); // Parse String to find Singer  substring from * to end

        txtSinger.setText(strSinger);
        txtSong.setText(strSong);

        //Log.d("Look","ListAdapter - getView: view is null->"+(view==null));

        return view;
    }
}
