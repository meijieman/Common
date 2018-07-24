package com.major.base.log;

import com.major.base.rx.rxtask.RxTask;
import com.major.base.util.FileUtil;

/**
 * 作者:meijie
 * 包名:com.major.base.log
 * 工程名:Common
 * 时间:2018/7/24 10:38
 * 说明: LogUtil 增强功能
 */
public class SaveLogUtil extends LogUtil{

    /**
     * 保存 log 到本地
     */
    public static void save(final String path, int level, String tag, Object msg, boolean isDebug, final boolean isAppend){
        if(isDebug){
            final String log = log(true, false, tag, msg, level);
            RxTask.doOnIOThread(new RxTask.IOTask(){
                @Override
                public void onIOThread(){
                    FileUtil.saveFile(log, path, isAppend);
                }
            });
        }
    }

    public static void save(final String path, Object msg){
        save(path, LEVEL_I, "", msg, true, true);
    }
}
