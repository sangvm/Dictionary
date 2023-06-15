//package com.example.dictionary.model;
//
//public class Word {
//    public String key = "";
//    public String value = "";
//
//    public Word() {
//
//    }
//
//    public Word(String key, String value) {
//        this.key = key;
//        this.value = value;
//    }
//}

package com.example.dictionary.model;

public class Word {
    public int id;
    public String word;
    public String description;
    public String html;
    public String pronounce;
    public String wordType;

    public Word() {

    }

    public Word(int id, String word, String html, String description, String pronounce) {
        this.id = id;
        this.word = word;
        this.html = html;
        this.description = description;
        this.pronounce = pronounce;
    }
}

