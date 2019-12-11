package com.fy.fayou.wxapi;

import com.fy.fayou.utils.SocialUtil;

import net.arvin.socialhelper.SocialHelper;
import net.arvin.socialhelper.WXHelperActivity;

/**
 * Created by arvinljw on 17/7/6 14:43
 * Function：
 * Desc：
 */
public class WXEntryActivity extends WXHelperActivity {

    @Override
    protected SocialHelper getSocialHelper() {
        return SocialUtil.INSTANCE.socialHelper;
    }
}
