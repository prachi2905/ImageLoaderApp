package com.pixabay.imageloaderapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixabay.imageloaderapp.databinding.NetworkStateItemBinding

/**
 * Show a new item of loading ui when the user scroll to bottom of adapter when loading
 * Also shows error when the next page couldnt be fetched.
 */
class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        val progress = holder.binding.progressBarItem
        val txtErrorMessage = holder.binding.errorMsgItem
        val errorBtn = holder.binding.retryBtn

        progress.isVisible = loadState is LoadState.Loading
        txtErrorMessage.isVisible = loadState is LoadState.Error
        errorBtn.isVisible = loadState is LoadState.Error

        if (loadState is LoadState.Error) {
            txtErrorMessage.text = loadState.error.localizedMessage
        }
        errorBtn.setOnClickListener {
            retry.invoke()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            NetworkStateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class LoadStateViewHolder(val binding: NetworkStateItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}