package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizIntroActivity extends AppCompatActivity {
    private int numberQuestion = -1;

    private Button startButton;

    private EditText numberQuestionEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_intro);
        setTitle("Quiz!!!");

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberQuestion != -1) {
                    Intent intent = new Intent(QuizIntroActivity.this, QuizzActivity.class);
                    intent.putExtra("number_question", numberQuestion);
                    startActivity(intent);
                }

            }
        });

        numberQuestionEditText = findViewById(R.id.numberQuestion);
        numberQuestionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    numberQuestion = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



    }
}
