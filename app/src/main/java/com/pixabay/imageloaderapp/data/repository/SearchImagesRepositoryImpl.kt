package com.pixabay.imageloaderapp.data.repository

import androidx.paging.*
import com.pixabay.imageloaderapp.data.api.ImageSearchApi
import com.pixabay.imageloaderapp.data.db.ImageSearchRoomDb
import com.pixabay.imageloaderapp.data.mediator.PixaBayRemoteMediator
import com.pixabay.imageloaderapp.domain.models.Image
import com.pixabay.imageloaderapp.domain.repositories.SearchImagesRepository
import com.pixabay.imageloaderapp.mappers.toDomainImage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchImagesRepositoryImpl @Inject constructor(
    private val imageSearchApi: ImageSearchApi,
    private val imageSearchRoomDb: ImageSearchRoomDb,
) : SearchImagesRepository {
    @ExperimentalPagingApi
    override fun searchImages(searchString: String): Flow<PagingData<Image>> {
        val pagingSourceFactory = { imageSearchRoomDb.imageDao().queryImages(searchString) }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = PixaBayRemoteMediator(
                searchString,
                imageSearchApi,
                imageSearchRoomDb
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { imageEntity ->
                imageEntity.toDomainImage()
            }
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

}