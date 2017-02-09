package com.happyq.rvn.happyq.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Booking_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Credits_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Favorites_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Invitecode_fragment;
import com.happyq.rvn.happyq.fragment.tab_profile_fragment.Location_fragment;
import com.happyq.rvn.happyq.sigin.DownloadImage;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.google.android.gms.R.id.toolbar;

public class TabFragment extends Fragment {
    Toolbar toolbar;
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 5 ;
    public static int[] tabIcons = {
            R.drawable.ic_ticket,
            R.drawable.ic_nav_favorites,
            R.drawable.ic_credit,
            R.drawable.ic_marker,
            R.drawable.ic_info_address
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        String name;
        String email;
        String kimageUrl;
        String gFname;
        String gLname;
        String gimgurl;

        View x =  inflater.inflate(R.layout.fragment_profile,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //tabLayout.setupWithViewPager(viewPager);

//        toolbar = (Toolbar) x.findViewById(R.id.toolbar);
//        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Profil</font>"));
        TextView nav_user = (TextView) x.findViewById(R.id.user_name);
        View view2 = (View) x.findViewById(R.id.view2);



        String userneym = getActivity().getIntent().getStringExtra("username");
        nav_user.setText(userneym);

        ImageView imgView = (ImageView) x.findViewById(R.id.user_image);
        imgView.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        //viewing name/username for navigation

        name = getActivity().getIntent().getStringExtra("fbname");
        email = getActivity().getIntent().getStringExtra("fbsurname");
        kimageUrl = getActivity().getIntent().getStringExtra("imageUrl");
        gFname = getActivity().getIntent().getStringExtra("gFname");
        gLname = getActivity().getIntent().getStringExtra("gLname");
        gimgurl = getActivity().getIntent().getStringExtra("gimgurl");

        if (userneym == null && name == null && email==null && kimageUrl==null) {
            view2.setVisibility(View.VISIBLE);
            nav_user.setText(gFname + "\n" + gLname);
            imgView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(gimgurl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) x.findViewById(R.id.user_image));




            } else if (userneym == null && gFname == null && gLname == null && gimgurl == null) {
                view2.setVisibility(View.VISIBLE);
                Bundle inBundle = getActivity().getIntent().getExtras();
                name = inBundle.get("fbname").toString();
                email = inBundle.get("fbsurname").toString();

                kimageUrl = inBundle.get("imageUrl").toString();

                nav_user.setText(name + " " + email);
                imgView.setVisibility(View.VISIBLE);
                new DownloadImage((ImageView) x.findViewById(R.id.user_image)).execute(kimageUrl);

            }


//            /**
//             *Set an Apater for the View Pager
//             */
//        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
//
//        /**
//         * Now , this is a workaround ,
//         * The setupWithViewPager dose't works without the runnable .
//         * Maybe a Support Library Bug .
//         */
//
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
                   }
        });
//

        return x;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new Booking_fragment(), null);
        adapter.addFrag(new Favorites_fragment(), null);
        adapter.addFrag(new Credits_fragment(), null);
        adapter.addFrag(new Location_fragment(), null);
        adapter.addFrag(new Invitecode_fragment(), null);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
//    class MyAdapter extends FragmentPagerAdapter {
//
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        /**
//         * Return fragment with respect to Position .
//         */
//
//        @Override
//        public Fragment getItem(int position)
//        {
//          switch (position){
//              case 0 : return new Booking_fragment();
//              case 1 : return new Favorites_fragment();
//              case 2 : return new Credits_fragment();
//              case 3 : return new Location_fragment();
//              case 4 : return new Invitecode_fragment();
//          }
//        return null;
//        }
//
//        @Override
//        public int getCount() {
//
//            return int_items;
//
//        }
//
//        /**
//         * This method returns the title of the tab according to the position.
//         */
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//
//            switch (position){
//                case 0 :
//                    return "MY booking";
//                case 1 :
//                    return "My Favorites";
//                case 2 :
//                    return "My Credits";
//                case 3 :
//                    return "My Location";
//                case 4 :
//                    return "Invite code";
//
//            }
//                return null;
//        }
//    }

}
