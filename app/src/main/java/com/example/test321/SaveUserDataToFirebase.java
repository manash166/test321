package com.example.test321;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveUserDataToFirebase {

    public static void saveUserDataToFirebase(String name, String phoneNumber) {
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNumber); // phone number is the key

        userRef.child("name").setValue(name);
        userRef.child("default_address").setValue("not_set");

    }
}
