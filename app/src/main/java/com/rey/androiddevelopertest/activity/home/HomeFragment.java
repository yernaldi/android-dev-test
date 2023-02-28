package com.rey.androiddevelopertest.activity.home;

import static com.rey.androiddevelopertest.util.constant.EXTRAS_JOB_DETAIL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rey.androiddevelopertest.R;
import com.rey.androiddevelopertest.activity.detail.DetailActivity;
import com.rey.androiddevelopertest.adapter.JobAdapter;
import com.rey.androiddevelopertest.data.model.Job;
import com.rey.androiddevelopertest.databinding.FragmentHomeBinding;
import com.rey.androiddevelopertest.databinding.LayoutSearchBinding;
import com.rey.androiddevelopertest.util.PaginationScrollListener;
import com.rey.androiddevelopertest.viewmodel.JobViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements JobAdapter.JobAdapterInterface{

    private FragmentHomeBinding binding;
    private LayoutSearchBinding searchBinding;
    private JobViewModel viewModel;
    private JobAdapter jobAdapter;

    private final List<Job> jobList = new ArrayList<>();
    int FILTER_CLICK = 0;
    boolean FULLTIME_STATUS = true;
    String LOCATION = "", DESCRIPTION = "";

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 10;

    public static final String TAG = "JobListFragment";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchBinding = binding.layoutSearch;
        viewModel = new ViewModelProvider(this).get(JobViewModel.class);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set list job
        setJob();

        // set job filter
        setFilter();

        // set scrolling
        setScrollListener();

    }

    private void setJob() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        jobAdapter = new JobAdapter();
        binding.recyclerView.setAdapter(jobAdapter);
        viewModel.loadJobs();
        viewModel.getJobs().observe(requireActivity(), jobs -> {
            jobList.clear();
            jobList.addAll(jobs);
            jobAdapter.setJobList(jobList, this);
        });
    }

    private void setScrollListener() {
        binding.recyclerView.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) binding.recyclerView.getLayoutManager()) {

            private static final long DELAY_MILLIS = 1000; // 500 ms delay
            private final Handler handler = new Handler();
            private Runnable runnable;

            @Override
            protected void loadMoreItems(int page) {
                if (!isLoading) {
                    isLoading = true;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "loadMoreItems: current page " + page);

                    // Cancel previous delayed runnable
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                    }

                    // Delay loading of next page by 500 ms
                    runnable = () -> {
                        loadNextPage(page);
                    };
                    handler.postDelayed(runnable, DELAY_MILLIS);
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

        });
    }

    private void loadNextPage(int page) {
        // Remove the observer to avoid getting updates from the previous page
        viewModel.getJobs().removeObservers(getViewLifecycleOwner());
        viewModel.searchJobs(DESCRIPTION, LOCATION, FULLTIME_STATUS, page);

        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            try {
                isLoading = false;
                jobList.addAll(jobs);
                jobAdapter.setJobList(jobList, this);
                binding.progressBar.setVisibility(View.GONE);
                if (jobs.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
            }
            catch (Exception e) {
                // Handle any exceptions that may occur
                binding.progressBar.setVisibility(View.GONE);
                viewModel.setError("Load next page error: " + e);
            }
        });
    }

    private void setFilter() {
        // show hide
        searchBinding.btnFilter.setOnClickListener(view -> {
            FILTER_CLICK++;
            if (FILTER_CLICK == 1) {
                searchBinding.layoutFilter.setVisibility(View.VISIBLE);
            } else {
                FILTER_CLICK = 0;
                searchBinding.layoutFilter.setVisibility(View.GONE);
            }
        });

        // get switch full time
        searchBinding.buttonSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            FULLTIME_STATUS = isChecked;
        });

        // search
        searchBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Perform search with the current text in the EditText
                String searchText = s.toString().trim(); // Get the search text from the EditText
                searchJobs(searchText); // Call the method to search for jobs with the search text
            }
        });

        // apply filter
        searchBinding.buttonApplyFilter.setOnClickListener(view -> {
            searchBinding.layoutFilter.setVisibility(View.GONE);
            applyFilter();
        });
    }

    private void searchJobs(String searchText) {
        if (!searchText.isEmpty()) {
            DESCRIPTION = searchText;
        }
        else {
            DESCRIPTION = "";
        }

        if (LOCATION.isEmpty()) {
            LOCATION = "";
        }

        viewModel.searchJobs(DESCRIPTION, LOCATION, FULLTIME_STATUS, currentPage);
        binding.progressBar.setVisibility(View.GONE);
    }

    private void applyFilter() {
        LOCATION = searchBinding.etLocation.getText().toString();
        FULLTIME_STATUS = searchBinding.buttonSwitch.isChecked();
        searchJobs(searchBinding.etSearch.getText().toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void getJobsById(String id) {
        // open detail activity
        Intent intent = new Intent(requireContext(), DetailActivity.class);
        intent.putExtra(EXTRAS_JOB_DETAIL, id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireActivity().startActivity(intent);
    }
}