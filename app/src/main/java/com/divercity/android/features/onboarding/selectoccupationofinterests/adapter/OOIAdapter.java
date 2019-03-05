package com.divercity.android.features.onboarding.selectoccupationofinterests.adapter;

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
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class OOIAdapter extends PagedListAdapter<OOIResponse, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;
    private List<String> idList = new ArrayList<>();

    private OOIViewHolder.Listener listener = ooi -> {
        if (ooi.isSelected())
            idList.add(ooi.getId());
        else
            idList.remove(ooi.getId());
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