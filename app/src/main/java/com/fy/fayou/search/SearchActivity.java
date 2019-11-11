package com.fy.fayou.search;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.SearchResultEvent;
import com.fy.fayou.search.adapter.FlowAdapter;
import com.fy.fayou.search.adapter.SearchAdapter;
import com.fy.fayou.search.bean.SearchEntity;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.flow.FlowLayoutManager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxKeyboardTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/home/search")
public class SearchActivity extends BaseActivity {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_cancel)
    TextView tvSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.iv_clean)
    ImageView ivClean;

    SearchAdapter mAdapter;
    FlowAdapter mFlowAdapter;
    AssociateFragment associateFragment;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(mAdapter = new SearchAdapter());

        loadRootFragment(R.id.associate_fl, associateFragment = AssociateFragment.newInstance());

        recycler.post(() -> {
            hideFragment(R.id.associate_fl);
        });

        addHeaderView();
    }

    private void addHeaderView() {
        List<String> list = UserService.getInstance().getHistorySearch();
        if (list != null && !list.isEmpty()) {
            if (mAdapter.getHeaderLayoutCount() == 0) {
                mAdapter.addHeaderView(getHeaderView());
            }
            mFlowAdapter.setNewData(list);
        }
    }

    @Override
    protected void initData() {

        requestHot();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && ivClean.getVisibility() == View.GONE) {
                    ivClean.setVisibility(View.VISIBLE);
                    tvSearch.setText("搜索");
                    tvSearch.setTextColor(Color.parseColor("#A0A0A0"));
                } else if (TextUtils.isEmpty(s)) {
                    ivClean.setVisibility(View.GONE);
                    tvSearch.setText("取消");
                    tvSearch.setTextColor(Color.parseColor("#D2D2D2"));
                }
            }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                jumpSearchResult(etSearch.getText().toString());
            }
            return false;
        });

        ivClean.setOnClickListener(v -> {
            etSearch.setText("");
        });

        tvSearch.setOnClickListener(v -> {
            if (ivClean.getVisibility() == View.VISIBLE) {
                jumpSearchResult(etSearch.getText().toString());
            } else {
                finish();
            }
        });
    }

    private void jumpSearchResult(String key) {
        ARouter.getInstance().build(Constant.HOME_RESULT_SEARCH)
                .withString(Constant.Param.KEYWORD, key)
                .navigation();

        // 保存历史记录
        UserService.getInstance().addHistorySearch(key);
        addHeaderView();
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
        recyclerView.setAdapter(mFlowAdapter = new FlowAdapter((v, key) -> {
            etSearch.setText(key);
            jumpSearchResult(key);
        }));
        header.findViewById(R.id.iv_clear).setOnClickListener(v -> {
            UserService.getInstance().clearHistorySearch();
            mAdapter.removeAllHeaderView();
        });
        return header;
    }

    private void requestHot() {
        EasyHttp.get(ApiUrl.HOT_SEARCH)
                .params("page", "0")
                .params("size", "20")
                .baseUrl(Constant.BASE_URL6)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<SearchEntity> list = ParseUtils.parseListData(s, SearchEntity.class);
                            mAdapter.setNewData(list);
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(SearchResultEvent event) {
        if (event.code == SearchResultEvent.RESULT_CLEAN) {
            if (etSearch != null) {
                etSearch.setText("");
                etSearch.postDelayed(() -> {
                    RxKeyboardTool.showSoftInput(mContext, etSearch);
                }, 160);
            }
        } else if (event.code == SearchResultEvent.RESULT_FINISH) {
            finish();
        } else if (event.code == SearchResultEvent.RESULT_FOCUS) {
            if (etSearch != null) {
                etSearch.requestFocus();
                etSearch.setSelection(etSearch.getText().length());
                etSearch.postDelayed(() -> {
                    RxKeyboardTool.showSoftInput(mContext, etSearch);
                }, 160);
            }
        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }
}
