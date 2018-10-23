package com.divercity.app.features.splash;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.AppConstants;
import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSplashBinding;
import com.divercity.app.features.dialogs.CustomOneBtnDialogFragment;
import com.divercity.app.features.profile.selectusertype.SelectUserTypeActivity;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;

import androidx.navigation.fragment.NavHostFragment;

public class SplashFragment extends BaseFragment {

    @Inject
    UserSharedPreferencesRepository userSharedPreferencesRepository;
    SplashViewModel viewModel;
    FragmentSplashBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_splash;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSplashBinding) getViewDataBinding();
        startTimer();
    }

    public void startTimer() {
        new Handler().postDelayed(() -> {
            if (userSharedPreferencesRepository.isUserLogged()) {
                subscribeToLiveData();
                navigator.navigateToSelectBirthday(getActivity());
//                navigator.navigateToPromptProfile(getActivity());
//                navigator.navigateToSelectGroups(getActivity());
//                navigator.navigateToSelectUserType(getActivity());
//                viewModel.fetchCurrentUserData();
//                navigator.navigateToSelectCompany(getActivity());
//                navigator.navigateToSelectIndustry(getActivity());
            } else {
                NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.action_splashFragment_to_onboardingActivity);
                getActivity().finish();
            }
        }, AppConstants.SPLASH_SCREEN_DELAY);
    }

    private void subscribeToLiveData() {
        viewModel.userData.observe(this, loginResponseResource -> {
            binding.includeLoading.setResource(loginResponseResource);
            switch (loginResponseResource.status) {
                case ERROR:
                    showErrorDialog();
                    break;
            }
        });

        viewModel.navigateToHome.observe(this, data -> {
            if (data) {
                NavHostFragment.findNavController(SplashFragment.this).navigate(R.id.action_splashFragment_to_homeActivity);
                getActivity().finish();
            }
        });

        viewModel.navigateToSelectUserType.observe(this, data -> {
            if (data) {
                getActivity().startActivity(SelectUserTypeActivity.getCallingIntent(getContext(), false));
                getActivity().finish();
            }
        });
    }

    void showErrorDialog() {
        CustomOneBtnDialogFragment customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance
                ("Ups!", getString(R.string.error_connection), getString(R.string.retry));
        customOneBtnDialogFragment.setListener(() ->
                viewModel.fetchCurrentUserData()
        );
        customOneBtnDialogFragment.show(getChildFragmentManager(), null);
    }
}
