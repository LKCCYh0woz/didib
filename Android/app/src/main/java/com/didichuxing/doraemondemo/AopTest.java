package com.didichuxing.doraemondemo;

import com.didichuxing.doraemonkit.aop.OkHttpHook;

import okhttp3.OkHttpClient;

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2020/4/22-11:38
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class AopTest {


    public void test(OkHttpClient okHttpClient) {
        OkHttpHook.performOkhttpOneParamBuilderInit(this, okHttpClient);
    }
}
