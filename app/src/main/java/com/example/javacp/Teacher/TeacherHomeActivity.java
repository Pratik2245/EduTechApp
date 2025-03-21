package com.example.javacp.Teacher;

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

import com.example.javacp.Common.LoginActivity;
import com.example.javacp.R;
import com.example.javacp.databinding.ActivityTeacherHomeBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TeacherHomeActivity extends AppCompatActivity {
ActivityTeacherHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityTeacherHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //set default fragement as home fragment
        replaceFragments(new TeacherFrag_1Home());

//        /.listener for sidebar
        binding.SidebarTeacher.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.homeTeacher) {
                    replaceFragments(new TeacherFrag_1Home());
                } else if (id == R.id.profile) {
                    replaceFragments(new TeacherFrag_2Profile());
                } else if (id == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(TeacherHomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeacherHomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.ViewCourseDetailsTeacher) {
                    replaceFragments(new TeacherFrag_4ViewCourseDetails());
                } else if (id==R.id.ManageCourses) {
                    replaceFragments(new TeacherFrag_3ManageCourses());
                } else if (id == R.id.rating) {
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
                return true;
            }
        });
        binding.bottomNavBarTeacher.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int selectedId = menuItem.getItemId();
                if (selectedId == R.id.homeTeacher) {
                    replaceFragments(new TeacherFrag_1Home());
                } else if (selectedId == R.id.viewCourseDetailsTeacher) {
                    replaceFragments(new TeacherFrag_4ViewCourseDetails());
                } else if (selectedId == R.id.manageCourseTeachers) {
                    replaceFragments(new TeacherFrag_3ManageCourses());
                } else if (selectedId == R.id.profileTeacher) {
                    replaceFragments(new TeacherFrag_2Profile());
                }
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