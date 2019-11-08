package com.fy.fayou.contract;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.contract.adapter.ExpandableItemAdapter;
import com.fy.fayou.contract.bean.Level0Item;
import com.fy.fayou.contract.bean.Level1Item;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/contract/filter")
public class FilterActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    ExpandableItemAdapter adapter;
    ArrayList<MultiItemEntity> list = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("婚内财产协议");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {

        list = generateData();
        adapter = new ExpandableItemAdapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                List<MultiItemEntity> list = adapter.getData();
                if (pos < list.size() && list.get(pos) instanceof Level1Item) {
                    Level1Item lv1 = (Level1Item) list.get(pos);
                    if (lv1.isLast) {
                        outRect.bottom = RxImageTool.dp2px(10);
                    }
                }
            }
        });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_contract_filter;
    }

    private ArrayList<MultiItemEntity> generateData() {
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        int lv0Count = 6;
        int lv1Count = 3;

        String[] titleList = {"当事人信息", "第一条 合同目的的描述", "第二条 财产所有制", "第三条 特别约定", "第四条 家庭消费", "署名条款"};
        for (int i = 0; i < lv0Count; i++) {
            Level0Item lv0 = new Level0Item();
            lv0.title = titleList[i];
            for (int j = 0; j < lv1Count; j++) {
                Level1Item lv1 = new Level1Item();
                if (i % 2 != 0) {
                    lv1.content = getResources().getString(R.string.contract_template);
                    lv0.childCount = lv0Count;
                    if (j == lv1Count - 1) {
                        lv1.isLast = true;
                    }
                    lv0.addSubItem(lv1);
                }
            }
            res.add(lv0);
        }
        return res;
    }
}
