package com.divercity.android.features.groups.creategroup.step3

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_group_description.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 25/10/2018.
 */

class GroupDescriptionFragment : BaseFragment() {

    lateinit var viewModel: GroupDescriptionViewModel

    companion object {
        private const val PARAM_GROUP_NAME = "paramGroupName"
        private const val PARAM_GROUP_PHOTO = "paramFilePhoto"

        fun newInstance(groupName: String, photoPath: String?): GroupDescriptionFragment {
            val fragment = GroupDescriptionFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_GROUP_NAME, groupName)
            arguments.putString(PARAM_GROUP_PHOTO, photoPath)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_group_description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[GroupDescriptionViewModel::class.java]
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.createGroupResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToHomeActivityAndClearTop(this)
                }
            }
        })
    }

    private fun setupView() {
        showPublic()
        checkFormIsCompleted()

        btn_public_private.setOnClickListener {
            btn_public_private.isSelected = !btn_public_private.isSelected
            if (btn_public_private.isSelected) {
                showPrivate()
            } else {
                showPublic()
            }
        }

        et_group_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }
        })
    }

    private fun checkFormIsCompleted() {
        if (et_group_description.text.toString() != "")
            enableCreateButton(true)
        else
            enableCreateButton(false)
    }

    private fun enableCreateButton(boolean: Boolean) {
        btn_create_group.isEnabled = boolean
        if (boolean) {
            btn_create_group.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            btn_create_group.setOnClickListener {
                viewModel.createGroup(
                        arguments!!.getString(PARAM_GROUP_NAME)!!,
                        et_group_description.text.toString(),
                        btn_public_private.isSelected,
                        arguments!!.getString(PARAM_GROUP_PHOTO))
            }
        } else
            btn_create_group.setTextColor(
                    ContextCompat.getColor(
                            activity!!,
                            R.color.whiteDisable
                    )
            )
    }

    private fun showPublic() {
        img_lock_unlock.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.img_unlock))
        txt_type.setText(R.string.public_group)
        txt_type_description.setText(R.string.public_group_desc)
    }

    private fun showPrivate() {
        img_lock_unlock.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.img_lock))
        txt_type.setText(R.string.private_group)
        txt_type_description.setText(R.string.private_group_desc)
    }
}