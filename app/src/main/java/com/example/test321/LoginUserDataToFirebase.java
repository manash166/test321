package com.example.test321;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginUserDataToFirebase {
    public static void loginUserDataToFirebase(String phoneNumber) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber);

        // You can log additional metadata here if needed
        usersRef.child("last_login_time").setValue(System.currentTimeMillis());

        // Optionally store other user data if required
        // usersRef.child("status").setValue("active");
    }
}

