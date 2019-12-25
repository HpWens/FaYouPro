package com.fy.fayou.detail.dialog;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UserService;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseDialog;
import com.vondear.rxtool.RxKeyboardTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.HashMap;

public class BottomCommentDialog extends BaseDialog {

    EditText mEtHint;
    TextView mTvObj;
    TextView mTvPublish;

    // 评论列表所在索引
    private int position = 0;
    private String userName = "";
    private String articleId = "";
    private String parentId = "";

    private boolean isForum;
    private String reUserId;

    private OnPublishListener mListener;

    public BottomCommentDialog setParams(String userName, String articleId, String parentId, int position) {
        this.position = position;
        this.userName = userName;
        this.articleId = articleId;
        if (articleId != null && articleId.equals("0")) {
            this.articleId = "";
        }
        this.parentId = parentId;
        return this;
    }

    public BottomCommentDialog() {
    }

    public BottomCommentDialog setReUserId(String reUserId) {
        this.reUserId = reUserId;
        return this;
    }

    public BottomCommentDialog setForum(boolean forum) {
        isForum = forum;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bottom_comment;
    }

    @Override
    protected void initView() {
        mEtHint = findViewById(R.id.et_hint);
        mTvObj = findViewById(R.id.tv_obj);
        mTvPublish = findViewById(R.id.tv_send);
        mEtHint.post(() -> {
            mEtHint.requestFocus();
            RxKeyboardTool.showSoftInput(getActivity(), mEtHint);
        });
    }

    @Override
    protected void initData() {
        mTvObj.setText(TextUtils.isEmpty(userName) ? ("正在评论 " + UserService.getInstance().getNickName()) : ("正在回复 " + userName));
        mEtHint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvPublish.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
            }
        });

        mTvPublish.setOnClickListener(v -> {
            mTvPublish.setEnabled(false);
            requestPublishComment();
        });
    }

    // 请求发表评论
    private void requestPublishComment() {
        String content = mEtHint.getText().toString().trim();
        HashMap<String, String> params = new HashMap<>();
        params.put(isForum ? "postId" : "articleId", articleId);
        params.put("content", content);
        params.put("parentId", parentId);
        if (isForum && !TextUtils.isEmpty(reUserId)) {
            params.put("reUserId", reUserId);
        }
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(isForum ? ApiUrl.APPEND_FORUM_COMMENT : ApiUrl.PUBLISH_COMMENT)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            RxToast.error(error);
                        });
                        mTvPublish.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(getActivity(), "评论成功", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(s)) {
                            CommentBean entity = ParseUtils.parseData(s, CommentBean.class);
                            if (mListener != null) {
                                mListener.onPublish(TextUtils.isEmpty(parentId), position, entity);
                            }
                        }
                        mTvPublish.setEnabled(true);
                        dismiss();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 显示在底部
        params.gravity = Gravity.BOTTOM;
        // 宽度填充满屏
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //显示dialog的时候,就显示软键盘
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        window.setAttributes(params);

        // 这里用透明颜色替换掉系统自带背景
        int color = ContextCompat.getColor(getActivity(), android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(color));
    }

    public interface OnPublishListener {
        void onPublish(boolean isParent, int position, CommentBean entity);
    }

    public BottomCommentDialog setOnPublishListener(OnPublishListener listener) {
        this.mListener = listener;
        return this;
    }
}
