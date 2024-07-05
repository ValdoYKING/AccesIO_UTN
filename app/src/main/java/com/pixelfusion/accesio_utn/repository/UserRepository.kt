package com.pixelfusion.accesio_utn.repository

import com.pixelfusion.accesio_utn.data.ApiCredentials
import com.pixelfusion.accesio_utn.model.UsuarioData
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(private val apiCredentials: ApiCredentials) {

    suspend fun login(): Response<UsuarioData> {
        return apiCredentials.login()
    }
}