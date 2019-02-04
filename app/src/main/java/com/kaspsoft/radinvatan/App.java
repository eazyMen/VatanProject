package com.kaspsoft.radinvatan;

import android.app.Application;
import android.media.MediaPlayer;
import android.widget.SeekBar;

import com.kaspsoft.radinvatan.data.global.module.NetworkModule;
import com.kaspsoft.radinvatan.ui.MainActivity;
import com.kaspsoft.radinvatan.ui.detail_list_menu.MusicAdapter;

public class App extends Application {

    private static AppComponent component;
    private static SeekBar seekBar;


    private static MusicAdapter ma;

    public static SeekBar getSeekBar() {
        return seekBar;
    }

    public static void setSeekBar(SeekBar seekBar2) {
        seekBar = seekBar2;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(getApplicationContext()))
                .build();
    }

    public static AppComponent getComponent(){
        return component;
    }


    public static MusicAdapter getMa() {
        return ma;
    }

    public static void setMa(MusicAdapter ma) {
        App.ma = ma;
    }

}
