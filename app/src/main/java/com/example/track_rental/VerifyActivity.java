package com.example.track_rental;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SmsYZR";

    private Button register;
    private EditText ETphone,ETcode;
    private TextView getcode;
    private Context context;
    private TimeCount time;
    String phone,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify);


        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();//左边按钮实现的功能逻辑
            }

            @Override
            public void OnRightButtonClick() {

            }
        });

        time = new TimeCount(60000, 1000);

        context=this;

        //初始化SDK
        MobSDK.init(this);

        register=(Button)findViewById(R.id.landing_btn);
        getcode=(TextView) findViewById(R.id.validateNum_btn);
        ETphone=(EditText)findViewById(R.id.tel_number);
        ETcode=(EditText)findViewById(R.id.validateNum);

        register.setOnClickListener(this);
        getcode.setOnClickListener(this);
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                        Toast.makeText(VerifyActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "get verification code successful.");
                    } else {
                        Log.d(TAG, data.toString());
                        Toast.makeText(VerifyActivity.this, "发送验证码失败", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    // TODO 处理错误的结果

                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        Log.d(TAG, "submit code successful");
                        Toast.makeText(VerifyActivity.this, "验证码正确", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(VerifyActivity.this,FirstActivity.class));
                        startActivity(new Intent(VerifyActivity.this,MainActivity.class));
                    }else{
                        Toast.makeText(VerifyActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, data.toString());
                    }

                } else{
                    // TODO 处理错误的结果
                    Toast.makeText(VerifyActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//                    try {
//                        Throwable throwable = (Throwable) data;
//                        throwable.printStackTrace();
//                        JSONObject object = new JSONObject(throwable.getMessage());
//                        String des = object.optString("detail");//错误描述
//                        int status = object.optInt("status");//错误代码
//                        //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
//                        Log.e(TAG, "status: " + status + ", detail: " + des);
//                        if (status > 0 && !TextUtils.isEmpty(des)) {
//                            Toast.makeText(MainActivity.this, des, Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.validateNum_btn:
                phone=ETphone.getText().toString().trim();
                Toast.makeText(VerifyActivity.this, phone, Toast.LENGTH_SHORT).show();
                if (null == phone || "".equals(phone) || phone.length() != 11) {
                    Toast.makeText(this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                }else{
                    sendCode("86",phone);
                    time.start();
                }
                break;
            case R.id.landing_btn:
                code=ETcode.getText().toString().trim();
                if (null != code && code.length() == 4) {
                    Log.d(TAG, ETcode.getText().toString());
                    submitCode("86",phone,code);
                } else {
                    Toast.makeText(this, "密码长度不正确", Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(VerifyActivity.this,MainActivity.class));
                break;
        }
    }



    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //getcode.setBackgroundColor(Color.parseColor("#a19b9d"));
            getcode.setTextColor(Color.parseColor("#a19b9d"));
            getcode.setClickable(false);
            getcode.setText("("+millisUntilFinished / 1000 +") 秒后重发");
        }

        @Override
        public void onFinish() {
            getcode.setText("重新获取验证码");
            getcode.setClickable(true);
            getcode.setTextColor(Color.parseColor("#4f52ed"));
            //getcode.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }
}
