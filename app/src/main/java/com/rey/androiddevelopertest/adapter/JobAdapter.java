package com.rey.androiddevelopertest.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rey.androiddevelopertest.MyApplication;
import com.rey.androiddevelopertest.R;
import com.rey.androiddevelopertest.data.model.Job;
import com.rey.androiddevelopertest.databinding.ItemLayoutJobListBinding;
import com.rey.androiddevelopertest.viewholder.JobViewHolder;
import com.rey.androiddevelopertest.viewmodel.JobViewModel;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {

    private List<Job> jobList;
    private JobAdapterInterface jobAdapterInterface;

    public interface JobAdapterInterface {
        void getJobsById(String id);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setJobList(List<Job> jobList, JobAdapterInterface jobAdapterInterface) {
        this.jobList = jobList;
        this.jobAdapterInterface = jobAdapterInterface;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutJobListBinding itemLayoutJoblistBinding = ItemLayoutJobListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new JobViewHolder(itemLayoutJoblistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        ItemLayoutJobListBinding binding = holder.binding;

        // check if job is null
        if (isJob(holder, job)) {
            // set binding
            setBind(binding, job);
        }

    }

    private void setBind(ItemLayoutJobListBinding binding, Job job) {
        // set title
        if (job.getTitle() != null) {
            binding.textFirst.setText(job.getTitle());
        } else {
            binding.textFirst.setText(R.string.text_null_object);
        }

        // set company name
        if (job.getCompany() != null) {
            binding.textTwo.setText(job.getCompany());
        } else {
            binding.textTwo.setText(R.string.text_null_object);
        }

        // set location
        if (job.getLocation() != null) {
            binding.textThird.setText(job.getLocation());
        } else {
            binding.textThird.setText(R.string.text_null_object);
        }

        // set image
        if (job.getUrl() != null) {
            Glide.with(MyApplication.getInstance()).load(job.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.ic_img_error)
                    .into(binding.imgCompany);
        }

        // click for detail
        binding.itemCardJob.setOnClickListener(view -> {
            String jobId = job.getId();
            jobAdapterInterface.getJobsById(jobId);
        });
    }

    private boolean isJob(JobViewHolder holder, Job job) {
        boolean isJob;
        if (job != null) {
            // show item view
            isJob = true;
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else {
            // hide item view
            isJob = false;
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
        return isJob;
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }
}
