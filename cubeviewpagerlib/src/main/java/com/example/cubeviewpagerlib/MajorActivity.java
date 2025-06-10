package com.example.cubeviewpagerlib;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;



public class MajorActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
    private final long AUTO_SLIDE_DELAY = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);

        viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.setPageTransformer(new CubeTransformer());

        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int next = (viewPager.getCurrentItem() + 1) % adapter.getItemCount();
                viewPager.setCurrentItem(next, true);
                sliderHandler.postDelayed(this, AUTO_SLIDE_DELAY);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, AUTO_SLIDE_DELAY);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    sliderHandler.removeCallbacks(sliderRunnable);
                } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    sliderHandler.postDelayed(sliderRunnable, AUTO_SLIDE_DELAY);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
