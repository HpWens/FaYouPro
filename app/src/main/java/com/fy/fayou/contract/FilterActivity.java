package com.fy.fayou.contract;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.contract.adapter.ExpandableItemAdapter;
import com.fy.fayou.contract.bean.Level1Item;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/contract/filter")
public class FilterActivity extends BaseActivity {

    @Autowired
    public String id = "";
    @Autowired
    public String name = "";
    @Autowired
    public String url = "";

    @BindView(R.id.recycler)
    RecyclerView recycler;

    ExpandableItemAdapter adapter;
    ArrayList<MultiItemEntity> list = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle(name);
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        setState(ViewState.LOADING);
        adapter = new ExpandableItemAdapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setAdapter(adapter);

        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                if (pos >= 0 && pos < adapter.getData().size()) {
                    MultiItemEntity entity = adapter.getData().get(pos);
                    if (entity instanceof Level1Item) {
                        Level1Item preLevel = (Level1Item) entity;
                        if (preLevel.childIndex == 0) {
                            outRect.top = RxImageTool.dip2px(15);
                        }
                    }
                }
            }
        });

        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        EasyHttp.get(ApiUrl.GET_TEMPLATE_TERMS)
                .params("contractTemplateId", id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        setState(ViewState.COMPLETED);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            try {
                                List<Level1Item> data = ParseUtils.parseListData(s, Level1Item.class);
                                for (Level1Item item : data) {
                                    int childIndex = 0;
                                    generateData(item, childIndex);
                                    list.add(item);
                                }
                                adapter.setNewData(list);
                            } catch (Exception e) {
                            }
                        }
                        setState(ViewState.COMPLETED);
                    }
                });
    }

    /**
     * 递归处理
     */
    private void generateData(Level1Item level, int index) {
        if (level.termsList != null && !level.termsList.isEmpty()) {
            for (Level1Item child : level.termsList) {
                child.childIndex = index;
                child.parentLevel = level;
                level.addSubItem(child);
                index++;
                generateData(child, index);
            }
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_contract_filter;
    }

    @OnClick({R.id.tv_clear, R.id.tv_down})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                if (adapter != null) {
                    adapter.clearSelectedArray();
                }
                break;
            case R.id.tv_down:
                List<Level1Item> data = adapter.getSelectedArray();
                if (data.isEmpty()) {
                    Toast.makeText(mContext, "至少选择一项条款", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuilder sb = new StringBuilder();
                String suffix = "";
                if (!data.isEmpty()) {
                    for (Level1Item lv : data) {
                        sb.append(lv.id + ",");
                    }
                    suffix = sb.substring(0, sb.length() - 1);
                }
                String h5url = url + "?ids=" + suffix;
                // if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(suffix)) {
                //     h5url = url.substring(0, url.lastIndexOf("/")) + "/" + suffix;
                // }
                ARoute.jumpH5(h5url, true, id, ARoute.TEMPLATE_TYPE, name);
                break;
        }
    }
}
