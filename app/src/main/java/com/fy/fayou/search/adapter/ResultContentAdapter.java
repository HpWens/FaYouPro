package com.fy.fayou.search.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.search.bean.SearchResultEntity;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class ResultContentAdapter extends MeiBaseAdapter<SearchResultEntity> {

    private String mKey;

    public ResultContentAdapter(String key) {
        super(R.layout.item_result_content, new ArrayList<>());
        this.mKey = key;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchResultEntity item) {
        setForegroundSpan(mKey, item.name, helper.getView(R.id.tv_title));
    }

    private void setForegroundSpan(String span, String text, TextView view) {
        if (text.contains(span)) {
            SpannableString spannableString = new SpannableString(text);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#ED4040"));
            spannableString.setSpan(colorSpan, text.indexOf(span), text.indexOf(span) + span.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            view.setText(spannableString);
        } else {
            view.setText(text);
        }
    }
}
