package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();

        String searchText = intent.getStringExtra("search_text");
        String searchResult = intent.getStringExtra("search_result");

        drawerLayout = findViewById(R.id.detail_layout);
        System.out.println("Search text    " + searchText);
        System.out.println("Search result    " + searchResult);
        TextView searchTextView = findViewById(R.id.detail_key);
        searchTextView.setText(searchText);

        TextView searchResultView = findViewById(R.id.detail_value);
        searchResultView.setText(searchResult);
    }
}