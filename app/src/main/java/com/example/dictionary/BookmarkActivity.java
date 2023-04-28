package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dictionary.model.Word;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        dbHelper = new DBHelper(this);
        ArrayList<String> bookmarkList = dbHelper.getWordFromBookmark();
        ListView bookmarkListView = findViewById(R.id.bookmark_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookmarkList);
        bookmarkListView.setAdapter(adapter);

        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWord = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(BookmarkActivity.this, DetailActivity.class);
                intent.putExtra("search_text", selectedWord);
                startActivity(intent);
            }
        });
    }
}