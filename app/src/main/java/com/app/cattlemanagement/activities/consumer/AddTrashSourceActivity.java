package com.app.cattlemanagement.activities.consumer;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.TrashSource;
import com.app.cattlemanagement.singletons.CurrentUserSingleton;
import com.app.cattlemanagement.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTrashSourceActivity extends AppCompatActivity {

    EditText etTitle;
    EditText etDescription;
    EditText etAddress;
    TextView tvStatus;

    String strEtTitle;
    String strEtDescription;
    String strEtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trash_source);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etAddress = findViewById(R.id.et_address);
        tvStatus = findViewById(R.id.tv_status);


        FirebaseDatabase.getInstance().getReference().child(Info.NODE_TRASH_SOURCE)
                .child(CurrentUserSingleton.getInstance().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TrashSource neighbourhood = snapshot.getValue(TrashSource.class);
                        if (neighbourhood != null) {
                            etTitle.setText(neighbourhood.getTitle());
                            etDescription.setText(neighbourhood.getDescription());
                            etAddress.setText(neighbourhood.getAddress());
                            tvStatus.setText(neighbourhood.getStatus());
                            if (neighbourhood.getStatus().equals(Info.STATUS_PENDING)) {
                                tvStatus.setTextColor(getColor(R.color.yellow_orig));
                            }
                            if (neighbourhood.getStatus().equals(Info.STATUS_REJECTED)) {
                                tvStatus.setTextColor(getColor(R.color.red));
                            }
                            if (neighbourhood.getStatus().equals(Info.STATUS_APPROVED)) {
                                tvStatus.setTextColor(getColor(R.color.green));
                            }
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


        TrashSource neighbourhood = new TrashSource(strEtTitle, strEtDescription, strEtAddress);
        neighbourhood.setStatus(Info.STATUS_PENDING);
        neighbourhood.setUserId(CurrentUserSingleton.getInstance().getId());
        FirebaseDatabase.getInstance().getReference().child(Info.NODE_TRASH_SOURCE)
                .child(CurrentUserSingleton.getInstance().getId())
                .setValue(neighbourhood)
                .addOnCompleteListener(task -> Toast.makeText(this, "Trash Source updated Successfully", Toast.LENGTH_SHORT).show());

    }
}