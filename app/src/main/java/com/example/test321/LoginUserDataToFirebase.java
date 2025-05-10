package com.example.test321;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginUserDataToFirebase {

    public static void loginUserDataToFirebase(Context context, String phoneNumber_final) {
        if (context == null || phoneNumber_final == null || phoneNumber_final.trim().isEmpty()) {
            Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show();
            Log.e("loginFirebase", "Invalid context or phone number");
            return;
        }

        Log.d("loginFirebase", "Trying with phone: " + phoneNumber_final);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber_final);
        Log.d("loginFirebase", "loginUserDataToFirebase:userRef is "+usersRef);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("loginFirebase", "User FOUND in DB: " + phoneNumber_final);

                    // Get username from "name" field
                    String username = snapshot.child("name").getValue(String.class);
                    Log.d("loginFirebase", "Username: " + username);

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber_final);
                    intent.putExtra("username", username); // pass username to MainActivity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    context.startActivity(intent);
                } else {
                    Log.d("loginFirebase", "User NOT found: " + phoneNumber_final);
                    Toast.makeText(context, "Phone number not registered.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("loginFirebase", "Database error: " + error.getMessage());
                Toast.makeText(context, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
