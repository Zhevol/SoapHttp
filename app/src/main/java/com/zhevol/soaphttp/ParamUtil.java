package com.zhevol.soaphttp;

import android.util.Log;
import android.util.SparseArray;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建请求参数的列表
 * Created by Administrator on 2018/3/8 0008.
 */

public class ParamUtil {

    public static Map<String, String> buildParams(Map<String, String> mapParam) {
        HashMap<String, String> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("2F865C8D75FC0061CA176AB53CE8AFEE");
        if (mapParam != null && !mapParam.isEmpty()) {
            for (Map.Entry<String, String> entry : mapParam.entrySet()) {
                sb.append(entry.getValue());
                params.put(entry.getKey(), entry.getValue());
            }
        }
        params.put("token", md5(sb.toString()));
        return params;
    }

    /**
     * 获取MD5加密后的字符串
     *
     * @param str 明文
     * @return 加密后的字符串
     */
    private static String md5(String str) {
        //18623627565
        StringBuilder sb = new StringBuilder();
        try {
            /* 创建MD5加密对象 */
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            /* 进行加密 */
            md5.update(str.getBytes("UTF-8"));
            /* 获取加密后的字节数组 */
            byte[] md5Bytes = md5.digest();
            for (byte bt : md5Bytes) {
                int temp = bt & 0xFF;
                if (temp <= 0xF) {// 转化成十六进制不够两位
                    sb.append("0");
                }
                sb.append(Integer.toHexString(temp));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            sb.append("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            sb.append("");
        }
        Log.e("getMD%", "源字符串" + str + " === 加密结果:" + sb.toString());
        return sb.toString();
    }

}
