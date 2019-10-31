package com.meis.base.mei.base;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.constant.DataConstants;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.status.ViewState;
import com.meis.base.mei.utils.ListUtils;
import com.meis.base.mei.utils.ParameterizedTypeImpl;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * desc:
 * author: ws
 * date: 2018/4/19.
 */

public abstract class BaseListFragment<T> extends BaseFragment {
    protected MeiBaseAdapter<T> mAdapter;

    private int mPageNo;

    @Override
    protected void initView() {
        mAdapter = getAdapter();
        RecyclerView recyclerView = getRecyclerView();
        if (mAdapter == null || recyclerView == null) {
            return;
        }
        recyclerView.setAdapter(mAdapter);
        if (canLoadMore()) {
            mAdapter.setEnableLoadMore(true);
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    loadPage(mAdapter.getPageCount() + 1);
                }
            }, recyclerView);
        }
        //是否首次加载 是否每次显示加载
        if (loadOnInit() || !loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (loadOnShow()) {
            loadPage(DataConstants.FIRST_PAGE);
        }
    }

    @Override
    public void onRefreshing() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    public void setPageNo(int pageNo) {
        this.mPageNo = pageNo;
    }

    protected void reload() {
        loadPage(DataConstants.FIRST_PAGE);
    }

    /**
     * 当前页数加载数据
     *
     * @param pageNo
     */
    protected void loadPage(final int pageNo) {
        if (mAdapter == null) {
            return;
        }
        mPageNo = pageNo;
        Observable<Result<List<T>>> observable = getListObservable(pageNo);
        if (observable == null) return;
        observable.filter(new Predicate<Result<List<T>>>() {
            @Override
            public boolean test(Result<List<T>> listResult) throws Exception {
                return pageNo == mPageNo;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Result<List<T>>>bindToLifecycle())
                .subscribe(new Observer<Result<List<T>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<List<T>> listResult) {
                        setRefreshing(false);
                        onDataLoaded(pageNo, listResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        setRefreshing(false);
                        if (pageNo == DataConstants.FIRST_PAGE) {
                            if (keepEmptyOnFail() && mAdapter.getDataCount() > 0) {
                                return;
                            }
                            setState(ViewState.EMPTY);
                        } else {
                            mAdapter.loadMoreFail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * @param pageNo
     * @param result
     */
    protected void onDataLoaded(int pageNo, Result<List<T>> result) {
        List<T> data = result.data;
        if (pageNo == DataConstants.FIRST_PAGE) {
            if (ListUtils.isEmpty(data)) {
                setState(ViewState.EMPTY);
            } else {
                mAdapter.setNewData(data);
                if (data.size() < getPageSize()) {
                    mAdapter.setEnableLoadMore(false);
                } else {
                    mAdapter.setEnableLoadMore(true);
                }
                setState(ViewState.COMPLETED);
            }
        } else {
            if (ListUtils.isEmpty(data)) {
                mAdapter.loadMoreEnd(mAdapter.getData().size() < getPageSize() / 2);
            } else {
                mAdapter.addData(data);
                if (data.size() < getPageSize()) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }
        }
    }

    protected boolean loadOnInit() {
        return false;
    }

    /**
     * 每次界面重新显示的时候 是否加载数据
     * true 加载
     * false 不加载
     *
     * @return
     */
    protected boolean loadOnShow() {
        return true;
    }

    /**
     * false 默认第一页数据为空或加载失败 显示空界面
     * true 不显示空界面
     *
     * @return false
     */
    protected boolean keepEmptyOnFail() {
        return false;
    }

    /**
     * 可以重写该方法 返回每页大小 默认返回20
     *
     * @return
     */
    protected int getPageSize() {
        return DataConstants.DEFAULT_PAGE_SIZE;
    }

    protected abstract RecyclerView getRecyclerView();

    protected abstract MeiBaseAdapter<T> getAdapter();

    protected abstract Observable<Result<List<T>>> getListObservable(int pageNo);

    public abstract boolean canLoadMore();

    @Override
    public abstract boolean canPullToRefresh();

    private List<T> parseListData(String json, Class<T> clazz) {
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        return new Gson().fromJson(json, listType);
    }

    protected Observable<Result<List<T>>> getListByField(final String json, final String field, final Class<T> clazz) {
        final Result<List<T>> result = new Result<>();
        return Observable.just(json).map(new Function<String, Result<List<T>>>() {
            @Override
            public Result<List<T>> apply(String s) throws Exception {
                if (!TextUtils.isEmpty(s)) {
                    JSONObject json = new JSONObject(s);
                    if (json != null && field != null && json.has(field)) {
                        List<T> list = parseListData(json.optString(field), clazz);
                        result.data = list;
                    }
                }
                return result;
            }
        });
    }

    protected Observable<Result<List<T>>> getListByField(Observable<String> observable, final String field, final Class<T> clazz) {
        final Result<List<T>> result = new Result<>();
        return observable.map(new Function<String, Result<List<T>>>() {
            @Override
            public Result<List<T>> apply(String s) throws Exception {
                if (!TextUtils.isEmpty(s)) {
                    JSONObject json = new JSONObject(s);
                    if (json != null && field != null && json.has(field)) {
                        List<T> list = parseListData(json.optString(field), clazz);
                        result.data = list;
                    }
                }
                return result;
            }
        });
    }
}
