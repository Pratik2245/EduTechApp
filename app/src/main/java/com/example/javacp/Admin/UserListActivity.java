package com.example.javacp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar; // ✅ Use this instead


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javacp.Adapter.UserAdapterAdmin;
import com.example.javacp.R;
import com.example.javacp.model.UserModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapterAdmin userAdapter;
    Toolbar toolbar ;
    private List<UserModel> userList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar= findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        userList = new ArrayList<>();
        userAdapter = new UserAdapterAdmin(this, userList);
        recyclerView.setAdapter(userAdapter);

        db = FirebaseFirestore.getInstance();

        // Get the role from intent
        String role = getIntent().getStringExtra("role");


        loadUsersByRole(role);
    }

    private void loadUsersByRole(String roleToFilter) {
        db.collection("users")
                .whereEqualTo("role", roleToFilter)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("fullName");
                        String userId = doc.getId();
                        if (name != null) {
                            userList.add(new UserModel(userId, name));
                        }
                    }
                    userAdapter.notifyDataSetChanged();
                });
    }
}
