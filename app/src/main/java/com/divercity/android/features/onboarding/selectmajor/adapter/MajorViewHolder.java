package com.divercity.android.features.onboarding.selectmajor.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.data.entity.major.MajorResponse;

public class MajorViewHolder extends RecyclerView.ViewHolder {

    private ImageView img;
    private TextView text;
    private Listener listener;

    private MajorViewHolder(View itemView, Listener listener) {
        super(itemView);
        img = itemView.findViewById(R.id.item_img_txt_img);
        text = itemView.findViewById(R.id.item_img_txt_txt);
        this.listener = listener;
    }

    public void bindTo(MajorResponse data) {
        img.setVisibility(View.GONE);
        text.setText(data.getAttributes().getName());
        itemView.setOnClickListener(view -> {
            listener.onMajorClick(data);
        });
    }

    public static MajorViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_img_text, parent, false);
        return new MajorViewHolder(view, listener);
    }

    public interface Listener {
        void onMajorClick(MajorResponse majorResponse);
    }
}
