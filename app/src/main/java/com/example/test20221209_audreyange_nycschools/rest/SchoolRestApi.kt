package com.example.test20221209_audreyange_nycschools.rest

import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.model.SchoolScore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Communicates with the servers to extract the data we need.
 */
interface SchoolRestApi {

    @GET("s3k6-pzi2.json")
    suspend fun getSchoolList(@QueryMap(encoded = true) filters: Map<String, String>): Response<List<School>>

    @GET("f9bf-2cp4.json")
    suspend fun getSchoolScoreById(@QueryMap(encoded = true) filters: Map<String, String>): Response<List<SchoolScore>>
}