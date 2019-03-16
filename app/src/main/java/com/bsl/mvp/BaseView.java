package com.bsl.mvp;

import android.content.Context;

public interface BaseView {
    /**
     * 显示正在加载view
     */
    void showLoading();
    /**
     * 关闭正在加载view
     */
    void hideLoading();
//    /**
//     * 显示提示
//     * @param msg
//     */
//    void showToast(String msg);
//    /**
//     * 显示请求错误提示
//     */
//    void showErr(String msg);
    /**
     * 获取上下文
     * @return 上下文
     */
    Context getContext();
    /**
     * 当数据请求成功后，调用此接口显示数据
     * @param data 数据源
     */
    void showData(String data);
}
