package com.vkrakatitsa.radio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;
import com.vkrakatitsa.radio.R;

public class MoreSearchResultAdapter extends BaseAdapter {

    private VKList<VKApiAudio> m_arrResult;
    LayoutInflater m_inflater ;

    public MoreSearchResultAdapter(Context context, VKList<VKApiAudio> list){
        m_arrResult=list;
        m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public int getCount() {
        return m_arrResult.size();
    }

    @Override
    public Object getItem(int i) {
        return m_arrResult.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view = m_inflater.inflate(R.layout.adapter_item_show_more_results,viewGroup,false);
        }

        TextView artist = (TextView) view.findViewById(R.id.moreResultItemSinger);
        TextView song= (TextView) view.findViewById(R.id.moreResultItemSong);

        VKApiAudio audioItem = (VKApiAudio)getItem(i);

        artist.setText(audioItem.artist);
        song.setText(audioItem.title);

        return view;
    }
}
