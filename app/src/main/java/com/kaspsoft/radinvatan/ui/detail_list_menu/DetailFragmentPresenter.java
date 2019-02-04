package com.kaspsoft.radinvatan.ui.detail_list_menu;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kaspsoft.radinvatan.App;
import com.kaspsoft.radinvatan.data.global.factories.NetworkFactories;
import com.kaspsoft.radinvatan.data.global.network.ListApi;
import com.kaspsoft.radinvatan.models.DetailList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class DetailFragmentPresenter extends MvpPresenter<DetailView> {

    int id;

    @Inject
    NetworkFactories factories;

    private ListApi listApi;

    DetailFragmentPresenter(int id){
        this.id = id;
        App.getComponent().ijectPresenterDerailList(this);
    }

    @Override
    public void attachView(DetailView view) {
        super.attachView(view);
        listApi = factories.getApi();
        loadDetail();
        getViewState().showProgress();
    }

    private void loadDetail(){
        if (getViewState() != null) {

            Call<DetailList> userDetailsRequest = listApi.getDetail(id);
            userDetailsRequest.enqueue(new Callback<DetailList>() {

                @Override
                public void onResponse(
                        @NonNull Call<DetailList> call,
                        @NonNull Response<DetailList> response
                ) {
                    if (getViewState() != null) {
                        if (response.isSuccessful()) {
                            getViewState().show(response.body());
                            getViewState().ohideProgress();
                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DetailList> call, @NonNull Throwable t) {
                }
            });
        }
    }
}
