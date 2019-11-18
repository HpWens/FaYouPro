package com.fy.fayou.pufa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.fy.fayou.R;
import com.luck.picture.lib.entity.LocalMedia;
import com.vondear.rxtool.RxKeyboardTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PublishMixingHelper {

    private PublishMixingAdapter mAdapter;
    private Context mContext;

    private int mTouchSlop;
    private float mLastRawX;
    private float mLastRawY;

    public void init(Context context, RecyclerView recyclerView, OnMixingListener listener) {
        if (recyclerView == null) {
            return;
        }
        mContext = context;
        mTouchSlop = ViewConfiguration.get(recyclerView.getContext()).getScaledTouchSlop();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new PublishMixingAdapter(context, new PublishMixingAdapter.OnMixingListener() {
            @Override
            public void onMaxCharLengthLimit() {

            }

            @Override
            public void onDeleteSinglePicture(View v, int position) {
                if (position == -1) return;
                if (position >= mAdapter.getData().size()) return;

                String afterText = "";
                // 获取图片下的输入框索引
                if (mAdapter.getData().size() > (position + 1)) {
                    if (mAdapter.getData().get(position + 1) instanceof TextEntity) {
                        TextEntity textEntity = (TextEntity) mAdapter.getData().get(position + 1);
                        afterText = textEntity.content;

                        mAdapter.notifyItemRemoved(position + 1);
                        mAdapter.getData().remove(position + 1);
                    }
                }

                // 删除图片
                if (mAdapter.getData().size() > position) {
                    if (mAdapter.getData().get(position) instanceof PicEntity) {
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.getData().remove(position);
                    }
                }

                // 后一个内容赋值给前一个输入框
                if (mAdapter.getData().size() > (position - 1)) {
                    if (!TextUtils.isEmpty(afterText) && mAdapter.getData().get(position - 1) instanceof TextEntity) {
                        TextEntity textEntity = (TextEntity) mAdapter.getData().get(position - 1);
                        textEntity.content += ("\n" + afterText);

                        mAdapter.getData().set(position - 1, textEntity);
                        mAdapter.notifyItemChanged(position - 1);
                    }
                }

            }

            @Override
            public void onFocusChange(boolean focus, int position) {
                listener.onFocusChange(focus, position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        List<Object> data = new ArrayList<>();
        TitleEntity titleEntity = new TitleEntity();
        data.add(titleEntity);

        TextEntity textEntity = new TextEntity();
        textEntity.hint = "请输入内容";
        data.add(textEntity);

        mAdapter.setNewData(data);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    InputMethodManager imm = (InputMethodManager) recyclerView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
            }
        });

        recyclerView.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastRawX = event.getRawX();
                            mLastRawY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (Math.abs(event.getRawX() - mLastRawX) < mTouchSlop ||
                                    Math.abs(event.getRawY() - mLastRawY) < mTouchSlop) {
                                // 点击空白区域
                                clickBlank(recyclerView);
                            }
                            mLastRawX = 0;
                            mLastRawY = 0;
                            break;
                    }
                    return false;
                }
        );

    }

    public boolean isEmpty() {
        if (mAdapter == null || mAdapter.getData().isEmpty()) {
            return true;
        }
        Object titleObj = mAdapter.getData().get(0);
        if (titleObj instanceof TitleEntity) {
            TitleEntity titleEntity = (TitleEntity) titleObj;
            if (TextUtils.isEmpty(titleEntity.name)) {
                Toast.makeText(mContext, "请输入标题", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        boolean isEmpty = true;

        for (int i = 1; i < mAdapter.getData().size(); i++) {
            Object obj = mAdapter.getData().get(i);
            if (obj instanceof TextEntity) {
                TextEntity textEntity = (TextEntity) obj;
                if (!TextUtils.isEmpty(textEntity.content)) {
                    isEmpty = false;
                    break;
                }
            } else if (obj instanceof PicEntity) {
                isEmpty = false;
                break;
            }
        }

        if (isEmpty) {
            Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    /**
     * 最后文本相应
     *
     * @param recyclerView
     */
    private void clickBlank(RecyclerView recyclerView) {
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            View view = recyclerView.getChildAt(i);
            Object tagObj = view.getTag();
            if (null != tagObj && tagObj instanceof String) {
                if (tagObj.toString().equals("text")) {
                    EditText etHind = view.findViewById(R.id.et_input);
                    if (etHind != null) {
                        if (!etHind.isFocused()) etHind.requestFocus();
                        RxKeyboardTool.showSoftInput(recyclerView.getContext(), etHind);
                        return;
                    }
                }
            }
        }
    }

    public List<String> covertStringArray(List<LocalMedia> mediaList) {
        List<String> list = new ArrayList<>();
        if (null != mediaList) {
            for (LocalMedia media : mediaList) {
                list.add(media.getPath());
            }
        }
        return list;
    }


    /**
     * @param urls
     */
    public void addMultiPicture(List<String> urls) {
        if (null == urls || urls.isEmpty()) return;
        if (mAdapter == null) return;

        int focusPos = mAdapter.getCurrentFocusPos();

        if (focusPos == -1) focusPos = 1;
        if (focusPos >= mAdapter.getData().size()) focusPos = mAdapter.getData().size() - 1;

        List<Object> list = new ArrayList<>();

        for (String url : urls) {

            // 1、添加图片
            PicEntity picEntity = new PicEntity();
            picEntity.path = url;

            File file = new File(url);
            if (file != null && file.exists()) {
                picEntity.originPath = file.getName();
            }
            list.add(picEntity);

            // 2、添加输入框
            TextEntity textEntity = new TextEntity();
            textEntity.hint = "点击此处可输入内容";

            list.add(textEntity);
        }

        // 刷新
        mAdapter.getData().addAll(focusPos + 1, list);
        mAdapter.notifyItemRangeInserted(focusPos + 1, list.size());

    }

    public interface OnMixingListener {
        void onFocusChange(boolean focus, int position);
    }

}
