package com.rey.androiddevelopertest.activity.home_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rey.androiddevelopertest.R;
import com.rey.androiddevelopertest.activity.login.LoginActivity;
import com.rey.androiddevelopertest.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    String name;
    String photoURL;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mUser != null) {
            name = mUser.getDisplayName();
            photoURL = String.valueOf(mUser.getPhotoUrl());
        }

        // set account
        setAccount();

        // sign out
        setSignOut();

    }

    private void setAccount() {
        binding.textNama.setText(name);
        Glide.with(this)
                .load(photoURL)
                .placeholder(R.drawable.ic_img_error)
                .into(binding.imgProfile);
    }

    private void setSignOut() {
        binding.buttonSignout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}