package com.happyq.rvn.happyq.fragment.tab_profile_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.listing.RVAdapter;
import com.happyq.rvn.happyq.fragment.listing.CountryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by RVN on 1/3/2017.
 */
public class Booking_fragment extends android.support.v4.app.Fragment  implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private List<CountryModel> mCountryModel;
    private RVAdapter adapter;
    private RelativeLayout relativeLayouta;
    LinearLayout linearaa;
    private AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_booking, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        relativeLayouta = (RelativeLayout) view.findViewById(R.id.relativebg);
        linearaa = (LinearLayout) view.findViewById(R.id.linearlayouts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        String[] locales = Locale.getISOCountries();
        mCountryModel = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            mCountryModel.add(new CountryModel(obj.getDisplayCountry(), obj.getISO3Country()));
        }

        adapter = new RVAdapter(mCountryModel);
        recyclerview.setAdapter(adapter);
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {



//        inflater.inflate(R.menu.menu_search, menu);
//
//        final MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(item,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
//                        adapter.setFilter(mCountryModel);
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<CountryModel> filteredModelList = filter(mCountryModel, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<CountryModel> filter(List<CountryModel> models, String query) {
        query = query.toLowerCase();

        final List<CountryModel> filteredModelList = new ArrayList<>();
        for (CountryModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    relativeLayouta.setVisibility(View.VISIBLE);
                } else {
                   // gender = false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        };}


}
