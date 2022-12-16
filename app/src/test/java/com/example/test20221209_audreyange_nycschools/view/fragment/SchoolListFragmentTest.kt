package com.example.test20221209_audreyange_nycschools.view.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class SchoolListFragmentTest {

    private lateinit var fragment: FragmentScenario<SchoolListFragment>

    @Before
    fun setUp() {
        fragment = launchFragment()
    }

    @Test
    fun fetchSchoolListWhenFragmentStartsInDefaultMode() {

    }

    @Test
    fun filterSchoolListWhenFragmentStartsInSearchMode() {

    }
}