package com.bsl.bslmvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.bsl.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class secondactivity extends Activity {
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        button=findViewById(R.id.tofirst);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("eventbus is most"));
//                finish();
            }
        });

    }
}
