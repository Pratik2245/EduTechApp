package com.example.javacp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javacp.Common.LoginActivity;
import com.example.javacp.R;
import com.example.javacp.databinding.ActivityAdminHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.home) {
                    replaceFragments(new Frag_1HomeFragment());
                } else if (id == R.id.profile) {
                   replaceFragments(new Frag_4ProfileFragment());
                } else if (id == R.id.logout) {
                 FirebaseAuth.getInstance().signOut();
                    Toast.makeText(AdminHomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.addCourses) {
                 replaceFragments(new Frag_3AddTeachersFragment());
                }else if (id == R.id.coursesList) {
                    replaceFragments(new Frag_2CoursesFragment());
                }else if (id == R.id.rating) {
                    // Redirect to Play Store for rating the app
                    String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else if (id == R.id.share) {
                    // Share App Link
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String shareBody = "Check out this amazing app: https://play.google.com/store/apps/details?id=" + getPackageName();
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Recommendation");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
                // Close the navigation drawer
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;            }
        });
        // Initialize with default fragment
        replaceFragments(new Frag_1HomeFragment());

        binding.bottomnavigationbar.setOnItemSelectedListener(menuItem -> {
            int selectedId = menuItem.getItemId();
            if (selectedId == R.id.home) {
                replaceFragments(new Frag_1HomeFragment());
            } else if (selectedId == R.id.Courses) {
                replaceFragments(new Frag_2CoursesFragment());
            } else if (selectedId == R.id.addCourse) {
                replaceFragments(new Frag_3AddTeachersFragment());
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
