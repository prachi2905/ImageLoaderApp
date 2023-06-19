package com.pixabay.imageloaderapp.domain.usecases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.pixabay.imageloaderapp.domain.models.Image
import com.pixabay.imageloaderapp.domain.repositories.SearchImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val searchImagesRepository: SearchImagesRepository) {

    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(payload: String): Flow<PagingData<Image>> {
        return searchImagesRepository.searchImages(payload)
    }
}