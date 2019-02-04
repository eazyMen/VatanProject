package com.kaspsoft.radinvatan.ui;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.models.ItemMenu;
import com.kaspsoft.radinvatan.ui.list_menu.AdapterFragmentList;

import java.util.List;

public class AdapterMenu  extends RecyclerView.Adapter<AdapterMenu.ViewHolder>{

    List<ItemMenu> menu;
    AdapterMenu(List<ItemMenu> menu){
        this.menu = menu;
    }
    int sel=-1;

    SetClick setClick;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.menu_adapter, parent, false);
        setClick = ((MainActivity)v.getContext());
        return new AdapterMenu.ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(menu.get(position).getName());
        if(position==sel){
            holder.cl.setBackgroundResource(R.color.colorSelect);
        }else{
            holder.cl.setBackgroundResource(R.color.colorUnSelect);
        }
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ConstraintLayout cl;
        public ViewHolder(View itemView) {
            super(itemView);
            cl = (ConstraintLayout) itemView.findViewById(R.id.constMenu);
            textView= (TextView)itemView.findViewById(R.id.txtMenu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a = getLayoutPosition();
                    setClick.onClick(menu.get(a).getId(), menu.get(a).getName());
                    sel = a;
                }
            });
        }

    }

    public void setSel(int sel) {
        this.sel = sel;
        notifyDataSetChanged();
    }

    public interface SetClick{
        public void onClick(int id, String link);
    }

}
