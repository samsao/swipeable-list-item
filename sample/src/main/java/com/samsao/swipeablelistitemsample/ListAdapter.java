package com.samsao.swipeablelistitemsample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.samsao.swipeablelistitem.views.SwipeableView;
import com.samsao.swipeablelistitemsample.model.Fruit;

import java.util.List;

/**
 * Created by lcampos on 2015-11-23.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder> {

    private final Context mContext;
    private final List<Fruit> mData;

    public ListAdapter(Context context, List<Fruit> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItem view = new ListItem(mContext);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setup(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        final ListItem mListItem;

        public Holder(ListItem itemView) {
            super(itemView);
            mListItem = itemView;
        }

        public void setup(final Fruit data) {
            mListItem.resetSwipe(); //to avoid problems with cell recycling
            mListItem.setup(data, new SwipeableView.LeftSwipeListener() {
                @Override
                public void onLeftSwipe() {
                    removeItem(data);
                }
            });
        }
    }


    public void removeItem(Fruit fruit) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).equals(fruit)) {
                mData.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
}
