package com.rey.androiddevelopertest.util;

import android.view.View;
import android.widget.ProgressBar;

import androidx.cardview.widget.CardView;

public class Buckets {
    public static void hideView(boolean HIDE, View view) {
        if (HIDE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

}
