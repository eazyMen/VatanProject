package com.kaspsoft.radinvatan.ui.detail_list_menu;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.models.AudioFiles;
import com.kaspsoft.radinvatan.ui.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    List<AudioFiles> list;
    int pos = -1;
    MainActivity main;
    Utilities until;

    int pausSeek=0;

    ImageView btn;

    Thread thread;
    SeekBar seekBar;

    Timer timer = new Timer();

    AudioManager am;
    MediaPlayer mediaPlayer;
    String content;

    ViewHolder vh;

    NotificationManager mNotificationManager;
    Notification mBuilder;

    int time = 100;

    boolean thredStart = true;

    public MusicAdapter(List<AudioFiles> list, DetailFragmentList dfl, String s) {
        this.list = list;
        until = new Utilities();
        content = s;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_detail_text, parent, false);
        } else {
            v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_music, parent, false);
        }

        am = (AudioManager) v.getContext().getSystemService(v.getContext().AUDIO_SERVICE);
        main = ((MainActivity) v.getContext());


        return new ViewHolder(v);
    }


    LinearLayout ll;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder v, final int posit) {

        if (v.getItemViewType() == 0) {
            v.txtCurrentDetail.setText(content);
        } else {
           // v.seekBar.setTag(position);

            vh=v;

            int posit2 = posit;
            final int position = posit2 - 1;

            v.txtDetail.setText("\"" + list.get(position).getTitle() + "\"");

            if (posit != pos) {
                v.btnPlay.setBackground(v.itemView.getContext().getResources().getDrawable(R.drawable.po));
               // v.seekBar.setVisibility(View.INVISIBLE);
                final ObjectAnimator animation3 = ObjectAnimator.ofFloat(v.ll, "translationY", 0);
                animation3.setDuration(500);
                animation3.start();
            } else if (main.getDetailDescr() == list.get(position).getId()) {
                final ObjectAnimator animation3 = ObjectAnimator.ofFloat(v.ll, "translationY", +60);
                animation3.setDuration(0);
                animation3.start();
                v.btnPlay.setBackground(v.itemView.getContext().getResources().getDrawable(R.drawable.pause_one));
              //  v.seekBar.setVisibility(View.VISIBLE);
            }

//0882
            v.btnPlay.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    btn = v.btnPlay;
                    if (v.btnPlay.getBackground().getConstantState() == v.itemView.getContext().getResources().getDrawable(R.drawable.pause_one).getConstantState()) {
                        //если нажата кнопка паузы вызываем сервис и ставим музыку на паузу

                        final ObjectAnimator animation3 = ObjectAnimator.ofFloat(v.ll, "translationY", 0);
                        animation3.setDuration(500);
                        animation3.start();

                        v.btnPlay.setBackground(v.itemView.getContext().getResources().getDrawable(R.drawable.po));
                        a=pos;
                        pos = -1;
                        mediaPlayer.pause();
                        timer.cancel();
                        v.seekBar.setVisibility(View.INVISIBLE);

                    } else if (v.btnPlay.getBackground().getConstantState() == v.itemView.getContext().getResources().getDrawable(R.drawable.po).getConstantState()) {

                      //  if (thread != null) thread.interrupt();

                        seekBar = v.seekBar;
                        if(thread!=null)
                        thread.interrupt();

                        v.seekBar.setVisibility(View.VISIBLE);
                        //предыдущий элемент
                        if (pos != posit && pos != -1) {
                            notifyItemChanged(pos);
                        }

                        final ObjectAnimator animation3 = ObjectAnimator.ofFloat(v.ll, "translationY", +60);
                        animation3.setDuration(500);
                        animation3.start();

                        ll = v.ll;
                        btn = v.btnPlay;

                        final Handler handler = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                if(mediaPlayer!=null)
                                    v.txtCurrnet.setText(until.milliSecondsToTimer(mediaPlayer.getCurrentPosition()) + "");
                                }
                        };

                        // проверяем позицию item и id экрана для продолжения воспроизведения записи или запуска новой
                        if (mediaPlayer!=null&&main.getDetailDescr() == list.get(position).getId()) {
                            mediaPlayer.start();

                            thread = new Thread(new Runnable() {
                                public void run() {
                                    if (mediaPlayer != null)
                                        try {
                                            for (int i = 0; i < mediaPlayer.getDuration(); i = mediaPlayer.getCurrentPosition()) {
                                                if (!thredStart) break;
                                                Thread.sleep(time);
                                                handler.sendEmptyMessage(0);
                                            }
                                        } catch (Exception e) {
                                        }
                                }
                            });

                            thread.start();
                        } else {
                            if (mediaPlayer != null)
                                mediaPlayer.stop();

                            mediaPlayer = new MediaPlayer();

                            try {
                                mediaPlayer.setDataSource(list.get(position).getUrl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            try {
                                mediaPlayer.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mediaPlayer.setOnPreparedListener(MusicAdapter.this);
                            mediaPlayer.setOnCompletionListener(MusicAdapter.this);


                            v.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                boolean touch = false;

                                @Override
                                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                    if (touch) {
                                        mediaPlayer.seekTo(i);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                    touch = true;
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                    touch = false;
                                }
                            });

                            v.seekBar.setMax(mediaPlayer.getDuration());

                            //добавляем в Main информацию о нажатом элементе и id экрана для того что бы при новом запуске
                            //взять значения из экрана активности
                            v.txtSize.setText(until.milliSecondsToTimer(mediaPlayer.getDuration()));

                            Intent snoozeIntent = new Intent(v.itemView.getContext(), BroadCast.class);
                            snoozeIntent.setAction("1");
                            PendingIntent snoozePendingIntent =
                                    PendingIntent.getBroadcast(v.itemView.getContext(), 0, snoozeIntent, 0);
                            Intent snoozeIntent2 = new Intent(v.itemView.getContext(), BroadCast.class);
                            snoozeIntent2.setAction("2");
                            PendingIntent snoozePendingIntent2 =
                                    PendingIntent.getBroadcast(v.itemView.getContext(), 0, snoozeIntent2, 0);

                            mBuilder = new Notification.Builder(v.itemView.getContext()).setVisibility(Notification.VISIBILITY_PUBLIC)
                                    .setSmallIcon(Icon.createWithResource(v.itemView.getContext(), R.drawable.play))
                                    .addAction(new Notification.Action.Builder(Icon.createWithResource(v.itemView.getContext(), R.drawable.pause_notify), "Pause", snoozePendingIntent).build())
                                    .addAction(new Notification.Action.Builder(Icon.createWithResource(v.itemView.getContext(), R.drawable.play_noty), "Pause", snoozePendingIntent2).build())
                                    .setContentTitle(list.get(position).getTitle())
                                    .setContentText("Vatan")
                                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0)).build();

                            mNotificationManager =
                                    (NotificationManager) v.itemView.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0, mBuilder);

                            thredStart = true;

                            thread = new Thread(new Runnable() {
                                public void run() {
                                    if (mediaPlayer != null)
                                        try {
                                            for (int i = 0; i < mediaPlayer.getDuration(); i = mediaPlayer.getCurrentPosition()) {
                                                if (!thredStart) break;
                                                Thread.sleep(time);
                                                handler.sendEmptyMessage(0);
                                            }
                                        } catch (Exception e) {
                                                int a=0;
                                        }
                                }
                            });

                            thread.start();

                            timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        if (mediaPlayer != null) {
                                            v.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                        }
                                    } catch (Exception e) {
                                        try {
                                            Thread.sleep(500);
                                        } catch (Exception s) {
                                        }
                                        v.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    }
                                }
                            }, 0, 1000);


                            main.setPositionDetail(position);
                            main.setDetailDescr(list.get(position).getId());
                        }
                        pos = posit;
                        v.btnPlay.setBackground(v.itemView.getContext().getResources().getDrawable(R.drawable.pause_one));
                    }
                }
            });
        }
    }

    int a = pos;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void mediaPause(String s) {
        if(mediaPlayer!=null) {
            if(mediaPlayer.isPlaying() && s.equals("1")) {
                mediaPlayer.pause();
               // notifyItemChanged(pos);
                final ObjectAnimator animation3 = ObjectAnimator.ofFloat(ll, "translationY", 0);
                animation3.setDuration(500);
                animation3.start();
                seekBar.setVisibility(View.INVISIBLE);
                btn.setBackground(vh.itemView.getContext().getResources().getDrawable(R.drawable.po));
                a=pos;
                pos = -1;
            }else if(!mediaPlayer.isPlaying() && s.equals("2")){
                mediaPlayer.start();

                final ObjectAnimator animation3 = ObjectAnimator.ofFloat(ll, "translationY",60);
                animation3.setDuration(500);
                animation3.start();

                seekBar.setVisibility(View.VISIBLE);
                btn.setBackground(vh.itemView.getContext().getResources().getDrawable(R.drawable.pause_one));
                pos=a;
               // notifyItemChanged(pos);
            }

        }
    }


    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size() + 1;
        } else return 1;
    }

    public void deletMedia() {
        if (main != null)
            main.setDetailDescr(-1);

        if(thread!=null){
            thread.interrupt();
        }

        if (mediaPlayer != null) {
            mNotificationManager.cancelAll();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView btnPlay;
        TextView txtDetail;
        SeekBar seekBar;
        ImageView musicImg;
        LinearLayout ll;
        TextView txtCurrnet;
        TextView txtSize;
        TextView txtCurrentDetail;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public ViewHolder(View v) {
            super(v);
            musicImg = (ImageView) v.findViewById(R.id.btnPlay2);
            txtCurrnet = (TextView) v.findViewById(R.id.txtCurrent);
            txtSize = (TextView) v.findViewById(R.id.txtEnd);
            btnPlay = (ImageView) v.findViewById(R.id.btnPlay);
            txtCurrentDetail = (TextView) v.findViewById(R.id.txtContent);
            txtDetail = (TextView) v.findViewById(R.id.txtDetail);
            seekBar = (SeekBar) v.findViewById(R.id.seekBarDetail);
            ll = (LinearLayout) v.findViewById(R.id.linAnim);
            if (seekBar != null) {
                seekBar.getThumb().mutate().setAlpha(0);
            }
        }
    }

    public interface Clickc {
        public void onClick(String gues);
    }


}
