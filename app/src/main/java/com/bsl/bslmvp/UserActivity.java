package com.bsl.bslmvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bsl.mvp.BasePresenter;
import com.bsl.mvp.BaseView;
import com.bsl.okhttp.MyConnect;
import com.bsl.tools.SpUtil;

import java.util.HashMap;

public class UserActivity extends AppCompatActivity implements BaseView {
    private BasePresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        presenter = new BasePresenter();
        presenter.attachView(this);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("Token", SpUtil.getString(UserActivity.this, "token") + "");
        presenter.getData(MyConnect.URL + "/getuser",map);
    }

    @Override
    public void showLoading() {
        Log.e(MainActivity.TAG,"加载进度条中...");
    }

    @Override
    public void hideLoading() {
        Log.e(MainActivity.TAG,"隐藏进度条");
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void showData(String data) {
        Log.e(MainActivity.TAG,"返回的数据:" + data);
    }
}
