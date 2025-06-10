package com.example.cubeviewpagerlib;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1: return new TwoFragment();
            case 2: return new ThreeFragment();
            case 3: return new FourFragment();
            default: return new OneFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
