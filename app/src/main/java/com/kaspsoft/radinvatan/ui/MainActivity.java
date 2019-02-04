package com.kaspsoft.radinvatan.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.kaspsoft.radinvatan.App;
import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.data.global.factories.NetworkFactories;
import com.kaspsoft.radinvatan.data.global.network.ListApi;
import com.kaspsoft.radinvatan.models.ItemMenu;
import com.kaspsoft.radinvatan.ui.detail_list_menu.DetailFragmentList;
import com.kaspsoft.radinvatan.ui.list_menu.AdapterFragmentList;
import com.kaspsoft.radinvatan.ui.list_menu.FragmentList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener,
        AdapterFragmentList.ClickBtn, AdapterMenu.SetClick {

    private Button buttonPlayStope;
    private MediaPlayer mediaPlayer;
    private ImageButton imageButton;
    private final Handler handler = new Handler();
    NavigationView navigationView;
    DrawerLayout drawer;
    public MediaPlayer mediaDetail;
    ProgressBar progressBar;
    final String DATA_STREAM = "http://stream1.radiostyle.ru:8001/radiovatan";
    List<ItemMenu> menuItem = new ArrayList<>();
    FragmentList fragmentList;
    ArrayList<Integer> fragments = new ArrayList<>();
    FragmentTransaction fTrans;
    DetailFragmentList detailFragment;
    Menu menu;
    private int detailDescr=-1;
    private boolean pause;
    private int positionDetail = -1;
    AdapterMenu adapterMenu;

    RecyclerView resView;

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public int getPositionDetail() {
        return positionDetail;
    }

    public void setPositionDetail(int positionDetail) {
        this.positionDetail = positionDetail;
    }

    public int getDetailDescr() {
        return detailDescr;
    }

    public void setDetailDescr(int detailDescr) {
        this.detailDescr = detailDescr;
    }

    @Inject
    NetworkFactories networkFactories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        resView = (RecyclerView)findViewById(R.id.resMenu);
        resView.setLayoutManager(new LinearLayoutManager(resView.getContext()));


        mediaDetail = new MediaPlayer();
        fragmentList = new FragmentList();
        detailFragment = new DetailFragmentList();

        App.getComponent().injectNetworkMainActivity(this);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();


        buttonPlayStope = (Button) findViewById(R.id.playAndPause);

        mediaPlayer = new MediaPlayer();
        setMenuItem();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();
        initViews();
        if (mediaPlayer.isPlaying()) {
            buttonPlayStope.setBackground(ContextCompat.getDrawable(this, R.drawable.pause));
        } else {
            buttonPlayStope.setBackground(ContextCompat.getDrawable(this, R.drawable.play));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
                mediaPlayer.setVolume(0.5f, 0.5f);
                try{
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setOnErrorListener(this);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setDataSource(DATA_STREAM);
                    mediaPlayer.prepareAsync();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void playAndStop(View v) {
        if(!mediaPlayer.isPlaying()){
            try{
                mediaPlayer.start();
                buttonPlayStope.setBackground(ContextCompat.getDrawable(this, R.drawable.pause));
            } catch (IllegalStateException e) {
                mediaPlayer.pause();
                buttonPlayStope.setBackground(ContextCompat.getDrawable(this, R.drawable.play));
            }
        } else {
            mediaPlayer.pause();
            buttonPlayStope.setBackground(ContextCompat.getDrawable(this, R.drawable.play));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = item.getTitle()+"";
        Uri address;
        Intent openlinkIntent;
        fTrans = getSupportFragmentManager().beginTransaction();
        progressBar.setVisibility(ProgressBar.GONE);
        switch (id){
            case R.id.contact:

                break;
            case R.id.sentText1:
                    item.setCheckable(false);

                break;
            default:
                item.setCheckable(true);
                fTrans.replace(R.id.container,  fragmentList.newInstance(menuItem.get(id).getId(), menuItem.get(id).getName()));

                break;

        }
        if(fragments.size()==0)
        fragments.add(1);

        fTrans.addToBackStack(null);
        fTrans.commit();


        if (id == R.id.contact) {

        }
        else if (id == R.id.sentText1) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void ndShow(View view) {
        drawer.openDrawer(navigationView);
    }

    public void nntShow(View view)
    {
        Uri address = Uri.parse("http://nnttv.ru/");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }
    public void fbShow(View view)
    {
        Uri address = Uri.parse("https://www.facebook.com/login/?next=https%3A%2F%2Fwww.facebook.com%2Fgroups%2Fradiovatan%2F");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }
    public void vkShow(View view)
    {
        Uri address = Uri.parse("https://vk.com/vatanradio");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }
    public void instagramShow(View view)
    {
        Uri address = Uri.parse("https://www.instagram.com/radiovatan/");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }
    public void youtubeShow(View view)
    {
        Uri address = Uri.parse("https://www.youtube.com/channel/UC3LM1K3Z__id9QdIHkAd8Tg");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }
    public void vatanShow(View view)
    {
        Uri address = Uri.parse("http://radiovatan.ru/");
        Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlinkIntent);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        buttonPlayStope.setVisibility(View.VISIBLE);
        buttonPlayStope.setEnabled(true);
        progressBar.setVisibility(ProgressBar.GONE);
    }

    void setMenuItem(){
        ListApi listApi = networkFactories.getApi();
        Call<List<ItemMenu>> usersRequest = listApi.getMenu();
        usersRequest.enqueue(new Callback<List<ItemMenu>>() {

            @Override
            public void onResponse(
                    @NonNull Call<List<ItemMenu>> call,
                    @NonNull Response<List<ItemMenu>> response
            ) {
                List<ItemMenu> menu = new ArrayList<>();
                menu.add(new ItemMenu());
                menu.add(new ItemMenu());
                menu.get(1).setName("Контакты");
                menu.get(1).setId(-1);
                menu.get(0).setName("Написать нам");
                menu.get(0).setId(-2);

                for (int i=0;i<response.body().size(); i++){
                    menu.add(response.body().get(i));
                }
                menuItem = response.body();
                adapterMenu = new AdapterMenu(menu);
                resView.setAdapter(adapterMenu);
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemMenu>> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() < 1) {
            adapterMenu.setSel(-1);
        }
    }

    @Override
    public void clickBtn(int login) {
        fragments.add(1);
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.container, detailFragment.newInstanse(login));
        fTrans.addToBackStack(null);
        fTrans.commit();
    }

    @Override
    public void onClick(int id, String link) {
        drawer.closeDrawers();
        Uri address;
        Intent openlinkIntent;
        if(id==-2){
            address = Uri.parse("http://radiovatan.ru/feedback");
            openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlinkIntent);
        }else if(id==-1){
            address = Uri.parse("http://radiovatan.ru/contacts");
            openlinkIntent = new Intent(Intent.ACTION_VIEW, address);
            startActivity(openlinkIntent);
        }else {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            fTrans = getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.container, fragmentList.newInstance(id, link));
            fTrans.addToBackStack(null);
            fTrans.commit();
            adapterMenu.notifyDataSetChanged();

        }
    }
}
