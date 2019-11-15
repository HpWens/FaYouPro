package com.fy.fayou.common;

import com.alibaba.android.arouter.launcher.ARouter;

public class ARoute {

    /**
     * 跳转到用户中心
     *
     * @param userId
     */
    public static void jumpUserCenter(String userId) {
        ARouter.getInstance()
                .build(Constant.USER_CENTER)
                .withString(Constant.Param.USER_ID, userId)
                .navigation();
    }


    /**
     * 跳转到全网通缉
     */
    public static void jumpWanted() {
        ARouter.getInstance()
                .build(Constant.WANTED)
                .navigation();
    }

    /**
     * 跳转到六大模块
     */
    // 1法律法规2司法解释
    public static final int LEGAL_TYPE = 1;
    public static final int JUDICIAL_TYPE = 2;
    // 3指导性意见
    public static final int GUIDE_TYPE = 3;
    // 4裁判文书
    public static final int JUDGE_TYPE = 4;
    // 5 合同模板
    public static final int TEMPLATE_TYPE = 5;
    // 6 合同模板
    public static final int BOOKS_TYPE = 6;

    public static void jumpModule(int type) {
        switch (type) {
            case JUDICIAL_TYPE:
            case LEGAL_TYPE:
            case GUIDE_TYPE:
            case JUDGE_TYPE:
            case BOOKS_TYPE:
                ARouter.getInstance()
                        .build(Constant.LEGAL)
                        .withInt(Constant.Param.TYPE, type)
                        .navigation();
                break;
            case TEMPLATE_TYPE:
                ARouter.getInstance()
                        .build(Constant.CONTRACT_TEMPLATE)
                        .navigation();
                break;
            default:
                break;
        }
    }

}
