package com.example.dictionary.model;

import java.util.ArrayList;

public class ThesaurusData {
    private String word;
    private ArrayList<String> synonyms;
    private ArrayList<String> antonyms;

    public String getSynonyms() {
        int num = 0;
        String result = "";
        for(String synonym : synonyms) {
            if(num > 5)
                break;
            result += synonym + ", ";
        }
        return result;
    }

    public String getAntonyms() {
        int num = 0;
        String result = "";
        for(String synonym : antonyms) {
            if(num > 5)
                break;
            result += synonym + ", ";
        }
        return result;
    }

}
