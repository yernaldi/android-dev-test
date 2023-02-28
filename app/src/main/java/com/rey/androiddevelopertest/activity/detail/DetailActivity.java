package com.rey.androiddevelopertest.activity.detail;

import static com.rey.androiddevelopertest.util.constant.EXTRAS_JOB_DETAIL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.androiddevelopertest.MyApplication;
import com.rey.androiddevelopertest.R;
import com.rey.androiddevelopertest.data.model.Job;
import com.rey.androiddevelopertest.databinding.ActivityDetailBinding;
import com.rey.androiddevelopertest.databinding.ItemLayoutJobListBinding;
import com.rey.androiddevelopertest.databinding.LayoutSpecificationBinding;
import com.rey.androiddevelopertest.viewmodel.JobViewModel;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding binding;
    ItemLayoutJobListBinding itemCompanyBinding;
    LayoutSpecificationBinding itemDetailBinding;
    JobViewModel jobViewModel;
    Job jobDetails;

    String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        itemCompanyBinding = binding.itemLayoutJobList;
        itemDetailBinding = binding.layoutSpecification;
        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        setContentView(binding.getRoot());

        // get detail
        getDetails();

        // button back
        binding.btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void getDetails() {
        String extrasID = getIntent().getStringExtra(EXTRAS_JOB_DETAIL);
        jobViewModel.getJobsById(extrasID);
        jobViewModel.getJobIntentData().observe(this, job -> {
            jobDetails = job;
            setDetails();
            Log.d(TAG, "getDetails: " + jobDetails);
        });
    }

    private void setDetails() {
        // company
        itemCompanyBinding.itemCardJob.setClickable(false);
        itemCompanyBinding.textFirst.setText(jobDetails.getCompany());
        itemCompanyBinding.textTwo.setText(jobDetails.getLocation());
        itemCompanyBinding.textThird.setText(R.string.text_go_to_web);
        itemCompanyBinding.textThird.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.teal_200));
        itemCompanyBinding.textThird.setOnClickListener(view -> {
            if (jobDetails.getCompany_url() != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jobDetails.getCompany_url()));
                startActivity(browserIntent);
            }
            else {
                Log.d(TAG, "setDetails: url " + jobDetails.getCompany_url() );
                Toast.makeText(this, "This company has no website!", Toast.LENGTH_SHORT).show();
            }

        });

        // set image
        Glide.with(this).load(jobDetails.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_img_error)
                .into(itemCompanyBinding.imgCompany);

        // detail
        itemDetailBinding.textTitle.setText(jobDetails.getTitle());
        itemDetailBinding.textFulltime.setText(jobDetails.getType());
        itemDetailBinding.textDesc.setText(jobDetails.getDescription());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}