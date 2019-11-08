package com.fy.fayou.common;

public class ApiUrl {

    public static final String LOGIN_MOBILE = "/login/mobile";


    //   查看当前用户信息
    public static final String USER_DETAIL = "/user/detail";


    // 更新用户信息（传什么参数，就会更新什么）
    public static final String USER_UPDATE = "/user/updateUser";


    // 提交用户反馈/举报
    public static final String FEED_BACK = "/feedBack";

    // 查找我的关注
    public static final String MY_FOLLOW = "/userRelationship/findFollowingsList";


    // 取消关注
    public static final String USER_CANCEL_FOLLOW = "/userRelationship/unFollower";


    // 关注
    public static final String USER_REQUEST_FOLLOW = "/userRelationship/follower";


    // 查找用户粉丝
    public static final String USER_FAN = "/userRelationship/findFollowerList";


    // 发送验证码
    public static final String SEND_VERIFY_CODE = "/sms/getVerifyCode";


    // 换绑手机
    public static final String BIND_MOBILE = "/user/changeBindMobile";


    // 我的消息
    public static final String MESSAGE_CENTER = "/message";

    // 推荐
    public static final String PUBLIC_INVITE = "/public/invite";

    // 联系客服
    public static final String CONTACT_SERVICE = "/public/customerService";

    // 我的评论
    public static final String MY_COMMENT = "/comment/myComment";


    // 我的收藏
    public static final String MY_COLLECT = "/collect";

    // 历史记录
    public static final String MY_HISTORY = "/browse";

    // 我的资讯
    public static final String MY_NEWS = "/article/myArticle";

    // 新闻根据审核状态统计
    public static final String MY_NEWS_STATUS = "/article/groupByAuditStatus";

    // 获取热搜
    public static final String HOT_SEARCH = "/search/getHotSearch";


    // 获取各种类数据记录数
    public static final String SEARCH_RESULT_MENU = "/search/getSearchDataNumber";

}
