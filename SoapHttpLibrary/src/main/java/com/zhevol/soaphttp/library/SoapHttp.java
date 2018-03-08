package com.zhevol.soaphttp.library;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

/**
 * SoapHttp
 * Created by Administrator on 2018/3/6 0006.
 */

public class SoapHttp {

    /**
     * SoapHttpHolder
     */
    private static class SoapHttpHolder {
        // 单例
        final static SoapHttp instance = new SoapHttp();
    }

    /**
     * 获取 SoapHttp 单例
     *
     * @return SoapHttp 单例
     */
    public static SoapHttp getInstance() {
        return SoapHttpHolder.instance;
    }

    /**
     * 构造器
     */
    private SoapHttp() {
        mSoapHandler = new SoapHandler();
    }

    /**
     * 去除初始化 Handler 时的警告，使用这样的方式
     */
    private static final class SoapHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String result = bundle.getString("result", "");
            Exception exception = (Exception) bundle.getSerializable("exception");
            OnSoapHttpListener onSoapHttpListener = getInstance().getOnSoapHttpListener();
            if (onSoapHttpListener != null) {
                if (TextUtils.isEmpty(result)) {
                    onSoapHttpListener.onFailed(exception == null ? new NullPointerException("返回数据为空") : exception);
                } else {
                    onSoapHttpListener.onSuccess(result);
                }
            }
        }
    }

    /**
     * Soap 命名空间
     */
    private String nameSpace;

    /**
     * 请求的 URL
     */
    private String requestUrl;

    /**
     * 请求的方法名
     */
    private String methodName;

    /**
     * 请求的参数集合
     */
    private Map<String, String> param;

    /**
     * 网络请求的回调
     */
    private OnSoapHttpListener mOnSoapHttpListener;

    /**
     * Handler 的子类对象，用于进行异步请求时的消息的传递和发送
     */
    private SoapHandler mSoapHandler;

    /**
     * 设置命名空间
     *
     * @param nameSpace 命名空间
     * @return 单例
     */
    public SoapHttp setNameSpace(String nameSpace) {
        SoapHttpHolder.instance.nameSpace = nameSpace;
        return SoapHttpHolder.instance;
    }

    /**
     * 设置请求的 URL
     *
     * @param requestUrl 请求的 URL
     * @return 单例
     */
    public SoapHttp setRequestUrl(String requestUrl) {
        SoapHttpHolder.instance.requestUrl = requestUrl;
        return SoapHttpHolder.instance;
    }

    /**
     * 设置请求的方法名称
     *
     * @param methodName 请求的方法名称
     * @return 单例
     */
    public SoapHttp setMethodName(String methodName) {
        SoapHttpHolder.instance.methodName = methodName;
        return SoapHttpHolder.instance;
    }

    /**
     * 设置请求的参数
     *
     * @param param 请求的参数
     * @return 单例
     */
    public SoapHttp setParam(Map<String, String> param) {
        SoapHttpHolder.instance.param = param;
        return SoapHttpHolder.instance;
    }

    /**
     * 设置网络请求的回调监听
     *
     * @param onSoapHttpListener 回调监听
     * @return 单例
     */
    public SoapHttp setOnSoapHttpListener(OnSoapHttpListener onSoapHttpListener) {
        SoapHttpHolder.instance.mOnSoapHttpListener = onSoapHttpListener;
        return SoapHttpHolder.instance;
    }

    /**
     * 获取监听器对象
     *
     * @return 监听器
     */
    OnSoapHttpListener getOnSoapHttpListener() {
        return mOnSoapHttpListener;
    }

    /**
     * 发送网络请求
     */
    public void doSoapHttpRequest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 命名空间 + 方法名称
                final String soapAction = nameSpace + methodName;
                // 创建请求
                SoapObject request = new SoapObject(nameSpace, methodName);
                // 生成调用web service 方法的soap请求消息
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                // 设置.net web service
                envelope.dotNet = true;
                // 请求參数
                if (param != null && !param.isEmpty()) {
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        request.addProperty(entry.getKey(), entry.getValue());
                    }
                }
                // 发送请求
                envelope.setOutputSoapObject(request);
                HttpTransportSE transport = new HttpTransportSE(requestUrl);
                SoapObject result = null;
                Exception exception = null;
                Bundle bundle = new Bundle();
                try {
                    // web service请求
                    transport.call(soapAction, envelope);
                    result = (SoapObject) envelope.bodyIn;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    exception = ex;
                } finally {
                    if (result != null) {
                        Log.e("HttpThread", "PropertyCount" + result.getPropertyCount());
                        Log.e("HttpThread", "Property(0)" + result.getProperty(0));
                        String resultS = null;
                        if (result.getPropertyCount() > 0) {
                            for (int i = 0; i < result.getPropertyCount(); i++) {
                                resultS = result.getProperty(i).toString();
                            }
                            bundle.putString("result", resultS);
                        } else {
                            resultS = "";
                            bundle.putString("result", resultS);
                        }
                    } else {
                        bundle.putSerializable("exception", exception == null ? new NullPointerException("返回数据为空") : exception);
                    }
                    Message msg = mSoapHandler.obtainMessage();
                    msg.setData(bundle);
                    mSoapHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 网络请求回调
     */
    public interface OnSoapHttpListener {
        /**
         * 请求成功
         *
         * @param result 成功后返回的内容
         */
        void onSuccess(String result);

        /**
         * 请求失败
         *
         * @param e 失败后返回异常
         */
        void onFailed(Exception e);
    }
}
