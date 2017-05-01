package com.example.dell.capston;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lee on 2016-05-06.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 6;
    private String tabTitles[] = new String[] { "공모전","도서관","빈강의실","식단표","버스", "설문" };



    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0)
            return PosterFragment.newInstance(position + 1);

        else if (position==1)
            return LibFragment.newInstance(position + 1);

        else if (position==2)
            return emptyClassFragment.newInstance(position + 1);
        else if(position==3)
            return DietFragment.newInstance(position +1);
        else if(position==4)
            return BusFragment.newInstance(position+1);
        else
            return SurveyFragment.newInstance(position+1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}