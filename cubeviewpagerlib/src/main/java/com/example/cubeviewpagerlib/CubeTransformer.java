package com.example.cubeviewpagerlib;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CubeTransformer implements ViewPager2.PageTransformer {
    private static final float ROTATION = 90f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        page.setCameraDistance(50000);

        if (position < -1) { // way off-screen to the left
            page.setAlpha(0);
        } else if (position <= 0) { // [-1, 0]
            page.setAlpha(1);
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight() / 2f);
            page.setRotationY(ROTATION * position);
        } else if (position <= 1) { // (0,1]
            page.setAlpha(1);
            page.setPivotX(0);
            page.setPivotY(page.getHeight() / 2f);
            page.setRotationY(ROTATION * position);
        } else { // way off-screen right
            page.setAlpha(0);
        }

        // Vertical shake - oscillate vertical translation on visible pages only
        if (position >= -1 && position <= 1) {
            float shakeAmplitude = 10f; // pixels
            // Use sine wave for smooth up/down shake, based on position and system time
            long time = System.currentTimeMillis();
            float phase = (time % 1000) / 1000f * 2 * (float) Math.PI; // oscillation every 1 second
            float shake = (float) Math.sin(phase) * shakeAmplitude;
            page.setTranslationY(shake);
        } else {
            page.setTranslationY(0);
        }

    }
}
