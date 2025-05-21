package com.example.test321;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MasterActivity extends AppCompatActivity {
  Button buttonMen,buttonWomen,buttonChildren;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_master);
        buttonMen=findViewById(R.id.button_men);
        buttonWomen=findViewById(R.id.button_women);
        buttonChildren=findViewById(R.id.button_children);
        buttonMen.setOnClickListener(v -> loadFragment(new MenFragment()));
        buttonWomen.setOnClickListener(v->loadFragment(new WomenFragment()));
        buttonChildren.setOnClickListener(v->loadFragment(new ChildrenFragment()));
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}