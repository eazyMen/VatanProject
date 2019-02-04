package com.kaspsoft.radinvatan.ui.list_menu;

import com.arellomobile.mvp.MvpView;
import com.kaspsoft.radinvatan.models.ListSchedule;

import java.util.List;

public interface ViewList extends MvpView {
    public void showList(List<ListSchedule> listList);
    public void notShow();
}
