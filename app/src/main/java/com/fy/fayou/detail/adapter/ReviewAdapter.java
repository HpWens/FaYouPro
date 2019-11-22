package com.fy.fayou.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.detail.bean.CommentBean;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.fy.fayou.view.CustomPopWindow;
import com.meis.base.mei.adapter.BaseMultiAdapter;
import com.vondear.rxtool.RxClipboardTool;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends BaseMultiAdapter<CommentBean> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;

    private OnClickListener mListener;

    private float rawX, rawY;

    public ReviewAdapter(OnClickListener listener) {
        super(new ArrayList<>());
        mListener = listener;
        addItemType(TYPE_LEVEL_0, R.layout.item_comment_level0);
        addItemType(TYPE_LEVEL_1, R.layout.item_comment_level1);
        addItemType(TYPE_LEVEL_2, R.layout.item_comment_level2);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentBean item) {
        switch (item.level) {
            case TYPE_LEVEL_0:
            case TYPE_LEVEL_1:
                helper.getView(R.id.tv_content).setOnLongClickListener(v -> {
                            showPop(mContext, v, item, helper);
                            return false;
                        }
                );
                helper.getView(R.id.tv_content).setOnTouchListener((v, event) -> {
                    rawX = event.getRawX();
                    rawY = event.getRawY();
                    return false;
                });

                boolean isChild = item.level == 1;

                ImageView ivAvatar = helper.getView(R.id.iv_avatar);
                Glide.with(helper.itemView.getContext())
                        .load(getNonEmpty(item.userAvatar))
                        .apply(GlideOption.getAvatarOption(RxImageTool.dp2px(isChild ? 20 : 30), RxImageTool.dp2px(isChild ? 20 : 30)))
                        .into(ivAvatar);

                helper.setText(R.id.tv_content, getNonEmpty(item.content))
                        .setText(R.id.tv_name, getNonEmpty(item.userName))
                        .setText(R.id.tv_praise_num, item.gives + "")
                        .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

                ImageView ivPraise = helper.getView(R.id.iv_praise);
                ivPraise.setSelected(item.give);
                ivPraise.setOnClickListener(v -> {
                    // 请求点赞
                    mListener.onPraise(v, helper.getAdapterPosition(), item);
                });

                helper.getView(R.id.tv_content).setOnClickListener(v -> {
                    clickComment(helper, item);
                });
                helper.itemView.setOnClickListener(v -> {
                    clickComment(helper, item);
                });
                helper.getView(R.id.iv_comment).setOnClickListener(v -> {
                    clickComment(helper, item);
                });

                ivAvatar.setOnClickListener(v -> {
                    ARoute.jumpUserCenter(item.userId);
                });
                if (item.level == TYPE_LEVEL_1) {
                    helper.setText(R.id.tv_rename, getNonEmpty(item.reUserName));
                }
                break;
            case TYPE_LEVEL_2:
                TextView tvExpand = helper.getView(R.id.tv_expand);
                if (item.laveCommentCount > 0) {
                    tvExpand.setVisibility(View.VISIBLE);
                    tvExpand.setText(tvExpand.getResources().getString(R.string.expand_comment, item.laveCommentCount));
                } else {
                    tvExpand.setVisibility(View.GONE);
                }
                tvExpand.setOnClickListener(v -> {
                    // 获取到最前面的id
                    if (mListener != null) {
                        List<String> ids = getHelperIds(item.helperId, helper.getAdapterPosition(), item.helperChildCount);
                        mListener.onLoadMoreComment(ids, item.helperId, helper.getAdapterPosition(), item.helperPage);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * @param helperId
     * @return
     */
    private List<String> getHelperIds(String helperId, int position, int childCount) {
        List<String> ids = new ArrayList<>();
        int count = 0;
        int start = position - childCount;
        if (start <= 0) start = 0;
        for (int i = start; i <= position; i++) {
            CommentBean bean = getData().get(i);
            if (bean.level == 1 && helperId.equals(bean.helperId)) {
                count++;
                ids.add(bean.id);
                if (count >= 2) {
                    break;
                }
            }
        }
        return ids;
    }

    private void clickComment(@NonNull BaseViewHolder helper, CommentBean item) {
        mListener.onComment(item.userName, item.articleId, item.id, helper.getAdapterPosition());
    }

    private void showPop(Context context, View view, CommentBean item, @NonNull BaseViewHolder helper) {
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(R.layout.pop_review_report)
                .create();
        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        int screenHeight = RxDeviceTool.getScreenHeight(context);
        int popHeight = RxImageTool.dp2px(200);
        boolean isCross = loc[1] + popHeight >= screenHeight;
        View contentView = popWindow.getContentView();
        if (contentView != null) {
            LinearLayout bottomLayout = contentView.findViewById(R.id.bottom_arrow_layout);
            LinearLayout topLayout = contentView.findViewById(R.id.top_arrow_layout);
            bottomLayout.setVisibility(isCross ? View.VISIBLE : View.GONE);
            topLayout.setVisibility(!isCross ? View.VISIBLE : View.GONE);

            TextView tvCopy = contentView.findViewById(R.id.tv_copy);
            TextView tvCopy2 = contentView.findViewById(R.id.tv_copy2);
            tvCopy.setOnClickListener(v -> {
                copyText(v.getContext(), item.content);
                popWindow.dissmiss();
            });
            tvCopy2.setOnClickListener(v -> {
                copyText(v.getContext(), item.content);
                popWindow.dissmiss();
            });

            TextView tvReply = contentView.findViewById(R.id.tv_reply);
            TextView tvReply2 = contentView.findViewById(R.id.tv_reply2);
            tvReply.setOnClickListener(v -> {
                clickComment(helper, item);
                popWindow.dissmiss();
            });
            tvReply2.setOnClickListener(v -> {
                clickComment(helper, item);
                popWindow.dissmiss();
            });

            TextView tvReport = contentView.findViewById(R.id.tv_report);
            TextView tvReport2 = contentView.findViewById(R.id.tv_report2);
            tvReport.setOnClickListener(v -> {
                ARoute.jumpReport(item.id, ARoute.REPORT_COMMENT);
            });
            tvReport2.setOnClickListener(v -> {
                ARoute.jumpReport(item.id, ARoute.REPORT_COMMENT);
            });

        }
        if (isCross) {
            popWindow.showAsDropDown(view, 0, -popHeight + RxImageTool.dp2px(32));
        } else {
            popWindow.showAsDropDown(view);
        }
    }

    /**
     * @param context
     * @param text
     */
    private void copyText(Context context, CharSequence text) {
        RxClipboardTool.copyText(context, text);
        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
    }

    public interface OnClickListener {
        void onPraise(View v, int position, CommentBean comment);

        void onComment(String userName, String articleId, String parentId, int position);

        void onLoadMoreComment(List<String> excludeIds, String parentId, int position, int helperPage);
    }
}
