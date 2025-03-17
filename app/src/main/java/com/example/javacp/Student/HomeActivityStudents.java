package com.example.javacp.Student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javacp.LoginActivity;
import com.example.javacp.R;
import com.example.javacp.databinding.ActivityHomeActiviyStudentsBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivityStudents extends AppCompatActivity {
    ActivityHomeActiviyStudentsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityHomeActiviyStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawer, binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragments(new StudentFrag_1home());
        binding.bottomNavigation.setOnItemSelectedListener(menuItem -> {
            int selectedId = menuItem.getItemId();
            if (selectedId == R.id.nav_home) {
                replaceFragments(new StudentFrag_1home());
            } else if (selectedId == R.id.nav_course_details) {
                replaceFragments(new StudentFrag_2CourseDetails());
            } else if (selectedId == R.id.nav_video_player) {
                replaceFragments(new StudentFrag_3SubscribedVideos());
            } else if (selectedId == R.id.nav_progress) {
                replaceFragments(new StudentFrag_4CourseProgress());
            } else if (selectedId == R.id.nav_profile) {
                replaceFragments(new StudentFrag_5ProfileFragment());
            }
            return true;
        });

        //navigation view
        binding.navigationViewStudent.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.home) {
                    replaceFragments(new StudentFrag_1home());
                } else if (id == R.id.subscribedVideos) {
                    replaceFragments(new StudentFrag_3SubscribedVideos());
                } else if (id == R.id.profileStudent) {
                    replaceFragments(new StudentFrag_5ProfileFragment());
                } else if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(HomeActivityStudents.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivityStudents.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else if (id == R.id.coursesProgress) {
                    replaceFragments(new StudentFrag_4CourseProgress());
                }else if (id == R.id.courseDetails) {
                    replaceFragments(new StudentFrag_2CourseDetails());
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
                binding.drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
}
    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();
    }
}