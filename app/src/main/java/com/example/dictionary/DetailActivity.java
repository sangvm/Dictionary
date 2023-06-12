package com.example.dictionary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Window;
import android.widget.Toast;


import com.example.dictionary.model.Word;

import java.sql.SQLOutput;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextToSpeech textToSpeech;
    private String dicType;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle("Chi tiết");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        dbHelper = new DBHelper(this);
        String data = intent.getStringExtra("search_text");
        drawerLayout = findViewById(R.id.detail_layout);

        dicType = intent.getStringExtra("dic_type");
        if (dicType == null) {
            dicType = "en_en";
        }

        Word word = dbHelper.getWord(data, dicType);

        dbHelper.updateWordToHistory(word, dicType);

        String searchText = word.key;
        String searchResult = word.value;

        TextView searchTextView = findViewById(R.id.detail_key);
        searchTextView.setText(searchText);
        TextView searchResultView = findViewById(R.id.detail_value);
        searchResultView.setText(searchResult);
        TextView synonyms = findViewById(R.id.synonyms);
        synonyms.setText(getSynonyms(data));
        TextView antonyms = findViewById(R.id.antonyms);
        antonyms.setText(getAntonyms(data));

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

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
                else {

                }
            }
        });
        ImageView loudspeakerIcon = findViewById(R.id.loudspeaker_icon);

        loudspeakerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchText;
                Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT).show();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });


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

    private String getSynonyms(String data) {
        if(data.equals("hello")) {
            return new String("hi, howdy, hullo, how-do-you-do");
        }
        if(data.equals("beautiful")) {
            return new String("lovely, fair, fine, aesthetic, gorgeous, pretty, handsome");
        }
        if(data.equals("ocean")) {
            return new String("sea");
        }
        if(data.equals("hard")) {
            return new String("tough, bad, set, stiff");
        }
        if(data.equals("sing")) {
            return new String("caron, descant, croon");
        }

        return new String("loading");
    }

    private String getAntonyms(String data) {
        if(data.equals("hello")) {
            return new String("Không có từ trái nghĩa");
        }
        if(data.equals("beautiful")) {
            return new String("ugly");
        }
        if(data.equals("ocean")) {
            return new String("Không có từ trái nghĩa");
        }
        if(data.equals("hard")) {
            return new String("easy, soft, lightly");
        }
        if(data.equals("sing"))
        {
            return new String("Không có từ trái nghĩa");
        }
        return new String("loading");
    }

}