package com.rey.androiddevelopertest;

import static com.rey.androiddevelopertest.util.constant.TEXT_NAV_ACCOUNT;
import static com.rey.androiddevelopertest.util.constant.TEXT_NAV_HOME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rey.androiddevelopertest.activity.login.LoginActivity;
import com.rey.androiddevelopertest.adapter.NavigationAdapter;
import com.rey.androiddevelopertest.databinding.ActivityMainBinding;
import com.rey.androiddevelopertest.databinding.ContentMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ContentMainBinding contentBinding;
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        contentBinding = binding.contentMain;
        setContentView(binding.getRoot());


        // set navigation
        setNavigation();

    }

    private void setNavigation() {
        NavigationAdapter pagerAdapter = new NavigationAdapter(this);
        contentBinding.viewPager.setAdapter(pagerAdapter);

        // Set up the TabLayout
        new TabLayoutMediator(contentBinding.tabLayout, contentBinding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(TEXT_NAV_HOME);
                            break;
                        case 1:
                            tab.setText(TEXT_NAV_ACCOUNT);
                            break;
                    }
                }).attach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUser == null) {
            // please login first
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            Toast.makeText(this, "Hello, " + mUser.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
    }
}