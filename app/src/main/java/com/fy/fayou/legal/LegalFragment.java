package com.fy.fayou.legal;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.legal.adapter.LegalAdapter;
import com.fy.fayou.legal.bean.JudgeEntity;
import com.fy.fayou.legal.bean.LegalEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.zhouyou.http.EasyHttp;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class LegalFragment extends BaseListFragment<LegalEntity> {

    RecyclerView mRecyclerView;
    LegalAdapter mAdapter;

    private String type;
    // 用来控制 列表 发布时间 标题字段
    private int preType;

    // 指导性意见筛选ID
    private String guideFilterId;

    private int mCollectType = ARoute.LEGAL_TYPE;

    private JudgeEntity mJudgeEntity = null;

    public static LegalFragment newInstance(int preType, String type, int collectType) {
        return newInstance(preType, type, collectType, null, "");
    }

    public static LegalFragment newInstance(int preType, String type, int collectType, JudgeEntity judgeEntity, String guideFilterId) {
        Bundle args = new Bundle();
        args.putString(Constant.Param.TYPE, type);
        args.putInt(Constant.Param.PRE_TYPE, preType);
        args.putSerializable(Constant.Param.ENTITY, judgeEntity);
        args.putString(Constant.Param.GUIDE_ID, guideFilterId);
        args.putInt(Constant.Param.COLLECT_TYPE, collectType);
        LegalFragment fragment = new LegalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(Constant.Param.TYPE, "");
            preType = bundle.getInt(Constant.Param.PRE_TYPE, 3);
            mCollectType = bundle.getInt(Constant.Param.COLLECT_TYPE, ARoute.LEGAL_TYPE);
            guideFilterId = bundle.getString(Constant.Param.GUIDE_ID);
            Serializable serializable = bundle.getSerializable(Constant.Param.ENTITY);
            if (serializable != null) {
                mJudgeEntity = (JudgeEntity) serializable;
            }
        }
        super.initView();
    }

    @Override
    protected RecyclerView getRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return mRecyclerView;
    }

    @Override
    protected MeiBaseAdapter<LegalEntity> getAdapter() {
        mAdapter = new LegalAdapter(preType, mCollectType);
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<LegalEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = null;
        if (mCollectType == ARoute.GUIDE_TYPE) {
            // 新增浏览记录
            HashMap<String, String> params = new HashMap<>();
            params.put("cardType", type);
            params.put("size", "20");
            params.put("type", TextUtils.isEmpty(guideFilterId) ? "0" : guideFilterId);
            params.put("page", (pageNo - 1) + "");
            JSONObject jsonObject = new JSONObject(params);
            observable = EasyHttp.post(ApiUrl.GET_CASE_LIST)
                    .upJson(jsonObject.toString())
                    .execute(String.class);
        } else if (mCollectType == ARoute.JUDGE_TYPE) {
            HashMap<String, String> params = new HashMap<>();
            params.put("judgementType", type);
            if (mJudgeEntity != null) {
                params.put("typeReason", mJudgeEntity.typeReason);
                params.put("typeCourtClass", mJudgeEntity.typeCourtClass);
                params.put("typeZone", mJudgeEntity.typeZone);
                params.put("typeSpnf", mJudgeEntity.typeSpnf);
                params.put("typeSpcx", mJudgeEntity.typeSpcx);
                params.put("typeBookType", mJudgeEntity.typeBookType);
            }
            params.put("size", "20");
            params.put("page", (pageNo - 1) + "");
            JSONObject jsonObject = new JSONObject(params);
            observable = EasyHttp.post(ApiUrl.GET_JUDGE_LIST)
                    .upJson(jsonObject.toString())
                    .execute(String.class);
        } else {
            observable = EasyHttp.get(preType == 3 ? ApiUrl.FIND_LEGAL_LIST : ApiUrl.FIND_JUDICIAL_LIST)
                    .params("type", type)
                    .params("size", "20")
                    .params("page", (pageNo - 1) + "")
                    .execute(String.class);
        }
        return getListByField(observable, "content", LegalEntity.class);
    }

    @Override
    public boolean canLoadMore() {
        return true;
    }

    @Override
    public boolean canPullToRefresh() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.comm_recycler;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected boolean loadOnShow() {
        return false;
    }
}
