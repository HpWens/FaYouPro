package com.fy.fayou.search;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.search.fragment.MoreSearchResultFragment;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;


@Route(path = "/more/search_result")
public class MoreSearchResultActivity extends BaseActivity {

    @Autowired(name = "type")
    public int moduleType;

    @Autowired(name = "keyword")
    public String keyword;

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        setLeftBackListener(v -> {
            finish();
        }).setToolBarCenterTitle(getTitleByType(moduleType));
    }

    @Override
    protected void initData() {
        findViewById(R.id.top_line).setVisibility(View.GONE);
        findViewById(R.id.top_space_line).setVisibility(View.VISIBLE);

        if (findFragment(MoreSearchResultFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MoreSearchResultFragment.newInstance(moduleType, keyword));
        }
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_comm_list;
    }

    /**
     * 1 法律      * 2 司法解释      * 3 裁判文书      * 4 指导性案例      * 5 合同模板      * 6 普法天地  * 7论坛帖子
     *
     * @param type
     * @return
     */
    private String getTitleByType(int type) {
        switch (type) {
            case 1:
                return "法律法规";
            case 2:
                return "司法解释";
            case 4:
                return "指导性意见";
            case 3:
                return "裁判文书";
            case 5:
                return "合同模板";
            case 6:
                return "普法天地";
        }
        return "法律法规";
    }
}
