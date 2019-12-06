package com.fy.fayou.common;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.legal.bean.JudgeLevel1;

import java.util.ArrayList;

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
                .withInt(Constant.Param.TYPE, WANTED_TYPE)
                .navigation();
    }

    /**
     * 跳转到六大模块
     */
    // 1法律法规
    public static final int LEGAL_TYPE = 1;
    // 2司法解释
    public static final int JUDICIAL_TYPE = 2;
    // 3指导性意见
    public static final int GUIDE_TYPE = 3;
    // 4裁判文书
    public static final int JUDGE_TYPE = 4;
    // 5 合同模板
    public static final int TEMPLATE_TYPE = 5;
    // 6 法律图书
    public static final int BOOKS_TYPE = 6;

    // 7、举报类型 文章
    public static final int ARTICLE_TYPE = 7;

    // 8 全网通缉
    public static final int WANTED_TYPE = 8;

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
                        .withInt(Constant.Param.TYPE, type)
                        .navigation();
                break;
            default:
                break;
        }
    }

    /**
     * 获取到收藏类型
     * ARTICLE（普法里面的视频和图片，都传这个),合同 （ CONTRACT）  通缉 （ CRIMINAL）    司法解释 （ JUDICIAL）  法律法规（LEGAL）裁判文书（JUDGE） 案例 （ CASE）
     *
     * @param moduleType
     * @return
     */
    public static String getCollectType(int moduleType) {
        String type = "ARTICLE";
        switch (moduleType) {
            case ARoute.LEGAL_TYPE:
                type = "LEGAL";
                break;
            case ARoute.JUDICIAL_TYPE:
                type = "JUDICIAL";
                break;
            case ARoute.GUIDE_TYPE:
                type = "CASE";
                break;
            case ARoute.JUDGE_TYPE:
                type = "JUDGE";
                break;
            case ARoute.TEMPLATE_TYPE:
                type = "CONTRACT";
                break;
            case ARoute.BOOKS_TYPE:
                type = "";
                break;
            case ARoute.ARTICLE_TYPE:
                type = "ARTICLE";
                break;
            case ARoute.WANTED_TYPE:
                type = "CRIMINAL";
                break;
        }
        return type;
    }

    /**
     * @param id
     * @param type 类型 0 文章 1 视频
     */
    public static void jumpDetail(String id, int type) {
        ARouter.getInstance()
                .build(Constant.DETAIL_ARTICLE)
                .withString(Constant.Param.ARTICLE_ID, id)
                .withInt(Constant.Param.TYPE, type)
                .navigation();
    }

    /**
     * @param id
     * @param type
     */
    public static void jumpDetail(String id, String type) {
        jumpDetail(id, 0, type);
    }

    public static void jumpDetail(String id, int position, String type) {
        ARouter.getInstance()
                .build(Constant.DETAIL_ARTICLE)
                .withString(Constant.Param.ARTICLE_ID, id)
                .withInt(Constant.Param.POSITION, position)
                .withInt(Constant.Param.TYPE, type.equals(Constant.Param.VIDEO) ?
                        Constant.Param.VIDEO_TYPE : Constant.Param.ARTICLE_TYPE)
                .navigation();
    }

    public static void jumpH5(String url) {
        jumpH5(url, false, "", ARoute.ARTICLE_TYPE);
    }

    public static void jumpH5(String url, boolean isShowMore, String id, int collectType) {
        ARouter.getInstance()
                .build(Constant.COMMON_WEB_VIEW)
                .withString(Constant.Param.URL, url)
                .withBoolean(Constant.Param.DETAIL, isShowMore)
                .withString(Constant.Param.ID, id)
                .withInt(Constant.Param.TYPE, collectType)
                .navigation();
    }


    // 1 评论 2 文章
    public static final int reportType = 1;

    public static final int REPORT_ARTICLE = 1;
    public static final int REPORT_COMMENT = 2;

    public static void jumpReport(String id, int type, boolean isForum) {
        if (!UserService.getInstance().checkLoginAndJump()) return;
        String reportType = type == 1 ? "ARTICLE" : "COMMENT";
        ARouter.getInstance()
                .build(Constant.REPORT)
                .withString(Constant.Param.ID, id)
                .withBoolean(Constant.Param.IS_FORUM, isForum)
                .withString(Constant.Param.TYPE, reportType)
                .navigation();
    }

    public static void jumpReport(String id, int type) {
        jumpReport(id, type, false);
    }

    /**
     * 跳转到搜索页
     */
    public static void jumpSearch() {
        ARouter.getInstance().build(Constant.HOME_SEARCH).navigation();
    }

    /**
     * 跳转到筛选界面
     *
     * @param id
     * @param title
     */
    public static final int REQUEST_CODE = 101;

    public static void jumpFilter(String id, String title, String url) {
        ARouter.getInstance()
                .build(Constant.CONTRACT_FILTER)
                .withString(Constant.Param.ID, id)
                .withString(Constant.Param.NAME, title)
                .withString(Constant.Param.URL, url)
                .navigation();
    }


    /**
     * 跳转到裁判文书筛选界面
     *
     * @param activity
     * @param list
     */
    public static void jumpJudgeFilter(Activity activity, ArrayList<JudgeLevel1> list) {
        ARouter.getInstance()
                .build(Constant.JUDGE_FILTER)
                .withParcelableArrayList(Constant.Param.LIST, list)
                .navigation(activity, REQUEST_CODE);
    }

    /**
     * 跳转到普法界面
     *
     * @param type
     */
    public static final int LEARN_NEWS_TAB = 0;
    public static final int LEARN_HOT_VIDEO_TAB = 1;
    public static final int LEARN_SMALL_VIDEO_TAB = 2;

    public static void jumpLearnClearTask(int pos) {
        ARouter.getInstance()
                .build(Constant.MAIN)
                .withInt(Constant.Param.POSITION, pos)
                .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK)
                .navigation();
    }

    /**
     * 跳转到论坛关注
     */
    public static void jumpForumFollow() {
        ARouter.getInstance()
                .build(Constant.PLATE_FOLLOW)
                .navigation();
    }

    /***
     * 板块列表
     * @param id
     */
    public static void jumpPlateList(String id) {
        ARouter.getInstance()
                .build(Constant.FORUM_LIST)
                .withString(Constant.Param.ID, id)
                .navigation();
    }

    // 板块选择
    public static void jumpPlateSelect() {
        ARouter.getInstance()
                .build(Constant.SELECT_PLATE)
                .navigation();
    }

    /**
     * 跳转到明星板块
     *
     * @param id
     */
    public static void jumpStarPlate(String id, String name) {
        ARouter.getInstance()
                .build(Constant.STAR_PLATE)
                .withString(Constant.Param.ID, id)
                .withString(Constant.Param.NAME, name)
                .navigation();
    }


    // 跳转到我的资讯
    public static void jumpMyNews() {
        ARouter.getInstance().build(Constant.NEWS_LIST)
                .navigation();
    }


    /**
     * 跳转到帖子详情
     *
     * @param id
     */
    public static void jumpForumDetail(String id) {
        ARouter.getInstance()
                .build(Constant.DETAIL_ARTICLE)
                .withString(Constant.Param.ARTICLE_ID, id)
                .withInt(Constant.Param.TYPE, Constant.Param.FORUM_TYPE)
                .navigation();
    }
}
