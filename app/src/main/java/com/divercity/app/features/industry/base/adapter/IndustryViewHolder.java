package com.divercity.app.features.industry.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;
import com.divercity.app.data.entity.industry.IndustryResponse;

public class IndustryViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView text;
    private Listener listener;

    private IndustryViewHolder(View itemView, Listener listener) {
        super(itemView);
        img = itemView.findViewById(R.id.item_img_txt_img);
        text = itemView.findViewById(R.id.item_img_txt_txt);
        this.listener = listener;
    }

    public void bindTo(IndustryResponse data) {
        img.setVisibility(View.GONE);
        text.setText(data.getAttributes().getName());
        itemView.setOnClickListener(view -> {
            listener.onIndustryClick(data);
        });
    }

    public static IndustryViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_img_text, parent, false);
        return new IndustryViewHolder(view, listener);
    }

    public interface Listener {
        void onIndustryClick(IndustryResponse industry);
    }
}
