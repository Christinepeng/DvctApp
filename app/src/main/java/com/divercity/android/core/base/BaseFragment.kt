package com.divercity.android.core.base

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.divercity.android.core.navigation.Navigator
import com.divercity.android.features.home.HomeActivity
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_layout.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator : Navigator

    abstract fun layoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutId(), container, false)

    protected fun showProgress() = progressStatus(View.VISIBLE)

    protected fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
            with(activity) { if (this is BaseActivity || this is HomeActivity) this.include_loading.visibility = viewStatus }

    protected fun showProgressNoBk() = progressStatusNoBk(View.VISIBLE)

    protected fun hideProgressNoBk() = progressStatusNoBk(View.GONE)

    private fun progressStatusNoBk(viewStatus: Int) =
            with(activity) { if (this is BaseActivity || this is HomeActivity) this.include_loading_nobk.visibility = viewStatus }

    fun hideKeyboard() {
        view?.run {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}