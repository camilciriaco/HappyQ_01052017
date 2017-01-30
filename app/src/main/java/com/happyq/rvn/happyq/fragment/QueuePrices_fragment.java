package com.happyq.rvn.happyq.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.happyq.rvn.happyq.R;

/**
 * Created by RVN on 1/3/2017.
 */
public class QueuePrices_fragment extends Fragment{
RelativeLayout relativeLayouta;
    Button addcredit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_queueprices, container, false);

        //linearaa = (LinearLayout) view.findViewById(R.id.linearlayouts);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //recyclerview.setLayoutManager(layoutManager);

        return view;
    }

}
