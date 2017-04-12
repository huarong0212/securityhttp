package com.kk.securityhttp.net.impls;


import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.exception.NullResonseListenerException;
import com.kk.securityhttp.net.interfaces.IHttpRequest;
import com.kk.securityhttp.net.listeners.OnHttpResonseListener;
import com.kk.securityhttp.net.utils.OKHttpUtil;
import com.kk.securityhttp.utils.LogUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zhangkai on 16/8/18.
 */
public class OKHttpRequest implements IHttpRequest {
    public static OKHttpRequest httpRequest;
    public static OkHttpClient client = null;

    private OKHttpRequest() {
        final Builder builder = OKHttpUtil.getHttpClientBuilder();
        client = builder.build();
    }

    public static OKHttpRequest getImpl() {
        synchronized (OKHttpRequest.class) {
            if (httpRequest == null) {
                httpRequest = new OKHttpRequest();
            }
        }
        return httpRequest;
    }

    @Override
    public com.kk.securityhttp.net.entry.Response get(String url, boolean isEncryptResponse) throws IOException {
        Request request = OKHttpUtil.getRequestBuilder(url).build();
        return sendRequest(request, isEncryptResponse);
    }

    @Override
    public void aget(String url, boolean isEncryptResponse, OnHttpResonseListener httpResonseListener) throws IOException, NullResonseListenerException {
        if (httpResonseListener == null) throw new NullResonseListenerException();

        Request request = OKHttpUtil.getRequestBuilder(url).build();
        sendRequest(request, isEncryptResponse, httpResonseListener);
    }

    @Override
    public com.kk.securityhttp.net.entry.Response post(String url, Map<String, String> params, boolean isrsa, boolean iszip, boolean isEncryptResponse) throws IOException, NullResonseListenerException {
        Request request = null;
        if(isrsa){
            iszip = true;
        }
        if (iszip) {
            request = OKHttpUtil.getRequest(url, params, isrsa, isEncryptResponse);
        } else {
            request = OKHttpUtil.getRequest(url, params, isEncryptResponse);
        }
        return sendRequest(request, isEncryptResponse);
    }

    @Override
    public void apost(String url, Map<String, String> params, boolean isrsa, boolean iszip, boolean isEncryptResponse, OnHttpResonseListener httpResonseListener) throws IOException, NullResonseListenerException {
        Request request = null;
        if(isrsa){
            iszip = true;
        }
        if (iszip) {
            request = OKHttpUtil.getRequest(url, params, isrsa, isEncryptResponse);
        } else {
            request = OKHttpUtil.getRequest(url, params, isEncryptResponse);
        }

        sendRequest(request, isEncryptResponse, httpResonseListener);
    }

    @Override
    public com.kk.securityhttp.net.entry.Response uploadFile(String url, UpFileInfo upFileInfo, Map<String, String>
            params, boolean
                                                                     isEncryptResponse) throws
            IOException {
        if (upFileInfo == null || upFileInfo.file == null) throw new FileNotFoundException("file is null");


        Request request = OKHttpUtil.getRequest(url, params, upFileInfo, isEncryptResponse);

        return sendRequest(request, isEncryptResponse);
    }

    @Override
    public void auploadFile(String url, UpFileInfo upFileInfo, Map<String, String> params, boolean
            isEncryptResponse,
                            OnHttpResonseListener
                                    httpResonseListener)
            throws IOException, NullResonseListenerException {
        if (httpResonseListener == null) throw new NullResonseListenerException();

        if (upFileInfo == null || upFileInfo.file == null) throw new FileNotFoundException("file == null");

        Request request = OKHttpUtil.getRequest(url, params, upFileInfo, isEncryptResponse);

        sendRequest(request, isEncryptResponse, httpResonseListener);
    }

    @Override
    public void cancel(String url) {
        if (client == null) throw new NullPointerException("client == null");

        for (Call call : client.dispatcher().queuedCalls()) {
            if (url.equals(call.request().tag())) call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (url.equals(call.request().tag())) call.cancel();
        }
    }

    private com.kk.securityhttp.net.entry.Response sendRequest(Request request, boolean isEncryptResponse) throws IOException {
        com.kk.securityhttp.net.entry.Response nresponse = null;
        if (request == null) {
            LogUtil.msg("error request is null", LogUtil.E);
            return null;
        }
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            String body = "";
            if (isEncryptResponse) {
                body = OKHttpUtil.decodeBody(response.body().byteStream());
            } else {
                body = response.body().string();
            }
            nresponse = OKHttpUtil.setResponse(response.code(), body);
            LogUtil.msg("服务器返回数据->" + body);
        }
        return nresponse;
    }

    private void sendRequest(Request request, final boolean
            isEncryptResponse, final OnHttpResonseListener httpResonseListener) {
        if (request == null) {
            LogUtil.msg("aerror request is null", LogUtil.E);
            return;
        }
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                com.kk.securityhttp.net.entry.Response response = OKHttpUtil.setResponse(-1, HttpConfig.NET_ERROR);
                httpResonseListener.onFailure(response);
                LogUtil.msg("网络请求失败->" + e.getMessage(), LogUtil.W);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                com.kk.securityhttp.net.entry.Response nresponse = null;
                String body = "";
                if (isEncryptResponse) {
                    body = OKHttpUtil.decodeBody(response.body().byteStream());
                } else {
                    body = response.body().string();
                }
                LogUtil.msg("服务器返回数据->" + body);
                nresponse = OKHttpUtil.setResponse(response.code(), body);
                httpResonseListener.onSuccess(nresponse);
            }
        });
    }
}
