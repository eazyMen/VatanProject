package com.kaspsoft.radinvatan.ui.detail_list_menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.kaspsoft.radinvatan.App;
import com.kaspsoft.radinvatan.R;

public class BroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        App.getMa().mediaPause(intent.getAction());
    }
}
