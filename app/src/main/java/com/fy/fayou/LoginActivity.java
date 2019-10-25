package com.fy.fayou;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListActivity;
import com.meis.base.mei.entity.Result;

import java.util.List;

import io.reactivex.Observable;

@Route(path = "/fy/login")
public class LoginActivity extends BaseListActivity {

    @Autowired
    public String name;
    @Autowired
    public int age;

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
    }

    @Override
    protected RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    protected MeiBaseAdapter getAdapter() {
        return null;
    }

    @Override
    protected Observable<Result<List>> getListObservable(int pageNo) {
        return null;
    }

    @Override
    public boolean canLoadMore() {
        return false;
    }

    @Override
    public boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected void initData() {

        Log.e("----------------", "***********" + name + "***" + age
                + "***" + getIntent().getStringExtra("name"));

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_login;
    }
}
