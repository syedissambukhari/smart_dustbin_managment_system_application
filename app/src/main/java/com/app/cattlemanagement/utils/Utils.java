package com.app.cattlemanagement.utils;

import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Utils {

    public static boolean validEt(EditText etUserName, String strEtUserName) {
        if (strEtUserName.isEmpty()) {
            etUserName.setError("Field Empty");
            return false;
        }


        return true;
    }

    public static DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static int getIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return 0; // If the value is not found, return the first item in the array as default
    }
}
