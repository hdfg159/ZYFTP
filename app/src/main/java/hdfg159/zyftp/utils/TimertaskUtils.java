package hdfg159.zyftp.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZZY2015 on 2016/2/15.
 */
public class TimertaskUtils {
    public static Timer timer = new Timer();

    /**
     * 每隔period时间段循环执行
     *
     * @param timerTask
     * @param period
     */
    public static void looptask(TimerTask timerTask, long period) {
        timer.schedule(timerTask, 0, period);
    }

    /**
     * 延时任务
     *
     * @param timerTask
     * @param delay
     */
    public static void delaytask(TimerTask timerTask, long delay) {
        timer.schedule(timerTask, delay);
    }
}
