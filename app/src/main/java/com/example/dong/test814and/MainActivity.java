package com.example.dong.test814and;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPhoneNumberEditText;
    private EditText mPassWordEditText;
    private EditText mIPEditText;
    private Button mLoginButton;
    //用于接收Http请求的servlet的URL地址，请自己定义
    private String ipadress;
    private String originAddress;
    //用于处理消息的Handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("OK".equals(msg.obj.toString())){
                result = "success";
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "fail";
            }else {
                result = msg.obj.toString();
            }
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        mPassWordEditText = (EditText) findViewById(R.id.passwordEditText);
        mIPEditText = (EditText) findViewById(R.id.ipeditText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
    }

    private void initEvent() {
        mLoginButton.setOnClickListener(this);
    }

    public void login() {
        //检查用户输入的账号和密码的合法性
        if (!isInputValid()){
            return;
        }

        ipadress=mIPEditText.getText().toString();
        originAddress = "http://"+ipadress+":8080/Test814/servlet/LoginServlet";

        //构造HashMap
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(User.PHONENUMBER, mPhoneNumberEditText.getText().toString());
        params.put(User.PASSWORD, mPassWordEditText.getText().toString());
        try {
            //构造完整URL
            TextView tt= (TextView) findViewById(R.id.textView2);
            tt.setText(originAddress);

            String compeletedURL = HttpUtil.getURLWithParams(originAddress, params);
            //发送请求
            HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInputValid() {
        //检查用户输入的合法性，这里暂且默认用户输入合法
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                login();
                break;
        }
    }
}