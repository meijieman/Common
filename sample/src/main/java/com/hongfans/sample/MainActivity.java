package com.hongfans.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hongfans.common.log.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:

                method1();

                break;
            default:

                break;
        }
    }

    private void method1() {
        method2();
    }

    private void method2() {

        LogUtil.init(getPackageName(), "tag_andy", true, true);

        LogUtil.v("hello");
        LogUtil.d("hello");
        LogUtil.i("hello");
        LogUtil.w("hello");
        LogUtil.e("hello");

        LogUtil.print("print", "print this log");

//        LogUtil.saveLog("保存的日志第一行");
//        LogUtil.saveLog("保存的日志第二行");
    }
}
