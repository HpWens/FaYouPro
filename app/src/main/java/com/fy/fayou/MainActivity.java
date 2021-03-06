package com.fy.fayou;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.ExitLoginEvent;
import com.fy.fayou.fragment.ForumFragment;
import com.fy.fayou.fragment.HomeFragment;
import com.fy.fayou.fragment.LearnFragment;
import com.fy.fayou.fragment.PersonalFragment;
import com.fy.fayou.view.BottomBar;
import com.fy.fayou.view.BottomBarTab;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.yokeyword.fragmentation.ISupportFragment;

@Route(path = "/fayou/main")
public class MainActivity extends BaseActivity {

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;
    private BottomBar mBottomBar;
    private ISupportFragment[] mFragments = new ISupportFragment[4];

    // 普法类型
    public int learnTabPos = -1;

    private ImageView mIvGuide;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        learnTabPos = intent.getIntExtra(Constant.Param.POSITION, -1);
        if (learnTabPos != -1) {
            mBottomBar.setCurrentItem(1);
            ((LearnFragment) mFragments[SECOND]).switchTab(learnTabPos);
            learnTabPos = -1;
        }
    }

    @Override
    protected void initView() {
        Eyes.translucentStatusBar(this, true, true);
        mIvGuide = findViewById(R.id.iv_guide);
        ISupportFragment firstFragment = findFragment(HomeFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFragment.newInstance();
            mFragments[SECOND] = LearnFragment.newInstance();
            mFragments[THIRD] = ForumFragment.newInstance();
            mFragments[FOURTH] = PersonalFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(LearnFragment.class);
            mFragments[THIRD] = findFragment(ForumFragment.class);
            mFragments[FOURTH] = findFragment(PersonalFragment.class);
        }

        mBottomBar = findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(this, R.mipmap.home_bottom_home, getString(R.string.bottom_home_name),
                getResources().getColor(R.color.bottom_bar_text_normal), R.mipmap.home_bottom_home_selected, getResources().getColor(R.color.bottom_bar_text_selected)))
                .addItem(new BottomBarTab(this, R.mipmap.home_bottom_learn, getString(R.string.bottom_learning_name),
                        getResources().getColor(R.color.bottom_bar_text_normal), R.mipmap.home_bottom_learn_selected, getResources().getColor(R.color.bottom_bar_text_selected)))
                .addItem(new BottomBarTab(this, R.mipmap.home_bottom_forum, getString(R.string.bottom_forum_name),
                        getResources().getColor(R.color.bottom_bar_text_normal), R.mipmap.home_bottom_forum_selected, getResources().getColor(R.color.bottom_bar_text_selected)))
                .addItem(new BottomBarTab(this, R.mipmap.home_bottom_personal, getString(R.string.bottom_personal_name),
                        getResources().getColor(R.color.bottom_bar_text_normal), R.mipmap.home_bottom_personal_selected, getResources().getColor(R.color.bottom_bar_text_selected)));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);

                if (position == 1 && !UserService.getInstance().isShowLearnGuide(mContext)) {
                    mIvGuide.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                if (position == 0 && mFragments[0] instanceof HomeFragment) {
                    ((HomeFragment) mFragments[0]).offsetTop();
                }
            }
        });

        mIvGuide.setOnClickListener(v -> {
            UserService.getInstance().setLearnGuide(mContext, true);
            mIvGuide.setVisibility(View.GONE);
        });
    }

    @Override
    protected void initData() {
//        ARouter.getInstance()
//                .build(Constant.EMAIL_LOGIN)
//                .withString(Constant.Param.ARTICLE_ID, "110328945139777536")
//                .navigation();



//        new ForceUpdateDialog(mContext).setOnItemClickListener(new ForceUpdateDialog.OnItemClickListener() {
//            @Override
//            public void onCancel(ForceUpdateDialog dialog) {
//                dialog.dismiss();
//                finish();
//            }
//
//            @Override
//            public void onSure(ForceUpdateDialog dialog) {
//                dialog.dismiss();
//
//                String code = "2.0.1";
//                String url = "http://qny.zhdfxm.com/zhihe.apk";
//
//                EasyHttp.downLoad(url)
//                        .savePath(RxFileTool.getSDCardPath() + "/apk")
//                        .saveName(code.hashCode() + ".apk")
//                        .execute(new DownloadProgressCallBack<String>() {
//                            @Override
//                            public void update(long bytesRead, long contentLength, boolean done) {
//                                int progress = (int) (bytesRead * 100 / contentLength);
//                            }
//
//                            @Override
//                            public void onComplete(String path) {
//                                AppUtils.installAPK(mContext, new File(path));
//                            }
//
//                            @Override
//                            public void onStart() {
//
//                            }
//
//                            @Override
//                            public void onError(ApiException e) {
//
//                            }
//                        });
//
//            }
//        }).show();
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExitLoginEvent(ExitLoginEvent event) {
        if (mBottomBar != null) {
            mBottomBar.setCurrentItem(0);
        }
    }

    private long mFirstTime;

    @Override
    public void onBackPressedSupport() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mFirstTime > 2000) {
            Toast.makeText(mContext, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mFirstTime = secondTime;
        } else {
            super.onBackPressedSupport();
        }
    }
}
