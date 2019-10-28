package com.fy.fayou.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.search.adapter.FlowAdapter;
import com.fy.fayou.search.adapter.SearchAdapter;
import com.fy.fayou.search.bean.SearchEntity;
import com.fy.fayou.view.flow.FlowLayoutManager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/home/search")
public class SearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    SearchAdapter mAdapter;
    FlowAdapter mFlowAdapter;

    String[] sign = {"中华人民共和国宪法", "中华和国宪法", "中和国宪法", "中和法", "中华人民共和"};


    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter = new SearchAdapter());
        mAdapter.addHeaderView(getHeaderView());

        List<SearchEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            SearchEntity entity = new SearchEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);

        loadRootFragment(R.id.associate_fl, AssociateFragment.newInstance());
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_home_search;
    }

    private View getHeaderView() {
        View header = View.inflate(this, R.layout.item_search_history_header, null);
        RecyclerView recyclerView = header.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new FlowLayoutManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mFlowAdapter = new FlowAdapter());
        header.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.removeAllHeaderView();
            }
        });

        List<String> list = new ArrayList<>();
        for (String value : sign) {
            list.add(value);
        }

        mFlowAdapter.setNewData(list);

        return header;
    }

}
