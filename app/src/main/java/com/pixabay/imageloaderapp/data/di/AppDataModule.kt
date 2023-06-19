package com.pixabay.imageloaderapp.data.di

import android.content.Context
import androidx.room.Room
import com.pixabay.imageloaderapp.BuildConfig
import com.pixabay.imageloaderapp.data.BASE_URL
import com.pixabay.imageloaderapp.data.DB_NAME
import com.pixabay.imageloaderapp.data.IMAGE_TYPE
import com.pixabay.imageloaderapp.data.KEY
import com.pixabay.imageloaderapp.data.api.ImageSearchApi
import com.pixabay.imageloaderapp.data.dao.ImageDao
import com.pixabay.imageloaderapp.data.db.ImageSearchRoomDb
import com.pixabay.imageloaderapp.data.repository.SearchImagesRepositoryImpl
import com.pixabay.imageloaderapp.domain.repositories.SearchImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt Module, used to provide dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object AppDataModule {
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ImageSearchApi = retrofit.create(ImageSearchApi::class.java)

    @Provides
    @Singleton
    fun providesRepository(
        imageSearchApi: ImageSearchApi,
        imageSearchRoomDb: ImageSearchRoomDb,
    ): SearchImagesRepository = SearchImagesRepositoryImpl(imageSearchApi, imageSearchRoomDb)


    private val apiInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        val originalHttpUrl = chain.request().url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(KEY.first, KEY.second)
            .addQueryParameter(IMAGE_TYPE.first, IMAGE_TYPE.second)
            .build()
        request.url(url)
        chain.proceed(request.build())
    }

    @Singleton
    @Provides
    fun providesPixaBayRoomDb(@ApplicationContext appContext: Context): ImageSearchRoomDb {
        return Room.databaseBuilder(
            appContext,
            ImageSearchRoomDb::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun providesImageDao(imageSearchRoomDb: ImageSearchRoomDb): ImageDao {
        return imageSearchRoomDb.imageDao()
    }
}