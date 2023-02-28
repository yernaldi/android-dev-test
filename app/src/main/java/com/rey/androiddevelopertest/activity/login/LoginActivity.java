package com.rey.androiddevelopertest.activity.login;

import static com.rey.androiddevelopertest.util.constant.REQ_ONE_TAP;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.rey.androiddevelopertest.R;
import com.rey.androiddevelopertest.databinding.ActivityLoginPageBinding;
import com.rey.androiddevelopertest.util.Buckets;
import com.rey.androiddevelopertest.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginPageBinding binding;
    LoginViewModel viewModel;
    private SignInClient oneTapClient;
    ActivityResultLauncher<IntentSenderRequest> loginResultHandler;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new LoginViewModel();

        // sign-in with google
        signInWithGoogle();

        // button sign-in
        setButtonSignIn();

    }

    private void setButtonSignIn() {
        binding.loginBtn.setOnClickListener(view -> {
            hideView(false, true);

            // show user email
            observer();
        });
    }

    private void observer() {
        viewModel.signIn(loginResultHandler);
        viewModel.getErrorMessage().observe(this, error -> {
            Log.d(TAG, "observer: " + error);
            hideView(true, false);
        });
        viewModel.getIdToken().observe(this, token -> {
            if (token.length() > 0) {
                Log.d(TAG, "onChanged: Token: " + token);
                viewModel.signInWithCredential(token, LoginActivity.this);
            }
            else {
                Toast.makeText(getApplicationContext(), "Something wrong. Try again later.", Toast.LENGTH_SHORT).show();
                hideView(true, false);
            }
        });
    }

    private void signInWithGoogle() {
        oneTapClient = Identity.getSignInClient(this);
        loginResultHandler = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: getResult " + result.getResultCode() );
                        viewModel.handleGoogleSignInResult(oneTapClient, REQ_ONE_TAP, result.getData());
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Log.d(TAG, "onActivityResult: OK " + result);

                        } else {
                            Log.d(TAG, "onActivityResult: NOT OK " + result);
                            hideView(true, false);
                        }
                    }
                }
        );
    }

    private void hideView(boolean HIDE_PROGRESS, boolean HIDE_LOGIN) {
        Buckets.hideView(HIDE_PROGRESS, binding.progressBar);
        Buckets.hideView(HIDE_LOGIN, binding.loginBtn);
    }
}