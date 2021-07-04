package com.example.covidmap.api

import com.example.covidmap.model.CenterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @GET("15077586/v1/centers")
    @Headers("content-type: text/plain; charset=utf8")
    fun searchCenter(
        @Query("page") page:Int,
        @Query("perPage") perPage:Int=10,
    ): Call<CenterResponse>
}