package com.fy.fayou;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.meis.base.mei.BaseApplication;
import com.vondear.rxtool.RxTool;

public class FYApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        initARouter();

        RxTool.init(this);
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
}
