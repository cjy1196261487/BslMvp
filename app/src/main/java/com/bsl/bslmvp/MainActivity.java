package com.bsl.bslmvp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bsl.mvp.BasePresenter;
import com.bsl.mvp.BaseView;
import com.bsl.okhttp.MyConnect;
import com.bsl.tools.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BaseView {
    private EditText zh,mm;
    private BasePresenter presenter;
    public static String TAG = "MVP--------->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zh = findViewById(R.id.zh);
        mm = findViewById(R.id.mm);
        presenter = new BasePresenter();
        //绑定当前view
        presenter.attachView(this);
    }


    public void getLogin(View view){
        HashMap<String,Object> map = new HashMap<>();
        map.put("Cellphone",zh.getText().toString() + "");
        map.put("Password",mm.getText().toString() + "");
        presenter.getData(MyConnect.URL + "/user/login",map);
    }

    public void getRegister(View view){
//        startActivity(new Intent(MainActivity.this, RegisterActivity.class)
//                .putExtra("type", "register"));
    }

    public void getForget(View view){
    }

    @Override
    public void showLoading() {
        Log.e(TAG,"加载进度条中...");
    }

    @Override
    public void hideLoading() {
        Log.e(TAG,"隐藏进度条");
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showData(String data) {
        Log.e(TAG,"返回的数据:" + data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int state = jsonObject.optInt("code");
            JSONObject json = jsonObject.optJSONObject("data");
            String token = json.optString("token");
            if (state == 0){
                SpUtil.putString(MainActivity.this, "token", token);
                startActivity(new Intent(MainActivity.this,UserActivity.class));
            }else {
                Log.e(TAG,"错误信息:" + state);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
