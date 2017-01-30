package com.happyq.rvn.happyq.fragment.listing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by RVN on 1/25/2017.
 */

public class AdapterRegQueue extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataQueue> mDataset;

        public class VHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView tvrquename, tvrqueuestime, tvrqueueetime, tvrqueueoperationday, tvAnnouncement, tvAtitle;



            public VHolder(View v) {
                super(v);
                tvrquename = (TextView) v.findViewById(R.id.tvqueuename);
                tvrqueuestime = (TextView) v.findViewById(R.id.tv_qstime);
                tvrqueueetime = (TextView) v.findViewById(R.id.tv_qetime);
                tvrqueueoperationday = (TextView) v.findViewById(R.id.tv_qoperationday);

                tvAnnouncement = (TextView) v.findViewById(R.id.tv_announcement);
                tvAtitle = (TextView) v.findViewById(R.id.tv_Atitle);
            }
        }

    public AdapterRegQueue(List<DataQueue> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_queueing, parent, false);
        // set the view's size, margins, paddings and layout parameters
        AdapterRegQueue.VHolder vh = new AdapterRegQueue.VHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterRegQueue.VHolder myHolder= (AdapterRegQueue.VHolder) holder;
        DataQueue current=mDataset.get(position);
        //myHolder.tvrquename.setText(current.queuename);
        myHolder.tvrqueuestime.setText(current.rqueuestime);
        myHolder.tvrqueueetime.setText(current.rqueueetime);
        myHolder.tvrqueueoperationday.setText(current.rqueueoperationday);
        myHolder.tvAtitle.setText(current.Atitle);
        myHolder.tvAnnouncement.setText(current.Announcement);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
