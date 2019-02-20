package com.netease.network.demo;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.network.library.utils.Constants;

public class SecondActivity extends BaseActivity {



    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    protected void NetWorkStatusChange(int netWorkStatus) {
        super.NetWorkStatusChange(netWorkStatus);

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_second;
    }

}
