package com.liuh.learn_arouter.interceptor;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.liuh.learn_arouter.PathConstants;

@Interceptor(priority = 1)
public class UseIInterceptor implements IInterceptor {

    @Override
    public void init(Context context) {
        Log.e("-----", "UseIInterceptor 拦截器 init ...");
    }

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        String name = Thread.currentThread().getName();

        Log.e("-----", "UseIInterceptor 拦截器开始执行，线程名称 ：" + name);

        if (postcard.getPath().equals(PathConstants.PATH_URL_INTERCEPTOR)) {
            Log.e("-----", "UseIInterceptor 进行了拦截处理");
        }
        callback.onContinue(postcard);
    }

}
