package com.samsao.swipeablelistitemsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.samsao.swipeablelistitem.listeners.SwipeableListOnItemTouchListener;
import com.samsao.swipeablelistitemsample.model.Fruit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView config
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ListAdapter listAdapter = new ListAdapter(this, getFruitsList());
        recyclerView.setAdapter(listAdapter);

        //Swipe config
        recyclerView.addOnItemTouchListener(new SwipeableListOnItemTouchListener());

    }

    private List<Fruit> getFruitsList() {
        List<Fruit> fruits = new ArrayList<>();

        fruits.add(new Fruit("Lemon", Color.parseColor("#8BC34A")));
        fruits.add(new Fruit("Kiwi", Color.parseColor("#388E3C")));
        fruits.add(new Fruit("Banana", Color.parseColor("#FFEE58")));
        fruits.add(new Fruit("Pineapple", Color.parseColor("#FFEB3B")));
        fruits.add(new Fruit("Mango", Color.parseColor("#FFC107")));
        fruits.add(new Fruit("Peach", Color.parseColor("#FF8A80")));
        fruits.add(new Fruit("Strawberry", Color.parseColor("#F44336")));
        fruits.add(new Fruit("Apple", Color.parseColor("#D84315")));
        fruits.add(new Fruit("Cherry", Color.parseColor("#E91E63")));
        fruits.add(new Fruit("Grape", Color.parseColor("#7B1FA2")));
        fruits.add(new Fruit("Blackberry", Color.parseColor("#250f51")));
        fruits.add(new Fruit("Blueberry", Color.parseColor("#3F51B5")));
        fruits.add(new Fruit("Coconut", Color.parseColor("#EFEBE9")));

        return fruits;
    }
}
