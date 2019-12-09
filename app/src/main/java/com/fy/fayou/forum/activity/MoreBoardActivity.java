package com.fy.fayou.forum.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.forum.fragment.MoreBoardFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

@Route(path = "/more/board")
public class MoreBoardActivity extends BaseActivity {

    @Autowired(name = "keyword")
    public String keyword = "";

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        setLeftBackListener(v -> {
            finish();
        }).setToolBarCenterTitle("更多板块");
    }

    @Override
    protected void initData() {
        findViewById(R.id.top_line).setVisibility(View.GONE);

        if (findFragment(MoreBoardFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MoreBoardFragment.newInstance(keyword));
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_comm_list;
    }
}
