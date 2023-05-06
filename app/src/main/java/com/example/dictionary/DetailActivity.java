package com.example.dictionary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Window;


import com.example.dictionary.model.Word;

public class DetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //getSupportActionBar().hide();

        setTitle("Chi tiết");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        dbHelper = new DBHelper(this);

        String data = intent.getStringExtra("search_text");

        drawerLayout = findViewById(R.id.detail_layout);

        Word word = dbHelper.getWord(data, "en_en");
        boolean isBookmarked = dbHelper.isWordMark(word);
        updateBookmarkIcon(isBookmarked);

        ImageView bookmarkIcon = findViewById(R.id.bookmark_icon);
        bookmarkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = dbHelper.isWordMark(word);
                if (isBookmarked) {
                    dbHelper.deleteBookmark(word);
                } else {
                    dbHelper.addBookmark(word);
                }
                updateBookmarkIcon(!isBookmarked);
            }
        });
        String searchText = word.key;
        String searchResult = word.value;

        TextView searchTextView = findViewById(R.id.detail_key);
        searchTextView.setText(searchText);

        TextView searchResultView = findViewById(R.id.detail_value);
        searchResultView.setText(searchResult);

//        Toolbar toolbar = findViewById(R.id.toolbar_detail);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            System.out.println("This iss wtkdaskdl;as DCMMMMMM");
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.println("DCMMM");
//                onBackPressed();
//            }
//        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    private void updateBookmarkIcon(boolean isBookmarked) {
        ImageView bookmarkIcon = findViewById(R.id.bookmark_icon);
        if (!isBookmarked) {
            bookmarkIcon.setImageResource(R.drawable.bookmark_dark);
        } else {
            bookmarkIcon.setImageResource(R.drawable.bookmark_light);
        }
    }
}