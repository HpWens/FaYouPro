package com.fy.fayou.person.phone;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxDataTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rokudol.com.pswtext.PwdText;

@Route(path = "/person/verify_code")
public class VerifyCodeActivity extends BaseActivity {

    @Autowired
    public String mobile = "";

    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.psw)
    PwdText psw;
    @BindView(R.id.tv_obtain)
    TextView tvObtain;
    @BindView(R.id.btn_next)
    Button btnNext;

    private String verifyCode = "";
    private int reSendTime = MAX_TIME;
    private static final int MAX_TIME = 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (reSendTime < 0) {
                    reSendTime = MAX_TIME;
                    tvObtain.setText("再次获取验证码");
                    tvObtain.setEnabled(true);
                    tvObtain.setTextColor(getResources().getColor(R.color.color_ed4040));
                } else {
                    tvObtain.setText(getString(R.string.again_get_verify_code, reSendTime));
                    reSendTime--;
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("请输入验证码");
        setLeftBackListener((v) -> finish());
    }

    @Override
    protected void initData() {
        String formatMobile = "";
        if (!TextUtils.isEmpty(mobile)) {
            formatMobile = "+86" + RxDataTool.hideMobilePhone4(mobile);
            requestVerifyCode();
        }
        tvHint.setText(getString(R.string.verify_mobile_hint, formatMobile));

        psw.setTextWatcher((password, isFinishInput) -> {
            if (!btnNext.isEnabled()) {
                btnNext.setEnabled(true);
            }
            verifyCode = password;
        });

        handler.sendEmptyMessage(1);

    }

    private void requestVerifyCode() {
        EasyHttp.get(ApiUrl.SEND_VERIFY_CODE)
                .params("phoneNumber", mobile)
                .params("type", "4")
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_verify_code;
    }

    @OnClick({R.id.tv_obtain, R.id.btn_next, R.id.tv_un_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_obtain:
                tvObtain.setEnabled(false);
                tvObtain.setTextColor(getResources().getColor(R.color.color_d2d2d2));
                handler.sendEmptyMessage(1);
                if (TextUtils.isEmpty(mobile)) {
                    requestVerifyCode();
                }
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(verifyCode)) {
                    Toast.makeText(mContext, "输入的验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    requestBindMobile();
                }
                break;
            case R.id.tv_un_verify:
                ARouter.getInstance().build(Constant.PERSON_APPEAL)
                        .navigation();
                break;
        }
    }

    private void requestBindMobile() {
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", verifyCode);
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.BIND_MOBILE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String s) {
                        ARouter.getInstance().build(Constant.PERSON_PHONE_BIND_SUCCESS)
                                .withString(Constant.Param.MOBILE, mobile)
                                .navigation();
                        finish();
                    }
                });
    }
}
