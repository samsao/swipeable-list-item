# Swipeable List Item
A library that allows recycler views with swiping items to right or left for Android.

![Swipe action](/assets/swipingGif.gif)

## Supported Android versions
3.1.x and higher

## Adding to your project

Add the dependency to your build.gradle:

	dependencies {
		compile 'com.samsaodev:swipeablelistitem:1.0.0'
	}

## Usage

1) Create a view to be your RecyclerView's cell and extend it from SwipeableView. Then implement the method 'getContent(ViewGroup parent)' to provide your cell's layout. Your class should look like this:

~~~java
public class ListItem extends SwipeableView {

    public ListItem(Context context) {
        super(context);
    }

    @Override
    public View getContent(ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_list_item, this, false);
    }
}
~~~

2) Add the following line to your RecylerView's setup:

~~~java
recyclerView.addOnItemTouchListener(new SwipeableListOnItemTouchListener());
~~~

3) To avoid problems with your list's cell recycling, call this every time you setup your cell in your ViewHolder class.

~~~java
mListItem.resetSwipe();
~~~

4) Set a listener to your cell to know when the user swiped to left, to right, or clicked on it. Example:

~~~java
listItem.setRightSwipeListener(new RightSwipeListener() {
            @Override
            public void onRightSwipe() {
                //Do something
            }
        });
~~~

If you don't add a swipe listener to a certain direction, it it the same as disabling it. 

### Customization

You can customize the views that show behind your cell when you swipe.

* Change the background

~~~java
setRightSwipeBackground(ContextCompat.getDrawable(getContext(), R.color.red));
~~~   

* Set a label:

~~~java
setRightSwipeText("delete");
setRightSwipeTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
setRightSwipeTextSize((int) getResources().getDimension(R.dimen.font_size));
setRightSwipeTextTypeface(getTypeface());
~~~   

* Set an icon

~~~java
setLeftDrawable(ContextCompat.getDrawable(getContext(), android.R.drawable.ic_delete));
~~~   

* Set a custom view

~~~java
setRightCustomView(LayoutInflater.from(getContext()).inflate(R.layout.custom_view, this, false));
~~~  

## License
SwipeableListItem is released under the MIT license. See the LICENSE file for details.
