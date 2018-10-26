package com.divercity.app.features.onboarding.selectethnicity;

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

public class SelectEthnicityAdapter extends RecyclerView.Adapter<SelectEthnicityAdapter.Holder> {

    private List<Ethnicity> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Listener listener;

    private void fillList(Context context) {

        int drawables[] = {
                R.drawable.ic_male,
                R.drawable.ic_female,
                R.drawable.ic_no_gender,
                R.drawable.ic_male,
                R.drawable.ic_female,
                R.drawable.ic_no_gender,
                R.drawable.ic_male,
                R.drawable.ic_female,
                R.drawable.ic_no_gender,
        };

        int strings[] = {
                R.string.asian,
                R.string.black,
                R.string.hispanic,
                R.string.indian,
                R.string.latin_american,
                R.string.mixed_race,
                R.string.native_american,
                R.string.south_east_asian,
                R.string.white
        };

        String ids[] = {
                context.getString(R.string.asian_id),
                context.getString(R.string.black_id),
                context.getString(R.string.hispanic_id),
                context.getString(R.string.indian_id),
                context.getString(R.string.latin_american_id),
                context.getString(R.string.mixed_race_id),
                context.getString(R.string.native_american_id),
                context.getString(R.string.south_east_asian_id),
                context.getString(R.string.white_id)
        };

        for (int i = 0; i < strings.length; i++)
            list.add(new Ethnicity(ids[i], drawables[i], strings[i]));
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
    public SelectEthnicityAdapter(Context context) {
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
        Ethnicity item = list.get(position);
        holder.imgIcon.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(item.drawable));
        holder.txtTitle.setText(item.textId);
        holder.itemView.setOnClickListener(view -> {
            listener.onEthnicityClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface Listener {
        void onEthnicityClick(Ethnicity gender);
    }
}