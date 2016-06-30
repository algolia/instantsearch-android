package com.algolia.instantsearch.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.algolia.instantsearch.R;

/**
 * Helper to set onItem[Long]Click listeners on a RecyclerView. <br />
 * <a href="http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/">Initially made by Hugo Visser</a> <br />
 * See (un)license at <a href="http://unlicense.org/">unlicense.org</a> <br />
 */
public class ItemClickSupport {
    private final RecyclerView mRecyclerView;

    private OnItemClickListener mOnItemClickListener;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClick(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private OnItemLongClickListener mOnItemLongClickListener;
    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClick(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    private final RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnItemClickListener != null) {
                view.setOnClickListener(mOnClickListener);
            }
            if (mOnItemLongClickListener != null) {
                view.setOnLongClickListener(mOnLongClickListener);
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {

        }
    };

    private ItemClickSupport(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support, this);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    public static ItemClickSupport removeFrom(RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support != null) {
            support.detach(view);
        }
        return support;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    /**
     * Interface definition for a callback to be invoked when an item has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this view has been clicked.
         *
         * @param recyclerView The {@link RecyclerView} where the click happened.
         * @param v            The view within the RecyclerView that was clicked.
         * @param position     The position of the view in the associated adapter.
         */
        void onItemClick(RecyclerView recyclerView, int position, View v);
    }

    /**
     * Interface definition for a callback to be invoked when an item has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * Callback method to be invoked when an item in this view has been clicked and held.
         *
         * @param recyclerView The {@link RecyclerView} where the click happened.
         * @param v            The view within the RecyclerView that was clicked.
         * @param position     The position of the view in the associated adapter.
         * @return {@code true} if the callback consumed the long click, {@code false} otherwise.
         */
        boolean onItemLongClick(RecyclerView recyclerView, int position, View v);
    }
}