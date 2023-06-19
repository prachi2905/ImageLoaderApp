package com.pixabay.imageloaderapp.mappers

import com.pixabay.imageloaderapp.data.models.dtos.ImageDto
import com.pixabay.imageloaderapp.data.models.entities.ImagesEntity
import com.pixabay.imageloaderapp.domain.models.Image

fun ImagesEntity.toDomainImage(): Image {
    return Image(
        comments = comments,
        downloads = downloads,
        id = id,
        largeImageURL = largeImageURL,
        likes = likes,
        tags = tags,
        user = user,
        user_id = user_id,
        views = views,
        searchTerm = searchTerm
    )
}

fun ImageDto.toImageEntity(searchSting:String): ImagesEntity {
    return ImagesEntity(
        comments = comments,
        downloads = downloads,
        id = id,
        largeImageURL = largeImageURL,
        likes = likes,
        tags = tags,
        user = user,
        user_id = user_id,
        views = views,
        searchTerm = searchSting
    )
}