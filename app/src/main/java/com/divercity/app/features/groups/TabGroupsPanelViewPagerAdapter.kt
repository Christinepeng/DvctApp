package com.divercity.app.features.groups

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.job.response.JobResponse
import javax.inject.Inject

/**
 * Created by lucas on 19/02/2018.
 */

class TabGroupsPanelViewPagerAdapter @Inject
constructor(private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private var list: List<JobResponse>? = null

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    fun setList(list: List<JobResponse>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
       val item = list?.let {
           it[position]
       }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.view_pager_panel_group, null)

//        item?.also {
//            try {
//                view.img.visibility = View.VISIBLE
//                val urlMain = data.attributes.pictureMain
//                GlideApp.with(itemView.context)
//                    .load(urlMain)
//                    .apply(RequestOptions().transforms(RoundedCorners(16)))
//                    .into(itemView.item_group_img)
//            } catch (e: NullPointerException) {
//                itemView.item_group_img.visibility = View.GONE
//            }
//
//            itemView.item_group_txt_title.setText(data.attributes.title)
//            itemView.item_group_txt_members.setText(data.attributes.followersCount.toString() + " Members")
//
//            if (data.attributes.isIsFollowedByCurrent) {
//                itemView.item_group_btn_join_member.setOnClickListener(null)
//                itemView.item_group_btn_join_member.setBackgroundResource(R.drawable.bk_white_stroke_blue_rounded)
//                itemView.item_group_btn_join_member.setTextColor(itemView.context.resources.getColor(R.color.appBlue))
//                itemView.item_group_btn_join_member.setText("Member")
//            } else {
//                itemView.item_group_btn_join_member.setOnClickListener { listener?.onGroupJoinClick(position, data) }
//                itemView.item_group_btn_join_member.setBackgroundResource(R.drawable.shape_backgrd_round_blue3)
//                itemView.item_group_btn_join_member.setTextColor(itemView.context.resources.getColor(android.R.color.white))
//                itemView.item_group_btn_join_member.setText("Join")
//            }
//            itemView.item_group_btn_join_member.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
//        }

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val vp = container as ViewPager
        val view = any as View
        vp.removeView(view)
    }
}