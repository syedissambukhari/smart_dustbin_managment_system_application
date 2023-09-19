package com.app.cattlemanagement.activities.consumer;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.Neighbourhood;
import com.app.cattlemanagement.singletons.CurrentUserSingleton;
import com.app.cattlemanagement.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddLocationActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDescription;
    EditText etAddress;

    String strEtTitle;
    String strEtDescription;
    String strEtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etAddress = findViewById(R.id.et_address);


        FirebaseDatabase.getInstance().getReference().child(Info.NODE_NEIGHBOURHOOD)
                .child(CurrentUserSingleton.getInstance().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Neighbourhood neighbourhood = snapshot.getValue(Neighbourhood.class);
                        if (neighbourhood != null) {
                            etTitle.setText(neighbourhood.getTitle());
                            etDescription.setText(neighbourhood.getDescription());
                            etAddress.setText(neighbourhood.getAddress());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void back(View view) {
        finish();
    }

    private void castStrings() {
        strEtTitle = etTitle.getText().toString();
        strEtDescription = etDescription.getText().toString();
        strEtAddress = etAddress.getText().toString();
    }

    public void Submit(View view) {
        castStrings();
        if (!Utils.validEt(etTitle, strEtTitle))
            return;
        if (!Utils.validEt(etDescription, strEtDescription))
            return;
        if (!Utils.validEt(etAddress, strEtAddress))
            return;


        Neighbourhood neighbourhood = new Neighbourhood(strEtTitle, strEtDescription, strEtAddress);

        FirebaseDatabase.getInstance().getReference().child(Info.NODE_NEIGHBOURHOOD)
                .child(CurrentUserSingleton.getInstance().getId())
                .setValue(neighbourhood)
                .addOnCompleteListener(task -> Toast.makeText(this, "Neighbourhood updated Successfully", Toast.LENGTH_SHORT).show());

    }
}