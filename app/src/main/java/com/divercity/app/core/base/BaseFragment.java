package com.divercity.app.core.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.divercity.app.core.navigation.Navigator;
import com.divercity.app.di.Injectable;

import javax.inject.Inject;

/**
 * Created by lucas on 27/09/2018.
 */

public abstract class BaseFragment extends Fragment implements Injectable {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    @Inject
    public Navigator navigator;

    private ViewDataBinding viewDataBinding;
    private ViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() != -1)
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        return viewDataBinding.getRoot();
    }

    public abstract int getLayoutId();

    public abstract ViewModel getViewModel();

    public abstract int getBindingVariable();

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    public void hideKeyboardInFragment() {
        InputMethodManager imm = (InputMethodManager) getView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

}
