package com.rxkj.wananpovertyaid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.rxkj.wananpovertyaid.utils.NetWorkUtils;


/**
 * created by 黄思凯 on 2019/8/23
 * 监听网络状态变更的广播接收器
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

    private NetChangeListener netChangeListener;

    public interface NetChangeListener {
        void onNetChange(boolean isConnect);
    }

    public NetworkConnectChangedReceiver(NetChangeListener netChangeListener) {
        this.netChangeListener = netChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean connected = NetWorkUtils.isConnected();
            if (netChangeListener != null) {
                netChangeListener.onNetChange(connected);
            }
        }
    }
}
