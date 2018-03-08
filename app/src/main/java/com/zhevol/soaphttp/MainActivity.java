package com.zhevol.soaphttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhevol.soaphttp.library.SoapHttp;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HashMap<String, String> params = new HashMap<>();
        String username = "18623627565";// 电话号码；
        String password = username.substring(username.length() - 6, username.length());
        params.put("username", username);// mobileCode：字符串，手机号码，最少前七位数字。
        params.put("password", password);// 字符串，免费用户为空字符串。
        SoapHttpManager.getInstance()
                .setShowDialog(true)
                .doRequest(this, "PasswordLogin", params, new SoapHttpManager.OnRequestListener<PassWordLogin>() {
                    @Override
                    public Class<PassWordLogin> getTClass() {
                        return PassWordLogin.class;
                    }

                    @Override
                    public void onSuccess(PassWordLogin passWordLogin) {
                        if (!passWordLogin.isSuccess()) {
                            Log.e("MainActivity", passWordLogin.getCurrentExceptionMessage());
                        } else {
                            Log.e("MainActivity", passWordLogin.getData().getAppuser().getUsername());
                        }
                    }
                });
    }


}
