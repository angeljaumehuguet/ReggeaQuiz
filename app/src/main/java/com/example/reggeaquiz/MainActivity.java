package com.example.reggeaquiz;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    
    // Fragments
    private GameFragment gameFragment;
    private InfoFragment infoFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        // Initialize fragments
        gameFragment = new GameFragment();
        infoFragment = new InfoFragment();
        settingsFragment = new SettingsFragment();
        
        fragmentManager = getSupportFragmentManager();
        
        // Set default fragment
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, gameFragment)
                .add(R.id.fragment_container, infoFragment)
                .add(R.id.fragment_container, settingsFragment)
                .hide(infoFragment)
                .hide(settingsFragment)
                .commit();
        
        // Set up navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.menu_game) {
                showFragment(gameFragment);
                return true;
            } else if (itemId == R.id.menu_info) {
                showFragment(infoFragment);
                return true;
            } else if (itemId == R.id.menu_settings) {
                showFragment(settingsFragment);
                return true;
            }
            return false;
        });
    }
    
    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .hide(gameFragment)
                .hide(infoFragment)
                .hide(settingsFragment)
                .show(fragment)
                .commit();
    }
}
