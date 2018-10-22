package com.divercity.app.features.login;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.databinding.FragmentLoginBinding;
import com.divercity.app.features.onboarding.OnboardingActivity;

import androidx.navigation.fragment.NavHostFragment;

public class LoginFragment extends BaseFragment {

    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideKeyboardInFragment();
        binding = (FragmentLoginBinding) getViewDataBinding();
        loginViewModel.setUserEmail(getArguments().getString("email"));
        setupEvents();
        setupToolbar();
        subscribeToLiveData();
    }

    private void setupToolbar(){
        ((OnboardingActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        ((OnboardingActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((OnboardingActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.includeToolbar.title.setText(getResources().getString(R.string.login));
    }

    private void subscribeToLiveData() {
        loginViewModel.getLogin().observe(this, response -> {
            Preconditions.checkNotNull(response);
            binding.includeLoading.setResource(response);
            switch (response.status){
                case ERROR:
                    showSnackbar(response.message);
                    break;
                case SUCCESS:
                    NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_homeActivity);
                    getActivity().finish();
                    break;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel getViewModel() {
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        return loginViewModel;
    }

    public void showSnackbar(String message) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    private void setupEvents(){
        binding.txtForgotPassword.setOnClickListener(view -> showToast());
    }

    private void showToast(){
        Toast.makeText(getActivity(),"Coming soon", Toast.LENGTH_SHORT).show();
    }

}
