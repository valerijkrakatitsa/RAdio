package com.vkrakatitsa.radio.ToolsAndConstants;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class RecyclerItemCallback extends ItemTouchHelper.Callback {

    public ItemTouchHelperForAdapterInterface touchHelperInerface=null;
    private Drawable mItemBackground;

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

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //Change Item color on start Drag
        if(actionState== ItemTouchHelper.ACTION_STATE_DRAG){
            View item = viewHolder.itemView;
            mItemBackground= item.getBackground(); //get current item background
            item.setBackgroundColor(Color.parseColor("#951e9dd6"));  //set background color
            item.setScaleX(0.97f);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        viewHolder.itemView.setBackground(mItemBackground);
        viewHolder.itemView.setScaleX(1f);
        super.clearView(recyclerView, viewHolder);
    }
}
