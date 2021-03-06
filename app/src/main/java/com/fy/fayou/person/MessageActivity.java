package com.fy.fayou.person;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.person.fragment.MessageListFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.ButterKnife;

@Route(path = "/person/message")
public class MessageActivity extends BaseActivity {

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("消息");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {
        if (findFragment(MessageListFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MessageListFragment.newInstance());
        }
    }


    @Override
    protected int layoutResId() {
        return R.layout.activity_person_message;
    }
}
