package com.happyq.rvn.happyq.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Booking_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Credits_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Favorites_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Invitecode_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Location_fragment;

public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 5 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */

            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                   }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
          switch (position){
              case 0 : return new Booking_fragment();
              case 1 : return new Favorites_fragment();
              case 2 : return new Credits_fragment();
              case 3 : return new Location_fragment();
              case 4 : return new Invitecode_fragment();
          }
        return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "My Booking";
                case 1 :
                    return "My Favorites";
                case 2 :
                    return "My Credits";
                case 3 :
                    return "My Location";
                case 4 :
                    return "Invite code";

            }
                return null;
        }
    }

}
