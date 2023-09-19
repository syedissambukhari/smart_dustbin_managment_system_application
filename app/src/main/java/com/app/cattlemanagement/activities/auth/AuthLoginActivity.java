package com.app.cattlemanagement.activities.auth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.activities.admin.AdminDashboardActivity;
import com.app.cattlemanagement.activities.consumer.ConsumerDashboard;
import com.app.cattlemanagement.activities.driver.DriverDashboard;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.User;
import com.app.cattlemanagement.singletons.CurrentUserSingleton;
import com.app.cattlemanagement.utils.DialogUtils;
import com.app.cattlemanagement.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * Email = cattlefyp@gmail.com
 * Password = finalyearproject
 */

public class AuthLoginActivity extends AppCompatActivity implements Info {

    public static Activity context;
    EditText etEmail;
    EditText etPassword;
    String strEtEmail;
    String strEtPassword;
    boolean isPassVisible = false;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_pass);

        loadingDialog = new Dialog(this);
        DialogUtils.initLoadingDialog(loadingDialog);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {




                loadingDialog.show();
                parseUserData();



        }

    }

    private void parseUserData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USERS)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        User userModel = snapshot.getValue(User.class);
                        if (userModel == null) {
                            Toast.makeText(AuthLoginActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (userModel.getUserType().equals(Info.TYPE_ADMIN)) {
                            startActivity(new Intent(AuthLoginActivity.this, AdminDashboardActivity.class));
                            finish();
                            return;
                        }
                        if (userModel.getVerStatus().equals(Info.USER_IN_ACTIVE)) {
                            Toast.makeText(AuthLoginActivity.this, "You are not allowed to login. Please wait for approval.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CurrentUserSingleton.setInstance(userModel);

                        if (userModel.getUserType().equals(Info.TYPE_DRIVER))
                            startActivity(new Intent(AuthLoginActivity.this, DriverDashboard.class));
                        else
                            startActivity(new Intent(AuthLoginActivity.this, ConsumerDashboard.class));
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void signUp(View view) {
        startActivity(new Intent(this, AuthRegistration.class));
    }

    public void showPassword(View view) {
        if (!isPassVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }

    }


    private void castStrings() {
        strEtEmail = etEmail.getText().toString().trim();
        strEtPassword = etPassword.getText().toString();
    }

    private boolean isEverythingValid() {
        if (!Utils.validEt(etEmail, strEtEmail))
            return false;
        return Utils.validEt(etPassword, strEtPassword);
    }

    public void Login(View view) {
        castStrings();
        if (!isEverythingValid())
            return;

        initSignIn();
    }

    private void initSignIn() {

        strEtPassword = etPassword.getText().toString();
        if (strEtPassword.length()<8) {
            etPassword.setError("Password cannot be less then 8 characters");
            etPassword.requestFocus();
        }
        else{
            loadingDialog.show();








        loadingDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(strEtEmail, strEtPassword)
                .addOnCompleteListener(task -> {
                    Log.i(TAG, "initSignIn: RESPONDED");
                    loadingDialog.dismiss();
                    if (task.isSuccessful()) {
                        initUserData();
                    } else {
                        if (task.getException() != null)
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "initSignIn: " + task.getException().getMessage());
                    }
                });
        }
    }

    private void initUserData() {
        loadingDialog.show();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USERS)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingDialog.dismiss();
                        User userModel = snapshot.getValue(User.class);
                        if (userModel == null) {
                            Toast.makeText(AuthLoginActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (userModel.getUserType().equals(Info.TYPE_ADMIN)) {
                            startActivity(new Intent(AuthLoginActivity.this, AdminDashboardActivity.class));
                            finish();
                            return;
                        }
                        if (userModel.getVerStatus().equals(Info.USER_IN_ACTIVE)) {
                            Toast.makeText(AuthLoginActivity.this, "You are not allowed to login. Please wait for approval.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        CurrentUserSingleton.setInstance(userModel);

                        if (userModel.getUserType().equals(Info.TYPE_DRIVER))
                            startActivity(new Intent(AuthLoginActivity.this, DriverDashboard.class));
                        else
                            startActivity(new Intent(AuthLoginActivity.this, ConsumerDashboard.class));
                        finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}