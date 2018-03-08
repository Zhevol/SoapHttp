package com.zhevol.soaphttp;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhevol.soaphttp.library.SoapHttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Soap 请求的管理者
 * Created by Administrator on 2018/3/8 0008.
 */

public class SoapHttpManager {

    /**
     * SoapHttpManager
     */
    private static class SoapHttpManagerHolder {
        // 单例
        final static SoapHttpManager instance = new SoapHttpManager();
    }

    /**
     * 获取 SoapHttpManager 单例
     *
     * @return SoapHttpManager 单例
     */
    public static SoapHttpManager getInstance() {
        return SoapHttpManagerHolder.instance;
    }

    /**
     * 初始化一个 Dialog
     */
    private ProgressDialog mProgressDialog;

    /**
     * 展示 Dialog
     */
    private boolean showDialog;

    /**
     * 获取 showDialog 的值
     *
     * @return true-展示 Dialog,false-不展示 Dialog
     */
    private boolean isShowDialog() {
        return showDialog;
    }

    /**
     * 设置是否展示 Dialog
     *
     * @param showDialog true-展示 Dialog,false-不展示 Dialog
     */
    public SoapHttpManager setShowDialog(boolean showDialog) {
        SoapHttpManagerHolder.instance.showDialog = showDialog;
        return SoapHttpManagerHolder.instance;
    }

    /**
     * 发送网络请求
     *
     * @param context    上下文
     * @param methodName 方法名
     * @param params     请求参数集合
     */
    public <T> void doRequest(final Context context, String methodName, Map<String, String> params, final OnRequestListener<T> listener) {
        if (mProgressDialog != null && isShowDialog()) {
            mProgressDialog = ProgressDialog.show(context, "", "请稍候…", true);
        }
        SoapHttp.getInstance()
                .setRequestUrl("http://118.126.108.178/FTCloudService.php")
                .setNameSpace("urn:FtCloudService")
                .setMethodName(methodName)
                .setParam(ParamUtil.buildParams(params))
                .setOnSoapHttpListener(new SoapHttp.OnSoapHttpListener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e("MainActivity", "请求的结果" + result);
                        closeDialog();
                        listener.onSuccess(parseObject(result, listener.getTClass()));
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("MainActivity", "错误信息" + e.toString());
                        closeDialog();
                        Toast.makeText(context, "请求异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                })
                .doSoapHttpRequest();
    }


    /**
     * 关闭 Dialog
     */
    private void closeDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 解析只包含一个对象的Json字符串
     *
     * @param jsonRes 源Json字符串
     * @param tClass  解析出来的对象的class
     * @param <T>     解析出来的对象的泛型标记
     * @return 解析出来的对象
     */
    private <T> T parseObject(String jsonRes, Class<T> tClass) {
        try {
            return new Gson().fromJson(jsonRes, tClass);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    /**
     * 发送请求的接口
     *
     * @param <T> 泛型
     */
    public interface OnRequestListener<T> {
        /**
         * 获取泛型的 Class 类型
         *
         * @return Class
         */
        Class<T> getTClass();

        /**
         * 请求成功的返回
         *
         * @param t 泛型对象
         */
        void onSuccess(T t);
    }

}
