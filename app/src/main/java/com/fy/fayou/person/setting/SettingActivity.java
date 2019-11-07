package com.fy.fayou.person.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.ExitLoginEvent;
import com.fy.fayou.utils.GlideCacheUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/person/setting")
public class SettingActivity extends BaseActivity {

    @BindView(R.id.recommend_layout)
    LinearLayout recommendLayout;
    @BindView(R.id.suggest_layout)
    LinearLayout suggestLayout;
    @BindView(R.id.skin_btn)
    SwitchButton skinBtn;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_service)
    TextView tvService;
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.about_layout)
    LinearLayout aboutLayout;
    @BindView(R.id.tv_exit)
    TextView tvExit;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("设置");
        setLeftBackListener(v -> finish());
    }


    @Override
    protected void initData() {

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.recommend_layout, R.id.suggest_layout, R.id.tv_cache, R.id.tv_service, R.id.tv_praise,
            R.id.about_layout, R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend_layout:
                requestInvite();
                break;
            case R.id.suggest_layout:
                ARouter.getInstance().build(Constant.PERSON_SUGGEST).navigation();
                break;
            case R.id.tv_cache:
                GlideCacheUtil.getInstance().clearImageAllCache(mContext);
                RxToast.normal("清理成功");
                break;
            case R.id.tv_service:
                requestContactService();
                break;
            case R.id.tv_praise:
                RxToast.normal("敬请期待~");
                break;
            case R.id.about_layout:
                ARouter.getInstance().build(Constant.ABOUT_MY).navigation();
                break;
            case R.id.tv_exit:
                UserService.getInstance().clearUser();
                ((FYApplication) getApplication()).clearTokenHeader();
                EventBus.getDefault().post(new ExitLoginEvent());
                finish();
                break;
        }
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

    private void requestInvite() {
        EasyHttp.get(ApiUrl.PUBLIC_INVITE)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            if (json.has("address")) {
                                createShare(json.optString("address"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void createShare(String content) {
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
        share_intent.setType("text/plain");//设置分享内容的类型
        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");//添加分享内容标题
        share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
        share_intent = Intent.createChooser(share_intent, "分享");
        startActivity(share_intent);
    }

    public void jumpTel(String phoneNumber) {
        // 跳转到拨号界面，同时传递电话号码
        Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(Intent);
    }
}
