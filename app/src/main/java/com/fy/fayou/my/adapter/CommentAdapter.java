package com.fy.fayou.my.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.my.bean.CommentEntity;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class CommentAdapter extends MeiBaseAdapter<CommentEntity> {

    private OnClickListener mListener;

    public CommentAdapter(OnClickListener listener) {
        super(R.layout.item_my_comment, new ArrayList<>());
        mListener = listener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CommentEntity item) {

        helper.setText(R.id.tv_title, getNonEmpty(item.articleTitle))
                .setText(R.id.tv_content, getNonEmpty(item.content))
                .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

        TextView tvReply = helper.getView(R.id.tv_reply);
        SpannableString span = new SpannableString("回复" + getNonEmpty(item.reUserName));
        ForegroundColorSpan foreSpan = new ForegroundColorSpan(Color.parseColor("#6A78BC"));
        span.setSpan(foreSpan, 2, 2 + getNonEmpty(item.reUserName).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvReply.setText(span);

        helper.getView(R.id.tv_delete).setOnClickListener(v -> {
            mListener.onDelete(v, helper.getAdapterPosition(),item.id);
        });
    }

    public interface OnClickListener {
        void onDelete(View v, int position, String id);
    }
}
