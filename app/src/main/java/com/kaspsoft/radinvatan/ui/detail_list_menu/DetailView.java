package com.kaspsoft.radinvatan.ui.detail_list_menu;

import com.arellomobile.mvp.MvpView;
import com.kaspsoft.radinvatan.models.DetailList;

import java.util.List;

public interface DetailView extends MvpView {
    public void showProgress();
    public void ohideProgress();
    public void show(DetailList list);
}
