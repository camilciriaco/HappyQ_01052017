package com.happyq.rvn.happyq.fragment.listing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.happyq.rvn.happyq.fragment.MainFragment;
import com.happyq.rvn.happyq.fragment.TabFragment;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;
    //detector = new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener());

    public interface OnItemClickListener{
        public String onItemClick(View view, int position);
    }


    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(child, rv.getChildPosition(child));

            Log.d("fpp", child.getClass().getName() + " is clicked");

            return false;
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }


//    @Override
//    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//        View childView = rv.findChildViewUnder(e.getX(),e.getY());
//        if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
//
//
//            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
