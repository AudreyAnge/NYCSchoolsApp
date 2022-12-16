package com.example.test20221209_audreyange_nycschools.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test20221209_audreyange_nycschools.repo.SchoolRepository

/**
 * Factory that create [ViewModel] instances.
 */
class ViewModelFactory : ViewModelProvider.Factory {
    private val repository = SchoolRepository

    @Suppress("UNCHECKED_CAST") // Safe from type checking
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SchoolListViewModel::class.java)) {
            return SchoolListViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(SchoolScoresViewModel::class.java)) {
            return SchoolScoresViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown view model")
    }
}