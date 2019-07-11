package com.divercity.android.features.picturessearch

import android.app.Activity
import android.content.Intent
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
import androidx.recyclerview.widget.GridLayoutManager
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_group_detail.include_toolbar
import kotlinx.android.synthetic.main.fragment_suggested_pictures.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class PictureSearchFragment : BaseFragment() {

    @Inject
    lateinit var adapter: PictureAdapter

    lateinit var viewModel: PictureSearchViewModel

    private var handlerSearch = Handler()

    companion object {

        const val PICTURE_PICKED = "picturePicked"

        fun newInstance(): PictureSearchFragment {
            return PictureSearchFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_suggested_pictures

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(PictureSearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupToolbar()
        setupSearch()
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.onPictureSelected = {
            val intent = Intent()
            intent.putExtra(PICTURE_PICKED, it.imageUrlsBySize?.regular)
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = adapter
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.search_picture_online)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.searchPicturesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    txt_no_results.visibility = View.GONE
                    txt_search.visibility = View.GONE
                    showProgressNoBk()
                }

                Status.ERROR -> {
                    hideProgressNoBk()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    if(response.data!!.isEmpty())
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                    adapter.list = response.data
                    hideProgressNoBk()
                }
            }
        })
    }

    private fun setupSearch() {
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
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            query?.let {
                viewModel.searchPictures(it)
            }
        }, AppConstants.SEARCH_DELAY)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}