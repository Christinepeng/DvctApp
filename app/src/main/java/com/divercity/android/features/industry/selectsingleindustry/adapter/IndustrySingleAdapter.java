package com.divercity.android.features.industry.selectsingleindustry.adapter;

import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.divercity.android.R;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.ui.NetworkStateViewHolder;
import com.divercity.android.core.ui.RetryCallback;
import com.divercity.android.data.entity.industry.IndustryResponse;

import java.util.Objects;

import javax.inject.Inject;

public class IndustrySingleAdapter extends PagedListAdapter<IndustryResponse, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;

    private IndustrySingleViewHolder.Listener listener;

    @Inject
    public IndustrySingleAdapter() {
        super(UserDiffCallback);
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    public void setListener(IndustrySingleViewHolder.Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_img_text:
                return IndustrySingleViewHolder.create(parent, listener);
            case R.layout.view_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_img_text:
                ((IndustrySingleViewHolder) holder).bindTo(getItem(position));
                break;
            case R.layout.view_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.view_network_state;
        } else {
            return R.layout.item_img_text;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean hadExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    @Override
    public void submitList(PagedList<IndustryResponse> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<IndustryResponse> currentList) {
        super.onCurrentListChanged(currentList);
    }

    private static DiffUtil.ItemCallback<IndustryResponse> UserDiffCallback = new DiffUtil.ItemCallback<IndustryResponse>() {

        @Override
        public boolean areItemsTheSame(IndustryResponse oldItem, IndustryResponse newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(IndustryResponse oldItem, IndustryResponse newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

}