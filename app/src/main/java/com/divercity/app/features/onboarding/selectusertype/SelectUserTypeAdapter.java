package com.divercity.app.features.onboarding.selectusertype;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeAdapter extends RecyclerView.Adapter<SelectUserTypeAdapter.Holder> {

    private List<UserType> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private UserTypeAdapterListener listener;

    private void fillList(Context context) {
        int drawables[] = {
                R.drawable.img_jobseeker,
                R.drawable.img_student,
                R.drawable.img_entrepreneur,
                R.drawable.img_professional,
                R.drawable.img_hiring_manager,
                R.drawable.img_recruiter
        };

        int strings[] = {
                R.string.job_seeker,
                R.string.student,
                R.string.entrepreneur,
                R.string.professional,
                R.string.hiring_manager,
                R.string.recruiter
        };

        String ids[] = {
                context.getString(R.string.job_seeker_id),
                context.getString(R.string.student_id),
                context.getString(R.string.entrepreneur_id),
                context.getString(R.string.professional_id),
                context.getString(R.string.hiring_manager_id),
                context.getString(R.string.recruiter_id)
        };

        for (int i = 0; i < drawables.length; i++)
            list.add(new UserType(ids[i], drawables[i], strings[i]));
    }

    static class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_type);
            textView = itemView.findViewById(R.id.txt_type);
        }
    }

    public SelectUserTypeAdapter(Context context, UserTypeAdapterListener listener) {
        this.listener = listener;
        fillList(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_type, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        UserType item = list.get(position);
        holder.imageView.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(item.drawable));
        holder.textView.setText(item.textId);
        holder.itemView.setOnClickListener(view -> {
            listener.onUserTypeClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface UserTypeAdapterListener {
        void onUserTypeClick(UserType userType);
    }
}