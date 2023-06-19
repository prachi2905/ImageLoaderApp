package com.pixabay.imageloaderapp.dbtest

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.pixabay.imageloaderapp.data.dao.ImageDao
import com.pixabay.imageloaderapp.data.dao.RemoteKeyDao
import com.pixabay.imageloaderapp.data.db.ImageSearchRoomDb
import com.pixabay.imageloaderapp.data.models.dtos.ImageResponseDto
import com.pixabay.imageloaderapp.data.models.entities.RemoteKey
import com.pixabay.imageloaderapp.mappers.toImageEntity
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.InputStream
import kotlin.random.Random


class RoomDBTest {
    private lateinit var db: ImageSearchRoomDb
    private lateinit var context: Context
    private lateinit var remoteKeyDao: RemoteKeyDao
    private lateinit var imageDao: ImageDao
    private val gson = Gson()

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, ImageSearchRoomDb::class.java).build()
        val jsonStream: InputStream = context.resources.assets.open("response.json")
        val jsonBytes: ByteArray = jsonStream.readBytes()

        val images = gson.fromJson(String(jsonBytes), ImageResponseDto::class.java).images

        images.map {
            it.searchTerm = "fruits"
        }

        imageDao = db.imageDao()
        remoteKeyDao = db.remoteKeyDao()

        val keys = images.map {
            RemoteKey(nextPage = 1, prevPage = 0, imageId = it.id)
        }

        runBlocking {
            db.withTransaction {
                imageDao.insertAll(images.map { it.toImageEntity("fruits") })
                remoteKeyDao.insertAll(keys)
            }
        }
    }

    @After
    fun clear() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun testNumberOfKeysEqualsNumberOfImages() = runBlocking {
        MatcherAssert.assertThat(
            imageDao.getAll().size, CoreMatchers.equalTo(
                remoteKeyDao.getAll().size
            )
        )
    }

    @Test
    fun searchTermIsFruit() = runBlocking {
        val result = imageDao.getAll()[Random(0).nextInt(19)].searchTerm == "fruits"
        MatcherAssert.assertThat(result, CoreMatchers.equalTo(true))
    }
}