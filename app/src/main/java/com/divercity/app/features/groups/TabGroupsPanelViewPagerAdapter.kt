package com.divercity.app.features.groups

import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.view_pager_panel_group.view.*
import javax.inject.Inject

/**
 * Created by lucas on 19/02/2018.
 */

class TabGroupsPanelViewPagerAdapter @Inject
constructor() : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private var list: List<GroupResponse>? = null
    var listener: Listener? = null
    lateinit var viewGroup: ViewGroup

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    fun setList(list: List<GroupResponse>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        viewGroup = container
        val item = list?.let {
            it[position]
        }
        layoutInflater = LayoutInflater.from(container.context)
        val view = layoutInflater!!.inflate(R.layout.view_pager_panel_group, null)

        item?.also { item ->
            item.attributes?.also {
                GlideApp.with(container)
                    .load(it.pictureMain)
                    .into(view.img_group)

                view.txt_title.text = it.title
                view.txt_members.text = it.followersCount.toString().plus(" Members")

//                TODO: check if group is private
                if (it.isIsFollowedByCurrent) {
                    view.btn_join.setOnClickListener(null)
                    view.btn_join.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
                    view.btn_join.setTextColor(ContextCompat.getColor(container.context, R.color.appBlue))
                    view.btn_join.text = "Member"
                } else {
                    view.btn_join.setOnClickListener {
                        listener?.onBtnJoinClicked(item.id, position)
                    }
                    view.btn_join.setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
                    view.btn_join.setTextColor(ContextCompat.getColor(container.context, android.R.color.white))
                    view.btn_join.text = "Join"
                }
                view.btn_join.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            }
        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        view.tag = position
        return view
    }

    //    TODO update with group response
    fun updateView(position: Int) {
        val view: View = viewGroup.findViewWithTag(position)

        list?.get(position)?.apply {
            attributes?.also {
                it.isIsFollowedByCurrent = true
                it.followersCount = it.followersCount?.plus(1)

                view.txt_members.text = it.followersCount.toString().plus(" Members")
            }
        }

        view.apply {
            btn_join.setOnClickListener(null)
            btn_join.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
            btn_join.setTextColor(ContextCompat.getColor(view.context, R.color.appBlue))
            btn_join.text = "Member"
        }
    }


    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val vp = container as ViewPager
        val view = any as View
        vp.removeView(view)
    }

    interface Listener {
        fun onBtnJoinClicked(jobId: String?, position: Int)
    }
}