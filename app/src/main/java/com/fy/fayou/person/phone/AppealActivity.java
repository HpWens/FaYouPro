package com.fy.fayou.person.phone;

import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/appeal")
public class AppealActivity extends BaseActivity {

    @BindView(R.id.btn_appeal)
    Button btnAppeal;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("申诉");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_appeal;
    }

    @OnClick(R.id.btn_appeal)
    public void onClick() {
        requestContactService();
    }


    // 请求联系客服
    private void requestContactService() {
        EasyHttp.get(ApiUrl.CONTACT_SERVICE)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            if (json.has("mobile")) {
                                jumpTel(json.optString("mobile"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void jumpTel(String phoneNumber) {
        // 跳转到拨号界面，同时传递电话号码
        Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(Intent);
    }
}
