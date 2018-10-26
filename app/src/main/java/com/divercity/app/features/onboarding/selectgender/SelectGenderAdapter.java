package com.divercity.app.features.onboarding.selectgender;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectGenderAdapter extends RecyclerView.Adapter<SelectGenderAdapter.Holder> {

    private List<Gender> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Listener listener;

    private void fillList(Context context) {
        int drawables[] = {
                R.drawable.ic_male,
                R.drawable.ic_female,
                R.drawable.ic_no_gender,
        };

        int strings[] = {
                R.string.male,
                R.string.female,
                R.string.non_binary,
        };

        String ids[] = {
                context.getString(R.string.male_id),
                context.getString(R.string.female_id),
                context.getString(R.string.non_binary_id),
        };

        for (int i = 0; i < drawables.length; i++)
            list.add(new Gender(ids[i], drawables[i], strings[i]));
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView txtTitle;

        Holder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.item_img_txt_img);
            txtTitle = itemView.findViewById(R.id.item_img_txt_txt);
        }
    }

    @Inject
    public SelectGenderAdapter(Context context) {
        fillList(context);
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
        View view = layoutInflater.inflate(R.layout.item_img_text, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        Gender item = list.get(position);
        holder.imgIcon.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(item.drawable));
        holder.txtTitle.setText(item.textId);
        holder.itemView.setOnClickListener(view -> {
            listener.onGenderClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface Listener {
        void onGenderClick(Gender gender);
    }
}