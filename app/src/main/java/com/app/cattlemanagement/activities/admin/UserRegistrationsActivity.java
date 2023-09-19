package com.app.cattlemanagement.activities.admin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.adapters.TypeRecyclerViewAdapter;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.Super;
import com.app.cattlemanagement.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationsActivity extends AppCompatActivity {
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    RecyclerView recyclerView;
    List<Super> listInstances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registrations);
        initRv();
        initData();
    }

    private void initData() {
        listInstances.clear();
        typeRecyclerViewAdapter.notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference()
                .child(Info.NODE_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listInstances.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User participant = child.getValue(User.class);
                            if (!participant.getUserType().equals(Info.TYPE_ADMIN))
                                listInstances.add(participant);
                        }
                        typeRecyclerViewAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initRv() {
        recyclerView = findViewById(R.id.rv_participants);
        listInstances = new ArrayList<>();
        typeRecyclerViewAdapter = new TypeRecyclerViewAdapter(this, listInstances, Info.RV_USER_REGISTRATIONS);
        recyclerView.setAdapter(typeRecyclerViewAdapter);
    }

    public void back(View view) {
        finish();
    }
}