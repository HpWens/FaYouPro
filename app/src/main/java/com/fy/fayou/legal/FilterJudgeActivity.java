package com.fy.fayou.legal;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.common.Constant;
import com.fy.fayou.legal.adapter.FilterAdapter;
import com.fy.fayou.legal.bean.JudgeLevel1;
import com.fy.fayou.legal.bean.JudgeLevel2;
import com.fy.fayou.view.flow.FlowLayout;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/judge/filter")
public class FilterJudgeActivity extends BaseActivity {

    @Autowired(name = "list")
    public ArrayList<JudgeLevel1> firstLevel = new ArrayList<>();

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.flow_layout)
    FlowLayout flowLayout;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;

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
                if (pos >= 0 && pos < adapter.getData().size()) {
                    MultiItemEntity entity = adapter.getData().get(pos);
                    if (entity instanceof JudgeLevel2) {
                        JudgeLevel2 level = (JudgeLevel2) entity;
                        if (level.isFirstChild) {
                            outRect.top = RxImageTool.dip2px(15);
                        }
                    }
                }
            }
        });

        adapter.setOnCheckListener((v, array) -> {

            addFilterArray(array);

        });
    }

    private void addFilterArray(List<JudgeLevel2> array) {
        topLayout.setVisibility(array.isEmpty() ? View.GONE : View.VISIBLE);

        flowLayout.removeAllViews();
        for (JudgeLevel2 level : array) {
            View tagView = View.inflate(this, R.layout.item_flow_filter, null);
            TextView tvName = tagView.findViewById(R.id.tv_name);
            tvName.setText(level.name);

            tagView.findViewById(R.id.iv_delete).setOnClickListener(v -> {
                adapter.removeSingleSelected(level);
                adapter.notifyDataSetChanged();
                addFilterArray(adapter.getSelectedArray());
            });

            flowLayout.addView(tagView);
        }
    }

    // 获取第一级栏目数据
    private void getFirstLevelData() {

        int index = 0;

        for (JudgeLevel1 level : firstLevel) {
            if (!level.name.equals(getString(R.string.judge_category_sign))) {
                level.helperIndex = index;
                list.add(level);
                index++;
            }
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_judge_filter;
    }

    @OnClick({R.id.tv_clear, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                adapter.clearSelectedArray();
                addFilterArray(adapter.getSelectedArray());
                break;
            case R.id.tv_ok:

                // 返回筛选后的数据
                Intent data = new Intent();
                if (!adapter.getSelectedArray().isEmpty()) {
                    data.putParcelableArrayListExtra(Constant.Param.LIST, adapter.getSelectedArray());
                }
                setResult(Constant.Param.TEMPLATE_FILTER_RESULT, data);
                finish();

                break;
        }
    }
}
