package com.netease.network.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.netease.network.library.annotation.Network;
import com.netease.network.library.bean.MethodManager;
import com.netease.network.library.type.NetType;
import com.netease.network.library.utils.Constants;
import com.netease.network.library.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {

    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;

    public NetStateReceiver() {
        // 初始化没有网络
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(Constants.LOG_TAG, "异常了");
            return;
        }

        // 处理广播事件
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_CHANGE_ACTION)) {
            Log.e(Constants.LOG_TAG, "网络发生改变");
            netType = NetworkUtils.getNetType(); // 网络类型
            if (NetworkUtils.isNetworkAvailable()) {
                Log.e(Constants.LOG_TAG, "网络连接成功");
            } else {
                Log.e(Constants.LOG_TAG, "没有网络连接");
            }
            // 通知所有注册的方法，网络发生了变化
            post(netType);
        }
    }

    // 同时分发
    private void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        // 比如获取MainActivity对象
        for (final Object getter : set) {
            // 所有注解的方法
            List<MethodManager> methodList = networkList.get(getter);
            if (methodList != null) {
                // 循环每个方法
                for (final MethodManager method : methodList) {
                    // public void network(Object netType) {错误的、不匹配的
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, getter, netType);
                                break;

                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;

                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;

                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, getter, netType);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager method, Object getter, NetType netType) {
        Method execute = method.getMethod();
        try {
            execute.invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void registerObserver(Object register) {
        // 获取MainActivity中所有的网络监听注解方法
        List<MethodManager> methodList = networkList.get(register);
        if (methodList == null) { // 不为空表示以前注册过
            // 开始添加方法，通过反射
            methodList = findAnnotationMethod(register);
            networkList.put(register, methodList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object register) {
        List<MethodManager> methodList = new ArrayList<>();

        Class<?> clazz = register.getClass();
        // 获取MainActivity所有的方法
        Method[] methods = clazz.getMethods();
        // 循环
        for (Method method : methods) {
            // 获取方法的注解
            Network network = method.getAnnotation(Network.class);
            if (network == null) {
                continue;
            }

            // 方法返回值校验
            Type returnType = method.getGenericReturnType();
            if (!"void".equals(returnType.toString())) {
                throw new RuntimeException(method.getName() + "方法返回必须是void");
            }

            // 参数校验
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "方法有且只有一个参数");
            }

            // 过滤的上面，得到符合要求的方法，才开始添加到集合methodList
            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodList.add(manager);
        }

        return methodList;
    }

    public void unRegisterObserver(Object register) {
        if (!networkList.isEmpty()) {
            networkList.remove(register);
        }
        Log.e(Constants.LOG_TAG, register.getClass().getName() + "注销成功");
    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        NetworkManager.getDefault().getApplication().unregisterReceiver(this);
        networkList = null;
        Log.e(Constants.LOG_TAG, "注销所有监听成功");
    }
}
