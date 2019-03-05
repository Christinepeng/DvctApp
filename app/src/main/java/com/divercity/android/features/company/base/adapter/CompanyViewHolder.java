package com.divercity.android.features.company.base.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.divercity.android.R;
import com.divercity.android.data.entity.company.response.CompanyResponse;

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
//        try {
//            img.setVisibility(View.VISIBLE);
//            String urlMain = data.getUserAttributes().getPhotos().getThumb();
//            GlideApp.with(itemView.getContext())
//                    .load(urlMain).into(img);
//        } catch (NullPointerException e) {
//            img.setVisibility(View.GONE);
//        }
        img.setVisibility(View.GONE);
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
