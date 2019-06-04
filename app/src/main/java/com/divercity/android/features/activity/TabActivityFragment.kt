package com.divercity.android.features.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_activity.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class TabActivityFragment : BaseFragment() {

    @Inject
    lateinit var adapter: TabActivityViewPagerAdapter

    private lateinit var viewModel: TabActivityViewModel

    companion object {

        fun newInstance(): TabActivityFragment = TabActivityFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(TabActivityViewModel::class.java)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            icon_notification.visibility = View.GONE
            supportActionBar?.let {
                it.setTitle(R.string.activity)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
    }

    private fun setupView() {
        viewPager.adapter = adapter
        viewModel.adapterPosition?.apply {
            viewPager.currentItem = this
        }
        tab_layout.setupWithViewPager(viewPager)
    }

    override fun onDestroyView() {
        viewModel.adapterPosition = viewPager.currentItem
        super.onDestroyView()
    }

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
//        viewPager.adapterTab = viewPagerEnterEmailAdapter
//        val viewPagerDotsPanel = TabGroupsPanelDotsViewPager(context,
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
//        }, AppConstants.CAROUSEL_PAGES_DELAY)
//    }

}