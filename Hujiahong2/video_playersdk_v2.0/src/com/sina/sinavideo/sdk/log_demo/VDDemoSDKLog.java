package com.sina.sinavideo.sdk.log_demo;

import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDSDKLogManager.VDSdkLog;

public class VDDemoSDKLog implements VDSdkLog {

    public final static String TAg = "VDDemoSDKLog";

    @Override
    public void onStatusChange(int index, int status) {
        // TODO Auto-generated method stub
        VDLog.d(TAg, "onStatusChange,status:" + status);
    }

    @Override
    public void onInfoLog(int index, int status) {
        // TODO Auto-generated method stub
        VDLog.d(TAg, "onInfoLog,status:" + status);
    }

    @Override
    public void onErrorLog(int index, int status) {
        // TODO Auto-generated method stub
        VDLog.d(TAg, "onErrorLog,status:" + status);
    }

    @Override
    public void release(int index) {
        // TODO Auto-generated method stub
        VDLog.d(TAg, "release");
    }

    @Override
    public void networkChanged(int index, int type) {
        // TODO Auto-generated method stub
        VDLog.d(TAg, "networkChanged,type:" + type);
    }

}
