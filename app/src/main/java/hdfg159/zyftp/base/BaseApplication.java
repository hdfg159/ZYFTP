package hdfg159.zyftp.base;

import android.app.Application;
import android.os.Looper;

import java.util.TimerTask;

import hdfg159.zyftp.utils.CrashReporter;
import hdfg159.zyftp.utils.TimertaskUtils;
import hdfg159.zyftp.utils.ToastUtil;

/**
 * Created by ZZY2015 on 2016/3/3.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReporter crashReporter = new CrashReporter(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(crashReporter);

        crashReporter.setOnCrashListener(new CrashReporter.CrashListener() {
            @Override
            public void onCrash(Thread thread, Throwable ex) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        ToastUtil.showToast(getApplicationContext(), "程序出现异常！已经生成日志报告");
                        Looper.loop();
                    }
                }).start();
                TimertaskUtils.delaytask(new TimerTask() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 1000);
            }
        });
    }
}
