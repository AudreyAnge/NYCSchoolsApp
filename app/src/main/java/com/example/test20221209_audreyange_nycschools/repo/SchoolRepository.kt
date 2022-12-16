package com.example.test20221209_audreyange_nycschools.repo

import com.example.test20221209_audreyange_nycschools.Config
import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.model.SchoolScore
import com.example.test20221209_audreyange_nycschools.rest.RestApiFactory
import com.example.test20221209_audreyange_nycschools.rest.SchoolRestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

/**
 * Handles data operations and provides a clean API with SchoolRestApi to gather data.
 */
object SchoolRepository {

    private val schoolService = RestApiFactory.createRestApi(SchoolRestApi::class.java)

    fun fetchSchoolList(offset: Int): Flow<List<School>?> {
        val queryMap = HashMap<String, String>().apply {
            put(Config.LIMIT_PARAM, Config.LIMIT_VALUE.toString())
            put(Config.OFFSET_PARAM, offset.toString())
            put(Config.ORDER_PARAM, Config.ORDER_VALUE)
        }
        return startSchoolListFetching(queryMap)
    }

    fun fetchSchoolByName(name: String): Flow<List<School>?> {
        val queryMap = HashMap<String, String>().apply {
            put(Config.SCHOOL_NAME_PARAM, name)
        }
        return startSchoolListFetching(queryMap)
    }

    @OptIn(FlowPreview::class)
    private fun startSchoolListFetching(queryMap: HashMap<String, String>): Flow<List<School>?> {
        return flow { emit(schoolService.getSchoolList(queryMap)) }.flatMapMerge { value ->
            flow {
                emit(
                    value.body()
                )
            }
        }
            .flowOn(Dispatchers.IO)
    }

    @OptIn(FlowPreview::class)
    suspend fun fetchSchoolScoreById(id: String): Flow<List<SchoolScore>?> {
        val queryMap = HashMap<String, String>().apply {
            put(Config.ID_PARAM, id)
        }
        return flow { emit(schoolService.getSchoolScoreById(queryMap)) }.flatMapMerge { value ->
            flow {
                emit(
                    value.body()
                )
            }
        }.flowOn(Dispatchers.IO)
    }
}