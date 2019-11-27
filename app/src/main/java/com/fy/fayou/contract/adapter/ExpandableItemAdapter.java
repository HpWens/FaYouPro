package com.fy.fayou.contract.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.fy.fayou.R;
import com.fy.fayou.contract.bean.Level1Item;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = ExpandableItemAdapter.class.getSimpleName();

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

    private boolean isOnlyExpandOne = false;
    private List<Level1Item> selectedArray = new ArrayList<>();
    private Level1Item currentSelectedItem;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_2, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_3, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_4, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_5, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_6, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_7, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_8, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_9, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_10, R.layout.item_expandable_lv1);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, MultiItemEntity item) {
        switch (item.getItemType()) {
            case TYPE_LEVEL_1:
                final Level1Item level = (Level1Item) item;
                holder.setText(R.id.tv_name, level.content)
                        .setImageResource(R.id.iv_more, level.isExpanded() ? R.mipmap.arrow_b : R.mipmap.arrow_r);
                holder.itemView.setOnClickListener(v -> {
                    // 无数据则请求
                    int pos = holder.getAdapterPosition();
                    if (level.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
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
                final Level1Item level2 = (Level1Item) item;
                holder.setText(R.id.tv_name, level2.content)
                        .setVisible(R.id.iv_transform, isVisibleIvTransform(level2.getSubItems()))
                        .setImageResource(R.id.iv_select, isSelected(level2) ? R.mipmap.filter_unselected_ic : R.mipmap.filter_selected_ic)
                        .setImageResource(R.id.iv_transform, !level2.isExpanded() ? R.mipmap.filter_collapse_ic : R.mipmap.filter_expand_ic);

                TextView tvName = holder.getView(R.id.tv_name);
                tvName.getPaint().setFakeBoldText(item.getItemType() == TYPE_LEVEL_2 || item.getItemType() == TYPE_LEVEL_3);
                tvName.setTextSize(item.getItemType() == TYPE_LEVEL_2 ? 14 : 12);

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.getView(R.id.iv_transform).getLayoutParams();
                lp.leftMargin = RxImageTool.dp2px(15 + (item.getItemType() - 2) * 30);

                holder.itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (level2.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });

                holder.getView(R.id.fl_check).setOnClickListener(v -> {
                    addSelectedArray(level2);
                });

                break;
            default:
                break;
        }
    }

    /**
     * @param level 产品规则 勾选选中父节点  取消所有的子节点
     */
    private void addSelectedArray(Level1Item level) {
        if (isSelected(level)) {
            // 如果选中 则取消
            level.isSelect = false;
            selectedArray.remove(currentSelectedItem);

            // 遍历所有的子节点 取消勾选
            traversalChildNode(level.getSubItems());
        } else {
            // 未选中 则勾选
            level.isSelect = true;
            selectedArray.add(level);
            traversalParentNode(level);
        }
        notifyDataSetChanged();
    }

    /**
     * @param childList
     */
    private void traversalChildNode(List<Level1Item> childList) {
        if (childList != null && !childList.isEmpty()) {
            for (Level1Item child : childList) {
                child.isSelect = false;
                if (isSelected(child)) {
                    selectedArray.remove(currentSelectedItem);
                }
                traversalChildNode(child.getSubItems());
            }
        }
    }

    /**
     * @param lv
     */
    private void traversalParentNode(Level1Item lv) {
        if (lv.parentLevel != null && !TextUtils.isEmpty(lv.parentLevel.id)) {
            lv.parentLevel.isSelect = true;
            if (!isSelected(lv.parentLevel)) {
                selectedArray.add(lv.parentLevel);
            }
            traversalParentNode(lv.parentLevel);
        }
    }

    private boolean isSelected(Level1Item level) {
        for (Level1Item lv : selectedArray) {
            if (lv.id.equals(level.id)) {
                currentSelectedItem = lv;
                return true;
            }
        }
        return false;
    }

    public void clearSelectedArray() {
        selectedArray.clear();
        notifyDataSetChanged();
    }

    public List<Level1Item> getSelectedArray() {
        return selectedArray;
    }

    private boolean isVisibleIvTransform(List<Level1Item> subList) {
        if (subList == null || subList.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean isOnlyExpandOne() {
        return isOnlyExpandOne;
    }

    public void setOnlyExpandOne(boolean onlyExpandOne) {
        isOnlyExpandOne = onlyExpandOne;
    }
}
