package com.bsl.okhttp;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyConnect {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int POSTJSON = 2;
    public static OkHttpClient client = new OkHttpClient();
    public static String TAG = "MyConnect";
    private CallBack callBack;
    public static String URL = "http://120.78.227.98:7500";
    public static boolean ServerException = false;
    public static Thread thread;

    public static int state = 0;


    public interface CallBack {
        void setSuccess(String result);

        void setFail(int state);
    }

    /**
     *      *
     *      * @param url 请求地址
     *      * @param json json字符串请求参数
     *      * @return
     *      * @throws IOException
     *      
     */
    public static String postJson(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
//response.body().string()这一句代码在方法体里面只能用一次(包括打印输出的使用)
        return response.body().string();

    }


    /**
     * *
     * * @param url 请求地址
     * * @param map 请求参数(把map转换成gson)
     *      
     */


    public static void rquestDataFromePostJson(Context context, final String url, final Map<String, Object> map, final CallBack callBack) {
//        if (!NetworkUtils.isNetWorkAvailable(context)) {
//            ToastCustom.getInstance(context).show("网络未打开，请打开网络。", ToastCustom.TIME);
//            return;
//        }
//        if (!NetworkUtils.isMobileDataEnable(context) && !NetworkUtils.isWifiDataEnable(context)) {
//            ToastCustom.getInstance(context).show("当前网络不可用。", ToastCustom.TIME);
//            return;
//        }
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    ServerException = true;
                    int state = 0;
                    //Map转JSON数据
                    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                    String result = postJson(url, gson.toJson(map));
                    Log.e(TAG, "MAP:" + gson.toJson(map));
                    Message message = Message.obtain();
                    message.what = POSTJSON;
                    message.obj = result;
                    Log.e(TAG, "JSON:" + result + "");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        state = jsonObject.optInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (state == 0) {
                        callBack.setSuccess(result + "");
                    } else {
                        callBack.setFail(state);
                    }
//                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        if (!ServerException) {
            callBack.setFail(10);
        }
    }



    /**
     * post请求
     * @param
     * @param url
     * @param map
     * @param callBack
     */
    public static void post(String url, final Map<String, Object> map, final CallBack callBack) {
//        if (!NetworkUtils.isNetWorkAvailable(context)) {
//            ToastCustom.getInstance(context).show("网络未打开，请打开网络。", ToastCustom.TIME);
//            return;
//        }
//        if (!NetworkUtils.isMobileDataEnable(context) && !NetworkUtils.isWifiDataEnable(context)) {
//            ToastCustom.getInstance(context).show("当前网络不可用。", ToastCustom.TIME);
//            return;
//        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");//"类型,字节码"
        //Map转JSON数据
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        //1.创建OkHttpClient对象
        OkHttpClient  okHttpClient = new OkHttpClient();
        //2.通过RequestBody.create 创建requestBody对象
        RequestBody requestBody =RequestBody.create(mediaType, gson.toJson(map));
        //3.创建Request对象，设置URL地址，将RequestBody作为post方法的参数传入
        Request request = new Request.Builder().url(url).post(requestBody).build();
        //4.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        Log.e(TAG, "URL:" + url);
        Log.e(TAG, "MAP:" + gson.toJson(map));
        //5.请求加入调度,重写回调方法
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.setFail(10);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.e(TAG,body + "");
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    state = jsonObject.optInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (state == 0) {
                    callBack.setSuccess(body + "");
                } else {
                    callBack.setFail(state);
                }
            }
        });

    }

    /**
     * get
     * @param context
     * @param url
     * @param callBack
     */
    public static void get(Context context,String url,CallBack callBack){
        //1.创建OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2.创建Request对象，设置一个url地址（百度地址）,设置请求方式。
        Request request = new Request.Builder().url(url).method("GET",null).build();
        //3.创建一个call对象,参数就是Request请求对象
        Call call = okHttpClient.newCall(request);
        //4.请求加入调度，重写回调方法
        call.enqueue(new Callback() {
            //请求失败执行的方法
            @Override
            public void onFailure(Call call, IOException e) {
            }
            //请求成功执行的方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }




//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
////                case GET:
////                    Log.i(TAG, "GET---->" + (String) msg.obj);
////                    break;
//
//
//                case POSTJSON:
//                    Log.i(TAG, "POSTJSON---->" + (String) msg.obj + msg.arg1);
//                    break;
//            }
//        }
//    };

}
