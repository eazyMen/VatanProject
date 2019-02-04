package com.kaspsoft.radinvatan.data.global.factories;

import android.content.Context;
import android.print.PrinterId;

import com.kaspsoft.radinvatan.BuildConfig;
import com.kaspsoft.radinvatan.data.global.network.ListApi;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkFactories {

    Context context;
    public NetworkFactories(Context ctx) {
        context = ctx;
    }

    int cacheSize = 25 * 1024 * 1024; // 10 MB


    public ListApi getApi(){
        return createRetrofit().create(ListApi.class);
    }

    private Interceptor interceptor(){
        return new HttpLoggingInterceptor()
                .setLevel(
                        BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.BASIC
                );
    }

    private OkHttpClient okHttpClient(){
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor())
                .cache(cache)
                .build();
    }

    private Retrofit createRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("http://radiovatan.ru/")
                .client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
