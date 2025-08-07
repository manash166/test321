package com.example.cubeviewpagerlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class MajorActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler handler = new Handler(Looper.getMainLooper());

    private final long SPIN_DELAY = 1000; // 1 second between pages for intro spin
    private final long AUTO_ROTATE_DELAY = 5000; // rotate every 5 seconds after spin

    private Runnable autoRotateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);

        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set the Cube effect
        viewPager.setPageTransformer(new CubeTransformer());

        // Start the 360 spin after 0.5s
        handler.postDelayed(() -> startIntroSpin(adapter.getItemCount()), 500);
    }

    private void startIntroSpin(int pageCount) {
        // Go through pages 1, 2, 3 to complete a spin
        for (int i = 1; i < pageCount; i++) {
            int finalI = i;
            handler.postDelayed(() -> viewPager.setCurrentItem(finalI, true), i * SPIN_DELAY);
        }

        // After spin, start auto-rotation
        handler.postDelayed(this::startAutoRotation, pageCount * SPIN_DELAY);
    }

    private void startAutoRotation() {
        autoRotateRunnable = new Runnable() {
            @Override
            public void run() {
                int next = (viewPager.getCurrentItem() + 1) % viewPager.getAdapter().getItemCount();
                viewPager.setCurrentItem(next, true);
                handler.postDelayed(this, AUTO_ROTATE_DELAY);
            }
        };

        handler.postDelayed(autoRotateRunnable, AUTO_ROTATE_DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
