package com.divercity.app.core.base

import android.os.Bundle
import com.divercity.app.R
import com.divercity.app.Session
import com.divercity.app.core.bus.RxBus
import com.divercity.app.core.bus.RxEvent
import com.divercity.app.core.navigation.Navigator
import com.divercity.app.core.ui.IOnBackPressed
import com.divercity.app.features.dialogs.CustomOneBtnDialogFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseActivity : DaggerAppCompatActivity() {

    private lateinit var logoutDisposable: Disposable

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        addFragment(savedInstanceState)

        logoutDisposable = RxBus.listen(RxEvent.EventUnauthorizedUser::class.java).subscribe {
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

    override fun onDestroy() {
        super.onDestroy()
        if (!logoutDisposable.isDisposed) logoutDisposable.dispose()
    }

    private fun showUnauthorizedUser() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.unauthorized_user),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener {
            session.logout()
            navigator.navigateToEnterEmailActivity(this)
        }
        customOneBtnDialogFragment.show(supportFragmentManager, null)
    }
}