package com.kaspsoft.radinvatan.ui.list_menu;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kaspsoft.radinvatan.App;
import com.kaspsoft.radinvatan.data.global.factories.NetworkFactories;
import com.kaspsoft.radinvatan.data.global.network.ListApi;
import com.kaspsoft.radinvatan.models.ListSchedule;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class PresenterFragmentList extends MvpPresenter<ViewList> {


    @Inject
    NetworkFactories networkFactories;

    private ListApi listApi;

    private int link;
    PresenterFragmentList(int link){
        this.link = link;
        App.getComponent().ijectPresenterFragmentList(this);
    }

    @Override
    public void attachView(ViewList view) {
        super.attachView(view);

        listApi = networkFactories.getApi();
        loadCategory();
    }

    private void loadCategory(){

        if(getViewState()!=null) {

            Call<List<ListSchedule>> usersRequest = listApi.getList(link);
            usersRequest.enqueue(new Callback<List<ListSchedule>>() {

                @Override
                public void onResponse(
                        @NonNull Call<List<ListSchedule>> call,
                        @NonNull Response<List<ListSchedule>> response
                ) {
                    if (getViewState() != null) {
                        if (response.isSuccessful()) {
                            List<ListSchedule> listSchedules  =response.body();
                            if(listSchedules.size()!=0) {
                                getViewState().showList(response.body());
                            }else {
                                getViewState().notShow();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<ListSchedule>> call, @NonNull Throwable t) {
                    getViewState().showList(null);
                }
            });
        }
    }
}
