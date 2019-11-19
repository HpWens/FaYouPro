package com.fy.fayou;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.bean.UserInfo;
import com.fy.fayou.common.Constant;
import com.fy.fayou.common.UserService;
import com.google.gson.Gson;
import com.meis.base.mei.BaseApplication;
import com.vondear.rxtool.RxTool;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import retrofit2.converter.gson.GsonConverterFactory;

public class FYApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initARouter();

        RxTool.init(this);

        UserService.init(this);

        initEasyHttp();

        initPush();
    }

    private void initPush() {
        JPushInterface.setDebugMode(isDebug());
        // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }

    private void initEasyHttp() {
        // 初始化网络框架
        EasyHttp.init(this);
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        UserInfo userInfo = UserService.getInstance().getUserInfo();
        if (null != userInfo && !TextUtils.isEmpty(userInfo.token)) {
            headers.put("Authorization", userInfo.token);
        }
        // 设置请求参数
        HttpParams params = new HttpParams();
        EasyHttp.getInstance()
                .debug("RxEasyHttp", isDebug() ? BuildConfig.DEBUG : false)
                .setReadTimeOut(10 * 60 * 1000)
                .setWriteTimeOut(10 * 60 * 1000)
                .setConnectTimeout(10 * 60 * 1000)
                .setRetryCount(3) // 默认网络不好自动重试3次
                .setRetryDelay(500) // 每次延时500ms重试
                .setRetryIncreaseDelay(500) // 每次延时叠加500ms
                .setBaseUrl(Constant.BASE_URL)
                .setCacheDiskConverter(new SerializableDiskConverter()) // 默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024) // 设置缓存大小为50M
                .setCacheVersion(1) // 缓存版本为1
                .setCertificates() // 信任所有证书
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCommonHeaders(headers) // 设置全局公共头
                .addCommonParams(params); // 设置全局公共参数
    }

    private void initARouter() {
        if (isDebug()) {
            ARouter.openLog();     // Print log
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    public boolean isDebug() {
        boolean debuggable = false;
        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), 0);
            debuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /*debuggable variable will remain false*/
        }
        return debuggable;
    }

    public void addEasyTokenHeader() {
        HttpHeaders headers = new HttpHeaders();
        UserInfo userInfo = UserService.getInstance().getUserInfo();
        if (null != userInfo && !TextUtils.isEmpty(userInfo.token)) {
            headers.put("Authorization", userInfo.token);
            EasyHttp.getInstance().getCommonHeaders().clear();
            EasyHttp.getInstance().addCommonHeaders(headers);
        }
    }

    public void clearTokenHeader() {
        EasyHttp.getInstance().getCommonHeaders().clear();
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
