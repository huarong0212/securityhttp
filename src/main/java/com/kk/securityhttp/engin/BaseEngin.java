package com.kk.securityhttp.engin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.listeners.Callback;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.entry.UpFileInfo2;
import com.kk.securityhttp.net.impls.OKHttpRequest;
import com.kk.securityhttp.net.listeners.OnHttpResonseListener;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.kk.utils.PathUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.VUiKit;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 2017/4/28.
 */

public abstract class BaseEngin<T> {

    private Context mContext;

    public BaseEngin(Context context) {
        this.mContext = context;
    }

    //< 同步请求get 3
    public T get(Type type, boolean isEncryptResponse) {
        return get(type, null, null, isEncryptResponse);
    }

    //< 同步请求get 2
    public T get(Type type, Map<String, String> params, boolean isEncryptResponse) {
        return get(type, params, null, isEncryptResponse);
    }

    //< 同步请求get 1
    public T get(Type type, Map<String, String> params, Map<String, String> headers, boolean isEncryptResponse) {
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().get(getUrl(), params, headers, isEncryptResponse);
            resultInfo = getResultInfo(response.body, type);
        } catch (Exception e) {
            LogUtil.msg("异常->" + e, LogUtil.W);
        }
        return resultInfo;
    }


    //< 同步请求rxjava get 3
    public Observable<T> rxget(final Type type, final Map<String, String> params, final Map<String, String> headers, final boolean
            isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                return get(type, params, headers, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtil.msg(throwable.getMessage());
                ToastUtil.toast2(mContext, throwable.getMessage());
                return null;
            }
        });
    }

    //< 同步请求rxjava get 2
    public Observable<T> rxget(final Type type, final Map<String, String> params, final boolean
            isEncryptResponse) {
        return rxget(type, params, null, isEncryptResponse);
    }

    //< 同步请求rxjava get 1
    public Observable<T> rxget(final Type type, final boolean isEncryptResponse) {
        return rxget(type, null, isEncryptResponse);
    }

    //< 同步请求post 2
    public T post(Type type, Map<String, String> params, boolean
            isrsa, boolean iszip, boolean isEncryptResponse) {
        return post(type, params, null, isrsa, iszip, isEncryptResponse);
    }

    //< 同步请求post 1
    public T post(Type type, Map<String, String> params, Map<String, String> headers, boolean
            isrsa, boolean iszip, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().post(getUrl(), params, headers, isrsa, iszip, isEncryptResponse);
            resultInfo = getResultInfo(response.body, type);
            if (isrsa && publicKeyError(resultInfo, response.body)) {
                return post(type, params, headers, isrsa, iszip, isEncryptResponse);
            }
        } catch (Exception e) {
            LogUtil.msg("异常->" + e, LogUtil.W);
        }
        return resultInfo;
    }


    //< 同步请求rxjava post 2
    public Observable<T> rxpost(final Type type, final Map<String, String>
            params, final boolean isrsa, final boolean iszip, final boolean isEncryptResponse) {
        return rxpost(type, params, null, isrsa, iszip, isEncryptResponse);
    }


    //< 同步请求rxjava post 1
    public Observable<T> rxpost(final Type type, final Map<String, String>
            params, final Map<String, String>
                                        headers, final boolean isrsa, final boolean iszip, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                return post(type, params, headers, isrsa, iszip, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Func1<Throwable, T>() {
            @Override
            public T call(Throwable throwable) {
                LogUtil.msg(throwable.getMessage());
                ToastUtil.toast2(mContext, throwable.getMessage());
                return null;
            }
        });
    }

    //< 异步请求apost 2
    public void apost(final Type type, Map<String, String> params,
                      final boolean isrsa, final boolean iszip, final boolean isEncryptResponse, final Callback<T> callback) {
        apost(type, params, null, isrsa, iszip, isEncryptResponse, callback);
    }

    //< 异步请求apost 1
    public void apost(final Type type, Map<String, String> params, final Map<String, String> headers,
                      final boolean isrsa, final boolean iszip, final boolean isEncryptResponse, final Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost(getUrl(), params, headers, isrsa, iszip, isEncryptResponse, new
                    OnHttpResonseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            T resultInfo = null;
                            try {
                                resultInfo = getResultInfo(response.body, type);
                                if (isrsa && publicKeyError(resultInfo, response.body)) {
                                    apost(type, finalParams, headers, isrsa, iszip, isEncryptResponse, callback);
                                    return;
                                }
                                success(callback, resultInfo);
                            } catch (JSONException je) {
                                response.body = HttpConfig.JSON_ERROR;
                                aerror(je, callback, response);
                            } catch (Exception e) {
                                response.body = HttpConfig.SERVICE_ERROR;
                                aerror(e, callback, response);
                            }
                        }

                        @Override
                        public void onFailure(Response response) {
                            failure(callback, response);
                        }
                    });
        } catch (Exception e) {
            aerror(e, callback);
        }
    }

    //< 异步请求get 3
    public void aget(final Type type, final boolean isEncryptResponse, final
    Callback<T> callback) {
        aget(type, null, null, isEncryptResponse, callback);
    }


    //< 异步请求get 2
    public void aget(final Type type, Map<String, String> params, final boolean isEncryptResponse, final
    Callback<T> callback) {
        aget(type, params, null, isEncryptResponse, callback);
    }

    //< 异步请求get 1
    public void aget(final Type type, Map<String, String> params, Map<String, String> headers, final boolean isEncryptResponse, final
    Callback<T> callback) {
        try {
            OKHttpRequest.getImpl().aget(getUrl(), params, headers, isEncryptResponse, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    T resultInfo = null;
                    try {
                        resultInfo = getResultInfo(response.body, type);
                        success(callback, resultInfo);
                    } catch (JSONException je) {
                        response.body = HttpConfig.JSON_ERROR;
                        aerror(je, callback, response);
                    } catch (Exception e) {
                        response.body = HttpConfig.SERVICE_ERROR;
                        aerror(e, callback, response);
                    }
                }

                @Override
                public void onFailure(Response response) {
                    failure(callback, response);
                }
            });
        } catch (Exception e) {
            aerror(e, callback);
        }
    }

    //< 同步请求uploadFile 2
    public T uploadFile(Type type, UpFileInfo upFileInfo, Map<String, String>
            params, boolean isEncryptResponse) {
        return uploadFile(type, upFileInfo, params, null, isEncryptResponse);
    }

    //< 同步请求uploadFile 1
    public T uploadFile(Type type, UpFileInfo
            upFileInfo, Map<String, String>
                                params, Map<String, String> headers, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().uploadFile(getUrl(), upFileInfo, params,
                    headers, isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, type);
        } catch (Exception e) {
            LogUtil.msg("异常->" + e, LogUtil.W);
        }
        return resultInfo;
    }

    //< 异步请求rxuploadFile 2
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo upFileInfo, final Map<String, String>
            params, final boolean isEncryptResponse) {
        return rxuploadFile(type, upFileInfo, params, null, isEncryptResponse);
    }

    //< 异步请求rxuploadFile 1
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo upFileInfo, final Map<String, String>
            params, final Map<String, String>
                                              headers, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return uploadFile(type, upFileInfo, params, headers, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    //< 异步请求auploadFile 2
    public void auploadFile(final Type type, final UpFileInfo upFileInfo, Map<String, String>
            params, final boolean isEncryptResponse, final Callback<T> callback) {
        auploadFile(type, upFileInfo, params, null, isEncryptResponse, callback);
    }

    //< 异步请求auploadFile 1
    public void auploadFile(final Type type, final UpFileInfo upFileInfo, Map<String, String>
            params, Map<String, String>
                                    headers, final boolean isEncryptResponse, final Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        try {
            OKHttpRequest.getImpl().auploadFile(getUrl(), upFileInfo, params, headers, isEncryptResponse, new
                    OnHttpResonseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            T resultInfo = null;
                            try {
                                resultInfo = getResultInfo(response.body, type);
                                success(callback, resultInfo);
                            } catch (JSONException je) {
                                response.body = HttpConfig.JSON_ERROR;
                                aerror(je, callback, response);
                            } catch (Exception e) {
                                response.body = HttpConfig.SERVICE_ERROR;
                                aerror(e, callback, response);
                            }
                        }

                        @Override
                        public void onFailure(Response response) {
                            failure(callback, response);
                        }
                    });
        } catch (Exception e) {
            aerror(e, callback);
        }
    }


    //< 同步请求uploadFile 2
    public T uploadFile(Type type, UpFileInfo2 upFileInfo, Map<String, String>
            params, boolean isEncryptResponse) {
        return uploadFile(type, upFileInfo, params, null, isEncryptResponse);
    }

    //< 同步请求uploadFile 1
    public T uploadFile(Type type, UpFileInfo2
            upFileInfo, Map<String, String>
                                params, Map<String, String> headers, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        T resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().uploadFile(getUrl(), upFileInfo, params,
                    headers, isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, type);
        } catch (Exception e) {
            LogUtil.msg("异常->" + e, LogUtil.W);
        }
        return resultInfo;
    }

    //< 异步请求rxuploadFile 2
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo2 upFileInfo, final Map<String, String>
            params, final boolean isEncryptResponse) {
        return rxuploadFile(type, upFileInfo, params, null, isEncryptResponse);
    }

    //< 异步请求rxuploadFile 1
    public Observable<T> rxuploadFile(final Type type, final UpFileInfo2 upFileInfo, final Map<String, String>
            params, final Map<String, String>
                                              headers, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<String, T>() {
            @Override
            public T call(String s) {
                return uploadFile(type, upFileInfo, params, headers, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    //< 异步请求auploadFile 2
    public void auploadFile(final Type type, final UpFileInfo2 upFileInfo, Map<String, String>
            params, final boolean isEncryptResponse, final Callback<T> callback) {
        auploadFile(type, upFileInfo, params, null, isEncryptResponse, callback);
    }

    //< 异步请求auploadFile 1
    public void auploadFile(final Type type, final UpFileInfo2 upFileInfo, Map<String, String>
            params, Map<String, String>
                                    headers, final boolean isEncryptResponse, final Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        try {
            OKHttpRequest.getImpl().auploadFile(getUrl(), upFileInfo, params, headers, isEncryptResponse, new
                    OnHttpResonseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            T resultInfo = null;
                            try {
                                resultInfo = getResultInfo(response.body, type);
                                success(callback, resultInfo);
                            } catch (JSONException je) {
                                response.body = HttpConfig.JSON_ERROR;
                                aerror(je, callback, response);
                            } catch (Exception e) {
                                response.body = HttpConfig.SERVICE_ERROR;
                                aerror(e, callback, response);
                            }
                        }

                        @Override
                        public void onFailure(Response response) {
                            failure(callback, response);
                        }
                    });
        } catch (Exception e) {
            aerror(e, callback);
        }
    }

    private void success(final Callback callback, final T lastResultInfo) {
        VUiKit.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(lastResultInfo);
            }
        });
    }

    private void failure(final Callback callback, final Response response) {
        VUiKit.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(response);
            }
        });
    }

    private void aerror(Exception e, Callback callback, Response response) {
        LogUtil.msg("异常->" + e, LogUtil.W);
        e.printStackTrace();
        if (response == null) {
            response = new Response();
            response.body = e.getMessage();
            response.code = HttpConfig.SERVICE_ERROR_CODE;
        }
        if (callback != null) {
            failure(callback, response);
        }
    }

    private void aerror(Exception e, Callback callback) {
        aerror(e, callback, null);
    }


    private T getResultInfo(String body, Type type) {
        T resultInfo;
        if (type != null) {
            resultInfo = JSON.parseObject(body, type);
        } else {
            resultInfo = JSON.parseObject(body, new TypeReference<T>() {
            }); //范型已被擦除 --！
        }
        return resultInfo;
    }

    public abstract String getUrl();


    private boolean publicKeyError(T resultInfo, String body) {
        if (resultInfo instanceof ResultInfo) {
            ResultInfo tmpResultInfo = (ResultInfo) resultInfo;
            if (resultInfo != null && tmpResultInfo.code == HttpConfig.PUBLICKEY_ERROR) {
                ResultInfo<GoagalInfo> resultInfoPE = JSON.parseObject(body, new
                        TypeReference<ResultInfo<GoagalInfo>>
                                (GoagalInfo.class) {
                        });
                if (resultInfoPE.data != null && resultInfoPE.data.getPublicKey() != null) {
                    GoagalInfo.get().publicKey = GoagalInfo.get().getPublicKey(resultInfoPE.data.getPublicKey());
                    LogUtil.msg("公钥出错->" + GoagalInfo.get().publicKey);
                    String name = "rsa_public_key.pem";
                    FileUtil.writeInfoToFile(GoagalInfo.get().publicKey, PathUtil.getConfigPath(mContext), name);
                    return true;
                }
            }
        }
        return false;
    }
}
