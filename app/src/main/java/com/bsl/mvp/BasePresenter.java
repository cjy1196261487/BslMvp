package com.bsl.mvp;


import com.bsl.callback.BaseCallBack;

import java.util.HashMap;

public class BasePresenter<V extends BaseView> {
    // View接口
    private V mView;

    public BasePresenter() {
        //构造方法中不再需要View参数
    }

    /**
     * 获取网络数据
     * @param params 参数
     */
    public void getData(String params, HashMap<String,Object> map){
        if (!isViewAttached()){
            //如果没有View引用就不加载数据
            return;
        }
        //显示正在加载进度条
        getView().showLoading();
        // 调用Model请求数据
        BaseModel.getNetData(params,map, new BaseCallBack() {
            @Override
            public void onSuccess(Object data) {
                //调用view接口显示数据
                if(isViewAttached()){
                    getView().showData(data + "");
                }
            }

            @Override
            public void onFailure(String msg) {
                //调用view接口提示失败信息
                if(isViewAttached()){
                    getView().showData(msg + "");
                }
            }

            @Override
            public void onError(String msg) {
                if(isViewAttached()){
                    getView().showData(msg + "");
                }
            }

            @Override
            public void onComplete() {
                // 隐藏正在加载进度条
                if(isViewAttached()){
                    getView().hideLoading();
                }
            }
        });
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     */
    public void attachView(V mvpView) {
        this.mView = mvpView;
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        this.mView = null;
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached(){
        return mView!= null;
    }

    /**
     * 获取连接的view
     */
    public V getView(){
        return mView;
    }

}
