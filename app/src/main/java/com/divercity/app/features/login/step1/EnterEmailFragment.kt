package com.divercity.app.features.login.step1

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Toast
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_enter_email.*
import javax.inject.Inject


/**
 * Created by lucas on 24/10/2018.
 */

class EnterEmailFragment : BaseFragment() {

    @Inject
    lateinit var viewPagerEnterEmailAdapter: ViewPagerEnterEmailAdapter

    lateinit var viewModel: EnterEmailViewModel
    lateinit var handlerViewPager: Handler

    companion object {

        fun newInstance(): EnterEmailFragment = EnterEmailFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_enter_email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[EnterEmailViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerViewPager = Handler()
        setupViewPager()
        subscribeToLiveData()
        setupEvents()
    }

    fun subscribeToLiveData() {
        viewModel.isEmailRegistered.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showSnackbar(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.navigateToLogin.observe(this, Observer {
            navigator.navigateToLoginActivity(activity!!, user_email.text.toString())
        })

        viewModel.navigateToSignUp.observe(this, Observer {
            navigator.navigateToSignUpActivity(activity!!, user_email.text.toString())
        })
    }

    fun showSnackbar(message: String?) {
        activity?.run {
            Snackbar.make(
                findViewById(android.R.id.content),
                message ?: "Error",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setupEvents() {
        btnFacebook.setOnClickListener { showToast() }
        btn_linkedin.setOnClickListener {
            navigator.navigateToLinkedinActivity(activity!!)
        }

        btn_send.setOnClickListener {
            viewModel.checkIfEmailRegistered(user_email.text.toString())
        }
    }

    private fun showToast() {
        Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
    }

    fun setupViewPager() {
        viewPager.adapter = viewPagerEnterEmailAdapter
        val viewPagerDotsPanel = ViewPagerDotsPanel(
            context,
            viewPagerEnterEmailAdapter.count,
            sliderDots
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                viewPagerDotsPanel.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        handlerViewPager.postDelayed({
            viewPager.currentItem = if (viewPager.currentItem == 0) 1 else 0
        }, AppConstants.ONBOARDING_PAGES_DELAY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        handlerViewPager.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}