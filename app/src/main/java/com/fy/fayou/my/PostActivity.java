package com.fy.fayou.my;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.my.fragment.PostFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/my/post")
public class PostActivity extends BaseActivity {

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("我的帖子");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        if (findFragment(PostFragment.class) == null) {
            loadRootFragment(R.id.fl_container, PostFragment.newInstance());
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_comm_list;
    }

}
