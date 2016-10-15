package nf.framework.core.util;

import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import nf.framework.core.exception.LogUtil;

/**
 * 时间测量工具类
 * @ClassName TimeUtil 
 * @Description measureNanoTime 用于android应用性能优化
 * @date 2015-4-14 上午11:34:41 
 *
 */
public class TimeUtil {
    private long startTime = 0L;    //起始时间
    private long endTime = 0L;      //结束时间
    private int Factor = 1;         //系数1
    private String msg = null;      //说明文字，指明执行的操作
    private double duration = 0.0;  //时间间隔，单位：纳秒（十亿分之一秒）
    
    public void start(String msg) {
        this.msg = msg;
        if(!TextUtils.isEmpty(this.msg)) {
            this.msg += ", ";
        }
        
        getStartTime();
    }
    
    public void end() {
        getEndTime();
        measureNanoTime();
        showNanoTimeInfo();
    }
    
    public void showNanoTimeInfo() {
        Log.i("时间测量", msg + "Duration " + duration + " anoseconds");
    }
    
    private void measureNanoTime() {
        if(Factor == 1) {
            duration = endTime - startTime;
        } else {
            duration = (double)(endTime-startTime) / 100000;
        }
    }
    
    public long getStartTime() {
        //在某些系统上可能不支持，返回-1
        startTime = Debug.threadCpuTimeNanos();
        if(startTime == -1) {
            Factor = 100000;
            //System.currentTimeMillis()精度和准确度不够，更改系统时间有影响。
            //System.nanoTime()要测量的操作可能被中断几次，来让出CPU时间给别的线程
            //测量结果可能包含其他代码的执行时间，用Debug.threadCpuTimeNanos()更好些.
            startTime = System.nanoTime();
        }
        return startTime;
    }
    
    public long getEndTime() {
        if(Factor == 1) {
            endTime = Debug.threadCpuTimeNanos();
        } else {
            endTime = System.nanoTime();
        }
        return endTime;
    }

    /**
     * 
     * @Description 指明执行的操作
     * @param msg 说明文字
     * void
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @Description 取时间间隔
     * @return
     * double 单位：纳秒
     */
    public double getDuration() {
        return duration;
    }
public static long getLongTime(String date){

    String substring1 = date.substring(0, 19);
    String dateTime = substring1.replace("T"," ");
    Calendar c = Calendar.getInstance();
    LogUtil.e("hjh dateTime ==>",dateTime);
    try {
        c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    long longTime = c.getTimeInMillis()/1000;
    return longTime;
}
}
