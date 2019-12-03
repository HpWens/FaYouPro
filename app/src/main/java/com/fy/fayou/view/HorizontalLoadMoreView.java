package com.fy.fayou.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.fy.fayou.R;

public class HorizontalLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.horizontal_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return com.chad.library.R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return com.chad.library.R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return com.chad.library.R.id.load_more_load_end_view;
    }
}
