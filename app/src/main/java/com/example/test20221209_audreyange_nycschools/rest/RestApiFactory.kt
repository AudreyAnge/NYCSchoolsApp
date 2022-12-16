package com.example.test20221209_audreyange_nycschools.rest

import com.example.test20221209_audreyange_nycschools.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Creates Rest Api using Retrofit.
 */
object RestApiFactory {

    private val retrofit: Retrofit
    private val okHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()

    init {
        okHttpClient.addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
        retrofit = Retrofit.Builder().baseUrl(Config.SCHOOL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            ).client(okHttpClient.build()).build()

    }

    @JvmStatic
    fun <S> createRestApi(service: Class<S>): S {
        return retrofit.create(service)
    }
}