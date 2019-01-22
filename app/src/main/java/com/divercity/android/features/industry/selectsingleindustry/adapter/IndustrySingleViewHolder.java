package com.divercity.android.features.industry.selectsingleindustry.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.data.entity.industry.IndustryResponse;

public class IndustrySingleViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView text;
    private Listener listener;

    private IndustrySingleViewHolder(View itemView, Listener listener) {
        super(itemView);
        this.listener = listener;
        img = itemView.findViewById(R.id.item_img_txt_img);
        text = itemView.findViewById(R.id.item_img_txt_txt);
    }

    public void bindTo(IndustryResponse data) {
        img.setVisibility(View.GONE);
        text.setText(data.getAttributes().getName());
        itemView.setOnClickListener(view -> {
            listener.onIndustryClick(data);
        });
    }

    public static IndustrySingleViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_img_text, parent, false);
        return new IndustrySingleViewHolder(view, listener);
    }

    public interface Listener {

        void onIndustryClick(IndustryResponse industry);
    }
}
