package com.pixabay.imageloaderapp.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.pixabay.imageloaderapp.presentation.model.ImagePresentation

val imageDiffCallback = object : DiffUtil.ItemCallback<ImagePresentation>() {
    override fun areItemsTheSame(oldItem: ImagePresentation, newItem: ImagePresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ImagePresentation, newItem: ImagePresentation): Boolean {
        return oldItem == newItem
    }
}