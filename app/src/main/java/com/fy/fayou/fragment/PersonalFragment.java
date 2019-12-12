package com.fy.fayou.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.fy.fayou.FYApplication;
import com.fy.fayou.R;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseFragment;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_fan)
    TextView tvFan;
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_mes_num)
    TextView tvMesNum;


    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
    }

    @Override
    protected void initData() {
        tvExit.setVisibility(((FYApplication) getActivity().getApplication()).isDebug() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.personal_fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserService.getInstance().isLogin()) {
            // Accept: application/json
            EasyHttp.get(ApiUrl.USER_DETAIL).execute(new SimpleCallBack<String>() {
                @Override
                public void onError(ApiException e) {
                    ParseUtils.handlerError(e);
                }

                @Override
                public void onSuccess(String s) {
                    UserInfo user = ParseUtils.parseData(s, UserInfo.class);
                    if (null != user) {
                        user.token = UserService.getInstance().getToken();
                        UserService.getInstance().saveUser(user);
                        fillingData(user);
                    }
                }
            });

            requestUnReadNum();
        }
    }

    private void requestUnReadNum() {
        EasyHttp.get(ApiUrl.MESSAGE_UNREAD_NUMBER)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        try {
                            JSONObject json = new JSONObject(s);
                            if (json.has("num")) {
                                int num = json.optInt("num");
                                tvMesNum.setText(num > 99 ? "99+" : (num + ""));
                                tvMesNum.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
    }

    private void fillingData(UserInfo user) {
        tvName.setText(user.nickName == null ? "" : user.nickName);
        tvPublish.setText(user.comments + "");
        tvAttention.setText(user.followings + "");
        tvFan.setText(user.followers + "");
        tvPraise.setText(user.gives + "");
        tvPhone.setText(user.mobile == null ? "" : hideMobilePhone4(user.mobile));

        if (!TextUtils.isEmpty(user.avatar)) {
            Glide.with(getActivity()).
                    load(user.avatar).
                    apply(GlideOption.getAvatarOption()).
                    thumbnail(0.5f).
                    into(ivHeader);
        }
    }

    private String hideMobilePhone4(String mobile_phone) {
        if (mobile_phone.length() != 11) {
            return "";
        }
        return mobile_phone.substring(0, 3) + "****" + mobile_phone.substring(7, 11);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fl_mes, R.id.iv_set, R.id.header_layout, R.id.layout_publish, R.id.layout_attention, R.id.layout_fan,
            R.id.layout_praise, R.id.my_collect, R.id.my_history, R.id.my_phone, R.id.my_wechat, R.id.tv_exit, R.id.my_news
            , R.id.my_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_mes:
                // 跳转到消息
                ARouter.getInstance().build(Constant.PERSON_MESSAGE)
                        .navigation();
                break;
            case R.id.iv_set:
                // 跳转到设置
                ARouter.getInstance().build(Constant.PERSON_SETTING).navigation();
                break;
            case R.id.header_layout:
                // 跳转到编辑资料
                ARouter.getInstance().build(Constant.PERSON_EDIT)
                        .navigation();
                break;
            case R.id.layout_publish:
                ARouter.getInstance().build(Constant.MY_COMMENT)
                        .navigation();
                break;
            case R.id.layout_attention:
                ARouter.getInstance().build(Constant.MY_FOLLOW)
                        .navigation();
                break;
            case R.id.layout_fan:
                ARouter.getInstance().build(Constant.MY_FAN)
                        .navigation();
                break;
            case R.id.layout_praise:
                break;
            case R.id.my_collect:
                ARouter.getInstance().build(Constant.MY_COLLECT)
                        .navigation();
                break;
            case R.id.my_history:
                ARouter.getInstance().build(Constant.MY_HISTORY)
                        .navigation();
                break;
            case R.id.my_post:
                ARouter.getInstance().build(Constant.MY_POST)
                        .navigation();
                break;
            case R.id.my_phone:
                ARouter.getInstance().build(Constant.PERSON_RESET_PHONE)
                        .withString(Constant.Param.MOBILE, UserService.getInstance().getMobile())
                        .navigation();
                break;
            case R.id.my_wechat:
                break;
            case R.id.tv_exit:
                UserService.getInstance().clearUser();
                if (getActivity().getApplication() instanceof FYApplication) {
                    ((FYApplication) getActivity().getApplication()).clearTokenHeader();
                }
                break;
            case R.id.my_news:
                ARoute.jumpMyNews();
                break;
        }
    }
}
