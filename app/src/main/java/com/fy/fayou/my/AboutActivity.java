package com.fy.fayou.my;

import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/about/my")
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("关于我们");
        setLeftBackListener(v -> finish());
    }

    @Override
    protected void initData() {

        // tvContent.setText(Html.fromHtml(html));
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_about;
    }

}
