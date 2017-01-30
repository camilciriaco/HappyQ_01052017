package com.happyq.rvn.happyq.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.model.MainQList;

import java.util.List;

/**
 * Created by vamsi on 06-May-16.
 */
public class  MainQAdapter extends RecyclerView.Adapter<MainQAdapter.ViewHolder> {

    private List<MainQList> mainqList;

    //Provide a reference to the views for each data item
    //Complex data items may need more than one view per item, and
    //you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        //each data item is just a string in this case
        public TextView tvname, tvtitle, tvnumber;
        public ImageView ivCover;

        public ViewHolder(View v) {
            super(v);
            tvname = (TextView)v.findViewById(R.id.tv_name);
            tvtitle = (TextView) v.findViewById(R.id.tv_title);
            tvnumber = (TextView) v.findViewById(R.id.tv_number);
            //tvYear = (TextView) v.findViewById(R.id.tv_year);
            ivCover = (ImageView) v.findViewById(R.id.iv_cover);
        }
    }

    //Provide a suitable constructor
    public MainQAdapter(List<MainQList> songList){
        this.mainqList = songList;
    }

    //Create new views (invoked by the layout manager)
    @Override
    public MainQAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Creating a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_queueing,parent,false);

        //set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    //Replace the contents of a view (invoked by the layout manager
    @Override
    public void onBindViewHolder(MainQAdapter.ViewHolder holder, int position) {

        // - get element from arraylist at this position
        // - replace the contents of the view with that element

        MainQList song = mainqList.get(position);
        holder.tvname.setText(String.valueOf(song.getRank()));
        holder.tvtitle.setText(song.getName());
        holder.tvnumber.setText(song.getSinger());
        //holder.tvYear.setText(song.getYear());
        holder.ivCover.setImageResource(song.getPic());
        //holder.tvYear.setText("2016");
    }

    @Override
    public int getItemCount() {
        return mainqList.size();
    }
}
