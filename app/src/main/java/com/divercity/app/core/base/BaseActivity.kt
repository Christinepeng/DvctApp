package com.divercity.app.core.base

import android.os.Bundle
import com.divercity.app.R
import com.divercity.app.core.ui.IOnBackPressed
import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)
        addFragment(savedInstanceState)
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
}