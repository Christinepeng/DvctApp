package com.divercity.android.features.major.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.major.base.SelectMajorFragment
import com.divercity.android.model.Major
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class SelectSingleMajorFragment : BaseFragment(), SelectMajorFragment.Listener {

    lateinit var viewModel: SelectSingleMajorViewModel

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val MAJOR_PICKED = "majorPicked"

        fun newInstance(): SelectSingleMajorFragment {
            return SelectSingleMajorFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectSingleMajorViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction()
            .add(
                R.id.fragment_fragment_container,
                SelectMajorFragment.newInstance()
            ).commit()

        (activity as SelectSingleMajorActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_major)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

//        setupEvents()
        subscribeToLiveData()
    }

//    private fun setupEvents(){
//
//        btn_continue.setOnClickListener {
//            val listStr = adapter.listSelected.toString()
//            viewModel.updateUserProfile(listStr.substring(1, listStr.length - 1))
//        }
//    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToProfessionalInfoActivity(requireActivity())
                }
            }
        })
    }

    override fun onMajorChosen(major: Major) {
        val intent = Intent()
        intent.putExtra(MAJOR_PICKED, major)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}