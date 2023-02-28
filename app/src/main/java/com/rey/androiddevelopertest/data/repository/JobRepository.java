package com.rey.androiddevelopertest.data.repository;

import com.rey.androiddevelopertest.data.model.Job;
import com.rey.androiddevelopertest.data.service.JobAPI;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JobRepository {

    private final JobAPI jobAPI;


    public JobRepository() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dev3.dansmultipro.co.id/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        this.jobAPI = retrofit.create(JobAPI.class);

    }

    public Call<List<Job>> getJobs() {
        return jobAPI.getAllJobs();
    }

    public Call<Job> getJobById(String id) {
        return jobAPI.getJobById(id);
    }

    public Call<List<Job>> searchJobs(String description, String location, boolean fulltime, int page) {
        return jobAPI.searchJobs(description, location, fulltime, page);
    }

}
