package com.vkrakatitsa.radio.ToolsAndConstants;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class MyItemTouchHelper extends ItemTouchHelper {
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     *
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public MyItemTouchHelper(Callback callback) {
        super(callback);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        super.onChildViewAttachedToWindow(view);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        view.setBackgroundColor(Color.YELLOW);
        super.onChildViewDetachedFromWindow(view);
    }
}
