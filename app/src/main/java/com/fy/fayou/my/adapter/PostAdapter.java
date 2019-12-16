package com.fy.fayou.my.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.forum.bean.ForumEntity;
import com.fy.fayou.my.dialog.SureCancelDialog;
import com.fy.fayou.utils.GlideOption;
import com.meis.base.mei.adapter.MeiBaseAdapter;
import com.vondear.rxtool.RxImageTool;
import com.vondear.rxui.view.dialog.RxDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;

public class PostAdapter extends MeiBaseAdapter<ForumEntity> {

    private boolean mIsUserCenter;

    public PostAdapter(boolean isUserCenter) {
        super(R.layout.forum_item_follow, new ArrayList<>());
        mIsUserCenter = isUserCenter;
    }

    public PostAdapter() {
        this(false);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ForumEntity item) {
        helper.setText(R.id.tv_title, getNonEmpty(item.title))
                .setText(R.id.tv_name, getNonEmpty(item.author))
                .setText(R.id.tv_content, getNonEmpty(item.description))
                .setText(R.id.tv_plate, getNonEmpty(item.boardName))
                .setVisible(R.id.tv_plate, !TextUtils.isEmpty(item.boardName))
                .setGone(R.id.iv_thumb, !TextUtils.isEmpty(item.cover))
                .setText(R.id.tv_scan, item.clicks + "人看过")
                .setText(R.id.tv_comment_num, item.comments + "评论")
                .setText(R.id.tv_praise_num, item.gives + "点赞")
                .setGone(R.id.iv_delete, mIsUserCenter);

        ViewGroup.MarginLayoutParams titleLp = (ViewGroup.MarginLayoutParams) helper.getView(R.id.tv_title).getLayoutParams();
        titleLp.topMargin = RxImageTool.dp2px(28);
        titleLp.rightMargin = RxImageTool.dp2px(31);

        ImageView ivHeader = helper.getView(R.id.iv_header);
        Glide.with(mContext)
                .load(getNonEmpty(item.authorAvatar))
                .apply(GlideOption.getItemCircleOption(20, 20))
                .into(ivHeader);

        ImageView ivThumb = helper.getView(R.id.iv_thumb);
        Glide.with(mContext)
                .load(getNonEmpty(item.cover))
                .apply(GlideOption.getRadiusOption(112, 74, 2))
                .into(ivThumb);

        helper.itemView.setOnClickListener(v -> {
            ARoute.jumpForumDetail(item.id);
        });

        helper.getView(R.id.iv_delete).setOnClickListener(v -> {
            SureCancelDialog dialog = new SureCancelDialog(mContext);
            dialog.setCancelListener(v1 -> {
                dialog.dismiss();
            });
            dialog.setSureListener(v12 -> {
                requestDelete(item.id, helper.getAdapterPosition(), dialog);
            });
            dialog.show();
        });
    }

    /**
     * @param id
     * @param position
     * @param dialog
     */
    private void requestDelete(String id, int position, RxDialog dialog) {
        EasyHttp.delete(ApiUrl.GET_FORUM_DETAIL + id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess(String s) {
                        notifyItemRemoved(position);
                        getData().remove(position);
                        dialog.dismiss();
                        if (getData().isEmpty()) {
                            if (mListener != null) mListener.onEmpty();
                        }
                    }
                });
    }

    private OnEmptyListener mListener;

    public interface OnEmptyListener {
        void onEmpty();
    }

    public void setOnEmptyListener(OnEmptyListener listener) {
        mListener = listener;
    }

}
