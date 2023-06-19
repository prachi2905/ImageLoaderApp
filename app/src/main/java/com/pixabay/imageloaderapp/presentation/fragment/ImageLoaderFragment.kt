package com.pixabay.imageloaderapp.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.pixabay.imageloaderapp.R
import com.pixabay.imageloaderapp.databinding.FragmentImageLoaderBinding
import com.pixabay.imageloaderapp.presentation.adapters.ImagesAdapter
import com.pixabay.imageloaderapp.presentation.adapters.LoadingStateAdapter
import com.pixabay.imageloaderapp.presentation.model.ImagePresentation
import com.pixabay.imageloaderapp.presentation.utils.*
import com.pixabay.imageloaderapp.presentation.viewmodel.ImageLoaderViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageLoaderFragment : Fragment(R.layout.fragment_image_loader) {
    private lateinit var binding: FragmentImageLoaderBinding
    private val adapter = ImagesAdapter { image, imageView -> navigate(image, imageView) }
    private val viewModel: ImageLoaderViewModel by activityViewModels()
    private var gridLayoutSpan = 2
    private var isInitiated = false
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentImageLoaderBinding.bind(view)

        binding.apply {
            setSearchViewListener()
            setUpAdapter()
            if (!isInitiated) init()

            binding.retryBtn.setOnClickListener {
                adapter.refresh()
            }
        }
    }

    private fun setUpAdapter() {
        val itemDecoration =
            ItemOffsetDecoration(requireContext(), R.dimen.item_margin)
        binding.recyclerView.addItemDecoration(itemDecoration)


        val currentOrientation = resources.configuration.orientation
        gridLayoutSpan = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            3
        } else {
            2
        }

        val gridLayoutManager = GridLayoutManager(requireContext(), gridLayoutSpan)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == IMAGE_VIEW_TYPE) 1
                else gridLayoutSpan
            }
        }
        binding.apply {
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.adapter = adapter
            recyclerView.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter { retry() })
        }

        adapter.addLoadStateListener { state ->

            binding.apply {
                progress.isVisible = state.refresh is LoadState.Loading
                emptySection.isVisible =
                    state.refresh is LoadState.NotLoading && adapter.itemCount == 0
                errorSection.isVisible =
                    state.refresh is LoadState.Error && adapter.snapshot().isEmpty()
            }
        }
    }

    private fun init() {
        isInitiated = true
        searchImages(viewModel.currentSearch)
        binding.searchView.apply {
            setText(viewModel.currentSearch)
            text?.length?.let { setSelection(it) }
        }
    }

    private fun searchImages(searchString: String, isUserInitiated: Boolean = false) {
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            if (isUserInitiated) adapter.submitData(PagingData.empty())
            viewModel.searchImages(searchString).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun setSearchViewListener() {
        binding.searchView.apply {
            addTextChangedListener { text: Editable? ->
                binding.cancelSearch.isVisible = text.toString().isNotEmpty()
            }
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = text.toString().trim()
                    if (query.isNotEmpty()) {
                        searchImages(query, true)
                    } else {
                        binding.searchView.text = null
                    }
                    hideSoftKeyboard(requireActivity())
                    return@OnEditorActionListener true
                }
                false
            })
        }
        binding.cancelSearch.setOnClickListener {
            binding.searchView.apply {
                text = null
                requestFocus()
            }
            showSoftKeyboard(requireActivity())
        }
    }

    fun navigate(image: ImagePresentation, imageView: ImageView) {
        viewModel.selectedImage = image
        val extras = FragmentNavigatorExtras(
            imageView to image.largeImageURL
        )
        val action = ImageLoaderFragmentDirections.toImageDetailFragment()
        findNavController().navigate(action, extras)
        changeStatusBarColorToBlack(requireActivity())
    }

    private fun retry() {
        adapter.retry()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().changeStatusBar(true)
        hideSoftKeyboard(requireActivity())
    }
}