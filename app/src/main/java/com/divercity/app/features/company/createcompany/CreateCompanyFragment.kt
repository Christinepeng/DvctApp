package com.divercity.app.features.company.createcompany

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.core.utils.ImageUtils
import com.divercity.app.data.Status
import com.divercity.app.data.entity.company.sizes.CompanySizeResponse
import com.divercity.app.data.entity.industry.IndustryResponse
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.company.companysize.CompanySizesFragment
import com.divercity.app.features.industry.withtoolbar.ToolbarIndustryFragment
import com.divercity.app.features.location.withtoolbar.ToolbarLocationFragment
import kotlinx.android.synthetic.main.fragment_create_company.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

/**
 * Created by lucas on 05/11/2018.
 */

class CreateCompanyFragment : BaseFragment() {

    lateinit var viewModel: CreateCompanyViewModel

    private var photoFile: File? = null

    companion object {

        private const val REQUEST_CODE_LOCATION = 150
        private const val REQUEST_CODE_INDUSTRY = 200
        private const val REQUEST_CODE_SIZE = 220

        private const val SAVE_PARAM_FILE = "saveParamFile"

        fun newInstance() = CreateCompanyFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_create_company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CreateCompanyViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            val data = savedInstanceState.getSerializable(SAVE_PARAM_FILE)
            if (data != null) {
                photoFile = data as File
                onPhotosReturned(photoFile)
            }
        }

        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.createCompanyResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    showToast(getString(R.string.company_successfully_created))
                    activity!!.finish()
                }
            }
        })
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_new_company)
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }


    private fun setupView() {
        enableCreateButton(false)

        btn_photo.setOnClickListener {
            EasyImage.openChooserWithGallery(this, "Pick source", 0)
        }

        lay_company_industry.setOnClickListener {
            navigator.navigateToToolbarIndustryActivityForResult(this, REQUEST_CODE_INDUSTRY)
        }

        lay_company_location.setOnClickListener {
            navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION)
        }

        lay_company_size.setOnClickListener {
            navigator.navigateToCompanySizesActivity(this, REQUEST_CODE_SIZE)
        }

        et_company_desc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }

        })

        et_company_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOCATION) {
                val location = data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
                setLocation(location)
            } else if (requestCode == REQUEST_CODE_INDUSTRY) {
                val industry = data?.extras?.getParcelable<IndustryResponse>(ToolbarIndustryFragment.INDUSTRY_PICKED)
                setIndustry(industry)
            } else if (requestCode == REQUEST_CODE_SIZE) {
                val size = data?.extras?.getParcelable<CompanySizeResponse>(CompanySizesFragment.COMPANY_SIZE_PICKED)
                setCompanySize(size)
            }
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : DefaultCallback() {
            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e!!.printStackTrace()
                showToast(e.message)
            }

            override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                onPhotosReturned(imageFiles[0])
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photoFile if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity!!)
                    photoFile?.delete()
                }
            }
        })

        checkFormIsCompleted()
    }

    private fun onPhotosReturned(file: File?) {
        photoFile = file
        GlideApp.with(this)
                .load(file)
                .apply(RequestOptions().circleCrop())
                .into(img_company)
    }

    private fun setLocation(location: LocationResponse?) {
        location?.attributes?.let {
            txt_company_location.text = it.name.plus(", ").plus(it.countryName)
        }
    }

    private fun setIndustry(industry: IndustryResponse?) {
        viewModel.industry = industry
        industry?.attributes?.let {
            txt_company_industry.text = it.name
        }
    }

    private fun setCompanySize(size: CompanySizeResponse?) {
        viewModel.companySize = size
        size?.attributes?.let {
            txt_company_size.text = it.name
        }
    }

    private fun enableCreateButton(boolean: Boolean) {
        btn_create_company.isEnabled = boolean
        if (boolean) {
            btn_create_company.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            btn_create_company.setOnClickListener {
                viewModel.createCompany(et_company_name.text.toString(),
                        viewModel.companySize?.id!!,
                        et_company_desc.text.toString(),
                        txt_company_location.text.toString(),
                        viewModel.industry?.id!!,
                        if (photoFile != null) ImageUtils.getStringBase64(photoFile!!, 400, 400) else "")
            }
        } else
            btn_create_company.setTextColor(ContextCompat.getColor(activity!!, R.color.whiteDisable))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(SAVE_PARAM_FILE, photoFile)
    }

    private fun checkFormIsCompleted() {
        if (et_company_name.text.toString() != "" &&
                txt_company_industry.text != "" &&
                txt_company_location.text != "" &&
                et_company_desc.text.toString() != "" &&
                txt_company_size.text != "")
            enableCreateButton(true)
        else
            enableCreateButton(false)
    }
}