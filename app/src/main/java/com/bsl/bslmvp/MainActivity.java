package com.bsl.bslmvp;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bsl.mvp.BasePresenter;
import com.bsl.mvp.BaseView;
import com.bsl.okhttp.MyConnect;
import com.bsl.service.MyService;
import com.bsl.tools.SpUtil;
import com.bsl.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class

MainActivity extends AppCompatActivity implements BaseView,View.OnClickListener {
    private EditText zh,mm;
    private BasePresenter presenter;
    private Button evebbustosecond,rigisterbus,sendmessage,unrigister,handlermessage;
    private TextView mText;
    public static String TAG = "MVP--------->";
    private int index = 0;
    private  int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        zh = findViewById(R.id.zh);
//        mm = findViewById(R.id.mm);
        mText=findViewById(R.id.messgae);
        rigisterbus=findViewById(R.id.rigisterbus);
        unrigister=findViewById(R.id.unrigisterbus);
        sendmessage=findViewById(R.id.sendmessage);
        handlermessage=findViewById(R.id.handevent);

        rigisterbus.setOnClickListener(this);
        sendmessage.setOnClickListener(this);
        unrigister.setOnClickListener(this);
        handlermessage.setOnClickListener(this);

     //   evebbustosecond=findViewById(R.id.evebbustosecond);
        presenter = new BasePresenter();
      //  EventBus.getDefault().register(this);
        //绑定当前view
        presenter.attachView(this);

      //  jumpActivity();



    }


    private void jumpActivity() {
        evebbustosecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, secondactivity.class));


            }
        });
    }

    private Handler mhandle=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Log.e("------","收到messge并处理");
                    Toast.makeText(MainActivity.this,"偶数"+msg.arg1,Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Log.e("------","收到messge并处理");
                    Toast.makeText(MainActivity.this,"奇数"+msg.arg1,Toast.LENGTH_LONG).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };






//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public  void xxxx(MessageEvent messageEvent){
//        Log.e("收到evenbus",messageEvent.getMessage());
//        mText.setText(messageEvent.getMessage());
//
//    }

    @Subscribe( threadMode = ThreadMode.MAIN,sticky = true)
    public  void  xxxx(MessageEvent messageEvent){
        Log.e("收到黏性evenbus",messageEvent.getMessage());
        mText.setText(messageEvent.getMessage());
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

    public void startservice(View view){

        }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    public  void  startplay(View view){
        Intent intent=new Intent(this, MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",MyService.Control.PLAY);
        intent.putExtras(bundle);
        startService(intent);


    }
    public  void  pause(View view){
        Intent intent=new Intent(this, MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",MyService.Control.PAUSE);
        intent.putExtras(bundle);
        startService(intent);

    }

    public  void close(View view){
        Intent intent=new Intent(this, MyService.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("key",MyService.Control.STOP);
        intent.putExtras(bundle);
        startService(intent);

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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sendmessage:
                EventBus.getDefault().postSticky(new MessageEvent(""+index++));
                break;

            case R.id.rigisterbus:
                if (!EventBus.getDefault().isRegistered(this)){
                    EventBus.getDefault().register(this);
                }

                break;

            case R.id.unrigisterbus:
                EventBus.getDefault().unregister(this);
                break;

            case R.id.handevent:
                count=60;


                Log.e("------","点击了");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (count>0){
                            count--;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //直接获取message对象

                            Message message= Message.obtain();
                            message.arg1=count;
                            message.what=count%2;

                            mhandle.sendMessage(message);
                        }


                    }
                }).start();
              //  mhandle.sendEmptyMessageDelayed(1,10000);



                break;


        }



    }
}
