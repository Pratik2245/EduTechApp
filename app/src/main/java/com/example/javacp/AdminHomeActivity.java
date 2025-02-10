package com.example.javacp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javacp.databinding.ActivityAdminHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize with default fragment
        replaceFragments(new Frag_1HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            int selectedId = menuItem.getItemId();
            if (selectedId == R.id.home) {
                replaceFragments(new Frag_1HomeFragment());
            } else if (selectedId == R.id.Courses) {
                replaceFragments(new Frag_2CoursesFragment());
            } else if (selectedId == R.id.addCourse) {
                replaceFragments(new Frag_3AddCoursesFragment());
            } else if (selectedId == R.id.profile) {
                replaceFragments(new Frag_4ProfileFragment());
            }
            return true; // Return true when an item is selected
        });
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}
