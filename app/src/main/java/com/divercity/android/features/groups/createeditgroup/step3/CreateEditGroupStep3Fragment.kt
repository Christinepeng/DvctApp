package com.divercity.android.features.groups.createeditgroup.step3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.fragment_group_description.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 25/10/2018.
 */

class CreateEditGroupStep3Fragment : BaseFragment() {

    lateinit var viewModel: CreateEditGroupStep3ViewModel

    private var group: GroupResponse? = null

    private var isEditing: Boolean = false

    companion object {
        private const val PARAM_GROUP_NAME = "paramGroupName"
        private const val PARAM_GROUP_PHOTO = "paramFilePhoto"
        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(
            groupName: String,
            photoPath: String?,
            group: GroupResponse?
        ): CreateEditGroupStep3Fragment {
            val fragment = CreateEditGroupStep3Fragment()
            val arguments = Bundle()
            arguments.putString(PARAM_GROUP_NAME, groupName)
            arguments.putString(PARAM_GROUP_PHOTO, photoPath)
            arguments.putParcelable(PARAM_GROUP, group)

            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_group_description

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        group = arguments?.getParcelable(PARAM_GROUP)
        isEditing = group != null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[CreateEditGroupStep3ViewModel::class.java]

        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(if (isEditing) R.string.edit_group else R.string.create_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.createEditGroupResponse.observe(this, Observer { response ->
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
        if (isEditing) {
            if (group!!.isPublic()) {
                btn_select_unselect.isSelected = false
                showPublic()
            } else {
                btn_select_unselect.isSelected = true
                showPrivate()
            }
            et_group_description.setText(group!!.attributes.description)
            et_group_description.setSelection(group?.attributes?.description?.length ?: 0)
            btn_create_edit_group.setText(R.string.save)
        } else {
            showPublic()
            btn_create_edit_group.setText(R.string.create)
        }

        checkFormIsCompleted()

        btn_select_unselect.setOnClickListener {
            btn_select_unselect.isSelected = !btn_select_unselect.isSelected
            if (btn_select_unselect.isSelected) {
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
        btn_create_edit_group.isEnabled = boolean
        if (boolean) {
            btn_create_edit_group.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            btn_create_edit_group.setOnClickListener {

                if (isEditing)
                    viewModel.editGroup(
                        group!!,
                        arguments!!.getString(PARAM_GROUP_NAME)!!,
                        et_group_description.text.toString(),
                        btn_select_unselect.isSelected,
                        arguments!!.getString(PARAM_GROUP_PHOTO)
                    )
                else
                    viewModel.createGroup(
                        arguments!!.getString(PARAM_GROUP_NAME)!!,
                        et_group_description.text.toString(),
                        btn_select_unselect.isSelected,
                        arguments!!.getString(PARAM_GROUP_PHOTO)
                    )
            }
        } else
            btn_create_edit_group.setTextColor(
                ContextCompat.getColor(
                    activity!!,
                    R.color.whiteDisable
                )
            )
    }

    private fun showPublic() {
        img_lock_unlock.setImageDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.img_unlock
            )
        )
        txt_type.setText(R.string.public_group)
        txt_type_description.setText(R.string.public_group_desc)
    }

    private fun showPrivate() {
        img_lock_unlock.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.img_lock))
        txt_type.setText(R.string.private_group)
        txt_type_description.setText(R.string.private_group_desc)
    }
}