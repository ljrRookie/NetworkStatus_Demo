package com.netease.network.library.listener;

import com.netease.network.library.type.NetType;

public interface NetChangeObserver {

    /** 网络连接连接时调用 */
    void onConnect(NetType type);

    /** 当前没有网络连接 */
    void onDisConnect();

}

