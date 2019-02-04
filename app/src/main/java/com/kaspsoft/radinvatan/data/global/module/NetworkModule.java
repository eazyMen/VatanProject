package com.kaspsoft.radinvatan.data.global.module;

import android.content.Context;

import com.kaspsoft.radinvatan.data.global.factories.NetworkFactories;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {
    private final Context context;

    public NetworkModule (Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

    @Provides
    NetworkFactories provideNetworkFactories(){
        return new NetworkFactories(context);
    }
}
