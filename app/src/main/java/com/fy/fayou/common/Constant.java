package com.fy.fayou.common;

public class Constant {

    public static final String BASE_URL = "http://118.31.76.141:9506";
    //public static final String BASE_URL = "http://192.168.1.21";

    public static final String TEST_URL = "http://api.zhdfxm.com";

    public static final String BASE_URL2 = "http://192.168.1.191:8080";
    public static final String BASE_URL3 = "http://192.168.1.166:8082";
    public static final String BASE_URL4 = "http://192.168.1.166:8081";
    public static final String BASE_URL5 = "http://192.168.1.166:8083";
    public static final String BASE_URL6 = "http://192.168.1.191:8089";
    public static final String BASE_URL_RELEASE = "http://192.168.1.166:8080";


    // 路由地址常量
    // 登录
    // public static final String LOGIN = "/fy/login";


    // 个人中心-消息页面
    public static final String PERSON_MESSAGE = "/person/message";


    // 个人中心-编辑资料
    public static final String PERSON_EDIT = "/person/edit";

    // 个人中心-设置
    public static final String PERSON_SETTING = "/person/setting";


    // 个人中心-意见反馈
    public static final String PERSON_SUGGEST = "/person/suggest";

    // 个人中心-重置手机
    public static final String PERSON_RESET_PHONE = "/person/reset_phone";

    // 个人中心-验证码
    public static final String PERSON_VERIFY_CODE = "/person/verify_code";


    // 个人中心-验证成功
    public static final String PERSON_PHONE_BIND_SUCCESS = "/person/phone_bind_success";

    // 个人中心-申诉
    public static final String PERSON_APPEAL = "/person/appeal";


    // 登录页面
    public static final String LOGIN = "/fy/login";


    // 创建昵称
    public static final String CREATE_NICKNAME = "/login/nickname";


    // 公共
    public static final String COMMON_WEB_VIEW = "/fy/webview";

    // 搜索
    public static final String HOME_SEARCH = "/home/search";

    // 搜索-结果
    public static final String HOME_RESULT_SEARCH = "/home/result_search";

    // 我的-关注
    public static final String MY_FOLLOW = "/my/follow";

    // 我的-粉丝
    public static final String MY_FAN = "/my/fan";

    // 发表-评论
    public static final String MY_PUBLISH_COMMENT = "/my/publish_comment";

    // 文章-详情
    public static final String DETAIL_ARTICLE = "/detail/article";

    // 资讯-发布
    public static final String NEWS_PUBLISH = "/news/publish";

    // 资讯-发布
    public static final String NEWS_PUBLISH_NEXT = "/news/publish_next";

    // 全网通缉
    public static final String WANTED = "/home/wanted";

    // 法律法规 司法解释
    public static final String LEGAL = "/home/legal";

    // 城市选择
    public static final String CITY_SELECT = "/city/select";

    // 资讯列表
    public static final String NEWS_LIST = "/news/list";

    public static final String MAIN = "/fayou/main";

    // 用户中心
    public static final String USER_CENTER = "/user/center";

    // 我的评论
    public static final String MY_COMMENT = "/my/comment";

    // 我的收藏
    public static final String MY_COLLECT = "/my/collect";

    // 我的历史
    public static final String MY_HISTORY = "/my/history";

    // 我的帖子
    public static final String MY_POST = "/my/post";

    // 关于我们
    public static final String ABOUT_MY = "/about/my";


    // 关于我们
    public static final String KT_ABOUT_MY = "/kt/about/my";

    // 合同模板
    public static final String CONTRACT_TEMPLATE = "/contract/template";

    // 合同筛选
    public static final String CONTRACT_FILTER = "/contract/filter";

    // 举报
    public static final String REPORT = "/detail/report";

    // 启动页
    public static final String FIRST_LAUNCH = "/first/launch";

    // 裁判文书筛选界面
    public static final String JUDGE_FILTER = "/judge/filter";

    // 板块关注
    public static final String PLATE_FOLLOW = "/plate/follow";

    // 选择板块
    public static final String SELECT_PLATE = "/select/plate";

    // 论坛详情
    public static final String FORUM_LIST = "/forum/list";

    // 明星板块
    public static final String STAR_PLATE = "/star/plate";

    // 更多板块 更多帖子
    public static final String MORE_BOARD = "/more/board";
    public static final String MORE_POST = "/more/post";

    // 邮箱登录
    public static final String EMAIL_LOGIN = "/email/login";

    // 更多搜索结果
    public static final String MORE_SEARCH_RESULT = "/more/search_result";

    public interface SP {

        String USER_INFO = "user_info_data";

        String CITY_INFO = "city_info_data";

        String SEARCH_HISTORY = "search_history";

        String PUBLISH_NEW = "publish_new";

        // 城市历史
        String CITY_HISTORY = "city_history";

        String LEARN_GUIDE = "city_history";

    }

    /**
     * 配置到 gradle
     */
    public interface QiNiu {
        String AccessKey = "cOUw3kpUgnqxLhPrQRZvwIAsTrOfOZcicNZFElCp"; // 此处填你自己的AccessKey
        String SecretKey = "ew5SxqqMY4Xxyk1MoWugSP2IEr9EfB2z1PxQSK8R"; // 此处填你自己的SecretKey
        String BUCKET_NAME = "fayou-storage";
        String DOMAIN = "http://qny.zhdfxm.com/";
    }

    public interface Param {

        String IS_FIRST_LAUNCH = "is_first_launch";

        String ID = "id";

        String ENTITY = "entity";

        String LIST = "list";

        String NAME = "name";

        String AUROT = "autor";

        String URL = "url";

        String DETAIL = "detail";

        String TITLE = "title";

        String MOBILE = "mobile";

        String TYPE = "type";

        String IS_RECOMMEND = "is_recommend";

        String POSITION = "position";

        String TAGS = "tags";

        String PRE_TYPE = "pre_type";

        String COLLECT_TYPE = "collect_type";

        String GUIDE_ID = "guide_id";

        String KEYWORD = "keyword";

        String CITY_NAME = "city_name";

        String FIXED_COLUMN = "fixed_column";

        String ARTICLE_ID = "article_id";

        String USER_ID = "userId";

        String RE_USER_ID = "re_userId";

        String CATEGORY_ID = "category_id";

        String USER_CENTER = "user_center";

        String VIDEO = "VIDEO";

        String IS_FORUM = "is_forum";

        String BEAN = "bean";

        String FILE_PATH = "file_path";

        int RESULT_CODE = 200;

        int REQUEST_CODE = 100;

        int JUDGE_RESULT_CODE = 201;

        int GUIDE_RESULT_CODE = 202;

        int VIDEO_TYPE = 1;

        int ARTICLE_TYPE = 0;

        int FORUM_TYPE = 2;

        long AGAIN_CLICK_DURATION = 1000;
    }
}
