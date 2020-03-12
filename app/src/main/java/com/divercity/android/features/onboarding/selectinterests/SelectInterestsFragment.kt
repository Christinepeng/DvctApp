package com.divercity.android.features.onboarding.selectinterests

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_header_profile.view.*
import javax.inject.Inject

class SelectInterestsFragment : BaseFragment() {

    lateinit var viewModel: SelectInterestsViewModel

    @Inject
    lateinit var adapter: SelectInterestsAdapter

    private var selectedIds: List<String>? = null

    companion object {
        fun newInstance() = SelectInterestsFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
                ViewModelProviders.of(this, viewModelFactory)[SelectInterestsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupView() {
        btn_continue.visibility = View.GONE
        include_search.visibility = View.GONE
        top_btn.visibility = View.VISIBLE
        btn_close.visibility = View.GONE
        btn_skip.visibility = View.GONE

        list.layoutManager = StaggeredGridLayoutManager(3, 1)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun setupHeader() {
        include_header.apply {

            txt_title.setText("\n" +
                "Select your interests to help us suggest\n" +
                    "suitable communities and personalize your feed")
            txt_title.setTextSize(17F)
            txt_title.setLineSpacing(5F,1.4F)

            btn_previous_page.setOnClickListener {
                navigator.navigateToProfessionalInfoActivity(requireActivity())
            }

            btn_next.setOnClickListener {
                viewModel.followInterests(selectedIds!!)
                navigator.navigateToSelectGroupActivity(requireActivity())
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchInterestsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    showErrorDialog(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.setList(response.data)
                }
            }
        })

        viewModel.followInterestsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
//                    navigator.navigateToHomeActivity(requireActivity())
                }
            }
        })
    }

    private fun showErrorDialog(msg : String?) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
                "Ups!",
                msg ?: "Error",
                getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { viewModel.fetchInterests() }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private val listener = SelectInterestsAdapter.Listener {
        selectedIds = it
        if (it.size >= 3) {
            include_header.btn_skip.setText(R.string.done)
        } else {
            include_header.btn_skip.setText(R.string.skip)
        }
    }
}