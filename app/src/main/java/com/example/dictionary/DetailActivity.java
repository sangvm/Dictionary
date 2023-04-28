package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dictionary.model.Word;

public class DetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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