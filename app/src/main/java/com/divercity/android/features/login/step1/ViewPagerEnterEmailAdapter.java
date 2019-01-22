package com.divercity.android.features.login.step1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.divercity.android.R;

import javax.inject.Inject;

/**
 * Created by lucas on 19/02/2018.
 */

public class ViewPagerEnterEmailAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] imgs = {
            R.drawable.onboarding1,
            R.drawable.onboarding2,
    };

    @Inject
    public ViewPagerEnterEmailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        int item = imgs[position];
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_pager_onboarding, null);
        ImageView imageView = view.findViewById(R.id.img_promo);
        imageView.setImageDrawable(context.getResources().getDrawable(item));

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}