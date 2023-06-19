package com.pixabay.imageloaderapp.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pixabay.imageloaderapp.data.models.entities.ImagesEntity


/**
 * Used to query cached Images
 */
@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImagesEntity>)

    @Query("SELECT * FROM image_table WHERE searchTerm LIKE :query")
    fun queryImages(query: String): PagingSource<Int, ImagesEntity>

    @Query("DELETE FROM image_table")
    suspend fun clearAll()

    @Query("SELECT * FROM image_table")
    suspend fun getAll(): List<ImagesEntity>

}