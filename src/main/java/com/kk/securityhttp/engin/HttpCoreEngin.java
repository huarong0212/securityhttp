package com.kk.securityhttp.engin;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.listeners.Callback;
import com.kk.securityhttp.net.entry.UpFileInfo;

import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/3/29.
 */

public class HttpCoreEngin<T> extends BaseEngin {
    public static HttpCoreEngin httpCoreEngin = null;

    public static HttpCoreEngin get() {
        synchronized (HttpCoreEngin.class) {
            if (httpCoreEngin == null) {
                httpCoreEngin = new HttpCoreEngin();
            }
        }
        return httpCoreEngin;
    }

    private String url;

    public ResultInfo get(String url, Class type, boolean isEncryptResponse) {
        this.url = url;
        return super.get(type, isEncryptResponse);
    }

    public Observable<ResultInfo> rxget(String url, Class type, boolean isEncryptResponse) {
        this.url = url;
        return super.rxget(type, isEncryptResponse);
    }

    public ResultInfo post(String url, Class type, Map params, boolean isEncryptResponse, boolean isrsa, boolean
            iszip) {
        this.url = url;
        return super.post(type, params, isEncryptResponse, isrsa, iszip);
    }

    public Observable<ResultInfo> rxpost(String url, Class type, Map params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse) {
        this.url = url;
        return super.rxpost(type, params, isrsa, iszip, isEncryptResponse);
    }

    public void apost(String url, Class type, Map params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse, Callback callback) {
        this.url = url;
        super.apost(type, params, isrsa, iszip, isEncryptResponse, callback);
    }

    public void aget(String url, Class type, Callback callback, boolean isEncryptResponse) {
        this.url = url;
        super.aget(type, isEncryptResponse, callback);
    }

    public ResultInfo uploadFile(String url, Class type, UpFileInfo upFileInfo, Map params, boolean isEncryptResponse) {
        this.url = url;
        return super.uploadFile(type, upFileInfo, params, isEncryptResponse);
    }

    public Observable<ResultInfo> rxuploadFile(String url, Class type, UpFileInfo upFileInfo, Map params, boolean
            isEncryptResponse) {
        this.url = url;
        return super.rxuploadFile(type, upFileInfo, params, isEncryptResponse);
    }

    public void auploadFile(String url, Class type, UpFileInfo upFileInfo, Map params, boolean isEncryptResponse,
                            Callback callback) {
        this.url = url;
        super.auploadFile(type, upFileInfo, params, isEncryptResponse, callback);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
