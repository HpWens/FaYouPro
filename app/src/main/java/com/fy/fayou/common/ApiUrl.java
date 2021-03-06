package com.fy.fayou.common;

public class ApiUrl {

    public static final String LOGIN_MOBILE = "/api-user/login/mobile";


    //   查看当前用户信息
    public static final String USER_DETAIL = "/api-user/user/detail";


    // 更新用户信息（传什么参数，就会更新什么）
    public static final String USER_UPDATE = "/api-user/user/updateUser";


    // 提交用户反馈/举报
    public static final String FEED_BACK = "/api-user/feedBack";

    // 查找我的关注
    public static final String MY_FOLLOW = "/api-user/userRelationship/findFollowingsList";


    // 取消关注
    public static final String USER_CANCEL_FOLLOW = "/api-user/userRelationship/unFollower";


    // 关注
    public static final String USER_REQUEST_FOLLOW = "/api-user/userRelationship/follower";


    // 查找用户粉丝
    public static final String USER_FAN = "/api-user/userRelationship/findFollowerList";


    // 发送验证码
    public static final String SEND_VERIFY_CODE = "/api-sms/public/sms/getVerifyCode";


    // 换绑手机
    public static final String BIND_MOBILE = "/api-user/user/changeBindMobile";


    // 我的消息
    public static final String MESSAGE_CENTER = "/api-message/message";

    // 推荐
    public static final String PUBLIC_INVITE = "/api-user/public/invite";

    // 联系客服
    public static final String CONTACT_SERVICE = "/api-user/public/customerService";

    // 我的评论
    public static final String MY_COMMENT = "/api-information/comment/myComment";


    // 我的收藏
    public static final String MY_COLLECT = "/api-collect/collect";

    // 历史记录
    public static final String MY_HISTORY = "/api-collect/browse";

    // 我的资讯
    public static final String MY_NEWS = "/api-information/article/myArticle";

    // 新闻根据审核状态统计
    public static final String MY_NEWS_STATUS = "/api-information/article/groupByAuditStatus";

    // 获取热搜
    public static final String HOT_SEARCH = "/api-searchengines/public/search/getHotSearch";


    // 获取各种类数据记录数
    public static final String SEARCH_RESULT_MENU = "/api-searchengines/search/getSearchDataNumber";


    // 查询法律法规
    public static final String SEARCH_LEGAL = "/api-searchengines/public/legalProvisions/search";

    // 查询法律法规
    public static final String SEARCH_LEGAL_MORE = "/api-searchengines/public/legalProvisions/loadMore";


    // 查询司法解释
    public static final String SEARCH_JUDICIAL = "/api-searchengines/judicialInterpretation/search";


    // 查询司法解释
    public static final String SEARCH_JUDICIAL_MORE = "/api-searchengines/judicialInterpretation/loadMore";

    // 查询资讯
    public static final String SEARCH_NEWS = "/api-searchengines/newsinfo/search";

    // 查询资讯
    public static final String SEARCH_NEWS_MORE = "/api-searchengines/newsinfo/loadMore";


    // 查询文书
    public static final String SEARCH_JUDGEMENT = "/api-searchengines/judgement/loadMore";


    // 查询文书
    public static final String SEARCH_JUDGEMENT_MORE = "/api-searchengines/judgement/loadMore";


    // 查询案例
    public static final String SEARCH_CASE_INFO_MORE = "/api-searchengines/caseInfo/loadMore";

    // 查询案例
    public static final String SEARCH_CASE_INFO = "/api-searchengines/caseInfo/search";


    // 新闻分页查找
    public static final String HOME_ARTICLE = "/api-information/public/article";

    // 文章保存
    public static final String SAVE_ARTICLE = "/api-information/article";

    // 查找嫌疑人分类及人数统计
    public static final String CRIMINAL_COLUMN = "/api-information/public/criminalSuspect/findTypeList";


    // 查找列表
    public static final String CRIMINAL_FIND_LIST = "/api-information/public/criminalSuspect/findList";

    // 查找分类
    public static final String LEGAL_FIND_LIST = "/api-lawsRegulations/public/lawRegulationsCategory/findAll";

    // 指导性意见 获取一级分类带计数
    public static final String GET_GUIDE_LIST = "/api-popularize/public/caseType/getTypeWithCount";

    // 根据类型查找司法解释列表
    public static final String FIND_JUDICIAL_LIST = "/api-lawsRegulations/public/judicialInterpretation/findList";

