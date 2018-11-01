package com.divercity.app.features.home.groups

import android.os.Bundle
import android.os.Handler
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.features.home.HomeActivity

/**
 * Created by lucas on 24/10/2018.
 */

class GroupsFragment : BaseFragment() {

//    @Inject
//    lateinit var viewPagerEnterEmailAdapter: ViewPagerEnterEmailAdapter
//
//    lateinit var viewModel: EnterEmailViewModel
    lateinit var handlerViewPager: Handler

    companion object {

        fun newInstance(): GroupsFragment = GroupsFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
//        AndroidSupportInjection.inject(this)
//        viewModel = ViewModelProviders.of(this, viewModelFactory)[EnterEmailViewModel::class.java]
//        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.groups)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
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
//        viewModel.isEmailRegistered.observe(this, Observer { response ->
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
//        viewModel.navigateToLogin.observe(this, Observer {
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