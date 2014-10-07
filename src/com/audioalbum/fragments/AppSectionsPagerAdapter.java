package com.audioalbum.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {
	public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new NewsFragment();
            case 1:
            	return new PopularFragment();
            case 2:
            	return new FriendsFragment();
            case 3: 
            	return new MeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
