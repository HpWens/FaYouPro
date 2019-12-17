package com.fy.fayou.person;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class MessageAdapter extends MeiBaseAdapter<MessageEntity> {

    public MessageAdapter() {
        super(R.layout.item_person_message, new ArrayList<MessageEntity>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageEntity item) {

        ImageView ivHeader = helper.getView(R.id.iv_header);

        Glide.with(mContext)
                .load(getNonEmpty(item.senderAvatar))
                .apply(GlideOption.getItemCircleOption(40, 40))
                .into(ivHeader);

        helper.setText(R.id.tv_name, getNonEmpty(item.senderName))
                .setText(R.id.tv_info, getNonEmpty(item.messageContent))
                .setText(R.id.tv_time, ParseUtils.getTime(item.createTime));

//        ivHeader.setOnClickListener(v -> {
//            ARoute.jumpUserCenter(item.id);
//        });

    }
}
