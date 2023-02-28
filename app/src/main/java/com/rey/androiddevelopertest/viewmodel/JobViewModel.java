package com.rey.androiddevelopertest.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rey.androiddevelopertest.MyApplication;
import com.rey.androiddevelopertest.data.model.Job;
import com.rey.androiddevelopertest.data.repository.JobRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobViewModel extends ViewModel {

    private final JobRepository jobRepository;
    private final MutableLiveData<List<Job>> jobListLiveData;
    private final MutableLiveData<Job> jobIntentData;
    private final MutableLiveData<Boolean> loadingLiveData;
    private final MutableLiveData<String> errorLiveData;

    public JobViewModel() {
        jobRepository = new JobRepository();
        jobListLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        jobIntentData = new MutableLiveData<>();
    }

    public void loadJobs() {
        loadingLiveData.setValue(true);
        jobRepository.getJobs().enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(@NonNull Call<List<Job>> call, @NonNull Response<List<Job>> response) {
                if (response.isSuccessful()) {
                    jobListLiveData.setValue(response.body());
                    loadingLiveData.setValue(true);
                }
                else {
                    loadingLiveData.setValue(false);
                    errorLiveData.setValue("Failed to get jobs");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Job>> call, @NonNull Throwable t) {

            }
        });
    }

    public void searchJobs(String description, String location, boolean fullTime, int page) {
        loadingLiveData.setValue(true);
        jobRepository.searchJobs(description, location, fullTime, page).enqueue(new Callback<List<Job>>() {
            @Override
            public void onResponse(@NonNull Call<List<Job>> call, @NonNull Response<List<Job>> response) {
                if (response.isSuccessful()) {
                    jobListLiveData.setValue(response.body());
                    loadingLiveData.setValue(false);
                } else {
                    loadingLiveData.setValue(false);
                    errorLiveData.setValue("Failed to get jobs");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Job>> call, @NonNull Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Failed to get jobs");
            }
        });
    }

    public void getJobsById(String id) {
        loadingLiveData.setValue(true);
        jobRepository.getJobById(id).enqueue(new Callback<Job>() {
            @Override
            public void onResponse(@NonNull Call<Job> call, @NonNull Response<Job> response) {
                if (response.isSuccessful()) {
                    loadingLiveData.setValue(false);
                    Job jobDetails = response.body();
                    jobIntentData.setValue(jobDetails);
                } else {
                    loadingLiveData.setValue(false);
                    errorLiveData.setValue("Failed to get jobs");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Job> call, @NonNull Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue("Failed to get jobs");
            }
        });
    }

    public LiveData<List<Job>> getJobs() {
        if (jobListLiveData.getValue() == null) {
            loadJobs();
        }
        return jobListLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void setError(String errorMessage) {
        errorLiveData.setValue(errorMessage);
    }

    public LiveData<Boolean> isLoading() {
        return loadingLiveData;
    }

    public LiveData<Job> getJobIntentData() {
        return jobIntentData;
    }
}
