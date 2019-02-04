package com.kaspsoft.radinvatan;

import com.kaspsoft.radinvatan.data.global.factories.NetworkFactories;
import com.kaspsoft.radinvatan.data.global.module.NetworkModule;
import com.kaspsoft.radinvatan.ui.MainActivity;
import com.kaspsoft.radinvatan.ui.detail_list_menu.DetailFragmentPresenter;
import com.kaspsoft.radinvatan.ui.list_menu.PresenterFragmentList;

import dagger.Component;

@Component(modules = NetworkModule.class)
public interface AppComponent {
    void ijectPresenterFragmentList(PresenterFragmentList presenterFragmentList);
    void ijectPresenterDerailList(DetailFragmentPresenter presenterFragmentList);
    void injectNetworkMainActivity(MainActivity mainActivity);
}
