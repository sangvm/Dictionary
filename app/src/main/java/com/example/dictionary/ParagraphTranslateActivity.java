package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ParagraphTranslateActivity extends AppCompatActivity {

    private EditText firstTextbox;
    private EditText secondTextbox;
    private Button translateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph_translate);

        firstTextbox = findViewById(R.id.first_textbox);
        translateButton = findViewById(R.id.translate_button);

    }

}