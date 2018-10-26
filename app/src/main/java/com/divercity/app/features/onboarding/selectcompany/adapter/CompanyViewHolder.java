package com.divercity.app.features.onboarding.selectcompany.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;
import com.divercity.app.core.utils.GlideApp;
import com.divercity.app.data.entity.company.CompanyResponse;

public class CompanyViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView text;
    private Listener listener;

    private CompanyViewHolder(View itemView, Listener listener) {
        super(itemView);
        img = itemView.findViewById(R.id.item_img_txt_img);
        text = itemView.findViewById(R.id.item_img_txt_txt);
        this.listener = listener;
    }

    public void bindTo(CompanyResponse data) {
        try {
            img.setVisibility(View.VISIBLE);
            String urlMain = data.getAttributes().getPhotos().getThumb();
            GlideApp.with(itemView.getContext())
                    .load(urlMain).into(img);
        } catch (NullPointerException e) {
            img.setVisibility(View.GONE);
        }
        text.setText(data.getAttributes().getName());

        itemView.setOnClickListener(view -> {
            listener.onCompanyClick(data);
        });
    }

    public static CompanyViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_img_text, parent, false);
        return new CompanyViewHolder(view, listener);
    }

    public interface Listener {
        void onCompanyClick(CompanyResponse company);
    }
}
