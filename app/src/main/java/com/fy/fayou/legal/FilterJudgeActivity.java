package com.fy.fayou.legal;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.legal.adapter.FilterAdapter;
import com.fy.fayou.legal.bean.JudgeLevel1;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/judge/filter")
public class FilterJudgeActivity extends BaseActivity {

    @Autowired(name = "list")
    public ArrayList<JudgeLevel1> firstLevel = new ArrayList<>();

    @BindView(R.id.recycler)
    RecyclerView recycler;

    FilterAdapter adapter;
    ArrayList<MultiItemEntity> list = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("筛选");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        getFirstLevelData();
        adapter = new FilterAdapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                List<MultiItemEntity> list = adapter.getData();
            }
        });
    }

    // 获取第一级栏目数据
    private void getFirstLevelData() {

        int index = 0;

        for (JudgeLevel1 level : firstLevel) {

            level.helperIndex = index;
            list.add(level);
            index++;

//            if (!level.name.equals(getString(R.string.judge_category_sign))) {
//                list.add(level);
//            }

        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_judge_filter;
    }
}
