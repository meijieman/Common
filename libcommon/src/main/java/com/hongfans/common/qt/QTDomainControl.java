package com.hongfans.common.qt;

import android.text.TextUtils;

import com.hongfans.common.log.LogUtil;
import com.hongfans.common.qt.bean.QTDomainList;
import com.hongfans.common.qt.bean.QTToken;
import com.hongfans.common.rx.RxSchedulers;
import com.hongfans.common.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class QTDomainControl {

    private static final String URL_QT_TOKEN = "http://api.open.qingting.fm/access?&grant_type=client_credentials&client_id=ZjBkMDEyNDYtMmY3Zi0xMWU1LTkyM2YtMDAxNjNlMDAyMGFk&client_secret=YWE0MjcyMDctMmI5Yy0zZWM0LWI3MmYtOWUyNjdiZGIwOTI3";

    public static final String TAG = "tag_qt";
    static String QT_DEFAULT_RET = "Hello Qingting!"; // qt 测试域名返回成功的字符串
    private static QTDomainControl sInstance = new QTDomainControl();
    private final String URL_TEST = "http://%s%s"; // 拼接成测试 url
    boolean isGetDomainList = true; // 是否查询域名列表
    private int total;
    private int count;
    private String test_path;
    private String[] domainArr; // 获取到的 域名列表
    private List<UrlEntry> domainList = new ArrayList<>();
    private ScheduledExecutorService service;
    private int index = -1;// 记录上次请求到的索引，每次重新 ping 后还原
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private QTDomainControl() {

    }

    public static QTDomainControl getInstance() {

        return sInstance;
    }

    // 测试
    public static void main(String args[]) {
        QTDomainControl control = new QTDomainControl();
        control.domainList.add(new UrlEntry("http://github.com.4", 0L, 30L));
        control.domainList.add(new UrlEntry("http://github.com.2", 0L, 30L));
        control.domainList.add(new UrlEntry("http://github.com.3", 0L, 5L));
        control.domainList.add(new UrlEntry("http://github.com.1", 0L, 10L));
        control.domainList.add(new UrlEntry("http://github.com.5", 0L, -10L));

        String domain = control.getFastUrl("http://www.baidu.2/666.mp3");
        System.out.println("\n------ domain " + domain);
    }

    public void startTask() {
        LogUtil.i(TAG, "执行任务");
        Runnable task = new Runnable() {

            @Override
            public void run() {
                if (total != count) {
                    LogUtil.e(TAG, " 上次执行有任务未完成  " + count + "/" + total);
                    mCompositeSubscription.clear();
                }
                total = 0;
                count = 0;
                index = -1;

                if (isGetDomainList) {
                    domainArr = null;
                    isGetDomainList = false;
                    getDomainList();
                } else {
                    pingUrl();
                }
            }
        };
        service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    public void stopTask() {
        if (service != null) {
            service.shutdown();
        }
    }

    public void getDomainList() {
        QTFactory.getInstance().getQTApi().getQTToken(URL_QT_TOKEN)
                .compose(RxSchedulers.<QTToken>switchThird())
                .subscribe(new Action1<QTToken>() {
                    @Override
                    public void call(QTToken qtToken) {
                        LogUtil.i(TAG, "获取到 token " + qtToken.getAccess_token());
                        if (StringUtils.isNotEmpty(qtToken.getAccess_token())) {
                            QTFactory.getInstance().getQTApi().getQTDomainList(qtToken.getAccess_token())
                                    .compose(RxSchedulers.<QTDomainList>switchThird())
                                    .subscribe(new Action1<QTDomainList>() {
                                        @Override
                                        public void call(QTDomainList qtDomainList) {
                                            LogUtil.i(TAG, "获取到域名列表 " + qtDomainList);
                                            if (qtDomainList != null) {
                                                QTDomainList.Data data = qtDomainList.getData();
                                                if (data != null) {
                                                    QTDomainList.RadioStation radiostations_hls = data.getRadiostations_hls();
                                                    if (radiostations_hls != null) {
                                                        List<QTDomainList.MediaCenter> mediacenters = radiostations_hls.getMediacenters();
                                                        if (mediacenters != null) {
                                                            for (QTDomainList.MediaCenter mediacenter : mediacenters) {
                                                                String backup_ips = mediacenter.getBackup_ips();
                                                                if (StringUtils.isNotEmpty(backup_ips)) {
                                                                    test_path = mediacenter.getTest_path();
                                                                }
                                                                domainArr = backup_ips.split(";");
                                                                // 开始 ping
                                                                if (TextUtils.isEmpty(test_path) || domainArr == null || domainArr.length == 0) {
                                                                    isGetDomainList = true;
                                                                } else {
                                                                    pingUrl();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    });
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void pingUrl() {
        LogUtil.i(TAG, " pingDomain  test_path " + test_path + " 获取到的 domain " + Arrays.toString(domainArr));
        if (TextUtils.isEmpty(test_path) || domainArr == null || domainArr.length == 0) {
            return;
        }
        total = domainArr.length;
        for (String domain : domainArr) {
            final String url = String.format(URL_TEST, domain, test_path);
            LogUtil.i(TAG, " 开始ping " + url);
            boolean isContain = false;
            synchronized (QTDomainControl.class) {
                long startTime = System.currentTimeMillis();
                for (UrlEntry entry : domainList) {
                    if (url.equalsIgnoreCase(String.format(URL_TEST, entry.domain, test_path))) {
                        entry.startTime = startTime;
                        entry.endTime = 0;
                        isContain = true;
                        break;
                    }
                }

                if (!isContain) {
                    domainList.add(new UrlEntry(domain, startTime));
                }
            }
            Subscription subscribe = QTFactory.getInstance().getQTApiString().pingQTDomain(url)
                    .compose(RxSchedulers.<String>switchThird())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String qtPing) {
                            synchronized (QTDomainControl.class) {
                                if (!QT_DEFAULT_RET.equalsIgnoreCase(qtPing)) {
                                    return;
                                }
                                long endTime = System.currentTimeMillis();
                                for (UrlEntry entry : domainList) {
                                    if (url.equalsIgnoreCase(String.format(URL_TEST, entry.domain, test_path))) {
                                        entry.endTime = endTime;
                                        break;
                                    }
                                }
                                count++;
                                if (count == total) {
                                    LogUtil.i(TAG, " 最快的 " + getFastUrl("http://github.com.1/666.mp3")); // 测试用
                                }
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
            mCompositeSubscription.add(subscribe);
        }
    }

    private void sortDomain() {
        synchronized (QTDomainControl.class) {
            // 删除没有 endTime 的域名
            for (int i = domainList.size() - 1; i >= 0; i--) {
                if (domainList.get(i).endTime == 0L) {
                    domainList.remove(i);
                }
            }
            if (domainList.size() <= 1) {
                return;
            }
            Collections.sort(domainList, new Comparator<UrlEntry>() {

                @Override
                public int compare(UrlEntry lhs, UrlEntry rhs) {
                    long lhsTime = lhs.endTime - lhs.startTime;
                    long rhsTime = rhs.endTime - rhs.startTime;

                    // 可能没有 endTime，则表示 url 超时
                    if (lhsTime > 0 && rhsTime > 0) {
                        return lhsTime <= rhsTime ? -1 : 1;
                    }

                    if (lhsTime > 0) {
                        return -1;
                    }

                    return 1;
                }
            });
        }
    }

    // http://hls.qingting.fm/live/1270.m3u8
    // http://http.hz.qingting.fm/1271.mp3
    public String getFastUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url cannot be null or empty");
        }
//		LogUtil.saveLog(Constant.getLogDir(),TAG + " 原始URL " + url);
        String protocol = url.substring(0, url.indexOf(":"));
        String tmp = url.replace(protocol + "://", "");
        int indexOf = tmp.indexOf("/");
        String originDomain = tmp.substring(0, indexOf);
        String path = tmp.substring(indexOf);
        String retUrl = protocol + "://%s" + path;

        if (index == -1) {
            sortDomain();
        }

        prettyPrint();

        if (domainList.size() == 0) {
            return null;
        } else if (domainList.size() == 1) {
            if (domainList.get(0).domain.equalsIgnoreCase(originDomain)) {
                return null;
            } else {
                return String.format(retUrl, domainList.get(0).domain);
            }
        } else {
            /*
             * 默认域名列表中没有两个域名相同
			 * 1. 若上一次取的是域名列表的最后一个，则本次取第一个，否则取上一次取的下一个
			 * 2. 若本次取的 url 和 原始 url 相同，则取下一个（规则为1）
			 */
            if (index >= domainList.size() - 1) {
                index = 0;
            } else {
                index++;
            }

            if (domainList.get(index).domain.equalsIgnoreCase(originDomain)) {
                if (index >= domainList.size() - 1) {
                    index = 0;
                } else {
                    index++;
                }
            }

            return String.format(retUrl, domainList.get(index).domain);
        }
    }

    public void prettyPrint() {
//		LogUtil.saveLog(Constant.getLogDir(),TAG + " ===============================================start");
//		for (UrlEntry entry : domainList)
//		{
//			LogUtil.saveLog(Constant.getLogDir(),TAG + entry);
//		}
//		LogUtil.saveLog(Constant.getLogDir(),TAG + " ===============================================end");
    }

    static class UrlEntry {

        String domain;

        long startTime;

        long endTime;

        public UrlEntry(String url) {
            this.domain = url;
        }

        public UrlEntry(String url, long startTime, long endTime) {
            this.domain = url;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public UrlEntry(String url, long startTime) {
            this.domain = url;
            this.startTime = startTime;
        }

        public String toString() {
            return " url " + domain + " startTime " + startTime
                    //      + " endTime " + endTime
                    + " used " + (endTime - startTime);
        }
    }
}
