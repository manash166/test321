package com.example.cubeviewpagerlib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        // Find the ImageView (Make sure it exists in fragment_two.xml)
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
        return view;
    }
}
