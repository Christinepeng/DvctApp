package com.divercity.app.features.onboarding.selectbirthdate

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.features.dialogs.DatePickerDialogFragment
import kotlinx.android.synthetic.main.fragment_select_birthday.*
import kotlinx.android.synthetic.main.view_header_profile.*

class SelectBirthdayFragment : BaseFragment(), DatePickerDialogFragment.DatePickerDialogListener {

    lateinit var viewModel: SelectBirthdayViewModel

    private var currentProgress: Int = 50

    private var isBirthdaySet: Boolean = false

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectBirthdayFragment {
            val fragment = SelectBirthdayFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_select_birthday

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectBirthdayViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        subscribeToLiveData()

        lay_date.setOnClickListener { showDatePickerDialog() }

        btn_continue.setOnClickListener {
            if (isBirthdaySet)
                navigator.navigateToNextOnboarding(activity!!,
                        viewModel.accountType,
                        currentProgress,
                        true
                )
            else
                showToast("Set your birthday")
        }
    }

    private fun showToast(msg : String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun showDatePickerDialog() {
        val f = DatePickerDialogFragment.newInstance()
        f.show(childFragmentManager, null)
    }

    private fun setupHeader() {
        include_header.apply {
            currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_birthday)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(activity!!,
                        viewModel.accountType,
                        currentProgress,
                        false
                )
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { school ->
            when (school?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    txt_date.text = school.data?.attributes?.birthdate ?: "Error"
                    isBirthdaySet = true
                }
            }
        })
    }

    override fun onDateSetListener(year: String?, month: String?, day: String?) {
        viewModel.updateUserProfile(year, month, day)
    }
}
