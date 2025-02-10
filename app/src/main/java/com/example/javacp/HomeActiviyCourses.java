package com.example.javacp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.javacp.databinding.ActivityHomeActiviyCoursesBinding;

public class HomeActiviyCourses extends AppCompatActivity {
ActivityHomeActiviyCoursesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityHomeActiviyCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}