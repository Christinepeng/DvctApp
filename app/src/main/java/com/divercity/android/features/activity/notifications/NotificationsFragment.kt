package com.divercity.android.features.activity.notifications

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.features.activity.notifications.adapter.NotificationsAdapter
import com.divercity.android.features.activity.notifications.adapter.NotificationsViewHolder
import kotlinx.android.synthetic.main.fragment_notifications.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class NotificationsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: NotificationsViewModel

    @Inject
    lateinit var adapter: NotificationsAdapter

    private var isListRefreshing = false

    companion object {

        fun newInstance(): NotificationsFragment {
            return NotificationsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_notifications

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(NotificationsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun subscribeToLiveData(){
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun initList() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                viewModel.refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }

//        txt_empty_array.setText(R.string.no_following)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedApplicantsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

            adapter.currentList?.let { pagedList ->

                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_notification.visibility = View.VISIBLE
                else
                    txt_no_notification.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private
    val listener: NotificationsViewHolder.Listener = object : NotificationsViewHolder.Listener {

        override fun onNotificationClick(notification: NotificationResponse) {

        }
    }
}