    // 根据ID查找文章
    public static final String ARTICLE_DETAIL = "/api-information/public/article/";

    // 文章点赞
    public static final String ARTICLE_PRAISE = "/api-information/article/";

    // 评论查找
    public static final String COMMENT_LIST = "/api-information/public/comment";

    // 评论删除
    public static final String COMMENT_DELETE = "/api-information/comment/";

    // 发表评论
    public static final String PUBLISH_COMMENT = "/api-information/comment";

    // 评论点赞取消赞
    public static final String COMMENT_PRAISE = "/api-information/comment/";

    // 查看用户信息
    public static final String USER_INFO = "/api-user/user/view/";

    // 关注用户
    public static final String FOLLOW_USER = "/api-user/userRelationship/follower";

    // 取消关注用户
    public static final String UN_FOLLOW_USER = "/api-user/userRelationship/unFollower";


    // 判断是否收藏
    public static final String IS_COLLECT = "/api-collect/collect/exists";


    // 根据ID查找嫌疑人
    public static final String FIND_SUSPECT = "/api-information/criminalSuspect/findById";


    // 根据ID查找嫌疑人
    public static final String SEARCH_HISTORY = "/api-searchengines/search/record";


    // 分类查找
    public static final String FIND_CATEGORY = "/api-information/public/category";


    // 查找所有新闻标签
    public static final String FIND_ARTICLE_TAG = "/api-information/tag/findAll";


    // 设置密码
    public static final String SET_PASSWORD = "/api-user/user/setPassword";

    // 更新密码
    public static final String UPDATE_PASSWORD = "/api-user/user/updatePassword";

    // 统计文章评论条数
    public static final String COMMENT_COUNT = "/api-information/public/comment/count";

    // 查找更多评论
    public static final String FIND_COMMENT_MORE = "/api-information/public/comment/findMore";

    // 根据分类获取法律列表
    public static final String FIND_LEGAL_LIST = "/api-lawsRegulations/public/legalProvisions/findList";

    // 根据分类查询 指导性意见
    public static final String FIND_GUIDE_LIST = "/api-popularize/public/case/selectCase";

    // 获取合同模板类型
    public static final String GET_TEMPLATE_TYPE = "/api-contract/public/contract/getTypeList";

    // 获取合同列表
    public static final String GET_TEMPLATE_LIST = "/api-contract/public/contract/findContractList";

    // 获取合同条款
    public static final String GET_TEMPLATE_TERMS = "/api-contract/public/contract/findTermsById";


    // 全局搜索全部模块信息
    public static final String GET_SEARCH_RESULT = "/api-searchengines/public/search/searchAll";

    // 查找需要展示收藏的栏目
    public static final String GET_HISTORY_CATEGORY = "/api-collect/menu/findBrowsMenu";

    // 查找需要展示浏览记录的栏目
    public static final String GET_COLLECT_CATEGORY = "/api-collect/menu/findCollectMenu";

    // 判决文书分类
    public static final String GET_JUDGE_LEVEL = "/api-popularize/public/judgementType";

    // 获取一级分类 带计数
    public static final String GET_JUDGE_CATEGORY = "/api-popularize/public/judgementType/getTypeWithCount";

    // 判决文书分类查询
    public static final String GET_JUDGE_LIST = "/api-popularize/public/judgement/selectJudgement";

    // 获取标签列表
    public static final String GET_TEMPLATE_TAG = "/api-contract/public/tag/findList";

    // 查询后获取横栏，带计数
    public static final String GET_JUDGE_FILTER_CATEGORY = "/api-popularize/public/judgement/selectJudgementRail";

    // 查询文章内容
    public static final String GET_JUDGE_RELATED = "/api-popularize/public/judgement/cheackJudgementBindById";


    // 获取直到意见绑定信息
    public static final String GET_CASE_RELATED = "/api-popularize/public/case/cheackCaseBindById";


    //根据司法解释获取关联的法律法规
    public static final String GET_LEGAL_RELATED = "/api-lawsRegulations/public/legalProvisions/findRela";


    //根据司法解释获取关联的法律法规
    public static final String GET_JUDICIAL_RELATED = "/api-lawsRegulations/public/legalProvisions/getByJudicialInterpretation";


    //获取用户对合同的限制下载次数
    public static final String GET_DOWNLOAD_COUNT = "/api-contract/admin/contract/getCanDownloadNumber";

