package com.divercity.app.features.onboarding.selectgroups.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.divercity.app.R;
import com.divercity.app.core.utils.GlideApp;
import com.divercity.app.data.entity.group.GroupResponse;

public class GroupsViewHolder extends RecyclerView.ViewHolder {

    private ImageView imgMain;
    private TextView txtName;
    private TextView txtMember;
    private AppCompatButton btnJoin;
    private Listener listener;

    private GroupsViewHolder(View itemView, Listener listener) {
        super(itemView);
        imgMain = itemView.findViewById(R.id.item_group_img);
        txtName = itemView.findViewById(R.id.item_group_txt_title);
        txtMember = itemView.findViewById(R.id.item_group_txt_members);
        btnJoin = itemView.findViewById(R.id.item_group_btn_join_member);
        this.listener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void bindTo(GroupResponse data, int position) {
        try {
            imgMain.setVisibility(View.VISIBLE);
            String urlMain = data.getAttributes().getPictureMain();
            GlideApp.with(itemView.getContext())
                    .load(urlMain)
                    .apply(new RequestOptions().transforms(new RoundedCorners(16)))
                    .into(imgMain);
        } catch (NullPointerException e) {
            imgMain.setVisibility(View.GONE);
        }
        txtName.setText(data.getAttributes().getTitle());
        txtMember.setText(data.getAttributes().getFollowersCount() + " Members");

        if(data.getAttributes().isIsFollowedByCurrent()){
            btnJoin.setOnClickListener(null);
            btnJoin.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded);
            btnJoin.setTextColor(itemView.getContext().getResources().getColor(R.color.appBlue));
            btnJoin.setText("Member");
        } else {
            btnJoin.setOnClickListener(view -> {
                listener.onGroupJoinClick(position, data);
            });
            btnJoin.setBackgroundResource(R.drawable.shape_backgrd_round_blue3);
            btnJoin.setTextColor(itemView.getContext().getResources().getColor(android.R.color.white));
            btnJoin.setText("Join");
        }
        btnJoin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    public static GroupsViewHolder create(ViewGroup parent, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_group, parent, false);
        return new GroupsViewHolder(view, listener);
    }

    public interface Listener {
        void onGroupJoinClick(int position, GroupResponse group);
    }
}
