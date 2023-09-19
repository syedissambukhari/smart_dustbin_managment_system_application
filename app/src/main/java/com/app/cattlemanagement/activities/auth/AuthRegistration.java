package com.app.cattlemanagement.activities.auth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.cattlemanagement.R;
import com.app.cattlemanagement.activities.consumer.ConsumerDashboard;
import com.app.cattlemanagement.info.Info;
import com.app.cattlemanagement.models.User;
import com.app.cattlemanagement.singletons.CurrentUserSingleton;
import com.app.cattlemanagement.utils.DialogUtils;
import com.app.cattlemanagement.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AuthRegistration extends AppCompatActivity implements Info {

    public static Activity context;

    public static String strEtPassword;
    boolean isPassVisible = false;

    EditText etEmail;
    EditText etPhone;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etFirstName;
    EditText etLastName;
    Spinner spnUserType;


    String strSpnUserType;
    String strEtFirstName;
    String strEtLastName;
    String strEtEmail;
    String strEtPhone;
    String strEtConfirmPassword;

    Dialog dgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        initViews();
        dgLoading = new Dialog(this);
        DialogUtils.initLoadingDialog(dgLoading);
    }

    public void showPassword(View view) {
        if (!isPassVisible) {
            etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isPassVisible = true;
        } else {
            etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isPassVisible = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void castStrings() {
        strEtFirstName = etFirstName.getText().toString();
        strEtLastName = etLastName.getText().toString();
        strEtEmail = etEmail.getText().toString();
        strEtPhone = etPhone.getText().toString();
        strEtPassword = etPassword.getText().toString();


        strEtConfirmPassword = etConfirmPassword.getText().toString();
        strSpnUserType = spnUserType.getSelectedItem().toString();
    }

    private void initViews() {
        spnUserType = findViewById(R.id.my_spinner);
        etEmail = findViewById(R.id.et_user_name);
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_pass);
        etConfirmPassword = findViewById(R.id.et_confirm_pass);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
    }

    public void back(View view) {
        finish();
    }

    public void SignUp(View view) {
        castStrings();

        if (!Utils.validEt(etFirstName, strEtFirstName))
            return;

        if (!Utils.validEt(etLastName, strEtLastName))
            return;

        if (!Utils.validEt(etEmail, strEtEmail))
            return;

        if (!Utils.validEt(etPhone, strEtPhone))
            return;

        if (!strEtPassword.equals(strEtConfirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (strEtPassword.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
        }
        else if (strEtPassword.length()<8) {
            etPassword.setError("Password cannot be less then 8 characters");
            etPassword.requestFocus();
        }
        else{
            String id = "0";
            User userModel = new User(id, strEtFirstName, strEtLastName, "-",
                    strEtPhone, "-", Info.USER_IN_ACTIVE);
            userModel.setUserType(strSpnUserType);
            initAuth(userModel);
        }


    }

    private void initAuth(User userModel) {
        dgLoading.show();
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(strEtEmail, strEtConfirmPassword)
                .addOnCompleteListener(task -> {
                    dgLoading.dismiss();
                    if (task.isSuccessful()) {
                        userModel.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        initData(userModel);
                    } else
                        Toast.makeText(AuthRegistration.this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                });
    }

    private void initData(User userModel) {
        dgLoading.show();
        FirebaseDatabase.getInstance().getReference()
                .child(NODE_USERS)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .setValue(userModel)
                .addOnCompleteListener(task -> {
                    dgLoading.dismiss();
                    if (task.isSuccessful()) {
                        CurrentUserSingleton.setInstance(userModel);
                        Toast.makeText(AuthRegistration.this, "Your Registration is sent to Admin. Please wait for approval", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AuthRegistration.this, AuthLoginActivity.class));
                        finish();

                    } else
                        Toast.makeText(AuthRegistration.this, "ERROR OCCURRED", Toast.LENGTH_SHORT).show();
                });
    }
}