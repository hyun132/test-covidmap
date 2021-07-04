package com.example.covidmap.api

import com.example.covidmap.constant.COVIDConstants.BASE_URL
import com.example.covidmap.constant.COVIDConstants.REQUEST_KEY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class Service {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Infuser $REQUEST_KEY" )
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(interceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api: Api by lazy { retrofit.create(Api::class.java) }
    }
}