    //下载合同模板
    public static final String GET_DOWNLOAD_URL = "/api-contract/admin/contract/download";

    // 设置默认昵称
    public static final String SET_DEFAULT_NICKNAME = "/api-user/user/setDefaultNickName";

    /*************************************论坛模块***********************************************/
    // 我的关注版块列表 全部版块一级版块
    public static final String FORUM_MY_FOLLOW = "/api-forum/board";

    // 我的关注帖子列表
    public static final String FORUM_HOME_LIST = "/api-forum/post/attentionList";

    // 论坛推荐列表分页
    public static final String FORUM_HOME_RECOMMEND_LIST = "/api-forum/post/recommendList";

    // 关注or取消关注版块
    public static final String FORUM_FOLLOW_PLATE = "/api-forum/board/followed";


    // 版块详情+明星版友+置顶帖子
    public static final String FORUM_PLATE_LIST = "/api-forum/board/";


    // 获取版块下帖子列表分页
    public static final String GET_FORUM_LIST = "/api-forum/post";

    // 获取版块管理员列表
    public static final String GET_STAR_PLATE_HEADER = "/api-forum/board/adminUsers";

    // 获取版块明星版友列表
    public static final String GET_STAR_PLATE_LIST = "/api-forum/board/starUsers";

    // 获取帖子详情
    public static final String GET_FORUM_DETAIL = "/api-forum/post/";

    // 帖子点赞
    public static final String POST_FORUM_PRAISE = "/api-forum/post/";

    // 帖子举报
    public static final String POST_FORUM_REPORT = "/api-forum/report";

    // 点赞评论or取消点赞评论 {id}为评论id
    public static final String FORUM_COMMENT_PRAISE = "/api-forum/postComment/";

    //获取帖子全部评论列表
    public static final String GET_FORUM_FIRST_COMMENT = "/api-forum/postComment";

    //添加帖子评论/追评
    public static final String APPEND_FORUM_COMMENT = "/api-forum/postComment";

    //获取评论详情子评列表
    public static final String GET_FORUM_SECOND_COMMENT = "/api-forum/postComment/detailList";

    // 会话列表
    public static final String GET_FORUM_THREE_COMMENT = "/api-forum/postComment/conversationList";

    // 消息列表-删除
    public static final String DELETE_MESSAGE = "/api-message/message/delete";

    // 发帖
    public static final String POST_FORUM = "/api-forum/post";

    // 帖子搜索结果
    public static final String GET_FORUM_SEARCH_RESULT = "/api-searchengines/public/forumpost/getPreviewData";

    // 更多板块
    public static final String GET_MORE_BOARD = "/api-forum/board/keyword";

    // 搜索 更多帖子
    public static final String GET_MORE_POST = "/api-searchengines/public/forumpost/search";

    // 搜索 更多帖子
    public static final String GET_MORE_POST_MORE = "  /api-searchengines/public/forumpost/loadMore";


    // 我的帖子
    public static final String GET_MY_POST = "/api-forum/post/mine";


    // 用户帖子
    public static final String GET_USER_POST = "/api-forum/post/others";


    // 邮箱登录
    public static final String EMAIL_LOGIN = "/api-user/login/mailbox";


    // 未读消息
    public static final String MESSAGE_UNREAD_NUMBER = "/api-message/message/unReaderNum";

    // 未读消息
    public static final String WECHAT_LOGIN = "/api-user/login/wechat";

    // 典型模块下载次数
    public static final String TYPICAL_TEMPLATE_DOWNLOAD_COUNT = "/api-contract/admin/contract/downloadTypical";


    // 单模块检索
    public static final String GET_MORE_SEARCH_RESULT = "/api-searchengines/public/search/single";

    /**************************************1.1版本改动*******************************************************/
    // 获取树形菜单
    public static final String GET_CASE_TOP_CATEGORY = "/api-popularize/public/caseType";

    // 横栏计数获取
    public static final String GET_CASE_CATEGORY = "/api-popularize/public/case/selectCaseRail";

    // 案例列表
    public static final String GET_CASE_LIST = "/api-popularize/public/case/selectCaseForPub";

    // 案例筛选树形
    public static final String GET_GUIDE_LEAVE = "/api-popularize/public/caseType";

    // 典型模板
    public static final String GET_CONTRACT_EXIST = "/api-contract/public/contract/findById";

    // 闪屏停留时间
    public static final String GET_SPLASH_DURATION = "/api-collect/public/appSetting/getEnterDuration";


}
