package com.fy.fayou.detail.dialog;


import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.fy.fayou.R;
import com.fy.fayou.detail.adapter.RelatedAdapter;
import com.fy.fayou.detail.bean.LawBean;
import com.vondear.rxui.view.dialog.RxDialog;

import java.util.List;

public class RelatedDialog extends RxDialog {

    RelatedAdapter adapter;
    RecyclerView recyclerView;
    List<LawBean> data;

    public RelatedDialog(FragmentActivity context, List<LawBean> list) {
        super(context);
        this.data = list;
        initView();
    }

    public void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_detail_related, null);
        recyclerView = dialogView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter = new RelatedAdapter(data));
        dialogView.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            dismiss();
        });
        setContentView(dialogView);
        mLayoutParams.gravity = Gravity.BOTTOM;
        mLayoutParams.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }


}
