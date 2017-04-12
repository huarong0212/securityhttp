package com.kk.securityhttp.listeners;


import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.entry.Response;

/**
 * Created by zhangkai on 16/9/20.
 */
public interface Callback<T> {
     void onSuccess(ResultInfo<T> resultInfo);
     void onFailure(Response response);
}
