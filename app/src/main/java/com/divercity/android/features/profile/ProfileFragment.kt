package com.divercity.android.features.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.FindQuery
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ProfileViewPagerAdapter

    private lateinit var viewModel: ProfileViewModel

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

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()


//        btn_connect.setOnClickListener {
//
//            useCase(GetGitRepositoryDataUseCase.Params.forRepo("CleanNews-2017", "lucasgriotto")) {
//                it.either(::handleError, ::handleSuccess)
//            }

//            val job = GlobalScope.launch(Dispatchers.Main) {
//                try {
//                    val result = useCase.call("CleanNews-2017", "lucasgriotto")
//
//                } catch (e : Throwable){
//                    Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
//                }
////                val test = result
////                btn_connect.text = test.repository()?.description()
//                //delay(3000)
//            }
//
//            GlobalScope.launch(Dispatchers.Main){
//                delay(2000)
//                job.cancel()
//            }

//        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    showData(response.data!!)
                }
            }
        })
    }

    private fun showData(userResponse: UserResponse) {
        var attr = userResponse.userAttributes
        GlideApp.with(this)
            .load(attr?.avatarMedium)
            .apply(RequestOptions().circleCrop())
            .into(img_profile)

        txt_name.text = attr?.name
        txt_user_track.text = attr?.followersCount.toString()
            .plus(" Followers \u00B7 ")
            .plus(attr?.followingCount)
            .plus(" Following \u00B7 ")
            .plus(attr?.groupOfInterestFollowingCount)
            .plus(" Groups")
    }

    private fun setupView() {

        viewPager.adapter = adapter

        tab_layout.setupWithViewPager(viewPager)

//        slidingTabLayout.setBackgroundColor(-0x545455)
//        slidingTabLayout.setDividerColors(0x00FFFFFF)
    }

    private fun handleError(message: String) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleSuccess(query: FindQuery.Data) {
//        btn_connect.text = query.repository()?.description()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.profile)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        menu?.clear()
////        super.onCreateOptionsMenu(menu, inflater)
//    }

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