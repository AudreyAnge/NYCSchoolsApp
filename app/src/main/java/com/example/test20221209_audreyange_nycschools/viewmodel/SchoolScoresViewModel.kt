package com.example.test20221209_audreyange_nycschools.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.test20221209_audreyange_nycschools.base.BaseViewModel
import com.example.test20221209_audreyange_nycschools.model.SchoolScore
import com.example.test20221209_audreyange_nycschools.repo.SchoolRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SchoolScoresViewModel(repository: SchoolRepository) : BaseViewModel(repository) {
    val schoolScoresLiveData: MutableLiveData<List<SchoolScore>> = MutableLiveData()

    fun fetchSchoolScores(id: String) {
        if (schoolScoresLiveData.value == null) {
            viewModelScope.launch {
                repository.fetchSchoolScoreById(id).catch {
                    schoolScoresLiveData.value = ArrayList()
                }.collect { schoolScoresLiveData.value = it ?: ArrayList() }
            }
        }
    }
}