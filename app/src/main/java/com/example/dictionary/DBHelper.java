package com.example.dictionary;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.dictionary.model.Word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final int LIMIT_SEARCH = 12;
    //    public static final String DATABASE_NAME = "Dictionary.db";
    public static final String DATABASE_NAME = "dictionary.db";
    public static final int DATABASE_VERSION = 10;
    private static final String SP_KEY_DB_VER = "db_ver";
    private String DATABASE_LOCATION = "";
    private String DATABASE_FULL_PATH = "";

    public SQLiteDatabase mDB;

    private final String vn_en = "va";
    private final String en_vn = "av";
    private final String fr_vn = "pv";
    private final String vn_fr = "vp";
    private final String ge_vn = "dv";
    private final String vn_ge = "vd";
    private final String ru_vn = "nv";
    private final String vn_vn = "vv";

    private final String COL_ID = "id";
    private final String COL_WORD = "word";
    private final String COL_HTML = "html";
    private final String COL_PRONOUNCE = "pronounce";
    private final String COL_DESCRIPTION = "description";
    private final String COL_DEFINITION = "definition";

    public DBHelper (Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        mContext = context;
//        initialize();
//        https://stackoverflow.com/questions/11601573/db-file-in-assets-folder-will-it-be-updated

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        DATABASE_LOCATION = "data/data/" + mContext.getPackageName() + "/databases/";
        DATABASE_FULL_PATH = DATABASE_LOCATION + DATABASE_NAME;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
        if (DATABASE_VERSION != dbVersion) {
            File dbFile = mContext.getDatabasePath(DATABASE_NAME);
            if (!dbFile.delete()) {
                Log.w(TAG, "Unable to update database");
            }
        }
        if(!isExistingDatabase()) {
            createDatabase();
        }
        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH, null);
    }

    private void initialize() {
        if (databaseExists()) {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION != dbVersion) {
                File dbFile = mContext.getDatabasePath(DATABASE_NAME);
                if (!dbFile.delete()) {
                    Log.w(TAG, "Unable to update database");
                }
            }
        }
        if (!databaseExists()) {
            createDatabase();
        }
    }

    public boolean isExistingDatabase() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    private boolean databaseExists() {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    private void createDatabase() {
        String parentPath = mContext.getDatabasePath(DATABASE_NAME).getParent();
        String path = mContext.getDatabasePath(DATABASE_NAME).getPath();

        File file = new File(parentPath);
        if (!file.exists()) {
            if (!file.mkdir()) {
                Log.w(TAG, "Unable to create database directory");
                return;
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = mContext.getAssets().open(DATABASE_NAME);
            os = new FileOutputStream(path);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(SP_KEY_DB_VER, DATABASE_VERSION);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    @SuppressLint("Range")
//    public ArrayList<String> getWord(String dictionaryType) {
//        String tableName = getTableName(dictionaryType);
//        String query = "SELECT * FROM " + tableName;
//        Cursor result = mDB.rawQuery(query, null);
//
//        ArrayList<String> source = new ArrayList<>();
//        while (result.moveToNext()) {
//            source.add(result.getString(result.getColumnIndex(COL_KEY)));
//        }
//        return source;
//    }

    @SuppressLint("Range")
    public ArrayList<String> getWord(String dictionaryType) {
        String tableName = getTableName(dictionaryType);
        String query = "SELECT * FROM " + tableName;
        Cursor result = mDB.rawQuery(query, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            source.add(result.getString(result.getColumnIndex(COL_WORD)));
        }
        return source;
    }

//    @SuppressLint("Range")
//    public Word getWord(String key, String dictionaryType) {
//        String tableName = getTableName(dictionaryType);
//        String searchText = key;
//        if (tableName == en_en) searchText = convertWordForm(searchText);
//        String query = "SELECT * FROM " + tableName + " WHERE [word]= ?";
//        Cursor result = mDB.rawQuery(query, new String[] {searchText});
//
//        Word word = new Word();
//        while (result.moveToNext()) {
//            word.key = result.getString(result.getColumnIndex(COL_KEY));
//            word.value += result.getString(result.getColumnIndex(COL_VALUE));
//        }
//        return word;
//    }

    @SuppressLint("Range")
    public Word getWord(String key, String dictionaryType) {
        String tableName = getTableName(dictionaryType);
        String searchText = key;
        String query = "SELECT * FROM " + tableName + " WHERE [word]= ?";
        Cursor result = mDB.rawQuery(query, new String[] {searchText});

        Word word = new Word();
        while (result.moveToNext()) {
            word.word = result.getString(result.getColumnIndex(COL_WORD));
            word.html = result.getString(result.getColumnIndex(COL_HTML));
            word.id = result.getInt(result.getColumnIndex(COL_ID));
            word.description = result.getString(result.getColumnIndex(COL_DESCRIPTION));
            word.pronounce = result.getString(result.getColumnIndex(COL_PRONOUNCE));
        }
        return word;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWordFromBookmark() {
        String query = "SELECT * FROM bookmark ORDER BY last_update DESC";
        Cursor result = mDB.rawQuery(query, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            String data = result.getString(result.getColumnIndex(COL_WORD));
            data += "\n";
            String value = result.getString(result.getColumnIndex(COL_DEFINITION));
            data += value.substring(0, Math.min(46, value.length())) + "...";
            source.add(data);
        }
        return source;
    }

    public boolean isWordMark(Word word) {
        String query = "SELECT * FROM bookmark WHERE word =? AND definition = ?";
        Cursor result = mDB.rawQuery(query, new String[] {word.word, word.description});
        return result.getCount() > 0;
    }

    public void addBookmark(Word word) {
        try {
            String query = "INSERT INTO bookmark (word, definition, last_update) VALUES (?, ?, strftime('%Y-%m-%d %H:%M:%S', datetime('now')));";
            mDB.execSQL(query, new Object[] {word.word, word.description});
        }
        catch (Exception e) {

        }
    }

    public void deleteBookmark(Word word) {
        try {
            String query = "DELETE FROM bookmark WHERE word = ?";
            mDB.execSQL(query, new String[] {word.word});
        }
        catch (Exception e) {

        }
    }

    public String convertWordForm(String str) {
        String result = str.toLowerCase();
        return result;
    }

    public String getTableName (String dicType) {
        String tableName = "";
        if(Objects.equals(dicType, "en_vn")) {
            tableName = en_vn;
        }
        if(Objects.equals(dicType, "vn_en")) {
            tableName = vn_en;
        }
        if(Objects.equals(dicType, "fr_vn")) {
            tableName = fr_vn;
        }
        if(Objects.equals(dicType, "vn_fr")) {
            tableName = vn_fr;
        }
        if(Objects.equals(dicType, "ge_vn")) {
            tableName = ge_vn;
        }
        if(Objects.equals(dicType, "vn_ge")) {
            tableName = vn_ge;
        }
        if(Objects.equals(dicType, "ru_vn")) {
            tableName = ru_vn;
        }
        if(Objects.equals(dicType, "vn_vn")) {
            tableName = vn_vn;
        }
        if(Objects.equals(dicType, null)) {
            tableName = en_vn;
        }
        if(Objects.equals(tableName, "")) {
            tableName = en_vn;
        }
        return tableName;
    }

    @SuppressLint("Range")
    public ArrayList<String> getWordFromHistory(String dictionaryType) {
        String tableName = "";
        if (Objects.isNull(dictionaryType)) {
            tableName = "history_en_vn";
        }
        else {
            switch (dictionaryType) {
                case "vn_en": {
                    tableName = "history_vn_en";
                    break;
                }
                case "en_vn": {
                    tableName = "history_en_vn";
                    break;
                }
                case "fr_vn": {
                    tableName = "history_fr_vn";
                    break;
                }
                case "vn_fr": {
                    tableName = "history_vn_fr";
                    break;
                }
                case "ge_vn": {
                    tableName = "history_ge_vn";
                    break;
                }
                case "vn_ge": {
                    tableName = "history_vn_ge";
                    break;
                }
                case "ru_vn": {
                    tableName = "history_ru_vn";
                    break;
                }
                case "vn_vn": {
                    tableName = "history_vn_vn";
                    break;
                }
                default: {
                    tableName = "history_en_vn";
                    break;
                }
            }
        }

        String query = "SELECT * FROM " + tableName + " ORDER BY last_update DESC LIMIT " + LIMIT_SEARCH;
        Cursor result = mDB.rawQuery(query, null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()) {
            String data = result.getString(result.getColumnIndex(COL_WORD));
            data += "\n";
            String value = result.getString(result.getColumnIndex(COL_DEFINITION));
            data += value.substring(0, Math.min(46, value.length())) + "...";
            source.add(data);
        }
        return source;
    }

    public void updateWordToHistory(Word word, String dictionaryType) {
        String tableName = "";
        if (Objects.isNull(dictionaryType)) {
            tableName = "history_en_vn";
        }
        else {
            switch (dictionaryType) {
                case "vn_en": {
                    tableName = "history_vn_en";
                    break;
                }
                case "en_vn": {
                    tableName = "history_en_vn";
                    break;
                }
                case "fr_vn": {
                    tableName = "history_fr_vn";
                    break;
                }
                case "vn_fr": {
                    tableName = "history_vn_fr";
                    break;
                }
                case "ge_vn": {
                    tableName = "history_ge_vn";
                    break;
                }
                case "vn_ge": {
                    tableName = "history_vn_ge";
                    break;
                }
                case "ru_vn": {
                    tableName = "history_ru_vn";
                    break;
                }
                case "vn_vn": {
                    tableName = "history_vn_vn";
                    break;
                }
                default: {
                    tableName = "history_en_vn";
                    break;
                }
            }
        }
        String query = "INSERT OR REPLACE INTO " + tableName + " (word, definition, last_update) VALUES (?, ?, strftime('%Y-%m-%d %H:%M:%S', datetime('now')))";
        mDB.execSQL(query, new Object[] {word.word, word.description});
    }
}
