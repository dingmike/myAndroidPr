package com.ejar.chapp.baselibrary.net;

import com.ejar.chapp.baselibrary.utils.LogUtils;
import com.ejar.chapp.baselibrary.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018\1\2 0002.
 */

public class RetrofitManager {
    private Retrofit retrofit;

    private RetrofitManager() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(NetConstants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(NetConstants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(NetConstants.WXCHAR_URL)
                .build();
//        service = retrofit.create(IdeaApiService.class);
    }

    //  创建单例
    private static class getRetrofitManager {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    public static  <T> T create(Class<T> tClass) {
        return getRetrofitManager.INSTANCE.create(tClass);
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {  //没网强制从缓存读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("Okhttp", "no network");
            }


            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
