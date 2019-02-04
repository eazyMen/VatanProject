package com.kaspsoft.radinvatan.ui.list_menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaspsoft.radinvatan.R;
import com.kaspsoft.radinvatan.models.ListSchedule;
import com.kaspsoft.radinvatan.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFragmentList extends RecyclerView.Adapter<AdapterFragmentList.ViewHolder>{

    List<ListSchedule> list;
    ClickBtn click;

    AdapterFragmentList(List<ListSchedule> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        click =(( MainActivity)v.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder v, int i) {
        v.txtView.setText(list.get(i).getTitle());
        Picasso.get()
                .load(list.get(i).getImg())
                .placeholder(R.drawable.load)
                .into(v.imgView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgView;
        private TextView txtView;
        public ViewHolder(View itemView) {
            super(itemView);

            imgView = (ImageView) itemView.findViewById(R.id.imageList);
            txtView = (TextView) itemView.findViewById(R.id.textList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.clickBtn(list.get(getLayoutPosition()).getId());
                }
            });
        }

    }

    public interface ClickBtn{
        void clickBtn(int i);
    }
}
