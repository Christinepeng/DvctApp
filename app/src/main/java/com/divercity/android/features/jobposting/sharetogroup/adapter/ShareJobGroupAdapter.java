package com.divercity.android.features.jobposting.sharetogroup.adapter;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import com.divercity.android.R;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.ui.NetworkStateViewHolder;
import com.divercity.android.core.ui.RetryCallback;
import com.divercity.android.data.entity.group.GroupResponse;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Objects;

public class ShareJobGroupAdapter extends PagedListAdapter<GroupResponse, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;

    private ArrayList<String> jobsIds = new ArrayList<>();

    private ShareJobGroupViewHolder.Listener listener = new ShareJobGroupViewHolder.Listener() {
        @Override
        public void onGroupShareClick(@NotNull GroupResponse group, boolean isSelected) {
            if (isSelected)
                jobsIds.add(group.getId());
            else
                jobsIds.remove(group.getId());
        }
    };

    @Inject
    public ShareJobGroupAdapter() {
        super(UserDiffCallback);
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_group_share:
                return ShareJobGroupViewHolder.Companion.create(parent, listener);
            case R.layout.view_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_group_share:
                ((ShareJobGroupViewHolder) holder).bindTo(getItem(position));
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
            return R.layout.item_group_share;
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
    public void submitList(PagedList<GroupResponse> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<GroupResponse> currentList) {
        super.onCurrentListChanged(currentList);
    }

    private static DiffUtil.ItemCallback<GroupResponse> UserDiffCallback = new DiffUtil.ItemCallback<GroupResponse>() {

        @Override
        public boolean areItemsTheSame(GroupResponse oldItem, GroupResponse newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(GroupResponse oldItem, GroupResponse newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public ArrayList<String> getJobsIds() {
        return jobsIds;
    }
}