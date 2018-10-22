package com.divercity.app.features.signup;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.databinding.FragmentSignUpBinding;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.features.onboarding.OnboardingActivity;
import com.divercity.app.core.utils.Preconditions;

import androidx.navigation.fragment.NavHostFragment;

public class SignUpFragment extends BaseFragment {

    private SignUpViewModel viewModel;
    private FragmentSignUpBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboardInFragment();

        binding = (FragmentSignUpBinding) getViewDataBinding();
        setupToolbar();
        setupEvents();
        subscribeToLiveData();

        viewModel.email.set(getArguments().getString("email"));

//        viewModel.email.set("esther@gmail.com");
//        viewModel.name.set("Esther Abosede");
//        viewModel.password.set("password");
//        viewModel.confirmPassword.set("password");
//        viewModel.username.set("esther");
    }

    private void setupToolbar(){
        ((OnboardingActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        ((OnboardingActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((OnboardingActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.includeToolbar.title.setText(getResources().getString(R.string.account_profile));
    }

    public void subscribeToLiveData() {
        viewModel.getSignUp().observe(this, voidResource ->{
            Preconditions.checkNotNull(voidResource);
            binding.includeLoading.setResource(voidResource);

            switch (voidResource.status){
                case ERROR:
                    showSnackbar(voidResource.message);
                    break;
                case SUCCESS:
                    NavHostFragment.findNavController(SignUpFragment.this).navigate(R.id.action_signUpFragment_to_homeActivity);
                    getActivity().finish();
                    break;
            }
        });
    }

    public void showSnackbar(String message) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    private void setupEvents(){
        binding.photo.setOnClickListener(view -> showToast());
    }

    private void showToast(){
        Toast.makeText(getActivity(),"Coming soon", Toast.LENGTH_SHORT).show();
    }

}
