package com.fy.fayou.person;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.meis.base.mei.base.BaseListActivity;
import com.meis.base.mei.entity.Result;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

@Route(path = "/person/message")
public class MessageActivity extends BaseListActivity {

    @BindView(R.id.recycler)
    SwipeRecyclerView recycler;

    MessageAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        super.initView();
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("消息");
        setLeftBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected RecyclerView getRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

                SwipeMenuItem addItem = new SwipeMenuItem(mContext).setBackgroundColor(getResources().getColor(R.color.color_ed4040))
                        .setText("删除")
                        .setTextSize(14)
                        .setTextColor(Color.WHITE)
                        .setWidth(RxImageTool.dp2px(100))
                        .setHeight(RxImageTool.dp2px(70));
                rightMenu.addMenuItem(addItem);
            }
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
    protected MeiBaseAdapter getAdapter() {
        mAdapter = new MessageAdapter();
        return mAdapter;
    }

    @Override
    protected Observable<Result<List>> getListObservable(int pageNo) {
        return null;
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
    protected void initData() {
        List<MessageEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            MessageEntity entity = new MessageEntity();
            list.add(entity);
        }
        mAdapter.setNewData(list);
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_person_message;
    }

}
