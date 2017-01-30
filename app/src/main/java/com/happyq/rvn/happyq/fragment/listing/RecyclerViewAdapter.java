package com.happyq.rvn.happyq.fragment.listing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.data.adapter.GetAdapter;

import java.util.ArrayList;

/**
 * Created by RVN on 1/11/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    Context context;

    ArrayList<Object> getAdapter;

    public RecyclerViewAdapter(ArrayList<Object> getAdapter, Context context){

        super();

        this.getAdapter = getAdapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_queue_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        GetAdapter getDataAdapter1 = (GetAdapter) getAdapter.get(position);

        holder.NameTextView.setText(getDataAdapter1.getName());

        holder.IdTextView.setText(String.valueOf(getDataAdapter1.getId()));


    }

    @Override
    public int getItemCount() {

        return getAdapter.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView IdTextView;
        public TextView NameTextView;


        public ViewHolder(View itemView) {

            super(itemView);

            IdTextView = (TextView) itemView.findViewById(R.id.tvqueuename) ;
            //NameTextView = (TextView) itemView.findViewById(R.id.tvqueueid) ;

        }
    }
}
