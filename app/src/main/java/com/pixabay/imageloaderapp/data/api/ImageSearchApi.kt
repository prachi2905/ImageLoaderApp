package com.pixabay.imageloaderapp.data.api

import com.pixabay.imageloaderapp.data.models.dtos.ImageResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchApi {

    @GET("api/")
    suspend fun searchImages(
        @Query("q") searchString: String? = null,
        @Query("per_page") per_page: Int? = null,
        @Query("page") page: Int? = null,
    ): ImageResponseDto
}