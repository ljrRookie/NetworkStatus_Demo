package com.netease.network.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.network.library.NetworkManager;
import com.netease.network.library.annotation.Network;
import com.netease.network.library.type.NetType;
import com.netease.network.library.utils.Constants;

public class MainActivity extends BaseActivity {

    private TextView tv;
    private ImageView img;
    private int num = 0;


    @Override
    protected void initWidget() {
        super.initWidget();
        tv = findViewById(R.id.tv);
        img = findViewById(R.id.img);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }

    @Override
    protected void NetWorkStatusChange(int netWorkStatus) {
        switch (netWorkStatus) {
            case 1:
                Log.e(Constants.LOG_TAG, "MainActivity:WIFI"+num++);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("正在使用wifi");
                break;
            case 2:
                // 有网络
                Log.e(Constants.LOG_TAG, "MainActivity:有网络"+num++);
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("正在使用移动数据");
                break;

            case 0:
                // 没有网络，提示用户跳转到设置
                Log.e(Constants.LOG_TAG, "MainActivity:没网络"+num++);
                img.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText("网络不给力，请检查网络设置");
                break;
        }
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

}
