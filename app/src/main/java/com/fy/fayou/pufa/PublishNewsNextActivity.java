package com.fy.fayou.pufa;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.pufa.adapter.ArticleLabelAdapter;
import com.fy.fayou.pufa.bean.LabelEntity;
import com.fy.fayou.pufa.dialog.LabelDialog;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


@Route(path = "/news/publish_next")
public class PublishNewsNextActivity extends BaseActivity {

    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_add_cover)
    ImageView ivAddCover;
    @BindView(R.id.et_origin)
    EditText etOrigin;
    @BindView(R.id.et_author)
    EditText etAuthor;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    ArticleLabelAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);


    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new GridLayoutManager(this, 3));
        recycler.setAdapter(mAdapter = new ArticleLabelAdapter(view -> {
            LabelDialog dialog = new LabelDialog(this);
            dialog.show();
        }));

        List<LabelEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            LabelEntity entity = new LabelEntity();
            list.add(entity);
        }

        mAdapter.setNewData(list);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_news_publish_next;
    }

    @OnClick({R.id.ic_close, R.id.tv_right, R.id.iv_add_cover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_close:
                break;
            case R.id.tv_right:
                break;
            case R.id.iv_add_cover:
                break;
        }
    }
}
