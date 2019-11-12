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


    // 查询法律法规
    public static final String SEARCH_LEGAL = "/legalProvisions/search";

    // 查询法律法规
    public static final String SEARCH_LEGAL_MORE = "/legalProvisions/loadMore";


    // 查询司法解释
    public static final String SEARCH_JUDICIAL = "/judicialInterpretation/search";


    // 查询司法解释
    public static final String SEARCH_JUDICIAL_MORE = "/judicialInterpretation/loadMore";

    // 查询资讯
    public static final String SEARCH_NEWS = "/newsinfo/search";

    // 查询资讯
    public static final String SEARCH_NEWS_MORE = "/newsinfo/loadMore";


    // 查询文书
    public static final String SEARCH_JUDGEMENT = "/judgement/loadMore";


    // 查询文书
    public static final String SEARCH_JUDGEMENT_MORE = "/judgement/loadMore";


    // 查询案例
    public static final String SEARCH_CASE_INFO_MORE = "/caseInfo/loadMore";

    // 查询案例
    public static final String SEARCH_CASE_INFO = "/caseInfo/search";


    // 新闻分页查找
    public static final String HOME_ARTICLE = "/article";


    // 查找嫌疑人分类及人数统计
    public static final String CRIMINAL_COLUMN = "/criminalSuspect/findTypeList";


    // 查找列表
    public static final String CRIMINAL_FIND_LIST = "/criminalSuspect/findList";

    // 查找分类
    public static final String LEGAL_FIND_LIST = "/lawRegulationsCategory/findAll";

    // 根据ID查找文章
    public static final String ARTICLE_DETAIL = "/article/";

    // 评论查找
    public static final String COMMENT_LIST = "/comment";
}
