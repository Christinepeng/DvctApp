package com.divercity.android.features.login.step1;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
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
            R.drawable.onboarding5,
            R.drawable.onboarding6,
            R.drawable.onboarding7,
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