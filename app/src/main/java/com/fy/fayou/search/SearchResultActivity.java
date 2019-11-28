package com.fy.fayou.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.SearchResultEvent;
import com.fy.fayou.search.adapter.ResultAdapter;
import com.fy.fayou.search.bean.ColumnEntity;
import com.fy.fayou.search.bean.MenuEntity;
import com.fy.fayou.search.bean.SearchEntity;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.fy.fayou.search.result.ContentFragment;
import com.fy.fayou.search.result.MenuListFragment;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.base.BaseFragment;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportHelper;

@Route(path = "/home/result_search")
public class SearchResultActivity extends BaseActivity {

    @Autowired
    public String keyword = "";

    @BindView(R.id.et_search)
    TextView tVSearch;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    ResultAdapter adapter;
    List<SearchResultEntity> list = new ArrayList<>();

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
    }

    @Override
    protected void initData() {

        tVSearch.setText(keyword);

        tVSearch.setOnClickListener(v -> {
            EventBus.getDefault().post(new SearchResultEvent(SearchResultEvent.RESULT_FOCUS));
            finish();
        });

        tvCancel.setOnClickListener(v -> {
            EventBus.getDefault().post(new SearchResultEvent(SearchResultEvent.RESULT_FINISH));
            finish();
        });

        ivClose.setOnClickListener(v -> {
            EventBus.getDefault().post(new SearchResultEvent(SearchResultEvent.RESULT_CLEAN));
            finish();
        });

        // requestMenuListData();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new ResultAdapter(list, keyword));
        adapter.setOnLoadMoreListener(() -> {
        }, recyclerView);

        setState(ViewState.LOADING);
        requestResult();
    }

    private void requestResult() {
        EasyHttp.get(ApiUrl.GET_SEARCH_RESULT)
                .params("keyword", keyword)
                .params("userId", UserService.getInstance().getUserId())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        setState(ViewState.COMPLETED);
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<SearchEntity> data = ParseUtils.parseListData(s, SearchEntity.class);
                            list.clear();
                            int index = 0;
                            for (SearchEntity entity : data) {
                                SearchResultEntity resultEntity = new SearchResultEntity();
                                resultEntity.name = entity.name;
                                resultEntity.columnType = entity.type;
                                resultEntity.logo = entity.logo;
                                resultEntity.itemType = 1;
                                resultEntity.headerIndex = index;

                                if (entity.data != null && !entity.data.isEmpty()) {
                                    list.add(resultEntity);
                                    // 热门视频 小视频
                                    if (entity.type == 6) {
                                        SearchResultEntity videoEntity = new SearchResultEntity();
                                        videoEntity.videoList = entity.data;
                                        videoEntity.itemType = 4;
                                        list.add(videoEntity);
                                    } else {
                                        // 新闻资讯
                                        if (entity.type == 5) {
                                            for (SearchResultEntity sre : entity.data) {
                                                sre.itemType = 3;
                                                list.add(sre);
                                            }
                                        }
                                        // 合同
                                        else if (entity.type == 4) {
                                            for (int i = 0; i < entity.data.size(); i++) {
                                                SearchResultEntity sre = entity.data.get(i);
                                                sre.itemType = 2;
                                                sre.childIndex = i;
                                                sre.isLastChild = (i == entity.data.size() - 1);
                                                list.add(sre);
                                            }
                                        } else {
                                            for (SearchResultEntity sre : entity.data) {
                                                sre.columnType = entity.type;
                                                list.add(sre);
                                            }
                                        }
                                    }
                                }

                                index++;
                            }
                            adapter.setNewData(list);
                            adapter.loadMoreEnd();
                            setState(ViewState.COMPLETED);
                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_search_result;
    }

    /*************************************旧版逻辑************************************/
    public void switchContentFragment(String menu) {
        ContentFragment fragment = ContentFragment.newInstance(menu, keyword);
        BaseFragment baseFragment = SupportHelper.findFragment(getSupportFragmentManager(), ContentFragment.class);
        if (baseFragment != null) {
            baseFragment.replaceFragment(fragment, false);
        }
    }

    private void requestMenuListData() {
        EasyHttp.get(ApiUrl.SEARCH_RESULT_MENU)
                .params("keyword", keyword)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            MenuEntity entity = ParseUtils.parseData(s, MenuEntity.class);

                            ArrayList<ColumnEntity> columnArray = new ArrayList<>();
                            if (entity.legalProvisions != null) {
                                ColumnEntity column = new ColumnEntity();
                                column.name = "法律法规\n(" + entity.legalProvisions + ")";
                                column.param = "legalProvisions";
                                columnArray.add(column);
                            }
                            if (entity.judicialInterpretation != null) {
                                ColumnEntity column = new ColumnEntity();
                                column.name = "司法解释\n(" + entity.judicialInterpretation + ")";
                                column.param = "judicialInterpretation";
                                columnArray.add(column);
                            }
                            if (entity.newInfo != null) {
                                ColumnEntity column = new ColumnEntity();
                                column.name = "新闻\n(" + entity.newInfo + ")";
                                column.param = "newInfo";
                                columnArray.add(column);
                            }
                            if (entity.judgement != null) {
                                ColumnEntity column = new ColumnEntity();
                                column.name = "判决文书\n(" + entity.judgement + ")";
                                column.param = "judgement";
                                columnArray.add(column);
                            }
                            if (entity.caseInfo != null) {
                                ColumnEntity column = new ColumnEntity();
                                column.name = "案例\n(" + entity.caseInfo + ")";
                                column.param = "caseInfo";
                                columnArray.add(column);
                            }

                            if (findFragment(MenuListFragment.class) == null) {
                                MenuListFragment menuListFragment = MenuListFragment.newInstance(columnArray);
                                loadRootFragment(R.id.fl_list_container, menuListFragment);
                                // false:  不加入回退栈;  false: 不显示动画
                                loadRootFragment(R.id.fl_content_container, ContentFragment.newInstance(columnArray.get(0).param, keyword), false, false);
                            }

                        }
                    }
                });
    }
}
