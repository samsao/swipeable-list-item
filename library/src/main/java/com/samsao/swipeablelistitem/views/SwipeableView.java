package com.samsao.swipeablelistitem.views;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.samsao.swipeablelistitem.R;


/**
 * Created by lcampos on 2015-11-05.
 */
public abstract class SwipeableView extends FrameLayout {

    private final static float SWIPE_RELEASE_THRESHOLD = 370;
    private final static int ANIMATION_DURATION = 150;

    /**
     * The view's content, what will be swiped
     */
    protected View mContent;

    private TextView mLeftTextView;
    private TextView mRightTextView;
    private Drawable mLeftBackground;
    private Drawable mRightBackground;
    private int[] mLeftPadding;
    private int[] mRightPadding;

    /**
     * The views under the content, to be shown when this one is swiped
     */
    protected View mLeftView;
    protected View mRightView;

    private LeftSwipeListener mLeftSwipeListener;
    private RightSwipeListener mRightSwipeListener;
    private ClickListener mClickListener;

    public SwipeableView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context);
        }
    }

    public SwipeableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context);
        }
    }

    public SwipeableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context);
        }
    }

    @TargetApi(21)
    public SwipeableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (!isInEditMode()) {
            init(context);
        }
    }

    /**
     * Init the view
     *
     * @param context
     */
    private void init(Context context) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContent = getContent(this);

        mLeftTextView = new TextView(context);
        mRightTextView = new TextView(context);
        mLeftView = mLeftTextView;
        mRightView = mRightTextView;
        setupLeftRightViews();
        addView(mContent);

        mRightTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        mLeftTextView.setGravity(Gravity.CENTER_VERTICAL);
        setLeftSwipePadding((int) getResources().getDimension(R.dimen.default_spacing), 0, 0, 0);
        setRightSwipePadding(0, 0, (int) getResources().getDimension(R.dimen.default_spacing), 0);

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onClick();
                }
            }
        });
    }

    /**
     * Add the views to the layout
     */
    private void setupLeftRightViews() {
        if (mLeftView.getParent() == null) {
            if (getChildCount() > 0) {
                removeViewAt(0);
            }
            addView(mLeftView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mRightView.getParent() == null) {
            if (getChildCount() > 1) {
                removeViewAt(1);
            }
            addView(mRightView, 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        mContent.setVisibility(VISIBLE);
        mLeftView.setVisibility(GONE);
        mRightView.setVisibility(GONE);
    }

    public void onSwipe(float delta) {
        if ((mContent.getX() + delta) >= 0) {
            mLeftView.setVisibility(VISIBLE);
            mRightView.setVisibility(GONE);
        } else {
            mLeftView.setVisibility(GONE);
            mRightView.setVisibility(VISIBLE);
        }
        if (mLeftSwipeListener != null && mRightSwipeListener == null) {
            if (delta >= 0) {
                mContent.setX(0);
            } else {
                mContent.setTranslationX(delta);
            }
        } else if (mRightSwipeListener != null && mLeftSwipeListener == null) {
            if (delta < 0) {
                mContent.setX(0);
            } else {
                mContent.setTranslationX(delta);
            }
        } else {
            mContent.setTranslationX(delta);
        }

    }

    public void onSwipeRelease(float delta) {
        if (delta >= 0) {
            if (mRightSwipeListener != null) {
                if (Math.abs(delta) < SWIPE_RELEASE_THRESHOLD) {
                    resetSwipeWithAnimation();
                } else {
                    rightSwipe(delta);
                }
            }
        } else {
            if (mLeftSwipeListener != null) {
                if (Math.abs(delta) < SWIPE_RELEASE_THRESHOLD) {
                    resetSwipeWithAnimation();
                } else {
                    leftSwipe(delta);
                }
            }
        }
    }

    protected void rightSwipe(float delta) {
        if (mRightSwipeListener != null) {
            mContent.animate().translationXBy(getWidth() - delta)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (mRightSwipeListener != null) {
                                mRightSwipeListener.onRightSwipe();
                            }
                            mContent.animate().setListener(null);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
        }
    }

    protected void leftSwipe(float delta) {
        if (mLeftSwipeListener != null) {
            mContent.animate().translationXBy(-getWidth() - delta)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (mLeftSwipeListener != null) {
                                mLeftSwipeListener.onLeftSwipe();
                            }
                            mContent.animate().setListener(null);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {
                        }
                    });
        }
    }


    ///////////////////
    // FUNCTIONALITY //
    ///////////////////

    /**
     * Unswipe view with animation
     */
    public void resetSwipeWithAnimation() {
        mContent.animate().translationXBy(-mContent.getX())
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(ANIMATION_DURATION);
    }

    /**
     * Unswipe view with no animation
     */
    public void resetSwipe() {
        mContent.setTranslationX(0);
    }

    public void setLeftSwipeListener(LeftSwipeListener leftSwipeListener) {
        mLeftSwipeListener = leftSwipeListener;
    }

    public void setRightSwipeListener(RightSwipeListener rightSwipeListener) {
        mRightSwipeListener = rightSwipeListener;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onClick();
    }

    public interface LeftSwipeListener {
        void onLeftSwipe();
    }

    public interface RightSwipeListener {
        void onRightSwipe();
    }

    /**
     * @return the layout resource
     */
    protected abstract View getContent(ViewGroup parent);


    ///////////////////
    // CUSTOMIZATION //
    ///////////////////

    /**
     * Changes the background of the view that shows when swiping left (if not custom)
     *
     * @param drawable
     */
    public void setRightSwipeBackground(Drawable drawable) {
        setViewBackground(mRightView, drawable);
        mRightBackground = drawable;
    }

    /**
     * Changes the background of the view that shows when swiping right (if not custom)
     *
     * @param drawable
     */
    public void setLeftSwipeBackground(Drawable drawable) {
        setViewBackground(mLeftView, drawable);
        mLeftBackground = drawable;
    }

    /**
     * Sets the padding of the text or icon of the view that shows when swiping right
     *
     * @param left   padding
     * @param top    padding
     * @param right  padding
     * @param bottom padding
     */
    public void setLeftSwipePadding(int left, int top, int right, int bottom) {
        mLeftPadding = new int[]{left, top, right, bottom};
        mLeftView.setPadding(left, top, right, bottom);
    }

    /**
     * Sets the padding of the text or icon of the view that shows when swiping left
     *
     * @param left   padding
     * @param top    padding
     * @param right  padding
     * @param bottom padding
     */
    public void setRightSwipePadding(int left, int top, int right, int bottom) {
        mRightPadding = new int[]{left, top, right, bottom};
        mRightView.setPadding(left, top, right, bottom);
    }

    /**
     * Sets a custom view that shows when swiping to right
     *
     * @param leftCustomView
     */
    public void setLeftCustomView(View leftCustomView) {
        mLeftView = leftCustomView;
        setupLeftRightViews();
    }

    /**
     * Sets a custom view that shows when swiping left
     *
     * @param rightCustomView
     */
    public void setRightCustomView(View rightCustomView) {
        mRightView = rightCustomView;
        setupLeftRightViews();
    }

    /**
     * Sets an icon to be shown when swiping left
     *
     * @param leftDrawable
     */
    public void setRightDrawable(Drawable leftDrawable) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(params);
        frameLayout.addView(imageView);
        imageView.setImageDrawable(leftDrawable);
        mRightView = frameLayout;
        setViewBackground(mRightView, mRightBackground);
        mRightView.setPadding(mRightPadding[0], mRightPadding[1], mRightPadding[2], mRightPadding[3]);
        setupLeftRightViews();
    }

    /**
     * Sets an icon to be shown when swiping right
     *
     * @param leftDrawable
     */
    public void setLeftDrawable(Drawable leftDrawable) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(params);
        frameLayout.addView(imageView);
        imageView.setImageDrawable(leftDrawable);
        mLeftView = frameLayout;
        setViewBackground(mLeftView, mLeftBackground);
        mLeftView.setPadding(mLeftPadding[0], mLeftPadding[1], mLeftPadding[2], mLeftPadding[3]);
        setupLeftRightViews();
    }

    /**
     * Sets a text to be shown when swiping right
     *
     * @param text
     */
    public void setLeftSwipeText(String text) {
        mLeftTextView.setText(text);
        mLeftView = mLeftTextView;
        setupLeftRightViews();
    }

    /**
     * Sets a text to be shown when swiping left
     *
     * @param text
     */
    public void setRightSwipeText(String text) {
        mRightTextView.setText(text);
        mRightView = mRightTextView;
        setupLeftRightViews();
    }

    /**
     * Sets the text color of the text to be shown when swiping right
     *
     * @param textColor
     */
    public void setLeftSwipeTextColor(@ColorInt int textColor) {
        mLeftTextView.setTextColor(textColor);
    }

    /**
     * Sets the text color of the text to be shown when swiping left
     *
     * @param textColor
     */
    public void setRightSwipeTextColor(@ColorInt int textColor) {
        mRightTextView.setTextColor(textColor);
    }

    /**
     * Sets the text size of the text to be shown when swiping right
     *
     * @param textSize
     */
    public void setLeftSwipeTextSize(int textSize) {
        mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * Sets the text size of the text to be shown when swiping left
     *
     * @param textSize
     */
    public void setRightSwipeTextSize(int textSize) {
        mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * Sets the text typeface of the text to be shown when swiping left
     *
     * @param typeface
     */
    public void setRightSwipeTextTypeface(Typeface typeface) {
        mRightTextView.setTypeface(typeface);
    }

    /**
     * Sets the text typeface of the text to be shown when swiping right
     *
     * @param typeface
     */
    public void setLeftSwipeTextTypeface(Typeface typeface) {
        mLeftTextView.setTypeface(typeface);
    }

    private void setViewBackground(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
