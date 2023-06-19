package com.pixabay.imageloaderapp.presentation.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pixabay.imageloaderapp.R
import com.pixabay.imageloaderapp.databinding.FragmentImageLoaderDetailsBinding
import com.pixabay.imageloaderapp.presentation.utils.changeStatusBar
import com.pixabay.imageloaderapp.presentation.utils.setToolbar
import com.pixabay.imageloaderapp.presentation.viewmodel.ImageLoaderViewModel

class ImageLoaderDetailFragment : Fragment(R.layout.fragment_image_loader_details) {
    private lateinit var binding: FragmentImageLoaderDetailsBinding
    private val viewModel: ImageLoaderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentImageLoaderDetailsBinding.bind(view)
        binding.image = viewModel.selectedImage
        loadTransition()

        binding.toolbar.apply {
            setToolbar(this)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    private fun loadTransition() {
        val imageUri = viewModel.selectedImage?.largeImageURL
        binding.imageView.apply {
             transitionName = imageUri
            Glide.with(requireContext())
                .load(imageUri)
                .into(this)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().changeStatusBar(false)
    }
}