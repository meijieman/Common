package com.major.base;

import android.app.Application;

import com.major.base.log.LogUtil;
import com.major.base.util.ToastUtil;

/**
 * TODO
 * Created by MEI on 2017/9/22.
 */

public class CommonConfig {

    private static Application sInstance;

    private CommonConfig(Application app, String tag, boolean isDebug, boolean isTrack) {
        sInstance = app;
        LogUtil.init(app.getPackageName(), tag, isDebug, isTrack);
        ToastUtil.init(app.getApplicationContext());
    }

    public static Application getContext() {
        return sInstance;
    }

    public void destroy() {
        sInstance = null;
    }

    public static class Build {
        private Application mApplication;
        private String mTag;
        private boolean mIsDebug;
        private boolean mIsTrack;

        public Build setApplication(Application app) {
            mApplication = app;
            return this;
        }

        public Build setLogUtil(String tag, boolean isDebug, boolean isTrack) {
            mTag = tag;
            mIsDebug = isDebug;
            mIsTrack = isTrack;
            return this;
        }

        public CommonConfig build() {
            return new CommonConfig(mApplication, mTag, mIsDebug, mIsTrack);
        }
    }
}
