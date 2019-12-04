package com.fy.fayou.forum.activity;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.forum.fragment.StarPlateFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/star/plate")
public class StarPlateActivity extends BaseActivity {

    @Autowired(name = "id")
    public String id = "";

    @Autowired(name = "name")
    public String name;


    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        findViewById(R.id.top_line).setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        setToolBarCenterTitle(name).setLeftBackListener(v -> {
            finish();
        });

        if (findFragment(StarPlateFragment.class) == null) {
            loadRootFragment(R.id.fl_container, StarPlateFragment.newInstance(id, name));
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_comm_list;
    }


}
