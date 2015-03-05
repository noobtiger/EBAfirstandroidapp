package eba.bodyloger;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by c-cpinnama on 2/11/2015.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Home", "Logger", "Profile" };
    private Context context;

        public MyFragmentPageAdapter(FragmentManager fm,Context context) {
            super(fm);
            this.context = context;
        }
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentHome.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    }

