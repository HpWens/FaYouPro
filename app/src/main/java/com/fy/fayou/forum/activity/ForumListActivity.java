package com.fy.fayou.forum.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.tablayout.SlidingTabLayout;
import com.fy.fayou.R;
import com.fy.fayou.forum.adapter.ForumListVPAdapter;
import com.fy.fayou.view.HomeViewpager;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/forum/list")
public class ForumListActivity extends BaseActivity {

    private final String[] mTitles = {
            "最新回复", "最新发帖", "热门"
    };

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.iv_avatar_first)
    CircleImageView ivAvatarFirst;
    @BindView(R.id.iv_avatar_second)
    CircleImageView ivAvatarSecond;
    @BindView(R.id.iv_avatar_three)
    CircleImageView ivAvatarThree;
    @BindView(R.id.more_plate_layout)
    LinearLayout morePlateLayout;
    @BindView(R.id.tab)
    SlidingTabLayout tab;
    @BindView(R.id.viewpager)
    HomeViewpager viewpager;

    ForumListVPAdapter mAdapter;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setRightMoreRes(R.mipmap.forum_publish_black_ic).setLeftBackListener(v -> {
            finish();
        }).setRightMoreListener(v -> {

        });
    }

    @Override
    protected void initData() {

        viewpager.setAdapter(mAdapter = new ForumListVPAdapter(getSupportFragmentManager(), mTitles));
        tab.setViewPager(viewpager);

        mAdapter.setOnScrollClashListener(isScroll -> viewpager.setScroll(isScroll));
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_forum_detail;
    }

    @OnClick({R.id.tv_follow, R.id.more_plate_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_follow:
                break;
            case R.id.more_plate_layout:
                break;
        }
    }
}
