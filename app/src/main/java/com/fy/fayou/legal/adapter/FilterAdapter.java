package com.fy.fayou.legal.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.legal.bean.JudgeLevel1;
import com.fy.fayou.legal.bean.JudgeLevel2;
import com.fy.fayou.utils.ParseUtils;
import com.vondear.rxtool.RxImageTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;
    public static final int TYPE_LEVEL_4 = 4;
    public static final int TYPE_LEVEL_5 = 5;
    public static final int TYPE_LEVEL_6 = 6;
    public static final int TYPE_LEVEL_7 = 7;
    public static final int TYPE_LEVEL_8 = 8;
    public static final int TYPE_LEVEL_9 = 9;
    public static final int TYPE_LEVEL_10 = 10;


    private List<JudgeLevel2> selectedArray = new ArrayList<>();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public FilterAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_1, R.layout.item_filter_lv1);
        addItemType(TYPE_LEVEL_2, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_3, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_4, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_5, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_6, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_7, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_8, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_9, R.layout.item_filter_lv2);
        addItemType(TYPE_LEVEL_10, R.layout.item_filter_lv2);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TYPE_LEVEL_1:
                final JudgeLevel1 level = (JudgeLevel1) item;
                helper.setText(R.id.tv_name, level.name)
                        .setGone(R.id.iv_more, level.hasChald)
                        .setImageResource(R.id.iv_more, level.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                helper.itemView.setOnClickListener(v -> {
                    // 无数据则请求
                    if (level.getSubItems() == null || level.getSubItems().isEmpty()) {
                        requestColumn(level, helper.getAdapterPosition());
                    } else {
                        int pos = helper.getAdapterPosition();
                        if (level.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_2:
            case TYPE_LEVEL_3:
            case TYPE_LEVEL_4:
            case TYPE_LEVEL_5:
            case TYPE_LEVEL_6:
            case TYPE_LEVEL_7:
            case TYPE_LEVEL_8:
            case TYPE_LEVEL_9:
            case TYPE_LEVEL_10:
                final JudgeLevel2 level2 = (JudgeLevel2) item;
                helper.setText(R.id.tv_name, level2.name)
                        .setVisible(R.id.checkbox, !level2.hasChald || !level2.isExpanded())
                        .setVisible(R.id.iv_transform, level2.hasChald)
                        .setChecked(R.id.checkbox, isColumnSelected(level2.helperIndex, level2.id))
                        .setImageResource(R.id.iv_transform, !level2.isExpanded() ? R.mipmap.filter_collapse_ic : R.mipmap.filter_expand_ic);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) helper.getView(R.id.iv_transform).getLayoutParams();
                lp.leftMargin = RxImageTool.dp2px(15 + (item.getItemType() - 2) * 30);
                helper.itemView.setOnClickListener(v -> {
                    // 无数据则请求
                    if (level2.getSubItems() == null || level2.getSubItems().isEmpty()) {
                        requestColumn2(level2, item.getItemType() + 1, helper.getAdapterPosition());
                    } else {
                        int pos = helper.getAdapterPosition();
                        if (level2.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });

                CheckBox cb = helper.getView(R.id.checkbox);
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    clearColumnSelected(level2.helperIndex);
                    addColumnSelected(level2);
                });

                break;
            default:
                break;
        }
    }

    private boolean isColumnSelected(int index, String id) {
        JudgeLevel2 level = selectedArray.get(index);
        if (level != null) {
            return level.isSelected && level.id.equals(id);
        }
        return false;
    }

    private void clearColumnSelected(int index) {
        JudgeLevel2 level = selectedArray.get(index);
        if (level != null) {
            level.isSelected = false;
        }
    }

    private void addColumnSelected(JudgeLevel2 level2) {
        level2.isSelected = true;
        selectedArray.put(level2.helperIndex, level2);
    }

    /**
     * @param level
     * @param itemType
     * @param position
     */
    private void requestColumn2(JudgeLevel2 level, int itemType, int position) {
        EasyHttp.get(ApiUrl.GET_JUDGE_LEVEL)
                .params("id", "" + level.id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<JudgeLevel2> list = ParseUtils.parseListData(s, JudgeLevel2.class);
                            for (JudgeLevel2 level2 : list) {
                                level2.level = itemType;
                                level2.itemType = itemType;
                                level2.helperIndex = level.helperIndex;
                                level.addSubItem(level2);
                            }
                            expand(position);
                        }
                    }
                });
    }

    /**
     * @param level
     */
    private void requestColumn(JudgeLevel1 level, int position) {
        EasyHttp.get(ApiUrl.GET_JUDGE_LEVEL)
                .params("id", "" + level.id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            List<JudgeLevel2> list = ParseUtils.parseListData(s, JudgeLevel2.class);
                            for (JudgeLevel2 level2 : list) {
                                level2.level = TYPE_LEVEL_2;
                                level2.helperId = level.id;
                                level2.itemType = TYPE_LEVEL_2;
                                level2.helperIndex = level.helperIndex;
                                level.addSubItem(level2);
                            }
                            expand(position);
                        }
                    }
                });
    }


}
