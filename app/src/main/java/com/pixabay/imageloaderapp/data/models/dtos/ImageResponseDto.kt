package com.pixabay.imageloaderapp.data.models.dtos

import com.google.gson.annotations.SerializedName

class ImageResponseDto(
    @SerializedName("hits")
    val images: List<ImageDto>
)