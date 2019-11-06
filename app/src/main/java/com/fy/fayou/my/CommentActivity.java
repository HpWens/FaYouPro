package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.my.fragment.CommentFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/my/comment")
public class CommentActivity extends BaseActivity {
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("我的评论");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        if (findFragment(CommentFragment.class) == null) {
            loadRootFragment(R.id.fl_container, CommentFragment.newInstance());
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_comm_list;
    }
}
