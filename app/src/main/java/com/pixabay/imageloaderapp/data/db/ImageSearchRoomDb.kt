package com.pixabay.imageloaderapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pixabay.imageloaderapp.data.dao.ImageDao
import com.pixabay.imageloaderapp.data.models.entities.ImagesEntity

import com.pixabay.imageloaderapp.data.dao.RemoteKeyDao
import com.pixabay.imageloaderapp.data.models.entities.RemoteKey

/**
 * Room, architecture component build on top of Sqlite.
 * Used to cache images.
 */

@Database(
    entities = [ImagesEntity::class, RemoteKey::class],
    version = 6, exportSchema = false
)
abstract class ImageSearchRoomDb : RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}