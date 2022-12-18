package com.example.test20221209_audreyange_nycschools.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test20221209_audreyange_nycschools.Config
import com.example.test20221209_audreyange_nycschools.base.BaseViewModel
import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.repo.SchoolRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SchoolListViewModel(repository: SchoolRepository) : BaseViewModel(repository) {
    @VisibleForTesting val cache: ArrayList<School> = ArrayList()
    val schoolListLiveData: MutableLiveData<List<School>> = MutableLiveData()
    val searchSchoolLiveData: MutableLiveData<List<School>> = MutableLiveData()
    var fetchType: FetchType = FetchType.DEFAULT
    private var offset: Int = 0
    private var nextOffset: Int = Config.OFFSET_INC_VALUE

    fun canLoadMore(): Boolean {
        return offset < nextOffset
    }

    fun loadMore() {
        if (canLoadMore()) {
            offset = nextOffset
            fetchSchoolList(FetchType.LOAD_MORE)
        }
    }

    fun refreshData() {
        offset = 0
        cache.clear()
        fetchSchoolList(FetchType.REFRESH)
    }

    fun fetchSchools() {
        if (schoolListLiveData.value == null || cache.isEmpty()) {
            fetchSchoolList()
            return
        }
        // Read from cache if we already have a valid response
        schoolListLiveData.value = cache

    }

    private fun fetchSchoolList(fetchType: FetchType = FetchType.DEFAULT) {
        Log.i("SchoolList", "loading data : $fetchType")
        this.fetchType = fetchType
        viewModelScope.launch {
            repository.fetchSchoolList(offset).catch {
                schoolListLiveData.value = cache
            }.collect {
                cache.addAll(it)
                if (fetchType == FetchType.LOAD_MORE) {
                    // Increment nextOffset for the next loading when there is a valid result
                    nextOffset =
                        if (it.isNotEmpty()) offset + Config.OFFSET_INC_VALUE else nextOffset
                }
                schoolListLiveData.value = it

            }
        }
    }

    fun searchSchool(queryText: String) {
        viewModelScope.launch {
            repository.fetchSchoolByName(queryText).catch {
                searchSchoolLiveData.value = ArrayList()
            }.collect {
                searchSchoolLiveData.value = it
            }
        }
    }

    fun filterSchool(queryText: String) {
        val filtered = schoolListLiveData.value?.filter { school ->
            school.schoolName.lowercase().contains(queryText.lowercase())
        } ?: ArrayList()
        searchSchoolLiveData.value = filtered
    }

    enum class FetchType {
        LOAD_MORE,
        REFRESH,
        DEFAULT
    }
}