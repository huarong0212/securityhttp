package com.kk.securityhttp.net.interfaces;


import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.exception.NullResonseListenerException;
import com.kk.securityhttp.net.listeners.OnHttpResonseListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhangkai on 16/8/18.
 */
public interface IHttpRequest {

    Response get(String url,boolean isEncryptResponse) throws IOException;

    void aget(String url,boolean isEncryptResponse, final OnHttpResonseListener httpResonseListener) throws IOException,
            NullResonseListenerException;

    Response post(String url, Map<String, String> params, boolean isrsa, boolean iszip, boolean
            isEncryptResponse) throws IOException, NullResonseListenerException;

    void apost(String url, Map<String, String> params, boolean isrsa, boolean iszip, boolean
                        isEncryptResponse, final OnHttpResonseListener httpResonseListener)
            throws
            IOException, NullResonseListenerException;

    Response uploadFile(String url, UpFileInfo upFileInfo, Map<String, String> params, boolean
            isEncryptResponse
    ) throws IOException;

    void auploadFile(String url, UpFileInfo upFileInfo, Map<String, String> params,  boolean
            isEncryptResponse,
                     OnHttpResonseListener
                             httpResonseListener) throws IOException, NullResonseListenerException;

    void cancel(String url);

}