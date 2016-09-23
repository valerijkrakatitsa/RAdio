package com.vkrakatitsa.radio.ToolsAndConstants;

public interface ItemTouchHelperForAdapterInterface {

    boolean onItemMove(int fromPosition, int toPosition);

    void onDismist(int position);
}
