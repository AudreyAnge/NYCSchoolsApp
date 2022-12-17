package com.example.test20221209_audreyange_nycschools.base

import androidx.lifecycle.ViewModel
import com.example.test20221209_audreyange_nycschools.repo.SchoolRepository

abstract class BaseViewModel(protected val repository: SchoolRepository) : ViewModel()