package com.divercity.android.features.agerange.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divercity.android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectAgeAdapter extends RecyclerView.Adapter<SelectAgeAdapter.Holder> {

    private LayoutInflater layoutInflater;
    private Listener listener;

    private List<AgeViewEntity> list;
    private int prevPositionSelected = -1;
    private String ageRangeSelected;

    static class Holder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        AppCompatImageButton imgBtn;

        Holder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgBtn = itemView.findViewById(R.id.btn_public_private);
        }
    }

    @Inject
    public SelectAgeAdapter(Context context) {
        list = new ArrayList<>();

        for(String age : Arrays.asList(context.getResources().getStringArray(R.array.age_range))){
            list.add(new AgeViewEntity(age));
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_btn_text, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        AgeViewEntity item = list.get(position);
        holder.txtTitle.setText(item.title);

        holder.imgBtn.setSelected(item.isSelected);

        holder.imgBtn.setOnClickListener(view -> {

            if(prevPositionSelected != -1) {
                list.get(prevPositionSelected).isSelected = false;
                notifyItemChanged(prevPositionSelected);
            }

            holder.imgBtn.setSelected(!item.isSelected);
            item.isSelected = holder.imgBtn.isSelected();

            if(item.isSelected)
                ageRangeSelected = item.title;

            prevPositionSelected = position;

            listener.onAgeClick(ageRangeSelected);
        });
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface Listener {
        void onAgeClick(String ageRange);
    }

    static class AgeViewEntity {
        String title;
        boolean isSelected;

        AgeViewEntity(String title) {
            this.title = title;
            this.isSelected = false;
        }

        @Override
        public String toString() {
            return this.title.substring(this.title.indexOf(" ")).trim();
        }
    }
}