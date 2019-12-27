package com.fy.fayou.home.adapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.home.bean.WantedEntity;
import com.fy.fayou.utils.GlideOption;
import com.fy.fayou.utils.KtTimeUtils;
import com.meis.base.mei.adapter.MeiBaseAdapter;

import java.util.ArrayList;

public class WantedAdapter extends MeiBaseAdapter<WantedEntity> {

    public WantedAdapter() {
        super(R.layout.item_wanted, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WantedEntity item) {

        TextView tvDetail = helper.getView(R.id.tv_detail);
        TextView tvContent = helper.getView(R.id.tv_content);
        String content = TextUtils.isEmpty(item.content) ? "" : item.content.replaceAll("　", "");

        tvContent.post(() -> {
            float dWidth = tvDetail.getWidth();
            float width = tvContent.getPaint().measureText(content);
            int lineWidth = tvContent.getWidth();

            if ((width + dWidth) >= 4 * lineWidth) {
                int endIndex = (int) ((4 * lineWidth - dWidth) / width * content.length());
                tvContent.setText(content.substring(0, endIndex - 11) + "...");
            } else {
                tvContent.setText(content);
            }
        });

        helper.setText(R.id.tv_name, getNonEmpty(item.name))
                .setText(R.id.tv_time, "发布时间：" + KtTimeUtils.INSTANCE.getYMDTime(item.pubTime));
        helper.setVisible(R.id.tv_time, !TextUtils.isEmpty(item.pubTime));

        ImageView ivThumb = helper.getView(R.id.iv_thumb);

        Glide.with(mContext)
                .load(getNonEmpty(item.photoUrl))
                .apply(GlideOption.getItemOption(75, 92))
                .into(ivThumb);

        helper.itemView.setOnClickListener(v -> {
            try {
                String id = item.toUrl.substring(item.toUrl.lastIndexOf("/") + 1);
                ARoute.jumpH5(item.toUrl, false, id, ARoute.WANTED_TYPE, getNonEmpty(item.name));
            } catch (Exception e) {
            }
        });

    }

    /**
     * 转半角的函数(DBC case)<br/><br/>
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * @param input 任意字符串
     * @return 半角字符串
     */
    private String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                //全角空格为12288，半角空格为32
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                //其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 转全角的方法(SBC case)<br/><br/>
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * @param input 任意字符串
     * @return 半角字符串
     */
    private String ToSBC(String input) {
        //半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        return sbNewText.toString();
    }
}
