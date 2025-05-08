package com.example.test321;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText phone_input;
    Button btnLogin;
    DatabaseReference databaseReference;
    TextView admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        btnLogin = findViewById(R.id.buttonLogin);
        admin = findViewById(R.id.adminscreenbtn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        EditText phone_input = findViewById(R.id.phone_input);

// Initially hide the end drawable (cancel icon)
        phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, 0, 0);

// TextWatcher for phone_input
        phone_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, R.drawable.cancel_24, 0);


                } else {
                    phone_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.call_20px, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

// Clear phone number when cancel icon is tapped
        phone_input.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable endDrawable = phone_input.getCompoundDrawables()[2]; // Right drawable
                if (endDrawable != null) {
                    int drawableStart = phone_input.getWidth() - phone_input.getPaddingEnd() - endDrawable.getIntrinsicWidth();
                    if (event.getX() >= drawableStart) {
                        phone_input.setText(""); // Clear input
                        // Accessibility compliance
                        v.performClick();
                        return true;
                    }
                }
            }
            return false;
        });

        // Apply fade-in animation to input fields
        fadeInAnimation(phone_input);
        // Handle admin screen navigation
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Admin.class);
                startActivity(intent);
            }
        });
        // Handle login button click

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Apply button click animation
                buttonClickAnimation(btnLogin);
            }
        });
    }
   // Button click animation
    private void buttonClickAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 1.1f, // Scale from 100% to 110%
                1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);
        view.startAnimation(scaleAnimation);
    }
    // Fade-in animation for EditText fields
    private void fadeInAnimation(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        view.startAnimation(alphaAnimation);
    }
}
