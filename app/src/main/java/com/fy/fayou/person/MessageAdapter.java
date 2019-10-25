package com.fy.fayou.person;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class MessageAdapter extends MeiBaseAdapter<MessageEntity> {

    public MessageAdapter() {
        super(R.layout.item_person_message, new ArrayList<MessageEntity>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MessageEntity item) {

    }
}
