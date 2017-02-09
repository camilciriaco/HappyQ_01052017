package com.happyq.rvn.happyq.fragment.listing;

/**
 * Created by RVN on 1/18/2017.
 */
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AdapterFavorites extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DataQueue> mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtquename, txtqueueid, tvqueueactivity, tvqueuestatus, tvqueuemaxreserve;
        public TextView favoritetitle, count;
        public ImageView overflow;



        public ViewHolder(View v) {
            super(v);
            favoritetitle = (TextView) v.findViewById(R.id.title);
            count = (TextView) v.findViewById(R.id.count);
            overflow = (ImageView) v.findViewById(R.id.overflow);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterFavorites(List<DataQueue> myDataset) {
        this.mDataset = myDataset;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder myHolder = (ViewHolder) holder;
        DataQueue current = mDataset.get(position);
        myHolder.favoritetitle.setText(current.queuename);
        myHolder.count.setText(current.queueactivity);

        myHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "click!", Toast.LENGTH_LONG).show();
                showPopupMenu(myHolder.overflow);

            }
        });
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

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, popup.getMenu());
        popup.setOnMenuItemClickListener(new AdapterFavorites.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove_favorite:
                    Toast.makeText(mContext, "Successfully removed", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_proceed:
                    Toast.makeText(mContext, "Proceed", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}