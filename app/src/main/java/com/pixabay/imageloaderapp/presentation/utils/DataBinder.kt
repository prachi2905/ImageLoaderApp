package com.pixabay.imageloaderapp.presentation.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Data binders to bind the tags and image to the views
 */
object DataBinder {
    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(imageView: ImageView, imageUrl: String?) {
        if (imageUrl != null && imageUrl.isNotEmpty()) {
            val progress = CircularProgressDrawable(imageView.context)
            progress.centerRadius = 25f
            progress.strokeWidth = 5f
            progress.start()
            Glide.with(imageView.context)
                .load(imageUrl)
                .placeholder(progress)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    //Its just adding "#" and don't need any translation
    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("modelTags")
    fun modelTags(textView: TextView, tags: String) {
        val tagList = tags.replace(", ", ", #") //Replacing with #
        textView.text = "#${tagList}"
    }

}