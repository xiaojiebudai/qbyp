package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by ppsher on 2017/3/13.
 */

public class ObservableListView extends ListView {

    private ScrollListener scrollListener = null;

    public ObservableListView(Context context) {
        super(context);
    }

    public ObservableListView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollListener != null) {
            scrollListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public interface ScrollListener {

        void onScrollChanged(ObservableListView scrollView, int x, int y, int oldX, int oldY);

    }
}
