package com.rey.androiddevelopertest.viewmodel;

import static com.rey.androiddevelopertest.util.constant.CLIENT_ID;
import static com.rey.androiddevelopertest.util.constant.REQ_ONE_TAP;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rey.androiddevelopertest.MainActivity;
import com.rey.androiddevelopertest.MyApplication;

public class LoginViewModel extends ViewModel {

    public static final String TAG = "LoginViewModel";
    private final MutableLiveData<String> idToken = new MutableLiveData<>();
    private final MutableLiveData<ApiException> googleSignInException = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    public LoginViewModel() {
        initSignIn();
    }

    private void initSignIn() {
        oneTapClient = Identity.getSignInClient(MyApplication.getInstance());

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(CLIENT_ID)
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
    }

    public void handleGoogleSignInResult(SignInClient signInClient, int requestCode, Intent intent) {
        if (requestCode == REQ_ONE_TAP) {
            try {
                SignInCredential credential = signInClient.getSignInCredentialFromIntent(intent);
                setIdToken(credential.getGoogleIdToken());
            } catch (ApiException e) {
                Log.d(TAG, "handleGoogleSignInResult: error = " + e);
                setGoogleSignInException(e);
            }
        }
    }

    public void signInWithCredential(String idToken, Activity activity) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser mUser = mAuth.getCurrentUser();
                        updateUI(mUser, activity);
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(null, activity);
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        setErrorMessage("Failed to sign in with Firebase");
                    }
                });
    }

    public void signIn(ActivityResultLauncher<IntentSenderRequest> loginResultLauncher) {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(result -> {
                    IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent()).build();
                    loginResultLauncher.launch(intentSenderRequest);
                })
                .addOnFailureListener(e -> {
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    setErrorMessage("OneTapSignInEx: " + e.getLocalizedMessage());
                });
    }

    private void updateUI(FirebaseUser user, Activity activity) {
        if (user != null) {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finishAffinity();
        } else {
            setErrorMessage("Sign-in failed. Please try again later.");
        }
    }

    public LiveData<String> getIdToken() {
        return idToken;
    }

    public void setIdToken(String token) {
        this.idToken.setValue(token);
    }

    public void setGoogleSignInException(ApiException e) {
        this.googleSignInException.setValue(e);
    }

    protected void setErrorMessage(String message) {
        errorMessage.postValue(message);
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
