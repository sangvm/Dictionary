package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dictionary.model.Word;

import java.util.ArrayList;
import java.util.Random;

public class QuizzActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private int numQuestion;

    private int curQuestion = 0;
    boolean right = false;
    boolean selected = false;
    ArrayList<Word> wordList = new ArrayList<>();
    ArrayList<Word> answerList = new ArrayList<>();

    ArrayList<Word> wrongList = new ArrayList<>();

    int numRight= 0;
    private TextView A, B, C, D, question;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        setTitle("Quizz");

        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        numQuestion = intent.getIntExtra("number_question", 5);

        A = findViewById(R.id.A);
        B = findViewById(R.id.B);
        C = findViewById(R.id.C);
        D = findViewById(R.id.D);

        submit = findViewById(R.id.submit);
        question = findViewById(R.id.question);


        for (int i = 1; i <= numQuestion; i++) {
            Word word = dbHelper.getWordById(randomInt(1, 10000), "en_vn");
            wordList.add(word);
        }

        for (int i = 1; i <= 30; i++) {
            Word word = dbHelper.getWordById(randomInt(1, 10000), "en_vn");
            answerList.add(word);
        }

        genQuestion();

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right = true;
                selected = true;
                A.setBackgroundResource(R.drawable.background_green);
                B.setBackgroundResource(R.drawable.background_default);
                C.setBackgroundResource(R.drawable.background_default);
                D.setBackgroundResource(R.drawable.background_default);

            }
        });

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right = false;
                selected = true;
                B.setBackgroundResource(R.drawable.background_green);
                A.setBackgroundResource(R.drawable.background_default);
                C.setBackgroundResource(R.drawable.background_default);
                D.setBackgroundResource(R.drawable.background_default);
            }
        });

        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right = false;
                selected = true;
                C.setBackgroundResource(R.drawable.background_green);
                B.setBackgroundResource(R.drawable.background_default);
                A.setBackgroundResource(R.drawable.background_default);
                D.setBackgroundResource(R.drawable.background_default);
            }
        });

        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right = false;
                selected = true;
                D.setBackgroundResource(R.drawable.background_green);
                B.setBackgroundResource(R.drawable.background_default);
                C.setBackgroundResource(R.drawable.background_default);
                A.setBackgroundResource(R.drawable.background_default);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected) {
                    selected = false;
                    if (right) {
                        numRight++;
                    } else {
//                        int save = curQuestion;
//                        wrongList.add(wordList.get(save));
                    }
                    right = false;
                    curQuestion++;
                    A.setBackgroundResource(R.drawable.background_default);
                    B.setBackgroundResource(R.drawable.background_default);
                    C.setBackgroundResource(R.drawable.background_default);
                    D.setBackgroundResource(R.drawable.background_default);

                    if (curQuestion < numQuestion) {
                        genQuestion();
                    } else {
                        Intent intent = new Intent(QuizzActivity.this, ResultActivity.class);
                        intent.putExtra("numRight", numRight);
                        intent.putExtra("numQuestion", numQuestion);
                        intent.putExtra("wrongList", wrongList);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    protected void genQuestion() {
        int type = randomInt(1, 4);
        int id1 = randomInt(0, 9);
        int id2 = randomInt(10, 19);
        int id3 = randomInt(19, 29);

        switch (type) {
            case 1:
                question.setText(String.format("Câu %d: Đâu là miêu tả của từ \n\n %s \n\n ", curQuestion+1, wordList.get(curQuestion).word));
                A.setText(String.format("A. %s",wordList.get(curQuestion).description));
                B.setText(String.format("B. %s",answerList.get(id1).description));
                C.setText(String.format("C. %s",answerList.get(id2).description));
                D.setText(String.format("D. %s",answerList.get(id3).description));
                break;
            case 2:
                question.setText(String.format("Câu %d: Đâu là cách phát âm của từ \n\n %s \n\n ", curQuestion+1, wordList.get(curQuestion).word));
                A.setText(String.format("A. %s",wordList.get(curQuestion).pronounce));
                B.setText(String.format("B. %s",answerList.get(id1).pronounce));
                C.setText(String.format("C. %s",answerList.get(id2).pronounce));
                D.setText(String.format("D. %s",answerList.get(id3).pronounce));
                break;
            case 3:
                question.setText(String.format("Câu %d: Từ nào có cách phát âm như sau \n\n %s \n\n ", curQuestion+1, wordList.get(curQuestion).pronounce));
                A.setText(String.format("A. %s",wordList.get(curQuestion).word));
                B.setText(String.format("B. %s",answerList.get(id1).word));
                C.setText(String.format("C. %s",answerList.get(id2).word));
                D.setText(String.format("D. %s",answerList.get(id3).word));
                break;
            case 4:
                question.setText(String.format("Câu %d: Từ nào có nghĩa như sau \n\n %s \n\n", curQuestion+1, wordList.get(curQuestion).description));
                A.setText(String.format("A. %s",wordList.get(curQuestion).word));
                B.setText(String.format("B. %s",answerList.get(id1).word));
                C.setText(String.format("C. %s",answerList.get(id2).word));
                D.setText(String.format("D. %s",answerList.get(id3).word));
                break;
        }
    }

    private int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}