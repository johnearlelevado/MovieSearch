package com.example.challenge.dagger.hilt

import com.example.challenge.BuildConfig
import com.example.challenge.api.ApiCore
import com.example.challenge.api.common.scheduler.AppSchedulerProvider
import com.example.challenge.api.common.scheduler.SchedulerProvider
import com.example.challenge.api.omdb.service.OmdbApiService
import com.example.challenge.repository.Repository
import com.example.challenge.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * For dependency injection, we use dagger hilt
 * **/

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofit() = ApiCore.getRetrofit(BuildConfig.API_URL)

    @Singleton
    @Provides
    fun provideScheduler():SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    fun provideOmdbApiService(retrofit : Retrofit):OmdbApiService
            = retrofit.create(OmdbApiService::class.java)

    @Singleton
    @Provides
    fun provideRepository(omdbApiService: OmdbApiService):Repository
            = RepositoryImpl(omdbApiService)
}