package com.rey.androiddevelopertest.data.service;

import com.rey.androiddevelopertest.data.model.Job;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface JobAPI {

    @GET("recruitment/positions.json")
    Call<List<Job>>getAllJobs();

    @GET("recruitment/positions.json")
    Call<List<Job>>searchJobs(
            @Query("description") String description,
            @Query("location") String location,
            @Query("full_time") boolean fulltime,
            @Query("page") int page
    );

    @GET("recruitment/positions/{id}")
    Call<Job>getJobById(@Path("id") String id);

}
