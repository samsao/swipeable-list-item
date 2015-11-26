package com.samsao.swipeablelistitem;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samsao.swipeablelistitem.model.Fruit;
import com.samsao.swipeablelstitem.views.SwipeableView;

/**
 * Created by lcampos on 2015-11-23.
 */
public class ListItem extends SwipeableView {

    private TextView mFruitNameTextView;
    private View mLayout;

    public ListItem(Context context) {
        super(context);

        /**
         * All your customization here
         */
        setRightSwipeBackground(ContextCompat.getDrawable(getContext(), android.R.color.holo_red_light));
        setRightSwipeText("delete");
        setRightSwipeTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        setRightSwipeTextSize((int) getResources().getDimension(R.dimen.font_size));
    }

    @Override
    public View getContent(ViewGroup parent) {
        mLayout = LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, this, false);
        mFruitNameTextView = (TextView) mLayout.findViewById(R.id.fruit_name);
        return mLayout;
    }

    public void setup(Fruit fruit, LeftSwipeListener swipeListener) {
        setLeftSwipeListener(swipeListener);

        mFruitNameTextView.setText(fruit.getName());
        mLayout.setBackgroundColor(fruit.getColor());
    }
}
