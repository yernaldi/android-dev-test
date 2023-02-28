package com.rey.androiddevelopertest.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rey.androiddevelopertest.activity.home.HomeFragment;
import com.rey.androiddevelopertest.activity.home_account.AccountFragment;

public class NavigationAdapter extends FragmentStateAdapter {

    public NavigationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
            default:
                return new HomeFragment();
            case 1:
                return new AccountFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
