package com.rey.androiddevelopertest.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rey.androiddevelopertest.databinding.ItemLayoutJobListBinding;

public class JobViewHolder extends RecyclerView.ViewHolder {

    public ItemLayoutJobListBinding binding;

    public JobViewHolder(@NonNull ItemLayoutJobListBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }
}
