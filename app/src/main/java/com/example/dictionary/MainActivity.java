package com.example.dictionary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictionary.model.Word;
import com.example.dictionary.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private EditText editText;
    private String dicType;
    private ActivityMainBinding binding;

    MenuItem menuSetting;
    ArrayList<String> searchHistoryList = new ArrayList<>();

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        MenuItem bookmarkItem = navigationView.getMenu().findItem(R.id.nav_bookmark);
        bookmarkItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
                return false;
            }
        });

        MenuItem paragraphItem = navigationView.getMenu().findItem(R.id.nav_paragraph);
        paragraphItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, ParagraphTranslateActivity.class);
                startActivity(intent);
                return false;
            }
        });


        dbHelper = new DBHelper(this);

        editText = findViewById(R.id.search_text);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String searchResult = editText.getText().toString();
                    if (searchResult.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy từ cần tìm!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Word word = dbHelper.getWord(searchResult, dicType);
                    if (word.value.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy từ cần tìm!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);;
                        intent.putExtra("search_text", searchResult);
                        intent.putExtra("dic_type", dicType);

                        startActivity(intent);
                        return true;
                    }

                }
                return false;
            }
        });

        searchHistoryList = dbHelper.getWordFromHistory(dicType);
        ListView searchHistoryView = findViewById(R.id.search_history_list);
        HomeAdapter adapter = new HomeAdapter(this, searchHistoryList);
        searchHistoryView.setAdapter(adapter);
        searchHistoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWord = (String) parent.getItemAtPosition(position);
                String word = selectedWord.substring(0, selectedWord.indexOf("\n"));
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("search_text", word);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_setting);

       String id = Global.getState(this, "dic_type");
       if (id != null) {
           onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
       }
       return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();
            if (!String.valueOf(id).equals("")) {
                Global.saveState(this, "dic_type", String.valueOf(id));
            }

            if (id == R.id.action_en_en) {
                dicType = "en_en";
                menuSetting.setIcon(getDrawable(R.drawable.ic_en_en));
            }
            if (id == R.id.action_vn_en) {
                dicType = "vn_en";
                menuSetting.setIcon(getDrawable(R.drawable.ic_vn_en));
            }
        }
        catch (Exception ex) {
            dicType = "en_en";
            menuSetting.setIcon(getDrawable(R.drawable.ic_en_en));
        }
        finally {
            searchHistoryList = dbHelper.getWordFromHistory(dicType);
            ListView searchHistoryView = findViewById(R.id.search_history_list);
            HomeAdapter adapter = new HomeAdapter(this, searchHistoryList);
            searchHistoryView.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}

class HomeAdapter extends ArrayAdapter<String> {
    private final int[] colors = new int[] { Color.WHITE, Color.rgb(240, 240, 240) };

    public HomeAdapter(Context context, List<String> bookmarks) {
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