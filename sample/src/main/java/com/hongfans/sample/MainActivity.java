package com.hongfans.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hongfans.sample.api.ApiFactory;
import com.hongfans.sample.api.ApiService;
import com.major.base.log.LogUtil;
import com.major.base.util.ToastUtil;
import com.major.http.api.rx.RxSchedulers;
import com.major.http.api.rx.RxSubscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);

        LogUtil.init(getPackageName(), "tag_andy", true, false);
        ToastUtil.init(getApplicationContext());

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_1:

                method1();
                        
                break;
            case R.id.btn_2:
                ApiFactory.getInstance().getService(ApiService.class).getCategories()
                        .compose(RxSchedulers.<Categories>combine())
                        .subscribe(new RxSubscriber<Categories>(){
                            @Override
                            public void onNext(Categories categories){

                            }

                            @Override
                            public void onError(String err, int code){

                            }
                        });

                break;
            default:

                break;
        }
    }

    private void method1(){
        method2();
    }

    private void method2(){

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
