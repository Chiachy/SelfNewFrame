package com.common.base.net;

/**
 * Created by HSK° on 2018/9/10.
 * --function:网咯响应回调
 */
public interface HttpResponseCallBack<T> {
    void onResponse(T data, int what);
}
