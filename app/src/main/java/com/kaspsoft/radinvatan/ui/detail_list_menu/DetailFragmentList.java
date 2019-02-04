package com.kaspsoft.radinvatan.ui.detail_list_menu;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kaspsoft.radinvatan.App;
import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.models.AudioFiles;
import com.kaspsoft.radinvatan.models.DetailList;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.interfaces.PBEKey;

public class DetailFragmentList extends MvpAppCompatFragment implements DetailView, MusicAdapter.Clickc {

    int pos;

    @InjectPresenter
    DetailFragmentPresenter presenter;

    View line;
    MusicAdapter musicAdapter;


    @ProvidePresenter
    DetailFragmentPresenter provideDetailFragment(){
        return  new DetailFragmentPresenter(pos);
    }

    TextView txtTitle;
    LinearLayout ll;
    ProgressBar pb;

    private final String KEY_POS="key";

    RecyclerView res;

    public DetailFragmentList newInstanse(int pos){
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POS, pos);
        DetailFragmentList fragmentPresenter = new DetailFragmentList();
        fragmentPresenter.setArguments(bundle);
        return fragmentPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pos = getArguments().getInt(KEY_POS);
    }

    MediaPlayer mediaPlayer;
    AudioManager am;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_layout, null);
        res = (RecyclerView) v.findViewById(R.id.recycleMusic);
        res.setLayoutManager(new LinearLayoutManager(res.getContext()));
        line = (View) v.findViewById(R.id.viewLine);
        txtTitle = (TextView) v.findViewById(R.id.textTitleDetail);
        ll = (LinearLayout) v.findViewById(R.id.linProgress);
        pb = (ProgressBar) v.findViewById(R.id.progressDetail);

        return v;
    }

    List<String> guids = new ArrayList<>();

    @Override
    public void showProgress() {
        ll.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    public void ohideProgress() {
        ll.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.INVISIBLE);
    }

    int a=0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void show(DetailList list) {
        if(a==0) {
            List<AudioFiles> audioFilesList = list.getAudio_files();
            App.setMa(new MusicAdapter(audioFilesList, this, list.getContent()));
            musicAdapter = App.getMa();
            musicAdapter.setHasStableIds(true);
            res.setAdapter(musicAdapter);
            txtTitle.setText(list.getTitle());
        }else {
            a=0;
        }
    }

    @Override
    public void onClick(String gues) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        a=1;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(musicAdapter!=null) musicAdapter.deletMedia();
        super.onDestroy();
    }
}
