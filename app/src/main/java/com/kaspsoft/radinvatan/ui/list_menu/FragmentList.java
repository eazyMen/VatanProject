package com.kaspsoft.radinvatan.ui.list_menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.models.ListSchedule;

import java.util.List;

public class FragmentList extends MvpAppCompatFragment implements ViewList {

    final private String KEY_LINK = "getLink";
    final private String KEY_TITLE = "getTitle";
    private int link;
    private String title;
    private Toolbar toolbar;
    private TextView textView;

    @InjectPresenter
    PresenterFragmentList presenter;

    RecyclerView resView;
    AdapterFragmentList adapter;

    @ProvidePresenter
    PresenterFragmentList providePresenter(){
        return new PresenterFragmentList(link);
    }

    public android.support.v4.app.Fragment newInstance(int link, String title){
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LINK, link);
        bundle.putString(KEY_TITLE, title);
        FragmentList frag = new FragmentList();
        frag.setArguments(bundle);
        return frag;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        link = getArguments().getInt(KEY_LINK);
        title = getArguments().getString(KEY_TITLE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, null);

        resView = (RecyclerView) v.findViewById(R.id.resList);
        resView.setLayoutManager(new LinearLayoutManager(resView.getContext()));
        //resView.addItemDecoration();
        textView = (TextView)v.findViewById(R.id.txtList);
        textView.setVisibility(View.INVISIBLE);
        toolbar = (Toolbar)v.findViewById(R.id.toolbarList);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.back_end);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return v;
    }

    @Override
    public void showList(List<ListSchedule> listList) {
        if(listList!=null) {
            adapter = new AdapterFragmentList(listList);
            resView.setAdapter(adapter);
        }
    }

    @Override
    public void notShow() {
        textView.setVisibility(View.VISIBLE);
    }
}
