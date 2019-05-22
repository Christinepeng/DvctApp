package com.divercity.android.features.singleuseraction

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.singleuseraction.SingleUserActionActivity.Companion.INTENT_EXTRA_PARAM_JOB_ID
import com.divercity.android.features.singleuseraction.SingleUserActionActivity.Companion.PARAM_ACTION_TYPE
import com.divercity.android.features.singleuseraction.SingleUserActionActivity.Companion.TYPE_ADD_ADMIN
import com.divercity.android.features.singleuseraction.SingleUserActionActivity.Companion.TYPE_SHARE_JOB
import com.divercity.android.features.user.useradapter.charpaginationsinglesel.UserCharPagSingleSelAdapter
import kotlinx.android.synthetic.main.fragment_user_action.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class SingleUserActionFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SingleUserActionViewModel

    @Inject
    lateinit var adapter: UserCharPagSingleSelAdapter

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(data: Bundle?): SingleUserActionFragment {
            val fragment = SingleUserActionFragment()
            fragment.arguments = data
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_user_action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SingleUserActionViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                when (arguments?.get(PARAM_ACTION_TYPE)) {
                    TYPE_ADD_ADMIN -> {
                        it.setTitle(R.string.add_admin)
                    }
                    TYPE_SHARE_JOB -> {
                        it.setTitle(R.string.share_via_message)
                    }
                }
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initView() {
        initAdapter()

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })

        when (arguments?.get(PARAM_ACTION_TYPE)) {
            TYPE_ADD_ADMIN -> {
                btn_action.setText(R.string.add)
                btn_action.setOnClickListener {

                }
            }
            TYPE_SHARE_JOB -> {
                btn_action.setText(R.string.share)
                btn_action.setOnClickListener {
                    val user = adapter.getUserSelected()
                    if (user != null)
                        viewModel.shareJobViaMessage(
                            user.id,
                            arguments?.getString(INTENT_EXTRA_PARAM_JOB_ID)!!
                        )
                    else
                        showToast("Please select one user")
                }
            }
        }

    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        list.adapter = adapter
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToLiveData() {

        viewModel.shareJobViaMessageResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast("Job shared successfully")
                    requireActivity().finish()
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(this, Observer {
            adapter.lastPositionSelected = null
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}