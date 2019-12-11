package com.fy.fayou.utils;

import net.arvin.socialhelper.SocialHelper;

/**
 * Created by arvinljw on 17/11/27 17:33
 * Function：
 * Desc：
 */
public enum SocialUtil {

    INSTANCE();

    public SocialHelper socialHelper;

    SocialUtil() {
        socialHelper = new SocialHelper.Builder()
                .setQqAppId("qqAppId")
                .setWxAppId("wx5618be1b79bde93b")
                .setWxAppSecret("f34a224e5d40536e0d69e61d2f24f579")
                .setWbAppId("wbAppId")
                .setWbRedirectUrl("wbRedirectUrl")
                .build();
    }
}
