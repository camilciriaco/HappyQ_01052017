package com.happyq.rvn.happyq.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.reservation_query.Now_Query;
import com.happyq.rvn.happyq.reservation_query.QueueToolbar;

/**
 * Created by RVN on 1/3/2017.
 */
public class PurchaseCredit_fragment extends android.support.v4.app.Fragment {
    Button BTbuyCredit, BTbuyGcash, BTbuySmartPadala;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_purchasecredit, container, false);


        BTbuyCredit = (Button) v.findViewById(R.id.CreditButton);
        BTbuyGcash = (Button) v.findViewById(R.id.GcashButton);
        BTbuySmartPadala = (Button) v.findViewById(R.id.smartpadalaButton);

        BTbuyCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open Reserve Now
                Intent openReserve_intent1 = new Intent(getActivity(), Activity_credit_purchasing.class);
                startActivity(openReserve_intent1);
            }
        });

  return v;

}


}
