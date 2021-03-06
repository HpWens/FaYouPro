package com.fy.fayou.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserBean;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.event.LoginSuccessOrExitEvent;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.utils.RegexUtils;
import com.fy.fayou.utils.SocialUtil;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxAnimationTool;
import com.vondear.rxtool.RxEncryptTool;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxtool.RxNetTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import net.arvin.socialhelper.SocialHelper;
import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.entities.ThirdInfoEntity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/email/login")
public class EmailLoginActivity extends BaseActivity implements SocialLoginCallback {

    @Autowired(name = "origin")
    public int jumpOrigin = 0;

    public static final String ORIGIN = "origin";
    // 首页-个人
    public static final int HOME_PERSONAL_ORIGIN = 0X0003;

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.iv_clean_phone)
    ImageView ivCleanPhone;
    @BindView(R.id.iv_clean_email)
    ImageView ivCleanEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.service)
    LinearLayout service;
    @BindView(R.id.root)
    RelativeLayout root;

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private float scale = 0.6f; //logo缩放比例

    private SocialHelper socialHelper;

    private static final String PASS_ENCRYPT_SUFFIX = "zhdfxm";

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);

        screenHeight = this.getResources().getDisplayMetrics().heightPixels; //获取屏幕高度
        keyHeight = screenHeight / 3; //弹起高度为屏幕高度的1/3
    }

    @Override
    protected void initData() {
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString().trim()) && ivCleanPhone.getVisibility() == View.GONE) {
                    ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s.toString().trim())) {
                    ivCleanPhone.setVisibility(View.GONE);
                }

            }
        });

        etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !TextUtils.isEmpty(etEmail.getText().toString().trim())) {
                ivCleanPhone.setVisibility(View.VISIBLE);
            } else {
                ivCleanPhone.setVisibility(View.GONE);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(s.toString().trim()) && ivCleanEmail.getVisibility() == View.GONE) {
                    ivCleanEmail.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s.toString().trim())) {
                    ivCleanEmail.setVisibility(View.GONE);
                }
            }
        });

        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                ivCleanEmail.setVisibility(View.VISIBLE);
            } else {
                ivCleanEmail.setVisibility(View.GONE);
            }
        });

        scrollView.setOnTouchListener((v, event) -> true);
        scrollView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
          /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
          现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
            if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                Log.e("wenzhihao", "up------>" + (oldBottom - bottom));
                int dist = content.getBottom() - bottom;
                if (dist > 0) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", 0.0f, -dist);
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    RxAnimationTool.zoomIn(logo, 0.6f, dist);
                }
                service.setVisibility(View.INVISIBLE);

            } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                Log.e("wenzhihao", "down------>" + (bottom - oldBottom));
                if ((content.getBottom() - oldBottom) > 0) {
                    ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(content, "translationY", content.getTranslationY(), 0);
                    mAnimatorTranslateY.setDuration(300);
                    mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                    mAnimatorTranslateY.start();
                    //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                    RxAnimationTool.zoomOut(logo, 0.6f);
                }
                service.setVisibility(View.VISIBLE);
            }
        });

        socialHelper = SocialUtil.INSTANCE.socialHelper;
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_email_login;
    }

    @OnClick({R.id.iv_clean_phone, R.id.btn_login, R.id.iv_wechat, R.id.iv_clean_email,
            R.id.iv_qq, R.id.iv_weibo, R.id.tv_protocol, R.id.tv_phone_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                etEmail.setText("");
                break;
            case R.id.iv_clean_email:
                etPassword.setText("");
                break;
            case R.id.btn_login:
                if (!RxNetTool.isAvailable(this)) {
                    Toast.makeText(mContext, "网络连接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                RxKeyboardTool.hideSoftInput(mContext);
                if (checkMobile() && checkVerifyCode()) {
                    // 请求接口
                    etEmail.postDelayed(() -> {
                        requestLogin(etEmail.getText().toString(), etPassword.getText().toString());
                    }, 300);
                }
                break;
            case R.id.iv_wechat:
                socialHelper.loginWX(this, this);
                break;
            case R.id.iv_qq:
            case R.id.iv_weibo:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_protocol:
                ARoute.jumpH5("http://fayou-h5.zhdfxm.com/privacy");
                break;
            case R.id.tv_phone_login:
                ARoute.jumpPhoneLogin();
                finish();
                break;
        }
    }

    private void requestVerifyCode() {
        EasyHttp.get(ApiUrl.SEND_VERIFY_CODE)
                .params("phoneNumber", "" + etEmail.getText().toString())
                .params("type", "2")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                    }
                });
    }

    private boolean checkMobile() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            RxToast.normal("请输入邮箱地址");
            return false;
        }

        if (!RegexUtils.checkEmail(email)) {
            RxToast.normal("请输入正确的邮箱地址");
            return false;
        }

        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            RxToast.normal("请输入密码");
            return false;
        }
        return true;
    }

    private boolean checkVerifyCode() {
        String verifyCode = etPassword.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {
            RxToast.normal("请输入短信验证码");
            return false;
        }
        return true;
    }

    private void requestLogin(String email, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("emailAddr", email);
        // password+"zhdfxm"
        // 用这个字符串，算md5,再传到后端
        params.put("password", RxEncryptTool.encryptMD5ToString(password + PASS_ENCRYPT_SUFFIX).toLowerCase());
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.EMAIL_LOGIN)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                        if (e.getCode() == 500) {
                            ParseUtils.handlerApiError(e, error -> {
                                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }

                    @Override
                    public void onSuccess(String s) {
                        handlerLoginData(s);
                    }
                });

    }

    /**
     * @param s
     */
    private void handlerLoginData(String s) {
        UserBean userBean = ParseUtils.parseData(s, UserBean.class);
        if (userBean != null && userBean.user != null) {
            UserInfo userInfo = userBean.user;
            userInfo.token = userBean.token;
            UserService.getInstance().saveUser(userInfo);
            // 第一步添加token
            if (getApplication() instanceof FYApplication) {
                ((FYApplication) getApplication()).addEasyTokenHeader();
            }

            // 发送登录成功事件
            EventBus.getDefault().post(new LoginSuccessOrExitEvent());

            // 判定是否设置昵称
            if (TextUtils.isEmpty(userInfo.nickName)) {
                ARouter.getInstance().build(Constant.CREATE_NICKNAME).navigation();
            }

            finish();
        }
    }

    @Override
    public void loginSuccess(ThirdInfoEntity info) {
        if (info.getPlatform().equals(ThirdInfoEntity.PLATFORM_WX)) {
            requestWXLogin(info);
        }
    }

    // 微信登陆
    private void requestWXLogin(ThirdInfoEntity info) {
        HashMap<String, String> params = new HashMap<>();
        params.put("unionId", info.getUnionId());
        params.put("openId", info.getOpenId());
        params.put("nickname", info.getNickname());
        params.put("sex", info.getSex().equals("M") ? "0" : "1");
        params.put("avatar", info.getAvatar());
        params.put("platform", info.getPlatform());
        JSONObject jsonObject = new JSONObject(params);
        EasyHttp.post(ApiUrl.WECHAT_LOGIN)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        handlerLoginData(s);
                    }
                });
    }

    @Override
    public void socialError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socialHelper != null) {
            socialHelper.clear();
        }
    }

    //用处：qq登录和分享回调，以及微博登录回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (socialHelper != null) {//qq分享如果选择留在qq，通过home键退出，再进入app则不会有回调
            socialHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
