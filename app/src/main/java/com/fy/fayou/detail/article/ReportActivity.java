package com.fy.fayou.detail.article;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.common.UploadService;
import com.fy.fayou.event.ReportSuccessEvent;
import com.fy.fayou.person.suggest.FullyGridLayoutManager;
import com.fy.fayou.person.suggest.GridImageAdapter;
import com.fy.fayou.utils.ParseUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;
import com.vondear.rxui.view.dialog.RxDialog;
import com.vondear.rxui.view.dialog.RxDialogShapeLoading;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/detail/report")
public class ReportActivity extends BaseActivity {

    @Autowired
    public String id = "";

    @Autowired
    public String type = "";

    @Autowired(name = "is_forum")
    public boolean isForum;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.et_suggest)
    EditText etSuggest;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_limit)
    TextView tvLimit;

    @BindView(R.id.cb_0)
    CheckBox cb0;
    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.cb_4)
    CheckBox cb4;
    @BindView(R.id.cb_5)
    CheckBox cb5;
    @BindView(R.id.cb_6)
    CheckBox cb6;
    @BindView(R.id.cb_7)
    CheckBox cb7;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<CheckBox> checkList = new ArrayList<>();

    private static final int MAX_CHAR_LIMIT = 2000;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("举报");
        setLeftBackListener(v -> finish()).setRightTextListener(v -> {
            if (checkContent()) {
                // 提交图片
                final RxDialogShapeLoading dialog = new RxDialogShapeLoading(this);
                dialog.show();

                if (null != selectList && !selectList.isEmpty()) {
                    UploadService.getInstance().syncUploadMultiFileByMedia(selectList, new UploadService.OnUploadListener() {
                        @Override
                        public void onSuccess(String key) {
                            requestFeedBack(dialog, key);
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                } else {
                    requestFeedBack(dialog, "");
                }

            }
        }, "提交");
    }

    private void requestFeedBack(final RxDialog dialog, String pics) {
        String contact = etContact.getText().toString();
        String suggest = etSuggest.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", contact);
        params.put("content", suggest);
        if (isForum) {
            params.put("type", type.equals("COMMENT") ? "COMMENT" : "POST");
        } else {
            params.put("type", "REPORT");
        }
        params.put("pics", pics);
        params.put("contentType", getCheckReportType());
        if (!TextUtils.isEmpty(id)) {
            params.put("transationId", id);
        }
        if (!TextUtils.isEmpty(type) && !isForum) {
            params.put("feedbackTransationType", type);
        }
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(isForum ? ApiUrl.POST_FORUM_REPORT : ApiUrl.FEED_BACK)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(mContext, "提交失败", Toast.LENGTH_SHORT).show();
                        });
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s) {
                        EventBus.getDefault().post(new ReportSuccessEvent());
                        RxToast.normal("举报成功");
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    private boolean checkContent() {
        if (!checkSelected()) {
            RxToast.normal("请勾选举报类型");
            return false;
        }
        String suggest = etSuggest.getText().toString();
        if (TextUtils.isEmpty(suggest)) {
            RxToast.normal("请输入举报理由");
            return false;
        }
        String contact = etContact.getText().toString();
        if (contact.length() > 50) {
            RxToast.normal("联系方式不能超过50个字");
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {
        // NestedScrollView 嵌套 RecyclerView
        int recyclerHeight = (RxDeviceTool.getScreenWidth(mContext) - RxImageTool.dp2px(30 + 5 * 2)) / 3;
        recyclerView.getLayoutParams().height = recyclerHeight;
        recyclerView.requestLayout();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(3);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                if (pos == 0) {
                    outRect.right = RxImageTool.dp2px(5);
                } else if (pos == 2) {
                    outRect.left = RxImageTool.dp2px(5);
                } else {
                    outRect.left = outRect.right = RxImageTool.dp2px(2.5f);
                }
            }
        });

        adapter.setOnItemClickListener((position, v) ->
                PictureSelector.create(mContext).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList));

        etSuggest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvLimit.setText(s.length() + "/" + MAX_CHAR_LIMIT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addCheckList();
    }

    private void addCheckList() {
        checkList.clear();
        cb0.setTag("BROADCAST");
        checkList.add(cb0);
        cb1.setTag("TITLE");
        checkList.add(cb1);
        cb2.setTag("AD");
        checkList.add(cb2);
        cb3.setTag("VULGAR");
        checkList.add(cb3);
        cb4.setTag("FAKE");
        checkList.add(cb4);
        cb5.setTag("EXPIRED");
        checkList.add(cb5);
        cb6.setTag("RUMOR");
        checkList.add(cb6);
        cb7.setTag("ILLEGAL");
        checkList.add(cb7);
    }

    private boolean checkSelected() {
        for (CheckBox cb : checkList) {
            if (cb.isChecked()) {
                return true;
            }
        }
        return false;
    }

    // 类型是举报，必传。举报内容类型（播放错误BROADCAST, 标题与内容不符合TITLE, 广告AD,低俗 VULGAR,假的FAKE, 过期 EXPIRED, 谣言RUMOR, 违法 ILLEGAL）
    private String getCheckReportType() {
        StringBuilder sb = new StringBuilder();
        for (CheckBox cb : checkList) {
            if (cb.isChecked()) {
                sb.append(cb.getTag().toString() + ",");
            }
        }
        return sb.length() > 1 ? sb.substring(0, sb.length() - 1) : "";
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_report;
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(mContext)
                    .openGallery(PictureMimeType.ofImage())
                    .theme(R.style.picture_default_style)
                    .maxSelectNum(3)
                    .minSelectNum(1)
                    .previewImage(true)
                    .isCamera(true)
                    .glideOverride(160, 160)
                    .previewEggs(true)
                    .isGif(true)
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
