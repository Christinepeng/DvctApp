package com.divercity.app.features.onboarding;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.AppConstants;
import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.databinding.FragmentOnboardingBinding;

import javax.inject.Inject;

import androidx.navigation.fragment.NavHostFragment;

public class OnboardingFragment extends BaseFragment {

    public static final int NAVIGATE_TO_SIGN_UP = 10;

    @Inject
    public ViewPagerOnboardingAdapter viewPagerOnboardingAdapter;
    FragmentOnboardingBinding binding;
    private OnboardingViewModel viewModel;

    private Handler handlerViewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handlerViewPager = new Handler();
        binding = (FragmentOnboardingBinding) getViewDataBinding();
        setupViewPager();
        subscribeToLiveData();
        setupEvents();

        //TODO Remove
        Bundle bundle = new Bundle();
        bundle.putString("email", "ucasgriotto@hotmail.com");
//        NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.action_onboardingFragment_to_loginFragment, bundle);
//        NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.action_onboardingFragment_to_signUpFragment, bundle);
    }

    public void subscribeToLiveData() {
        viewModel.getIsEmailRegistered().observe(this, response -> {
            Preconditions.checkNotNull(response);
            binding.includeLoading.setResource(response);

            switch (response.status){
                case ERROR:
                    showSnackbar(response.message);
                    break;
                case SUCCESS:
                    Bundle bundle = new Bundle();
                    bundle.putString("email", binding.etEmail.getText().toString());
                    if (response.data)
                        NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.action_onboardingFragment_to_loginFragment, bundle);
                     else
                        NavHostFragment.findNavController(OnboardingFragment.this).navigate(R.id.action_onboardingFragment_to_signUpFragment, bundle);
                    break;
            }
        });

        viewModel.getNavigateTo().observe(this, integer -> {
            Preconditions.checkNotNull(integer);
            if (integer != NAVIGATE_TO_SIGN_UP) {

            }
        });
    }

    public void setupViewPager() {
        binding.viewPager.setAdapter(viewPagerOnboardingAdapter);
        final ViewPagerDotsPanel viewPagerDotsPanel = new ViewPagerDotsPanel(getContext(),
                viewPagerOnboardingAdapter.getCount(),
                binding.sliderDots);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPagerDotsPanel.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        handlerViewPager.postDelayed(() ->
                        binding.viewPager.setCurrentItem(
                                binding.viewPager.getCurrentItem() == 0 ? 1 : 0)
                , AppConstants.ONBOARDING_PAGES_DELAY);
    }

    public void showSnackbar(String message) {
        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                message,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_onboarding;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(OnboardingViewModel.class);
        return viewModel;
    }

    private void setupEvents(){
        binding.btnFacebook.setOnClickListener(view -> showToast());
        binding.btnTwitter.setOnClickListener(view -> showToast());
    }

    private void showToast(){
        Toast.makeText(getActivity(),"Coming soon", Toast.LENGTH_SHORT).show();
    }

}
