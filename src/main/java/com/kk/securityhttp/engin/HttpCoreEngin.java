package com.kk.securityhttp.engin;

import android.content.Context;

import com.kk.securityhttp.listeners.Callback;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.entry.UpFileInfo2;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/4/28.
 */

public class HttpCoreEngin<T> extends BaseEngin<T> {

    public static HttpCoreEngin httpCoreEngin = null;

    public HttpCoreEngin(Context context) {
        super(context);
    }

    public static HttpCoreEngin get(Context context) {
        synchronized (HttpCoreEngin.class) {
            if (httpCoreEngin == null) {
                httpCoreEngin = new HttpCoreEngin(context);
            }
        }
        return httpCoreEngin;
    }

    private String url;

    public T get(String url, Type type, boolean isEncryptResponse) {
        return get(url, type, null, isEncryptResponse);
    }


    public T get(String url, Type type, Map<String, String> params, boolean
            isEncryptResponse) {
        return get(url, type, params, null, isEncryptResponse);
    }


    public T get(String url, Type type, Map<String, String> params, Map<String, String> headers, boolean
            isEncryptResponse) {
        this.url = url;
        return super.get(type, params, headers, isEncryptResponse);
    }

    public Observable<T> rxget(String url, Type type, boolean isEncryptResponse) {
        return rxget(url, type, null, null, isEncryptResponse);
    }


    public Observable<T> rxget(String url, Type type, Map<String, String> params, boolean isEncryptResponse) {
        return rxget(url, type, params, null, isEncryptResponse);
    }

    public Observable<T> rxget(String url, Type type, Map<String, String> params, Map<String, String> headers,
                               boolean isEncryptResponse) {
        this.url = url;
        return super.rxget(type, params, headers, isEncryptResponse);
    }


    public T post(String url, Type type, Map<String, String> params, boolean isrsa, boolean
            iszip, boolean isEncryptResponse) {
        return post(url, type, params, null, isrsa, iszip, isEncryptResponse);
    }


    public T post(String url, Type type, Map<String, String> params, Map<String, String> headers,
                  boolean isrsa, boolean
                          iszip, boolean isEncryptResponse) {
        this.url = url;
        return super.post(type, params, headers, isEncryptResponse, isrsa, iszip);
    }

    public Observable<T> rxpost(String url, Type type, Map params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse) {
        return rxpost(url, type, params, null, isrsa, iszip, isEncryptResponse);
    }


    public Observable<T> rxpost(String url, Type type, Map<String, String> params, Map<String, String> headers,
                                boolean isrsa, boolean iszip, boolean
                                        isEncryptResponse) {
        this.url = url;
        return super.rxpost(type, params, headers, isrsa, iszip, isEncryptResponse);
    }

    public void apost(String url, Type type, Map params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse, Callback callback) {
        apost(url, type, params, null, isrsa, iszip, isEncryptResponse, callback);
    }


    public void apost(String url, Type type, Map<String, String> params, Map<String, String> headers, boolean
            isrsa, boolean iszip, boolean
                              isEncryptResponse, Callback callback) {
        this.url = url;
        super.apost(type, params, headers, isrsa, iszip, isEncryptResponse, callback);
    }

    public void aget(String url, Type type, Callback callback, boolean isEncryptResponse) {
        aget(url, type, null, null, isEncryptResponse, callback);
    }


    public void aget(String url, Type type, Map<String, String> params, boolean
            isEncryptResponse, Callback callback) {
        aget(url, type, params, null, isEncryptResponse, callback);
    }


    public void aget(String url, Type type, Map<String, String> params, Map<String, String> headers, boolean
            isEncryptResponse, Callback
                             callback) {
        this.url = url;
        super.aget(type, params, headers, isEncryptResponse, callback);
    }


    public T uploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params, boolean
            isEncryptResponse) {
        return uploadFile(url, type, upFileInfo, params, null, isEncryptResponse);
    }


    public T uploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params, Map<String, String> headers, boolean
            isEncryptResponse) {
        this.url = url;
        return super.uploadFile(type, upFileInfo, params, headers, isEncryptResponse);
    }

    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params,
                                      boolean
                                              isEncryptResponse) {
        return rxuploadFile(url, type, upFileInfo, params, null, isEncryptResponse);
    }


    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params,
                                      Map<String, String> headers, boolean
                                              isEncryptResponse) {
        this.url = url;
        return super.rxuploadFile(type, upFileInfo, params, headers, isEncryptResponse);
    }

    public void auploadFile(String url, Type type, UpFileInfo upFileInfo, Map params, boolean isEncryptResponse,
                            Callback callback) {
        auploadFile(url, type, upFileInfo, params, null, isEncryptResponse, callback);
    }

    public void auploadFile(String url, Type type, UpFileInfo upFileInfo, Map<String, String> params,
                            Map<String, String> headers, boolean isEncryptResponse,
                            Callback callback) {
        this.url = url;
        super.auploadFile(type, upFileInfo, params, headers, isEncryptResponse, callback);
    }


    public T uploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map<String, String> params, boolean
            isEncryptResponse) {
        return uploadFile(url, type, upFileInfo, params, null, isEncryptResponse);
    }


    public T uploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map<String, String> params, Map<String,
            String> headers, boolean
                                isEncryptResponse) {
        this.url = url;
        return super.uploadFile(type, upFileInfo, params, headers, isEncryptResponse);
    }

    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map<String, String> params,
                                      boolean
                                              isEncryptResponse) {
        return rxuploadFile(url, type, upFileInfo, params, null, isEncryptResponse);
    }


    public Observable<T> rxuploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map<String, String> params,
                                      Map<String, String> headers, boolean
                                              isEncryptResponse) {
        this.url = url;
        return super.rxuploadFile(type, upFileInfo, params, headers, isEncryptResponse);
    }

    public void auploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map params, boolean isEncryptResponse,
                            Callback callback) {
        auploadFile(url, type, upFileInfo, params, null, isEncryptResponse, callback);
    }

    public void auploadFile(String url, Type type, UpFileInfo2 upFileInfo, Map<String, String> params,
                            Map<String, String> headers, boolean isEncryptResponse,
                            Callback callback) {
        this.url = url;
        super.auploadFile(type, upFileInfo, params, headers, isEncryptResponse, callback);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
