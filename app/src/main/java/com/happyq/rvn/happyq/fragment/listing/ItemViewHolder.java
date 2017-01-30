package com.happyq.rvn.happyq.fragment.listing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name_TextView;
    public TextView iso_TextView;



    public ItemViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        name_TextView = (TextView) itemView.findViewById(R.id.reservename);
        iso_TextView = (TextView) itemView.findViewById(R.id.reservedate);

    }

    public void bind(CountryModel countryModel) {
        name_TextView.setText(countryModel.getName());
        iso_TextView.setText(countryModel.getisoCode());

    }


}
