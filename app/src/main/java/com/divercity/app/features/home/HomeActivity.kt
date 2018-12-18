package com.divercity.app.features.home

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.divercity.app.R
import com.divercity.app.features.dialogs.CompletedProfileDialogFragment
import com.divercity.app.features.groups.TabGroupsFragment
import com.divercity.app.features.home.home.HomeFragment
import com.divercity.app.features.home.notifications.NotificationsFragment
import com.divercity.app.features.jobs.TabJobsFragment
import com.divercity.app.features.profile.ProfileFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: HomeActivityViewModel

    companion object {
        private const val INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE = "showDialogProfile"

        fun getCallingIntent(context: Context, showDialogProfileCompleted: Boolean): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE, showDialogProfileCompleted)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeActivityViewModel::class.java)
        setContentView(R.layout.activity_home)
        showDialogProfileComplete(intent.getBooleanExtra(INTENT_EXTRA_PARAM_SHOW_DIALOG_PROFILE, false))
        setupBottomNavigationView()
        savedInstanceState ?: selectItem(0)
        setSupportActionBar(include_toolbar.toolbar)
    }

    /**
     * Fragment creation according to tab position
     */
    fun selectItem(position: Int) {
        var selectedFragment: Fragment = HomeFragment.newInstance()
        when (position) {
            0 -> selectedFragment = HomeFragment.newInstance()
            1 -> selectedFragment = TabGroupsFragment.newInstance()
            2 -> selectedFragment = TabJobsFragment.newInstance()
            3 -> selectedFragment = NotificationsFragment.newInstance()
            4 -> selectedFragment = ProfileFragment.newInstance()
        }
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, selectedFragment)
        transaction.commit()
    }

    private val myOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_item_home -> selectItem(0)
                    R.id.menu_item_groups -> selectItem(1)
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

}
