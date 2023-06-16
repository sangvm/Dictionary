package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        setTitle("Danh sách yêu thích");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        ArrayList<String> bookmarkList = dbHelper.getWordFromBookmark();
        ListView bookmarkListView = findViewById(R.id.bookmark_list_view);
        BookmarkAdapter adapter = new BookmarkAdapter(this, bookmarkList);
        bookmarkListView.setAdapter(adapter);

        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWord = (String) parent.getItemAtPosition(position);
                String word = selectedWord.substring(0, selectedWord.indexOf("\n"));
                Intent intent = new Intent(BookmarkActivity.this, DetailActivity.class);
                intent.putExtra("search_text", word);
                startActivity(intent);
            }
        });
    }
}

class BookmarkAdapter extends ArrayAdapter<String> {
    private final int[] colors = new int[] { Color.WHITE, Color.rgb(240, 240, 240) };

    public BookmarkAdapter(Context context, List<String> bookmarks) {
        super(context, android.R.layout.simple_list_item_1, bookmarks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int color = colors[position % colors.length];
        view.setBackgroundColor(color);
        return view;
    }
}