package com.example.javacp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.javacp.databinding.ActivityTeacherHomeBinding;
import com.google.android.material.navigation.NavigationBarView;

public class TeacherHomeActivity extends AppCompatActivity {
ActivityTeacherHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityTeacherHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //set default fragement as home fragment
        replaceFragments(new TeacherFrag_1Home());
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