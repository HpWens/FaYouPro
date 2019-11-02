package com.fy.fayou.pufa.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.pufa.bean.LabelEntity;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxui.view.dialog.RxDialog;

import java.util.ArrayList;
import java.util.List;

public class LabelDialog extends RxDialog {

    RecyclerView mRecyclerView;
    LabelAdapter mAdapter;

    public LabelDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public LabelDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public LabelDialog(Context context) {
        super(context);
        initView();
    }

    public LabelDialog(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_article_publish_label, null);
        mRecyclerView = dialogView.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setAdapter(mAdapter = new LabelAdapter());
        setContentView(dialogView);

        List<LabelEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LabelEntity entity = new LabelEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);

        mLayoutParams.width = (int) (RxDeviceTool.getScreenWidth(getContext()) * 0.8f);

    }

}
