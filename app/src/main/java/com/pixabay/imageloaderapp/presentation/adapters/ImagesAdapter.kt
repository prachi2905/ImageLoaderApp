package com.pixabay.imageloaderapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pixabay.imageloaderapp.databinding.ImageItemBinding
import com.pixabay.imageloaderapp.presentation.model.ImagePresentation
import com.pixabay.imageloaderapp.presentation.utils.IMAGE_VIEW_TYPE
import com.pixabay.imageloaderapp.presentation.utils.NETWORK_VIEW_TYPE
import com.pixabay.imageloaderapp.presentation.utils.imageDiffCallback


class ImagesAdapter(private val clicked: (ImagePresentation, ImageView) -> Unit) :
    PagingDataAdapter<ImagePresentation, ImagesAdapter.ImageViewHolder>(imageDiffCallback) {

    inner class ImageViewHolder(private val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imagePassed: ImagePresentation) {
            binding.apply {
                image = imagePassed
                tags.isSelected = true
                root.setOnClickListener {
                    clicked.invoke(imagePassed, imageView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = getItem(position)!!

        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            NETWORK_VIEW_TYPE
        } else {
            IMAGE_VIEW_TYPE
        }
    }
}