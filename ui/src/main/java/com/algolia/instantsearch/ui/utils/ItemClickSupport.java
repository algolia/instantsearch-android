/*
 * Initially made by Hugo Visser: http://www.littlerobots.nl/blog/Handle-Android-RecyclerView-Clicks/
 * See (un)license at unlicense.org
 */
package com.algolia.instantsearch.ui.utils;

import android.view.View;

import com.algolia.instantsearch.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Sets onItem[Long]Click listeners on a RecyclerView.
 */
@SuppressWarnings("UnusedReturnValue") //chaining
public class ItemClickSupport {
    private final RecyclerView mRecyclerView;

    @Nullable
    private OnItemClickListener mOnItemClickListener;
    @Nullable
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(@NonNull View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClick(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private OnItemLongClickListener mOnItemLongClickListener;
    @Nullable
    private final View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(@NonNull View v) {
            if (mOnItemLongClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                return mOnItemLongClickListener.onItemLongClick(mRecyclerView, holder.getAdapterPosition(), v);
            }
            return false;
        }
    };

    @Nullable
    private final RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
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

    @Deprecated // Should not be used by library users
    @NonNull public static ItemClickSupport addTo(@NonNull RecyclerView view) {
        ItemClickSupport support = (ItemClickSupport) view.getTag(R.id.item_click_support);
        if (support == null) {
            support = new ItemClickSupport(view);
        }
        return support;
    }

    /**
     * Sets an {@link OnItemClickListener}, eventually replacing the previous one.
     *
     * @param listener the listener.
     * @return this {@link ItemClickSupport} for chaining.
     */
    @NonNull public ItemClickSupport setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    /**
     * Sets an {@link OnItemLongClickListener}, eventually replacing the previous one.
     *
     * @param listener the listener.
     * @return this {@link ItemClickSupport} for chaining.
     */
    @NonNull public ItemClickSupport setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
        return this;
    }

    private void detach(@NonNull RecyclerView view) {
        view.removeOnChildAttachStateChangeListener(mAttachListener);
        view.setTag(R.id.item_click_support, null);
    }

    /**
     * Invokes a callback when an item has been clicked.
     */
    public interface OnItemClickListener {
        /**
         * Reacts when an item in this view has been clicked.
         *
         * @param recyclerView The {@link RecyclerView} where the click happened.
         * @param v            The view within the RecyclerView that was clicked.
         * @param position     The position of the view in the associated adapter.
         */
        void onItemClick(RecyclerView recyclerView, int position, View v);
    }

    /**
     * Invokes a callback when an item has been clicked and held.
     */
    public interface OnItemLongClickListener {
        /**
         * Reacts when an item in this view has been clicked and held.
         *
         * @param recyclerView The {@link RecyclerView} where the click happened.
         * @param v            The view within the RecyclerView that was clicked.
         * @param position     The position of the view in the associated adapter.
         * @return {@code true} if the callback consumed the long click, {@code false} otherwise.
         */
        boolean onItemLongClick(RecyclerView recyclerView, int position, View v);
    }
}