package com.pixabay.imageloaderapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pixabay.imageloaderapp.data.FIRST_PAGE
import com.pixabay.imageloaderapp.data.PAGED_DATA_PER_PAGE
import com.pixabay.imageloaderapp.data.api.ImageSearchApi
import com.pixabay.imageloaderapp.data.db.ImageSearchRoomDb
import com.pixabay.imageloaderapp.data.models.entities.ImagesEntity
import com.pixabay.imageloaderapp.mappers.toImageEntity
import com.pixabay.imageloaderapp.data.models.entities.RemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PixaBayRemoteMediator(
    private val query: String,
    private val imageSearchApi: ImageSearchApi,
    private val imageSearchRoomDb: ImageSearchRoomDb

) : RemoteMediator<Int, ImagesEntity>() {


    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImagesEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPage?.minus(1) ?: FIRST_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val imagesResponse = imageSearchApi.searchImages(
                searchString = query,
                page = page,
                per_page = PAGED_DATA_PER_PAGE
            )
            val images = imagesResponse.images
            val endOfPaginationReached = images.isEmpty()
            imageSearchRoomDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    imageSearchRoomDb.remoteKeyDao().clearRemoteKeys()
                    imageSearchRoomDb.imageDao().clearAll()
                }
                val prevPage = if (page == FIRST_PAGE) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1
                val keys = images.map {
                    RemoteKey(imageId = it.id, prevPage = prevPage, nextPage = nextPage)
                }
                imageSearchRoomDb.remoteKeyDao().insertAll(keys)
                imageSearchRoomDb.imageDao()
                    .insertAll(images.map { it.toImageEntity(searchSting = query) })
            }
            return MediatorResult.Success(endOfPaginationReached = false)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ImagesEntity>): RemoteKey? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                imageSearchRoomDb.remoteKeyDao().remoteKeysImageId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ImagesEntity>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                imageSearchRoomDb.remoteKeyDao().remoteKeysImageId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImagesEntity>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                imageSearchRoomDb.remoteKeyDao().remoteKeysImageId(repoId)
            }
        }
    }
}