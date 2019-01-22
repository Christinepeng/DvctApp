package com.divercity.android.features.industry.onboarding.adapter;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.data.entity.industry.IndustryResponse;

public class IndustryMultipleViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private AppCompatImageButton imgBtn;
    private Listener listener;

    private IndustryMultipleViewHolder(View itemView, Listener listener) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txt_title);
        imgBtn = itemView.findViewById(R.id.btn_public_private);
        this.listener = listener;
    }

    public void bindTo(IndustryResponse data) {
        txtTitle.setText(data.getAttributes().getName());

        imgBtn.setSelected(data.isSelected());
        imgBtn.setOnClickListener(view -> {
            imgBtn.setSelected(!data.isSelected());
            data.setSelected(imgBtn.isSelected());

            listener.onIndustryClick(data);
        });
    }

    public static IndustryMultipleViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_btn_text, parent, false);
        return new IndustryMultipleViewHolder(view, listener);
    }

    public interface Listener {
        void onIndustryClick(IndustryResponse industry);
    }
}
