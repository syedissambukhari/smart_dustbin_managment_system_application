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
import com.app.cattlemanagement.models.TrashSource;
import com.app.cattlemanagement.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DriverAssignmentActivity extends AppCompatActivity {
    public static List<User> listDrivers;
    TypeRecyclerViewAdapter typeRecyclerViewAdapter;
    RecyclerView recyclerView;
    List<Super> listInstances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_assignment);
        initRv();

        initUsers();

    }

    private void initUsers() {
        listDrivers = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference()
                .child(Info.NODE_USERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listDrivers.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User participant = child.getValue(User.class);
                            if (participant != null)
                                if (participant.getUserType().equals(Info.TYPE_DRIVER))
                                    listDrivers.add(participant);
                        }
                        initData();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initData() {
        listInstances.clear();
        typeRecyclerViewAdapter.notifyDataSetChanged();
        FirebaseDatabase.getInstance().getReference()
                .child(Info.NODE_TRASH_SOURCE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listInstances.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            TrashSource trashSource = child.getValue(TrashSource.class);
                            listInstances.add(trashSource);
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
        typeRecyclerViewAdapter = new TypeRecyclerViewAdapter(this, listInstances, Info.RV_DRIVER_ASSIGNMENTS);
        recyclerView.setAdapter(typeRecyclerViewAdapter);
    }

    public void back(View view) {
        finish();
    }
}