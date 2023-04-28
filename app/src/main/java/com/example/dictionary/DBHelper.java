package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dictionary.model.Word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    public static final String DATABASE_NAME = "Dictionary.db";
    public static final int DATABASE_VERSION = 1;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";

    public SQLiteDatabase mDB;

    private final String en_en = "en_en";

    private final String COL_KEY = "word";
    private final String COL_VALUE = "definition";

    public DBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        DATABASE_LOCATION = "data/data/" + mContext.getPackageName() + "/databases/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;
        if(!isExistingDatabase()) {
            try {
                File databaseLocation = new File(DATABASE_LOCATION);
                databaseLocation.mkdir();
                extractAssetToDatabaseDirectory(DATABASE_NAME);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }

    public boolean isExistingDatabase() {
        File file = new File(DATABASE_FULL_PATH);
        return file.exists();
    }

    public void extractAssetToDatabaseDirectory (String fileName) throws IOException {
        int length;
        InputStream sourceDatabase = this.mContext.getAssets().open(fileName);
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination = new FileOutputStream(destinationPath);
        byte[] buffer = new byte[4096];
        while ((length = sourceDatabase.read(buffer)) > 0) {
            destination.write(buffer, 0, length);
        }
        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @SuppressLint("Range")
    public ArrayList<String> getWord(String dictionaryType) {
        String tableName = getTableName(dictionaryType);
        String query = "SELECT * FROM " + tableName;
        Cursor result = mDB.rawQuery(query, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString(result.getColumnIndex(COL_KEY)));
        }
        return source;
    }

    @SuppressLint("Range")
    public Word getWord(String key, String dictionaryType) {
        String tableName = getTableName(dictionaryType);
        String searchText = convertWordForm(key);
        String query = "SELECT * FROM " + tableName + " WHERE [word]= ?";
        Cursor result = mDB.rawQuery(query, new String[] {searchText});

        Word word = new Word();
        while (result.moveToNext()) {
            word.key = result.getString(result.getColumnIndex(COL_KEY));
            word.value += result.getString(result.getColumnIndex(COL_VALUE));
        }
        return word;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWordFromBookmark() {
        String query = "SELECT * FROM bookmark";
        Cursor result = mDB.rawQuery(query, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString(result.getColumnIndex(COL_KEY)));
        }
        return source;
    }

    public boolean isWordMark(Word word) {
        String query = "SELECT * FROM bookmark WHERE word =? AND definition = ?";
        Cursor result = mDB.rawQuery(query, new String[] {word.key, word.value});
        return result.getCount() > 0;
    }

    public void addBookmark(Word word) {
        try {
            String query = "INSERT INTO bookmark (\"" + COL_KEY + "\", \"" + COL_VALUE + "\") VALUES (?, ?);";
            mDB.execSQL(query, new Object[] {word.key, word.value});
        }
        catch (Exception e) {

        }
    }

    public void deleteBookmark(Word word) {
        try {
            String query = "DELETE FROM bookmark WHERE word = ?";
            mDB.execSQL(query, new String[] {word.key});
        }
        catch (Exception e) {

        }
    }

    public String convertWordForm(String str) {
        String result = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
        return result;
    }
    public String getTableName (String dicType) {
        String tableName = "";
        if(dicType == en_en) {
            tableName = en_en;
        }
        return  tableName;
    }
}
