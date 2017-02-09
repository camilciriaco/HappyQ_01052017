package com.happyq.rvn.happyq.fragment.listing;

/**
 * Created by RVN on 1/18/2017.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;

import java.util.List;

public class AdapterRVQueue extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataQueue> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtquename, txtqueueid, tvqueueactivity, tvqueuestatus, tvqueuemaxreserve;



        public ViewHolder(View v) {
            super(v);
            txtquename = (TextView) v.findViewById(R.id.tvqueuename);
            tvqueueactivity = (TextView) v.findViewById(R.id.queuactivity);
            tvqueuestatus = (TextView) v.findViewById(R.id.queuestatus);
            tvqueuemaxreserve = (TextView) v.findViewById(R.id.queuamaxreserve);

        }



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRVQueue(List<DataQueue> myDataset) {
        this.mDataset = myDataset;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_queue_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder myHolder = (ViewHolder) holder;
        DataQueue current = mDataset.get(position);
        myHolder.txtquename.setText(current.queuename);
        myHolder.tvqueueactivity.setText(current.queueactivity);
        myHolder.tvqueuestatus.setText(current.queuestatus);
        myHolder.tvqueuemaxreserve.setText(current.queuemaxreserve);



    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void clear() {
        int size = this.mDataset.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mDataset.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    }