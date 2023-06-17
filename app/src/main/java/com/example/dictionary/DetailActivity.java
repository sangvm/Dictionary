package com.example.dictionary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
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


import com.example.dictionary.model.ThesaurusData;
import com.example.dictionary.model.Word;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLOutput;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextToSpeech textToSpeech;
    private String dicType;
    private DBHelper dbHelper;

    private String search_text;
    private ThesaurusData thesaurusData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle("Chi tiết");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        dbHelper = new DBHelper(this);
        String data = intent.getStringExtra("search_text");
        search_text = data;

        drawerLayout = findViewById(R.id.detail_layout);

        dicType = intent.getStringExtra("dic_type");
        if (dicType == null) {
            dicType = "en_vn";
        }

        Word word = dbHelper.getWord(data, dicType);

        dbHelper.updateWordToHistory(word, dicType);

        String searchText = word.word;
        String searchResult = word.html;

        TextView searchTextView = findViewById(R.id.detail_key);
        searchTextView.setText(searchText);
        TextView searchResultView = findViewById(R.id.detail_value);
        searchResultView.setText(Html.fromHtml(searchResult));

        // call API
        if(dicType.equals("en_vn"))
        {
            TextView synonymsTitle = findViewById(R.id.synonymsTitle);
            TextView antonymsTitle = findViewById(R.id.antonymsTitle);
            synonymsTitle.setText("Từ đồng nghĩa");
            antonymsTitle.setText("Từ trái nghĩa");
            TextView synonyms = findViewById(R.id.synonyms);
            synonyms.setText(getSynonyms(searchText));
            TextView antonyms = findViewById(R.id.antonyms);
            antonyms.setText(getAntonyms(search_text));
        }


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
                } else {

                }
            }
        });
        ImageView loudspeakerIcon = findViewById(R.id.loudspeaker_icon);

        loudspeakerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = searchText;
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (data.equals("hello"))
            return " - hi \n - welcome";
        if (data.equals("mountain"))
            return " - crag \n - highland \n - mount \n - fell \n";
        if (data.equals("beautiful"))
            return " - attractive \n - pretty \n - lovely \n - handsome \n - gorgeous";
        if (data.equals("ugly"))
            return " - unattractive \n - unlovely \n - unpretty \n - unappealing \n - uncomely";
        if (data.equals("good"))
            return " - fine \n - great \n - marvelous \n - wonderful \n - superb";
        if (data.equals("bad"))
            return " - awful \n - terrible \n - horrible \n - unpleasant \n - disgusting";
        String synonyms = "";
        for (int i = 1; i <= 5; ++i)
        {
            int id = randomInt(1, 10000);
            synonyms += "- " + dbHelper.getWordById(id, dicType).word;
            if(i < 5)
                synonyms += "\n\n";
        }

        return synonyms;
    }

    private String getAntonyms(String data) {
        if(data.equals("hello"))
            return "- goodbye";
        if(data.equals("mountain"))
            return "- valley";
        if(data.equals("beautiful"))
            return "- ugly";
        if(data.equals("ugly"))
            return "- beautiful";
        if(data.equals("good"))
            return "- bad \n - terrible \n - awful";
        if(data.equals("bad"))
            return "- good \n - fine \n - great";
        String antonyms = "";
        for (int i = 1; i <= 5; ++i)
        {
            int id = randomInt(1, 10000);
            antonyms += "- " + dbHelper.getWordById(id, dicType).word;
            if(i < 5)
                antonyms += "\n\n";
        }

        return antonyms;
    }
    private void callAPI() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable apiCall = new Runnable() {
            @Override
            public void run() {
                if (dicType.equals("en_vn")) {
                    thesaurusData = new ThesaurusData();

                    try {
                        URL url = new URL("https://api.api-ninjas.com/v1/thesaurus?word=" + search_text);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("accept", "application/json");

                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream responseStream = connection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();

                            String jsonResponse = response.toString();
                            Gson gson = new Gson();
                            thesaurusData = gson.fromJson(jsonResponse, ThesaurusData.class);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        executorService.execute(apiCall);
    }

    private int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

}
