package com.divercity.app.features.home.profile

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.home.HomeActivity

/**
 * Created by lucas on 24/10/2018.
 */

class ProfileFragment : BaseFragment() {

//    @Inject
//    lateinit var viewPagerEnterEmailAdapter: ViewPagerEnterEmailAdapter
//
//    lateinit var viewModelJobs: EnterEmailViewModel
    lateinit var handlerViewPager: Handler

    companion object {

        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar()
//        AndroidSupportInjection.inject(this)
//        viewModelJobs = ViewModelProviders.of(this, viewModelFactory)[EnterEmailViewModel::class.java]
//        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.profile)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        handlerViewPager = Handler()
//        setupViewPager()
//        subscribeToLiveData()
//        setupEvents()
//    }
//
//    fun subscribeToLiveData() {
//        viewModelJobs.isEmailRegistered.observe(this, Observer { response ->
//            when (response?.status) {
//                Status.LOADING -> {
//                    showProgress()
//                }
//                Status.ERROR -> {
//                    hideProgress()
//                    showSnackbar(response.message)
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                }
//            }
//        })
//
//        viewModelJobs.navigateToLogin.observe(this, Observer {
//
//        })
//    }
//
//    fun showSnackbar(message: String?) {
//        activity?.run {
//            Snackbar.make(
//                    findViewById(android.R.id.content),
//                    message ?: "Error",
//                    Snackbar.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    private fun setupEvents() {
//        btnFacebook.setOnClickListener { showToast() }
//        btnTwitter.setOnClickListener { showToast() }
//    }
//
//    private fun showToast() {
//        Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
//    }
//
//    fun setupViewPager() {
//        viewPager.adapter = viewPagerEnterEmailAdapter
//        val viewPagerDotsPanel = ViewPagerDotsPanel(context,
//                viewPagerEnterEmailAdapter.getCount(),
//                sliderDots)
//
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
//
//            override fun onPageSelected(position: Int) {
//                viewPagerDotsPanel.onPageSelected(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
//
//        handlerViewPager.postDelayed({
//            viewPager.currentItem = if(viewPager.currentItem == 0) 1 else 0
//        }, AppConstants.ONBOARDING_PAGES_DELAY)
//    }

}