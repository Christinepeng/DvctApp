package com.divercity.android.core.base

import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.features.usecase.LogoutUseCase
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.navigation.Navigator
import com.divercity.android.core.ui.IOnBackPressed
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseActivity : DaggerAppCompatActivity() {

    private lateinit var unauthorizedDisposable: Disposable

    @Inject
    lateinit var session: LogoutUseCase

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        addFragment(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        unauthorizedDisposable = RxBus.listen(RxEvent.EventUnauthorizedUser::class.java).subscribe {
            showUnauthorizedUser()
        }
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    private fun addFragment(savedInstanceState: Bundle?) {
        savedInstanceState
            ?: supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment()).commit()
    }

    protected abstract fun fragment(): BaseFragment

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.fragment_container)
        (fragment as? IOnBackPressed)?.onBackPressed()
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        if (!unauthorizedDisposable.isDisposed) unauthorizedDisposable.dispose()
    }

    private fun showUnauthorizedUser() {
        Timber.v("On showUnauthorizedUser")
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

    fun logout(){
        include_loading.visibility = View.VISIBLE
        session.execute(::onFinish)
    }

    private fun onFinish(){
        include_loading.visibility = View.GONE
        navigator.navigateToEnterEmailActivity(this)
    }
}