package com.fy.fayou;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.adapter.LaunchAdapter;
import com.fy.fayou.bean.LaunchEntity;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.utils.GallerySnapHelper;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/first/launch")
public class FirstLaunchActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    LaunchAdapter mAdapter;
    GallerySnapHelper mSnapHelper;

    // 第一版 3  第二版 5
    private static final int LAUNCH_PIC_COUNT = 5;

    @Override
    protected void initView() {
        Eyes.translucentStatusBar(this, true, false);
    }

    @Override
    protected void initData() {

        List<LaunchEntity> data = new ArrayList<>();
        for (int i = 0; i < LAUNCH_PIC_COUNT; i++) {
            LaunchEntity entity = new LaunchEntity();
            // entity.itemType = i;
            data.add(entity);
        }

        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter = new LaunchAdapter(data, v -> {
            UserService.getInstance().saveFirstLaunch(true);
            ARouter.getInstance().build(Constant.MAIN).navigation();
            finish();
        }));
        mSnapHelper = new GallerySnapHelper();
        mSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected int layoutResId() {
        return R.layout.comm_recycler;
    }
}
