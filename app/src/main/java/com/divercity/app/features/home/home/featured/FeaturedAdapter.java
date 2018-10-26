package com.divercity.app.features.home.home.featured;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;
import com.divercity.app.core.utils.GlideApp;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 20/02/2018.
 */

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.Holder> {

    private List<StoriesFeaturedResponse> list;
    private FeaturedAdapterListener listener;

    static class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;
        Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_main);
            textView = itemView.findViewById(R.id.txt_main);
        }
    }

    @Inject
    public FeaturedAdapter() {
//        this.listener = listener;
    }

    public void submitList(List<StoriesFeaturedResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        StoriesFeaturedResponse item = list.get(position);
        holder.textView.setText(item.getText());
        GlideApp.with(holder.itemView)
                .load(item.getPictureMain()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface FeaturedAdapterListener {
        void onPostClicked(StoriesFeaturedResponse post);
    }

}