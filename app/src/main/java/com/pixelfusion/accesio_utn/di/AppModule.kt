package com.pixelfusion.accesio_utn.di

import com.pixelfusion.accesio_utn.data.ApiCredentials
import com.pixelfusion.accesio_utn.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesAPiCredentials(retrofit: Retrofit) : ApiCredentials {
        return retrofit.create(ApiCredentials::class.java)
    }

}