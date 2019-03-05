package com.divercity.android.features.onboarding.selectoccupation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divercity.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectOccupationAdapter extends RecyclerView.Adapter<SelectOccupationAdapter.Holder> {

    private List<OccupationViewEntity> list = new ArrayList<>();
    private List<OccupationViewEntity> listSelected = new ArrayList<>();

    private void fillList() {

        String strings[] = {
                "💼   Accountant",
                "✈️   Aerospace Engineer",
                "🎨   Artist",
                "💰   Banker",
                "👩‍🔬 Chemical Engineer",
                "📐   Civil Engineer",
                "🎓   College Student",
                "📚   Consultant",
                "💃   Dancer",
                "🖥   Data Analyst/Manager",
                "💊   Doctor",
                "🔬   Engineering Designer",
                "🎧   Entertainer",
                "👠   Fashion Designer",
                "💼   General Manager",
                "🎓   Graduate Student",
                "💇‍♀️ Hair & Beauty Professional",
                "🎒   High School Student",
                "👨🏽‍💻   Human Resources",
                "💵   Investment Banker",
                "⚖️   Lawyer",
                "📈   Marketing Manager/Associate",
                "⚙️   Mechanical Engineer",
                "🎤   Musician",
                "🏥   Nurse",
                "📂   Other",
                "📝   Physical Therapist",
                "📊   Product Manager",
                "📋   Project Manager",
                "✒️   Psychologist",
                "🏠   Realtor",
                "🗂   Recruiter",
                "👨‍👩‍👧‍👦   Social Worker",
                "💻   Software Engineer",
                "📖   Teacher",
                "📱   Web/Mobile Designer"
        };

        for (String str : strings)
            list.add(new OccupationViewEntity(str));
    }

    @Inject
    public SelectOccupationAdapter() {
        fillList();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_btn_text, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        OccupationViewEntity data = list.get(position);

        holder.txtTitle.setText(data.title);
        holder.imgBtn.setSelected(data.isSelected);
        holder.imgBtn.setOnClickListener(view -> {
            holder.imgBtn.setSelected(!data.isSelected);
            data.isSelected = holder.imgBtn.isSelected();

            if (data.isSelected)
                listSelected.add(data);
            else
                listSelected.remove(data);
        });
    }

    @Override
    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public List<OccupationViewEntity> getListSelected() {
        return listSelected;
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        AppCompatImageButton imgBtn;

        Holder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            imgBtn = itemView.findViewById(R.id.btn_public_private);
        }
    }

    static class OccupationViewEntity {
        String title;
        boolean isSelected;

        OccupationViewEntity(String title) {
            this.title = title;
            this.isSelected = false;
        }

        @Override
        public String toString() {
            return this.title.substring(this.title.indexOf(" ")).trim();
        }
    }
}