package com.bestnewsapp.sendnewsdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NewsSitesList> addData = new ArrayList<>();
    private NewsSiteListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsRecyclerView = (RecyclerView) findViewById(R.id.newList);

        addData.add(new NewsSitesList("The New York Times", "https://static01.nyt.com/images/icons/t_logo_291_black.png"));
        addData.add(new NewsSitesList("Washington Post", "http://www.washingtonpost.com/grid/local/wp-content/themes/wapo-grid-theme-v3/img/fb.png"));
        addData.add(new NewsSitesList("USA Today", "http://www.celebritywealthexpo.com/wp-content/uploads/2016/01/usatoday.png"));

        adapter = new NewsSiteListAdapter(getApplicationContext(), addData);
        adapter.notifyDataSetChanged();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setAdapter(adapter);

    }
}
