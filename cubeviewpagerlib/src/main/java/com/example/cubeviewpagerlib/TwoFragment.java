package com.example.cubeviewpagerlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;  // ✅ Make sure this import is present

public class TwoFragment extends Fragment {
    public TwoFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        Button btnAddBride = view.findViewById(R.id.button_for_bride);

        btnAddBride.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClassName(
                    requireActivity().getPackageName(),  // "com.example.test321"
                    "com.example.test321.bride_special"  // full package + class name
            );
            startActivity(intent);
        });


        // Find the ImageView (Make sure it exists in fragment_two.xml)
        ImageView glow_animation = view.findViewById(R.id.glowStar);
        ImageView brideGif = view.findViewById(R.id.brideGif);
        ImageView groomGif = view.findViewById(R.id.groomGif);
        // Load the GIF using Glide

        Glide.with(this)
                .asGif()
                .load(R.drawable.rbm)  // Replace with your actual GIF file name
                .into(brideGif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.rbf)  // Replace with your actual GIF file name
                .into(groomGif);
        Glide.with(this)
                .asGif()
                .load(R.drawable.stars)  // Replace with your actual GIF file name
                .into(glow_animation);

        return view;
    }
}
