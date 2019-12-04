package com.fy.fayou.forum.activity;

import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.forum.bean.PlateEntity;
import com.fy.fayou.forum.fragment.MenuContentFragment;
import com.fy.fayou.forum.fragment.MenuFragment;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.base.BaseFragment;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportHelper;

@Route(path = "/select/plate")
public class SelectPlateActivity extends BaseActivity {

    ArrayList<PlateEntity> mPlateEntityList = new ArrayList<>();

    @BindView(R.id.tv_plate)
    TextView tvPlate;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("选择板块").setLeftBackListener(v -> {
            finish();
        }).setRightTextListener(v -> {

        }, "下一步");
        getToolbarRightTextView().setTextColor(getResources().getColor(R.color.color_8f_ed4040));
    }

    @Override
    protected void initData() {

        EasyHttp.get(ApiUrl.FORUM_MY_FOLLOW)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<PlateEntity> list = ParseUtils.parseListData(s, "content", PlateEntity.class);
                            mPlateEntityList.addAll(list);

                            if (findFragment(MenuFragment.class) == null) {
                                MenuFragment menuListFragment = MenuFragment.newInstance(mPlateEntityList);
                                loadRootFragment(R.id.fl_list_container, menuListFragment);
                                // false:  不加入回退栈;  false: 不显示动画
                                if (!mPlateEntityList.isEmpty())
                                    loadRootFragment(R.id.fl_content_container, MenuContentFragment.newInstance(mPlateEntityList.get(0).id), false, false);
                            }
                        }
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_forum_select_plate;
    }

    public void switchContentFragment(String id) {
        MenuContentFragment fragment = MenuContentFragment.newInstance(id);
        BaseFragment baseFragment = SupportHelper.findFragment(getSupportFragmentManager(), MenuContentFragment.class);
        if (baseFragment != null) {
            baseFragment.replaceFragment(fragment, false);
        }
    }

    /**
     * @param selectedArray
     */
    public void setSelectedPlate(List<PlateEntity> selectedArray) {
        if (null != selectedArray) {
            if (selectedArray.isEmpty()) {
                tvPlate.setText("");
            } else {
                tvPlate.setText(selectedArray.get(0).name);
            }
        }
    }
}
