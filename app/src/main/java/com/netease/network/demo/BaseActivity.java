package com.netease.network.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.netease.network.library.NetworkManager;
import com.netease.network.library.annotation.Network;
import com.netease.network.library.type.NetType;
import com.netease.network.library.utils.Constants;

public abstract class BaseActivity extends AppCompatActivity {
    private static  int NetWorkStatus = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layId = getContentLayoutId();
        setContentView(layId);
        // 注册
        NetworkManager.getDefault().registerObserver(this);
        initWidget();
        initData();
    }
    /**
     * 初始化控件
     */
    protected void initWidget() {
        Log.e(Constants.LOG_TAG, "BaseActivity:initWidget()");
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }
    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(Constants.LOG_TAG, "WIFI");
                NetWorkStatus = 1;
                break;
            case CMNET:
            case CMWAP:
                // 有网络
                NetWorkStatus = 2;
                Log.e(Constants.LOG_TAG, "有网络");
                break;

            case NONE:
                NetWorkStatus = 0;
                // 没有网络，提示用户跳转到设置
                Log.e(Constants.LOG_TAG, "没网络");
                break;
        }
        NetWorkStatusChange(NetWorkStatus);
    }

    protected void NetWorkStatusChange(int netWorkStatus) {
        switch (netWorkStatus) {
            case 1:
                Toast.makeText(this,"网络状态：正在使用wifi",Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG_TAG, "BaseActivity:WIFI");
                break;
            case 2:
                // 有网络
                Toast.makeText(this,"网络状态：正在使用移动数据",Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG_TAG, "BaseActivity:移动数据");
                break;

            case 0:
                // 没有网络，提示用户跳转到设置
                Toast.makeText(this,"网络不给力，请检查网络设置",Toast.LENGTH_LONG).show();
                Log.e(Constants.LOG_TAG, "BaseActivity:网络不给力，请检查网络设置");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 反注册、解绑
        NetworkManager.getDefault().unRegisterObserver(this);

    }
}
