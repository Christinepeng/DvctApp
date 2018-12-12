package com.divercity.app.features.onboarding.selectoccupationofinterests.adapter;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.divercity.app.R;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.ui.NetworkStateViewHolder;
import com.divercity.app.core.ui.RetryCallback;
import com.divercity.app.data.entity.occupationofinterests.OOIResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class OOIAdapter extends PagedListAdapter<OOIResponse, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;
    private List<String> idList = new ArrayList<>();

    private OOIViewHolder.Listener listener = new OOIViewHolder.Listener() {
        @Override
        public void onOOIClick(OOIResponse ooi) {
            if (ooi.isSelected())
                idList.add(ooi.getId());
            else
                idList.remove(ooi.getId());
        }
    };

    @Inject
    public OOIAdapter() {
        super(UserDiffCallback);
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_img_text:
                return OOIViewHolder.create(parent, listener);
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
                ((OOIViewHolder) holder).bindTo(getItem(position));
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
    public void submitList(PagedList<OOIResponse> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<OOIResponse> currentList) {
        super.onCurrentListChanged(currentList);
    }

    private static DiffUtil.ItemCallback<OOIResponse> UserDiffCallback = new DiffUtil.ItemCallback<OOIResponse>() {

        @Override
        public boolean areItemsTheSame(OOIResponse oldItem, OOIResponse newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(OOIResponse oldItem, OOIResponse newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public List<String> getIdList() {
        return idList;
    }
}