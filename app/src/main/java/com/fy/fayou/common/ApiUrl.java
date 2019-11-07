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

}
