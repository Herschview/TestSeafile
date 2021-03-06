package com.hersch.testseafile.process;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.Process;
import java.util.List;

/**
 * @author Hersch
 * 类功能:判断进程是否在前台或者后台运行
 */
public class CustomProcess {
    static Process process;
    static int processNum = 150;
    public static boolean isProcessRunning(Context context,String processName){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(processNum);
        if(runningTaskInfos.size()<=0){
            return false;
        }
        for(ActivityManager.RunningTaskInfo runningTaskInfo:runningTaskInfos){
            if(runningTaskInfo.baseActivity.getPackageName().equals(processName)){
                return true;
            }
        }
        return false;
    }
    public static boolean isServiceRunning(Context context,String processName){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = activityManager.getRunningServices(processNum);
        if(runningServiceInfos.size()<=0){
            return false;
        }
        for(ActivityManager.RunningServiceInfo runningServiceInfo:runningServiceInfos){
            if(runningServiceInfo.service.getPackageName().equals(processName)){
                return true;
            }
        }
        return false;

    }
    /**
     * 结束进程,执行操作调用即可即调用了adb shell的su am force stop com.tencent.mm
     */
    public static void kill(String packageName) {
        initProcess();
        killProcess(packageName);
        close();
    }

    /**
     * 初始化进程
     */
    private static void initProcess() {
        if (process == null)
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    /**
     * 结束进程
     */
    private static void killProcess(String packageName) {
        OutputStream out = process.getOutputStream();
        String cmd = "am force-stop " + packageName + " \n";
        try {
            out.write(cmd.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     */
    private static void close() {
        if (process != null)
            try {
                process.getOutputStream().close();
                process = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
