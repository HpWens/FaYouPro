package com.fy.fayou.person.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.person.MessageAdapter;
import com.fy.fayou.person.MessageEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListFragment;
import com.meis.base.mei.entity.Result;
import com.vondear.rxtool.RxImageTool;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhouyou.http.EasyHttp;

import java.util.List;

import io.reactivex.Observable;

public class MessageListFragment extends BaseListFragment<MessageEntity> {

    MessageAdapter adapter;
    SwipeRecyclerView recycler;

    public static MessageListFragment newInstance() {
        Bundle args = new Bundle();
        MessageListFragment fragment = new MessageListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView getRecyclerView() {
        recycler = getView().findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setSwipeMenuCreator((leftMenu, rightMenu, position) -> {

            SwipeMenuItem addItem = new SwipeMenuItem(getActivity()).setBackgroundColor(getResources().getColor(R.color.color_ed4040))
                    .setText("删除")
                    .setTextSize(14)
                    .setTextColor(Color.WHITE)
                    .setWidth(RxImageTool.dp2px(100))
                    .setHeight(RxImageTool.dp2px(70));
            rightMenu.addMenuItem(addItem);
        });
        recycler.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
                menuBridge.closeMenu();
                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();

            }
        });
        return recycler;
    }

    @Override
    protected MeiBaseAdapter<MessageEntity> getAdapter() {
        mAdapter = new MessageAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List<MessageEntity>>> getListObservable(int pageNo) {
        Observable<String> observable = EasyHttp.get(ApiUrl.MESSAGE_CENTER)
                .params("page", (pageNo - 1) + "")
                .params("size", "20")
                .execute(String.class);
        return getListByField(observable, "content", MessageEntity.class);
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
        return R.layout.comm_swipe_recycler;
    }
}
