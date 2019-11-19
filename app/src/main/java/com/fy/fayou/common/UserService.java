package com.fy.fayou.common;

import android.app.Application;
import android.text.TextUtils;

import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.pufa.PicEntity;
import com.fy.fayou.pufa.TextEntity;
import com.fy.fayou.pufa.TitleEntity;
import com.fy.fayou.utils.ACache;
import com.fy.fayou.utils.ParseUtils;
import com.google.gson.Gson;
import com.vondear.rxtool.RxSPTool;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void saveFirstLaunch(boolean isLaunched) {
        RxSPTool.putBoolean(mContext, Constant.Param.IS_FIRST_LAUNCH, isLaunched);
    }

    public boolean isFirstLaunch() {
        return RxSPTool.getBoolean(mContext, Constant.Param.IS_FIRST_LAUNCH);
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

    // 获取手机号码
    public String getMobile() {
        UserInfo userInfo = getUserInfo();
        if (null != userInfo) {
            return userInfo.mobile;
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

    // 保存历史记录
    public void saveHistorySearch(String record) {
        ACache.get(mContext).put(Constant.SP.SEARCH_HISTORY, record);
    }

    public List<String> getHistorySearch() {
        String data = ACache.get(mContext).getAsString(Constant.SP.SEARCH_HISTORY);
        if (TextUtils.isEmpty(data)) {
            return new ArrayList<>();
        } else {
            return ParseUtils.parseListData(data, String.class);
        }
    }

    public void addHistorySearch(String key) {
        List<String> data = getHistorySearch();
        if (data != null) {
            if (data.isEmpty()) {
                data.add(key);
            } else {
                if (data.contains(key)) {
                    data.remove(key);
                }
                data.add(0, key);
            }
            saveHistorySearch(new Gson().toJson(data));
        }
    }

    public void clearHistorySearch() {
        saveHistorySearch("");
    }


    // 分类保持 历史记录

    /**
     * @param json
     * @param classify 1 城市选择
     */

    public static int CLASSIFY_CITY_HISTORY = 1;

    public void saveClassify(String json, int classify) {
        String key = getClassifyKey(classify);
        ACache.get(mContext).put(key, json);
    }

    @NotNull
    private String getClassifyKey(int classify) {
        String key = Constant.SP.CITY_HISTORY;
        switch (classify) {
            default:
            case 1:
                key = Constant.SP.CITY_HISTORY;
                break;
        }
        return key;
    }

    public List<String> getClassify(int classify) {
        String key = getClassifyKey(classify);
        String data = ACache.get(mContext).getAsString(key);
        if (TextUtils.isEmpty(data)) {
            return new ArrayList<>();
        } else {
            return ParseUtils.parseListData(data, String.class);
        }
    }

    /**
     * 添加分类的数据
     *
     * @param value
     * @param classify
     */
    public void addClassify(String value, int classify) {
        List<String> data = getClassify(classify);
        if (data != null) {
            if (data.isEmpty()) {
                data.add(value);
            } else {
                if (data.contains(value)) {
                    data.remove(value);
                }
                data.add(0, value);
            }
            saveClassify(new Gson().toJson(data), classify);
        }
    }

    /**
     * 清理分类数据
     *
     * @param classify
     */
    public void clearClassify(int classify) {
        saveClassify("", classify);
    }


    /****************************************************发布资讯****************************************************/

    public void savePublishNew(List<Object> data) {
        ACache.get(mContext).put(Constant.SP.PUBLISH_NEW, new Gson().toJson(data));
    }

    public void clearPublishNew() {
        savePublishNew(new ArrayList<>());
    }

    /**
     * 获取发布缓存数据
     *
     * @return
     */
    public List<Object> getPublishNew() {
        String json = ACache.get(mContext).getAsString(Constant.SP.PUBLISH_NEW);
        if (TextUtils.isEmpty(json)) {
            return new ArrayList<>();
        } else {
            List<Object> data = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    if (object.has("type")) {
                        int type = object.optInt("type");
                        switch (type) {
                            case 0:
                                TitleEntity title = ParseUtils.parseData(object.toString(), TitleEntity.class);
                                data.add(title);
                                break;
                            case 1:
                                TextEntity text = ParseUtils.parseData(object.toString(), TextEntity.class);
                                data.add(text);
                                break;
                            case 2:
                                PicEntity pic = ParseUtils.parseData(object.toString(), PicEntity.class);
                                data.add(pic);
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

}
