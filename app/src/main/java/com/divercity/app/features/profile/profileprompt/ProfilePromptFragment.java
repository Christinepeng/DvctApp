package com.divercity.app.features.profile.profileprompt;


import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentProfilePromptBinding;

public class ProfilePromptFragment extends BaseFragment {

    ProfilePromptViewModel viewModel;
    FragmentProfilePromptBinding binding;

    public ProfilePromptFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile_prompt;
    }

    @Override
    public ViewModel getViewModel() {
        return null;
    }

    @Override
    public int getBindingVariable() {
        return -1;
    }

    public static ProfilePromptFragment newInstance() {
        return new ProfilePromptFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentProfilePromptBinding) getViewDataBinding();

        binding.btnNext.setOnClickListener(view1 ->
                navigator.navigateToSelectUserType(getActivity(), true)
        );

        binding.btnNotNow.setOnClickListener(view1 ->
                navigator.navigateToSelectUserType(getActivity(), false)
        );
    }
}
