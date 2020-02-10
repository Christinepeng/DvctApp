package com.divercity.android.features.login.step1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.ViewPagerDotsPanel
import com.facebook.CallbackManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_onboarding_getstarted.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class GetStartedFragment : BaseFragment() {

    @Inject
    lateinit var getStartedViewPagerAdapter: GetStartedViewPagerAdapter

    private lateinit var handlerViewPager: Handler

    private val callbackManager = CallbackManager.Factory.create()

    companion object {

        fun newInstance() = GetStartedFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_getstarted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            Timber.e("Called Launch")
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Error deleting notification token")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerViewPager = Handler()
        setupViewPager()
        setupEvents()
    }

    private fun setupEvents() {
        btn_get_start_login.setOnClickListener {
            navigator.navigateToEnterEmailFragment(requireActivity()) }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun setupViewPager() {
        viewPager.adapter = getStartedViewPagerAdapter
        val viewPagerDotsPanel = ViewPagerDotsPanel(
            context,
            getStartedViewPagerAdapter.count,
            sliderDots
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                viewPagerDotsPanel.onPageSelected(position)
                handlerViewPager.removeCallbacksAndMessages(null)
                handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
    }

    private val runnable = Runnable {
        viewPager.currentItem =
            if (viewPager.currentItem == getStartedViewPagerAdapter.count - 1)
                0
            else
                viewPager.currentItem + 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlerViewPager.removeCallbacksAndMessages(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}