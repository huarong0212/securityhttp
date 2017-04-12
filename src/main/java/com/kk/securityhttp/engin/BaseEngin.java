package com.kk.securityhttp.engin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.listeners.Callback;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.Response;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.kk.securityhttp.net.impls.OKHttpRequest;
import com.kk.securityhttp.net.listeners.OnHttpResonseListener;
import com.kk.securityhttp.net.utils.OKHttpUtil;
import com.kk.securityhttp.utils.PathUtil;
import com.kk.securityhttp.utils.FileUtil;
import com.kk.securityhttp.utils.LogUtil;
import com.kk.securityhttp.utils.VUiKit;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangkai on 16/9/20.
 */
public abstract class BaseEngin<T> {
    public BaseEngin() {
    }

    //< 同步请求get
    public ResultInfo<T> get(Class<T> type, boolean isEncryptResponse) {
        ResultInfo<T> resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().get(getUrl(), isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
            });
        } catch (Exception e) {
            resultInfo = error(e);
        }
        return resultInfo;
    }

    //< 同步请求rxjava get
    public Observable<ResultInfo<T>> rxget(final Class<T> type, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, ResultInfo<T>>() {
            @Override
            public ResultInfo<T> call(Object o) {
                return get(type, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    //< 同步请求post
    public ResultInfo<T> post(Class<T> type, Map<String, String> params, boolean
            isrsa, boolean iszip, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        ResultInfo<T> resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().post(getUrl(), params, isrsa, iszip, isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
            });
            if (isrsa && publicKeyError(resultInfo, response.body)) {
                return post(type, params, isrsa, iszip, isEncryptResponse);
            }
        } catch (Exception e) {
            resultInfo = error(e);
        }
        return resultInfo;
    }

    //< 同步请求rxjava post
    public Observable<ResultInfo<T>> rxpost(final Class<T> type, final Map<String, String>
            params, final boolean isrsa, final boolean iszip, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<Object, ResultInfo<T>>() {
            @Override
            public ResultInfo<T> call(Object o) {
                return post(type, params, isrsa, iszip, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread());
    }

    //< 异步请求
    public void apost(final Class<T> type, Map<String, String> params,
                      final boolean isrsa, final boolean iszip, final boolean isEncryptResponse, final Callback<T> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        final Map<String, String> finalParams = params;
        try {
            OKHttpRequest.getImpl().apost(getUrl(), params, isrsa, iszip, isEncryptResponse, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<T> resultInfo = null;
                    try {

                        resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
                        });

                        if (isrsa && publicKeyError
                                (resultInfo, response.body)) {
                            apost(type, finalParams, isrsa, iszip, isEncryptResponse, callback);
                            return;
                        }

                        success(callback, resultInfo);
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

    //< 异步请求
    public void aget(final Class<T> type, final boolean isEncryptResponse, final
    Callback<T> callback) {
        try {
            OKHttpRequest.getImpl().aget(getUrl(), isEncryptResponse, new OnHttpResonseListener() {
                @Override
                public void onSuccess(Response response) {
                    ResultInfo<T> resultInfo = null;
                    try {

                        resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
                        });
                        success(callback, resultInfo);
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

    public ResultInfo<T> uploadFile(Class<T> type, UpFileInfo upFileInfo, Map<String, String>
            params, boolean isEncryptResponse) {
        if (params == null) {
            params = new HashMap<>();
        }
        ResultInfo<T> resultInfo = null;
        try {
            Response response = OKHttpRequest.getImpl().uploadFile(getUrl(), upFileInfo, params, isEncryptResponse);
            resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
            });
        } catch (Exception e) {
            resultInfo = error(e);
        }
        return resultInfo;
    }

    public Observable<ResultInfo<T>> rxuploadFile(final Class<T> type, final UpFileInfo upFileInfo, final Map<String, String>
            params, final boolean isEncryptResponse) {
        return Observable.just("").map(new Func1<String, ResultInfo<T>>() {
            @Override
            public ResultInfo<T> call(String s) {
                return uploadFile(type, upFileInfo, params, isEncryptResponse);
            }
        }).subscribeOn(Schedulers.newThread());
    }


    public void auploadFile(final Class<T> type, final UpFileInfo upFileInfo, Map<String, String>
            params, final boolean isEncryptResponse, final Callback<ResultInfo<T>> callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        try {
            OKHttpRequest.getImpl().auploadFile(getUrl(), upFileInfo, params, isEncryptResponse, new
                    OnHttpResonseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            ResultInfo<T> resultInfo = null;
                            try {
                                resultInfo = JSON.parseObject(response.body, new TypeReference<ResultInfo<T>>(type) {
                                });
                                success(callback, resultInfo);
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

    private void success(final Callback callback, final ResultInfo<T> lastResultInfo) {
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

    private boolean publicKeyError(ResultInfo resultInfo, String body) {
        if (resultInfo != null && resultInfo.code == HttpConfig.PUBLICKEY_ERROR) {
            ResultInfo<GoagalInfo> resultInfoPE = JSON.parseObject(body, new
                    TypeReference<ResultInfo<GoagalInfo>>
                            (GoagalInfo.class) {
                    });
            if (resultInfoPE.data != null && resultInfoPE.data.getPublicKey() != null) {
                GoagalInfo.get().publicKey = GoagalInfo.get().getPublicKey(resultInfoPE.data.getPublicKey());
                LogUtil.msg("公钥出错->" + GoagalInfo.get().publicKey);
                String name = "rsa_public_key.pem";
                FileUtil.writeInfoInSDCard(GoagalInfo.get().publicKey, PathUtil.getConfigPath(), name);
                return true;
            }
        }
        return false;
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


    private ResultInfo error(Exception e) {
        LogUtil.msg("异常:" + e, LogUtil.W);
        e.printStackTrace();

        ResultInfo resultInfo = new ResultInfo<>();
        resultInfo.code = HttpConfig.SERVICE_ERROR_CODE;
        resultInfo.message = e.getMessage();
        return resultInfo;
    }

    public abstract String getUrl();
}
