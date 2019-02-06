package com.divercity.android.features.home.home.feed.adapter;

import android.arch.paging.AsyncPagedListDiffer;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.divercity.android.R;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.ui.NetworkStateViewHolder;
import com.divercity.android.core.ui.RetryCallback;
import com.divercity.android.data.entity.home.HomeItem;
import com.divercity.android.data.entity.home.Recommended;
import com.divercity.android.data.entity.questions.QuestionResponse;
import com.divercity.android.features.home.home.recommended.RecommendedAdapter;

import javax.inject.Inject;

public class HomeAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private NetworkState networkState;
    private RetryCallback retryCallback;
    private RecommendedAdapter recommendedAdapter;

    private AsyncPagedListDiffer<HomeItem> mDiffer;

    @Inject
    public HomeAdapterNew() {
        final AdapterListUpdateCallback adapterCallback = new AdapterListUpdateCallback(this);
        mDiffer = new AsyncPagedListDiffer<>(new ListUpdateCallback() {
            @Override
            public void onInserted(int i, int i1) {
                adapterCallback.onInserted(i, i1);
            }

            @Override
            public void onRemoved(int i, int i1) {
                adapterCallback.onRemoved(i, i1);
            }

            @Override
            public void onMoved(int i, int i1) {
                adapterCallback.onMoved(i, i1);
            }

            @Override
            public void onChanged(int i, int i1, @Nullable Object o) {
                adapterCallback.onChanged(i, i1, o);
            }
        }, new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<HomeItem>() {
            @Override
            public boolean areItemsTheSame(@NonNull HomeItem homeItem, @NonNull HomeItem t1) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull HomeItem homeItem, @NonNull HomeItem t1) {
                return false;
            }
        }).build());
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    public void submitList(PagedList<HomeItem> pagedList) {
        mDiffer.submitList(pagedList);
    }

    public void setRecommendedAdapter(RecommendedAdapter recommendedAdapter) {
        this.recommendedAdapter = recommendedAdapter;
    }

    public PagedList<HomeItem> getCurrentList() {
        return mDiffer.getCurrentList();
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
                ((QuestionsViewHolder) holder).bindTo((QuestionResponse) mDiffer.getItem(position));
                break;
            case R.layout.view_network_state:
                ((NetworkStateViewHolder) holder).bindTo(networkState);
                break;
            case R.layout.item_list_recommended:
                ((RecommendedViewHolder) holder).bindTo((Recommended) mDiffer.getItem(position), recommendedAdapter);
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
        } else if (position == 0) {
            return R.layout.item_list_recommended;
        } else {
            return R.layout.item_question;
        }
    }

    @Override
    public int getItemCount() {
        return mDiffer.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean hadExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(mDiffer.getItemCount());
            } else {
                notifyItemInserted(mDiffer.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }
}