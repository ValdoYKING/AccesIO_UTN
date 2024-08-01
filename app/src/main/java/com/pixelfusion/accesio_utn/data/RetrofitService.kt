package com.pixelfusion.accesio_utn.data

import com.pixelfusion.accesio_utn.model.ResoultSchedule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {

    @GET("horario")
    suspend fun getHorario(): ResoultSchedule
}

object RetrofitServiceFactory {
    fun makeRetrofitService(): RetrofitService {
            return Retrofit.Builder()
                .baseUrl("http://localhost:8000/api/schedule/getSchedule")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)

    }
}
