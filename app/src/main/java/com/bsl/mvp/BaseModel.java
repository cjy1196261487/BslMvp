package com.bsl.mvp;

import android.os.Handler;

import com.bsl.callback.BaseCallBack;
import com.bsl.okhttp.MyConnect;

import java.util.HashMap;

public class BaseModel {
    /**
     * 获取网络接口数据
     * @param param 请求参数
     * @param callback 数据回调接口
     */
    public static void getNetData(final String param, HashMap<String,Object> map, final BaseCallBack callback){
//        // 利用postDelayed方法模拟网络请求数据的耗时操作
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                switch (param){
//                    case "normal":
//                        callback.onSuccess("根据参数"+param+"的请求网络数据成功");
//                        break;
//                    case "failure":
//                        callback.onFailure("请求失败：参数有误");
//                        break;
//                    case "error":
//                        callback.onError("获取数据异常");
//                        break;
//                }
//                callback.onComplete();
//            }
//        },2000);
        MyConnect.post(param, map, new MyConnect.CallBack() {
            @Override
            public void setSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void setFail(int state) {
                callback.onFailure("请求失败:" + state);
            }
        });
    }
}
