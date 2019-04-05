package com.divercity.android.features.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.divercity.android.R
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.navigation.Navigator
import com.divercity.android.features.activity.TabActivityFragment
import com.divercity.android.features.dialogs.CompletedProfileDialogFragment
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.divercity.android.features.home.home.HomeFragment
import com.divercity.android.features.home.people.TabPeopleFragment
import com.divercity.android.features.jobs.TabJobsFragment
import com.divercity.android.features.profile.pcurrentuser.CurrentUserProfileFragment
import com.divercity.android.features.usecase.LogoutUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var logoutDisposable: Disposable

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    @Inject
    lateinit var navigator: Navigator

    lateinit var viewModel: HomeActivityViewModel

    companion object {
        private const val INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE = "showDialogProfile"

        fun getCallingIntent(context: Context?, showDialogProfileCompleted: Boolean): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE, showDialogProfileCompleted)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(HomeActivityViewModel::class.java)
        setContentView(R.layout.activity_home)
        showDialogProfileComplete(
            intent.getBooleanExtra(
                INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE,
                false
            )
        )
        setupBottomNavigationView()
        savedInstanceState ?: selectItem(0)
        setSupportActionBar(include_toolbar.toolbar)

        logoutDisposable = RxBus.listen(RxEvent.EventUnauthorizedUser::class.java).subscribe {
            showUnauthorizedUser()
        }

        viewModel.checkFCMDevice()

        viewModel.navigateToGroupDetail.observe(this, Observer {
            navigator.navigateToGroupDetail(this, it)
        })

        viewModel.checkDeepLinkRedirection()
    }

    /**
     * Fragment creation according to tab position
     */
    fun selectItem(position: Int) {
        var selectedFragment: Fragment = HomeFragment.newInstance()
        when (position) {
            0 -> selectedFragment = HomeFragment.newInstance()
            1 -> selectedFragment = TabPeopleFragment.newInstance()
            2 -> selectedFragment = TabJobsFragment.newInstance()
            3 -> selectedFragment = TabActivityFragment.newInstance()
            4 -> selectedFragment = CurrentUserProfileFragment.newInstance()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, selectedFragment)
        transaction.commit()
    }

    private val myOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_home -> selectItem(0)
                R.id.menu_item_people -> selectItem(1)
                R.id.menu_item_jobs -> selectItem(2)
                R.id.menu_item_activity -> selectItem(3)
                R.id.menu_item_profile -> selectItem(4)
            }
            return@OnNavigationItemSelectedListener true
        }

    fun showDialogProfileComplete(boolean: Boolean) {
        if (boolean) {
            val dialog = CompletedProfileDialogFragment.newInstance()
            dialog.show(supportFragmentManager, null)
        }
    }

    private fun setupBottomNavigationView() {

        bottom_nav_view.apply {
            val typeface = ResourcesCompat.getFont(this@HomeActivity, R.font.avenir_medium)
            setTypeface(typeface)
            isItemHorizontalTranslationEnabled = false
            itemIconTintList = null

            // To fix bug that large labels dissapears on menu
            for (i in 0 until itemCount) {
                val view = getLargeLabelAt(i)
                view.setPadding(0, 0, 0, 0)
            }

            onNavigationItemSelectedListener = myOnNavigationItemSelectedListener
        }

        Glide
            .with(this)
            .load(viewModel.getProfilePictureUrl())
            .apply(RequestOptions().circleCrop())
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val states = StateListDrawable()
                    states.addState(intArrayOf(android.R.attr.state_pressed), resource)
                    states.addState(intArrayOf(android.R.attr.state_checked), resource)
                    states.addState(intArrayOf(), resource)
                    val menu = bottom_nav_view.menu
                    val te = menu.findItem(R.id.menu_item_profile)
                    te.icon = states
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!logoutDisposable.isDisposed) logoutDisposable.dispose()
        logoutUseCase.cancel()
    }

    private fun showUnauthorizedUser() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.unauthorized_user),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener {
            logout()
        }
        customOneBtnDialogFragment.show(supportFragmentManager, null)
    }

    fun logout() {
        include_loading.visibility = View.VISIBLE
        logoutUseCase.execute(::onFinish)
    }

    private fun onFinish() {
        include_loading.visibility = View.GONE
        navigator.navigateToEnterEmailActivity(this)
    }
}
