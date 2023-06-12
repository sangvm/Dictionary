package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class QuizzActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    int numQuestion = 0;

    int numRight= 0;
    private TextView A, B, C, D, question;

    private String[] mockAnswer1 = {"Không có", "croon", "sea", "tough", "hill"};
    private String[] mockAnswer2 = {"Không có", "Không có", "Không có", "Easy", "Không có"};
    private String[] mockAnswer3 = {"Các", "Hát", "Biển", "Khó", "Núi"};

    private String[] mockWord1 = {"apple", "banana", "cat", "dog", "elephant", "fish", "giraffe", "horse", "igloo", "jungle",
            "kangaroo", "lion", "monkey", "nest", "ocean", "penguin", "quail", "rabbit", "snake", "tiger",
            "umbrella", "violet", "whale", "xylophone", "yak", "zebra", "quail", "rabbit", "snake", "tiger"};
    private String[] mockWord2 = {"đồng hồ", "xe hơi", "nhà", "máy tính", "quả cam", "chó", "búp bê", "trái cây", "máy ảnh",
            "cầu", "bàn", "hoa", "bút", "áo", "đám mây", "mặt trời", "cái ghế", "bóng đá", "giày", "khách sạn", "quần áo",
            "núi", "sông", "biển", "rừng", "thảm", "gương", "hoa quả", "vườn", "bánh mì"};

    private ArrayList<String> bookmarkList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);
        setTitle("Quizz");

        dbHelper = new DBHelper(this);
        bookmarkList = dbHelper.getWordFromBookmark();

        A = findViewById(R.id.A);
        B = findViewById(R.id.B);
        C = findViewById(R.id.C);
        D = findViewById(R.id.D);
        question = findViewById(R.id.question);

        int ok = genQuestion();

        A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numQuestion++;
                numRight++;
                if(numQuestion >= bookmarkList.size())
                {
                    int ok1 = genResult();
                }
                else
                {
                    int ok2 = genQuestion();
                }
            }
        });

        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numQuestion++;
                if(numQuestion >= bookmarkList.size())
                {
                    int ok1 = genResult();
                }
                else
                {
                    int ok2 = genQuestion();
                }
            }
        });

        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numQuestion++;
                if(numQuestion >= bookmarkList.size())
                {
                    int ok1 = genResult();
                }
                else
                {
                    int ok2 = genQuestion();
                }
            }
        });

        D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numQuestion++;
                if(numQuestion >= bookmarkList.size())
                {
                    int ok1 = genResult();
                }
                else
                {
                    int ok2 = genQuestion();
                }
            }
        });

    }
    private int genQuestion() {
        Random random = new Random();
        int type = random.nextInt(3) + 1;
        switch (type) {
            case 1:
                question.setText(String.format("Đâu là từ đồng nghĩa với từ %s", bookmarkList.get(numQuestion)));
                A.setText(String.format("A. %s",mockAnswer1[numQuestion]));
                B.setText(String.format("B. %s",mockWord1[randomInt(0, 9)]));
                C.setText(String.format("C. %s",mockWord1[randomInt(10, 19)]));
                D.setText(String.format("D. %s",mockWord1[randomInt(20, 29)]));
                break;
            case 2:
                question.setText(String.format("Đâu là từ trái nghĩa với từ %s", bookmarkList.get(numQuestion)));
                A.setText(String.format("A. %s",mockAnswer2[numQuestion]));
                B.setText(String.format("B. %s",mockWord1[randomInt(0, 9)]));
                C.setText(String.format("C. %s",mockWord1[randomInt(10, 19)]));
                D.setText(String.format("D. %s",mockWord1[randomInt(20, 29)]));
                break;
            case 3:
                question.setText(String.format("Đâu là từ nghĩa của từ %s", bookmarkList.get(numQuestion)));
                A.setText(String.format("A. %s",mockAnswer3[numQuestion]));
                B.setText(String.format("B. %s",mockWord2[randomInt(0, 9)]));
                C.setText(String.format("C. %s",mockWord2[randomInt(10, 19)]));
                D.setText(String.format("D. %s",mockWord2[randomInt(20, 29)]));
                break;
        }
        return 2;
    }

    private int genResult() {
        question.setText(String.format("Số câu trả lời đúng là %s/%s", numRight, numQuestion));
        A.setText("");
        B.setText("");
        C.setText("");
        D.setText("");
        return 3;
    }
    private int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}