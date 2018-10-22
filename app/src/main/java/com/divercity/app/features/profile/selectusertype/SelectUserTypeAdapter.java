package com.divercity.app.features.profile.selectusertype;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.divercity.app.R;
import com.divercity.app.databinding.ItemUserTypeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeAdapter extends RecyclerView.Adapter<SelectUserTypeAdapter.Holder> {

    private List<UserType> list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private UserTypeAdapterListener listener;

    private void fillList(){
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

        for(int i = 0; i < drawables.length; i++)
            list.add(new UserType(0,drawables[i],strings[i]));
    }

    static class Holder extends RecyclerView.ViewHolder {

        private final ItemUserTypeBinding binding;

        Holder(ItemUserTypeBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

    public SelectUserTypeAdapter(UserTypeAdapterListener listener) {
        this.listener = listener;
        fillList();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemUserTypeBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user_type, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        UserType item = list.get(position);
        holder.binding.imgType.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(item.drawable));
        holder.binding.txtType.setText(item.textId);
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public interface UserTypeAdapterListener {
        void onUserTypeClick(UserType userType);
    }

}