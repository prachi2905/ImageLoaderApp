package com.pixabay.imageloaderapp.presentation.mappers

import com.pixabay.imageloaderapp.domain.models.Image
import com.pixabay.imageloaderapp.presentation.model.ImagePresentation


fun Image.toImagePresentation(): ImagePresentation {
    return ImagePresentation(
        comments, downloads, id, largeImageURL, likes, tags, user, user_id, views, searchTerm
    )
}