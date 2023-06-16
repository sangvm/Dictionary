package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

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

    }
}
