package com.kk.securityhttp.net.utils;

import android.os.Build;


import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.security.Encrypt;
import com.kk.securityhttp.utils.EncryptUtil;
import com.kk.securityhttp.utils.LogUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhangkai on 16/9/9.
 */
public final class OKHttpUtil {
    public static Request.Builder getRequestBuilder(String url) {
        LogUtil.msg("客户端请求url->" + url);
        Request.Builder builder = new Request.Builder()
                .tag(url)
                .url(url);
        return builder;
    }

    public static OkHttpClient.Builder getHttpClientBuilder() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HttpConfig.TIMEOUT, TimeUnit.MILLISECONDS);
        return builder;
    }

    public static Response setResponse(int code, String body) {
        Response response = new Response();
        response.code = code;
        response.body = body;
        return response;
    }

    public static FormBody.Builder setBuilder(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, false);
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.add(key, value);
            }
        }
        return builder;
    }

    public static MultipartBody.Builder setBuilder(UpFileInfo upFileInfo, Map<String, String> params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                builder.addFormDataPart(key, value);
            }
        }
        builder.addFormDataPart(upFileInfo.name, upFileInfo.filename, RequestBody.create(MediaType.parse
                        ("multipart/form-data"),
                upFileInfo.file));
        return builder;
    }

    public static Request getRequest(String url, Map<String, String> params, boolean isrsa, boolean isEncryptResponse) {
        byte[] data = OKHttpUtil.encodeParams(params, isrsa, isEncryptResponse);
        RequestBody requestBody = RequestBody.create(HttpConfig.MEDIA_TYPE, data);
        return OKHttpUtil.getRequestBuilder(url).post(requestBody).build();
    }

    public static Request getRequest(String url, Map<String, String> params, boolean isEncryptResponse) {
        params = OKHttpUtil.encodeParams(params, isEncryptResponse);
        return OKHttpUtil.getRequestBuilder(url).post(OKHttpUtil.setBuilder(params).build()).build();
    }

    public static Request getRequest(String url, Map<String, String> params, UpFileInfo upFileInfo, boolean isEncryptResponse) {
        MultipartBody requestBody = OKHttpUtil.setBuilder(upFileInfo, params, isEncryptResponse).build();
        return OKHttpUtil.getRequestBuilder(url).post(requestBody).build();
    }

    //<  加密正文
    public static byte[] encodeParams(Map params, boolean isrsa, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();
        LogUtil.msg("客户端请求数据->" + jsonStr);
        if (isrsa) {
            jsonStr = EncryptUtil.rsa(jsonStr);
        }
        return EncryptUtil.compress(jsonStr);
    }

    //< 不加密参数 正常请求正文
    public static Map<String, String> encodeParams(Map params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        setDefaultParams(params, isEncryptResponse);
        return params;
    }

    ///< 解密返回值
    public static String decodeBody(InputStream in) {
        return Encrypt.decode(EncryptUtil.unzip(in));
    }


    //设置默认参数
    private static void setDefaultParams(Map<String, String> params, boolean encryptResponse) {
        if (encryptResponse) {
            params.put("encrypt-response", "true");
        }

        if (defaultParams != null) {
            params.putAll(defaultParams);
        }

        if (!isUseDefaultParams) return;

        String agent_id = "1";
        if (GoagalInfo.get().channelInfo != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id + "";
        }
        params.put("agent_id", agent_id);
        params.put("ts", System.currentTimeMillis() + "");
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = android.os.Build.MODEL.contains(android.os.Build.BRAND) ? android.os.Build.MODEL + " " + android
                .os.Build.VERSION.RELEASE : Build.BRAND + " " + android
                .os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionName + "");
        }
    }

    public static void setDefaultParams(Map<String, String> params) {
        OKHttpUtil.defaultParams = params;
    }

    public static Map<String, String> defaultParams;

    public static void setIsUseDefaultParams(boolean isUseDefaultParams) {
        OKHttpUtil.isUseDefaultParams = isUseDefaultParams;
    }

    private static boolean isUseDefaultParams = true;

}
