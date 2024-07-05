package com.pixelfusion.accesio_utn.data

import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.util.Constants.ENDPOINT_LOGIN
import com.pixelfusion.accesio_utn.viewmodel.LoginViewModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiCredentials{

    @GET(ENDPOINT_LOGIN)
    suspend fun login(): Response<UsuarioData>

    @POST(ENDPOINT_LOGIN)
    suspend fun login(@Body login: LoginViewModel): Response<UsuarioData>
}