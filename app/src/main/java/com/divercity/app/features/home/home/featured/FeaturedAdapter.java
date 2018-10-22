package com.divercity.app.features.home.home.featured;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.divercity.app.R;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;
import com.divercity.app.databinding.ItemFeaturedBinding;
import com.divercity.app.core.utils.GlideApp;

import java.util.List;

/**
 * Created by lucas on 20/02/2018.
 */

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.Holder> {

    private List<StoriesFeaturedResponse> list;
    private LayoutInflater layoutInflater;
    private FeaturedAdapterListener listener;

    static class Holder extends RecyclerView.ViewHolder {

        private final ItemFeaturedBinding binding;

        Holder(ItemFeaturedBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

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
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemFeaturedBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_featured, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        StoriesFeaturedResponse item = list.get(position);
        holder.binding.txtMain.setText(item.getText());
        GlideApp.with(holder.itemView)
                .load(item.getPictureMain()).into(holder.binding.imgMain);
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface FeaturedAdapterListener {
        void onPostClicked(StoriesFeaturedResponse post);
    }

}