package com.pixabay.imageloaderapp.domain.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.pixabay.imageloaderapp.domain.models.Image
import kotlinx.coroutines.flow.Flow

interface SearchImagesRepository {
    @ExperimentalPagingApi
    fun searchImages(searchString: String): Flow<PagingData<Image>>
}