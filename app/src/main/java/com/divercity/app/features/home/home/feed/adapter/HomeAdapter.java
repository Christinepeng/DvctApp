package com.divercity.app.features.home.home.feed.adapter;

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
import com.divercity.app.data.entity.home.HomeItem;
import com.divercity.app.data.entity.home.Recommended;
import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.features.home.home.recommended.RecommendedAdapter;

import java.util.Objects;

import javax.inject.Inject;

public class HomeAdapter extends PagedListAdapter<HomeItem, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;
    private RecommendedAdapter recommendedAdapter;

    @Inject
    public HomeAdapter() {
        super(UserDiffCallback);
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    public void setRecommendedAdapter(RecommendedAdapter recommendedAdapter) {
        this.recommendedAdapter = recommendedAdapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.item_question:
                return QuestionsViewHolder.create(parent);
            case R.layout.view_network_state:
                return NetworkStateViewHolder.create(parent, retryCallback);
            case R.layout.item_list_recommended:
                return RecommendedViewHolder.Companion.create(parent);
            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.item_question:
                ((QuestionsViewHolder) holder).bindTo((QuestionResponse) getItem(position));
                break;
            case R.layout.view_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
            case R.layout.item_list_recommended:
                ((RecommendedViewHolder) holder).bindTo((Recommended) getItem(position), recommendedAdapter);
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
        } else if(position == 0){
            return R.layout.item_list_recommended;
        } else {
            return R.layout.item_question;
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
    public void submitList(PagedList<HomeItem> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<HomeItem> currentList) {
        super.onCurrentListChanged(currentList);
    }

    private static DiffUtil.ItemCallback<HomeItem> UserDiffCallback = new DiffUtil.ItemCallback<HomeItem>() {

        @Override
        public boolean areItemsTheSame(HomeItem oldItem, HomeItem newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(HomeItem oldItem, HomeItem newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };
}