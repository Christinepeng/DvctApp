package com.divercity.app.features.groups;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.divercity.app.R;

/**
 * Created by lucas on 21/02/2018.
 */

public class TabGroupsPanelDotsViewPager {

    ImageView[] mDots;
    int mDotsCount;
    Context mContext;

    public TabGroupsPanelDotsViewPager(Context context, int count, LinearLayout layDotsPanel){
        mContext = context;
        mDotsCount = count;
        mDots = new ImageView[mDotsCount];

        for (int i = 0; i < mDotsCount; i++) {
            mDots[i] = new ImageView(context);
            mDots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_nonactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);

            layDotsPanel.addView(mDots[i], params);
        }

        mDots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dot_active));
    }

    public void onPageSelected(int position) {
        for (int i = 0; i < mDotsCount; i++)
            mDots[i].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.dot_nonactive));

        mDots[position].setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.dot_active));
    }

}
