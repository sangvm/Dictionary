package com.example.dictionary;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dictionary.model.Word;
import com.example.dictionary.ui.bookmark.BookmarkFragment;
import com.example.dictionary.ui.detail.DetailFragment;
import com.example.dictionary.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DetailFragment detailFragment;
    private EditText editText;
    private ActivityMainBinding binding;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bookmark)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        dbHelper = new DBHelper(this);

//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.nav_host_fragment_content_main, new HomeFragment());
//        fragmentTransaction.commit();
        editText = findViewById(R.id.search_text);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Lấy giá trị từ EditText
                    String searchResult = editText.getText().toString();
                    System.out.println("search text ở home" + searchResult);

                    Word word = dbHelper.getWord(searchResult, "en_en");

                    // Tạo bundle để chứa giá trị searchResult
                    Bundle bundle = new Bundle();
                    //bundle.putString("search_result", searchResult);
                    bundle.putString("search_result", word.value);

                    // Tạo instance của DetailFragment và truyền bundle vào
                    detailFragment = new DetailFragment();
                    detailFragment.setArguments(bundle);

                    // Thay thế fragment hiện tại bằng DetailFragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, detailFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        System.out.println("ID---------------------------     " + id);
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,
                    new HomeFragment()).commit();
        } else if (id == R.id.nav_bookmark) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,
                    new BookmarkFragment()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}