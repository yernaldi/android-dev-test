package com.rey.androiddevelopertest.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;
    private final int visibleThreshold = 5;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    protected abstract void loadMoreItems(int page);

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

    private int getTotalItemCount() {
        return layoutManager.getItemCount();
    }

    private int getLastVisibleItemPosition() {
        return layoutManager.findLastVisibleItemPosition();
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount = getTotalItemCount();
        int lastVisibleItemPosition = getLastVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((totalItemCount - lastVisibleItemPosition) <= visibleThreshold) {
                currentPage++;
                loadMoreItems(currentPage);
                isLoading = true;
            }
        }
    }

    public void reset() {
        currentPage = 1;
        isLoading = false;
        isLastPage = false;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
