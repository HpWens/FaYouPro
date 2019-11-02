package com.fy.fayou.common;

import android.app.Application;
import android.text.TextUtils;

import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.utils.ACache;

public class UserService {

    private volatile static UserService singleton = null;

    private static Application mContext;

    public UserService() {

    }

    public static UserService getInstance() {
        testInitialize();
        if (singleton == null) {
            synchronized (UserService.class) {
                if (singleton == null) {
                    singleton = new UserService();
                }
            }
        }
        return singleton;
    }

    private static void testInitialize() {
        if (mContext == null)
            throw new ExceptionInInitializerError("请先在全局Application中调用 UserService.init() 初始化！");
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public static void init(Application app) {
        mContext = app;
    }


    public void saveUser(UserInfo user) {
        ACache.get(mContext).put(Constant.SP.USER_INFO, user);
    }

    public void clearUser() {
        ACache.get(mContext).put(Constant.SP.USER_INFO, new UserInfo());
    }

    public UserInfo getUserInfo() {
        Object userObj = ACache.get(mContext).getAsObject(Constant.SP.USER_INFO);
        if (null != userObj && userObj instanceof UserInfo) {
            return (UserInfo) userObj;
        }
        return new UserInfo();
    }

    public String getToken() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.token;
        }
        return "";
    }

    // 获取到头像
    public String getAvatar() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.avatar;
        }
        return "";
    }

    // 获取昵称
    public String getNickName() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.nickName;
        }
        return "";
    }

    // 获取用户id
    public String getUserId() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.id;
        }
        return "";
    }


    // 设置昵称
    public void setNickName(String nick) {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo && !TextUtils.isEmpty(nick)) {
            userInfo.nickName = nick;
            saveUser(userInfo);
        }
    }

    // 设置头像
    public void setAvatar(String avatar) {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo && !TextUtils.isEmpty(avatar)) {
            userInfo.avatar = avatar;
            saveUser(userInfo);
        }
    }

    // 判定是否登陆
    public boolean isLogin() {
        UserInfo userInfo = getUserInfo();
        if (null == userInfo || TextUtils.isEmpty(userInfo.id)) {
            return false;
        }
        return true;
    }
}
