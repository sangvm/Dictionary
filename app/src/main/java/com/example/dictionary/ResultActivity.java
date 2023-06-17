package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.model.Word;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<String> wordList = new ArrayList<>();
    private DBHelper dbHelper;
    private String dicType = "en_vn";
    private TextView resultNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Kết quả");

        Intent intent = getIntent();

        int numRight = intent.getIntExtra("numRight", 0);
        int numQuestion = intent.getIntExtra("numQuestion", 0);

        TextView result = findViewById(R.id.result);
        result.setText("Bạn đã trả lời đúng " + numRight + " câu trên tổng số " + numQuestion + " câu hỏi.");


        dbHelper = new DBHelper(this);
        dataList = intent.getStringArrayListExtra("wrongList");
        for(int i = 0; i < dataList.size(); i++) {
            Word currentWord = dbHelper.getWord(dataList.get(i), dicType);
            String data = dataList.get(i);
            data += "\n";
            data += currentWord.description.substring(0, Math.min(40, currentWord.description.length())) + "...";
            wordList.add(data);
        }

        resultNotification = findViewById(R.id.result_notification);
        if (wordList.size() == 0) {
            resultNotification.setText("Chúc mừng bạn đã trả lời đúng tất cả các câu hỏi");
        }

        ListView wordListView = findViewById(R.id.result_list_view);
        ResultAdapter adapter = new ResultAdapter(this, wordList);
        wordListView.setAdapter(adapter);
        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWord = (String) parent.getItemAtPosition(position);
                String word = selectedWord.substring(0, selectedWord.indexOf("\n"));
                //String word = selectedWord;
                Intent intent = new Intent(ResultActivity.this, DetailActivity.class);
                intent.putExtra("search_text", word);
                intent.putExtra("dic_type", dicType);
                startActivity(intent);
            }
        });
    }
}

class ResultAdapter extends ArrayAdapter<String> {
    private final int[] colors = new int[] { Color.WHITE, Color.rgb(240, 240, 240) };

    public ResultAdapter(Context context, List<String> bookmarks) {
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