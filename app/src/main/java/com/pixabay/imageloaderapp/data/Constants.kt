package com.pixabay.imageloaderapp.data

import com.pixabay.imageloaderapp.BuildConfig


const val DB_NAME = "pixabay_image_loader_db"
const val BASE_URL = BuildConfig.BASE_URL
val IMAGE_TYPE = Pair("image_type", "photo")
val KEY = Pair("key", BuildConfig.KEY)
const val CACHE_NAME = "pixabay_cache"
const val FIRST_PAGE = 1
const val DEFAULT_SEARCH = "fruits"
const val PAGED_DATA_PER_PAGE = 50