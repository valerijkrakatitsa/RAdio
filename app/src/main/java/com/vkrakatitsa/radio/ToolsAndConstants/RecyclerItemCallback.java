package com.vkrakatitsa.radio.ToolsAndConstants;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerItemCallback extends ItemTouchHelper.Callback {

    public ItemTouchHelperForAdapterInterface touchHelperInerface=null;

    public RecyclerItemCallback(ItemTouchHelperForAdapterInterface touchHelperInerface){
        this.touchHelperInerface = touchHelperInerface;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.START |ItemTouchHelper.END;

        return makeMovementFlags(dragFlags,swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        touchHelperInerface.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        touchHelperInerface.onDismist(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


}
