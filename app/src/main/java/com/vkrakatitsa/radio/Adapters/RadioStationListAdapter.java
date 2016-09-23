package com.vkrakatitsa.radio.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vkrakatitsa.radio.Model.RadioStationItem;
import com.vkrakatitsa.radio.R;
import com.vkrakatitsa.radio.ToolsAndConstants.ItemTouchHelperForAdapterInterface;

import java.util.ArrayList;
import java.util.Collections;

public class RadioStationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperForAdapterInterface{

    private ArrayList<RadioStationItem> stationList;

    public OnRecycleItemClickListener itemClickListener;

    public RadioStationListAdapter(ArrayList<RadioStationItem> radioList){

        Log.d("Look","Adapter create ");
        stationList=radioList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater infater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = infater.inflate(R.layout.adapter_item_radio_station_list,parent,false);

        return new DataHolder(child);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioStationItem item = stationList.get(position);
        DataHolder myHolder = (DataHolder) holder;

        myHolder.icon.setImageResource(item.getIcon());
        myHolder.nItemID = item.getId();
        myHolder.stationName.setText(item.getStationName());
       // Log.d("Look","RadioStationListAdapter-onBindViewHolder: get StationName ->"+ myHolder.stationName.getText().toString());
    }

    @Override
    public int getItemCount() {
        return stationList.size();
    }

    @Override
    public long getItemId(int position) {
        return stationList.get(position).getId();
    }


    public void setOnRecycleItemClickListener(OnRecycleItemClickListener listener){
        itemClickListener = listener;
    }
    //Method for Drag items
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if(fromPosition<toPosition){
            for(int i= 0 ; i<toPosition;i++){
                Collections.swap(stationList,i,i+1);

            }
        }else{
            for (int i = fromPosition; i > toPosition; i--){
                Collections.swap(stationList, i,i-1);
            }
        }
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }

    public RadioStationItem getItem(){
        return stationList.get(0);
    }


    //Method for Swipe item
    @Override
    public void onDismist(int position) {
        stationList.remove(position);
        notifyItemRemoved(position);
    }



    //------Return Radio Station List with users settings---------
    public ArrayList<RadioStationItem> getUserListSettings(){
        return stationList;
    }



    public class DataHolder extends RecyclerView.ViewHolder{

        long nItemID=-1;
        ImageView icon =null;
        TextView stationName=null;

        DataHolder(View view){
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemClickListener!=null){
                        Log.d("Look","DataHolder-constructor: item id -> "+nItemID);
                        itemClickListener.onRecycleItemClick(nItemID);
                    }
                }
            });


            icon=(ImageView)view.findViewById(R.id.station_icon);
            stationName =(TextView)view.findViewById(R.id.radio_station_name);
        }

    }

    public interface OnRecycleItemClickListener {

        void onRecycleItemClick(long id);
    }


}
