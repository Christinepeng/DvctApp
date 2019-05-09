package com.divercity.android.core.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.data.Status;

import androidx.recyclerview.widget.RecyclerView;

public class NetworkStateHorizontalViewHolder extends RecyclerView.ViewHolder {

    private TextView errorMessageTextView;
    private ImageButton retryLoadingButton;
    private ProgressBar loadingProgressBar;

    private NetworkStateHorizontalViewHolder(View itemView, final RetryCallback retryCallback) {
        super(itemView);
        errorMessageTextView = itemView.findViewById(R.id.view_net_sta_txt_msg);
        retryLoadingButton = itemView.findViewById(R.id.view_net_sta_btn_retry);
        loadingProgressBar = itemView.findViewById(R.id.view_net_sta_progress);
        retryLoadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryCallback.retry();
            }
        });
    }

    public void
    bindTo(NetworkState networkState) {
        //error message
//        errorMessageTextView.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
//        if (networkState.getMessage() != null) {
//            errorMessageTextView.setText(networkState.getMessage());
//        }

        //loading and retry
        retryLoadingButton.setVisibility(networkState.getStatus() == Status.ERROR ? View.VISIBLE : View.GONE);
        loadingProgressBar.setVisibility(networkState.getStatus() == Status.LOADING ? View.VISIBLE : View.GONE);
    }

    public static NetworkStateHorizontalViewHolder create(ViewGroup parent, RetryCallback retryCallback) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_network_state_horizontal, parent, false);

        return new NetworkStateHorizontalViewHolder(view, retryCallback);
    }

}
