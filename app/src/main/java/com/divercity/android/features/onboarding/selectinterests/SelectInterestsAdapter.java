package com.divercity.android.features.onboarding.selectinterests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divercity.android.R;
import com.divercity.android.data.entity.interests.InterestsResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectInterestsAdapter extends RecyclerView.Adapter<SelectInterestsAdapter.Holder> {

    private List<InterestsResponse> list = Collections.emptyList();
    private List<String> idsSelected = new ArrayList<>();
    private Listener listener;
    private ListenerByPosition listenerByPosition;

    static class Holder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        Holder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }

    @Inject
    public SelectInterestsAdapter() {
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setListener(ListenerByPosition listener) {
        this.listenerByPosition = listener;
    }

    public void setList(List<InterestsResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<InterestsResponse> getList() {
        return list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_interest, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        InterestsResponse item = list.get(position);

        holder.txtTitle.setText(item.getAttributes().getTitle());

        setBackground(holder, item);

        holder.itemView.setOnClickListener(view -> {
            if (listener != null || listenerByPosition != null) {
                item.setSelected(!item.isSelected());
                setBackground(holder, item);

                if (listener != null)
                    listener.onInterestClick(idsSelected);

                if (listenerByPosition != null)
                    listenerByPosition.onInterestClick(item, position);
            }
        });
    }

    private void setBackground(Holder holder, InterestsResponse item) {
        Context context = holder.itemView.getContext();
        if (item.isSelected()) {
            holder.txtTitle.setBackground(ContextCompat.getDrawable
                    (context, R.drawable.shape_backgrd_round_blue3));

            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.white));
            idsSelected.add(item.getId());
        } else {
            holder.txtTitle.setBackground(ContextCompat.getDrawable
                    (holder.itemView.getContext(), R.drawable.bk_white_stroke_blue_rounded));

            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.appBlue));
            idsSelected.remove(item.getId());
        }
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    interface Listener {

        void onInterestClick(List<String> idsSelected);
    }

    public interface ListenerByPosition {

        void onInterestClick(InterestsResponse interests, int position);
    }
}