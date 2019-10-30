package com.fy.fayou.person.suggest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxtool.view.RxToast;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/person/suggest")
public class SuggestActivity extends BaseActivity {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.et_suggest)
    EditText etSuggest;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.tv_limit)
    TextView tvLimit;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();

    private static final int MAX_CHAR_LIMIT = 200;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("意见反馈");
        setLeftBackListener(v -> finish()).setRightTextListener(v -> {
            if (checkContent()) {
                requestFeedBack();
            }
        }, "提交");
    }

    private void requestFeedBack() {
        String contact = etContact.getText().toString();
        String suggest = etSuggest.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", contact);
        params.put("content", suggest);
        params.put("type", "COMMENTS");
        JSONObject jsonObject = new JSONObject(params);

        EasyHttp.post(ApiUrl.USER_UPDATE)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        RxToast.normal("意见反馈成功");
                        finish();
                    }
                });
    }

    private boolean checkContent() {
        String suggest = etSuggest.getText().toString();
        if (TextUtils.isEmpty(suggest)) {
            RxToast.normal("请输入问题及建议");
            return false;
        }
        String contact = etContact.getText().toString();
        if (TextUtils.isEmpty(contact)) {
            RxToast.normal("请留下联系方式");
            return false;
        }
        return true;
    }

    @Override
    protected void initData() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
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
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_suggest;
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
