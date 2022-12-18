package com.example.test20221209_audreyange_nycschools.viewmodel

import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.repo.SchoolRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SchoolListViewModelTest {

    private lateinit var viewModel: SchoolListViewModel
    private val repo: SchoolRepository = mockk()
    private val school: School = mockk()

    @Before
    fun setUp() {
        coEvery { repo.fetchSchoolList(any()) } returns flow { emit(ArrayList<School>().apply { add(school) }) }
        viewModel = SchoolListViewModel(repo)
    }

    @Test
    fun testFetchSchools_returnsResults() {
        viewModel.fetchSchools()

        coEvery { repo.fetchSchoolList(any()) }
        assertNotNull(viewModel.schoolListLiveData.value)
        assertEquals(1, viewModel.schoolListLiveData.value?.size)
        assertEquals(school, viewModel.schoolListLiveData.value?.get(0))
    }

    @Test
    fun testLoadMore_loadsAdditionalData() {
        viewModel.fetchSchools()

        viewModel.loadMore()

        assertNotNull(viewModel.schoolListLiveData.value)
        assertEquals(1, viewModel.schoolListLiveData.value?.size)
        assertEquals(2, viewModel.cache.size)
        assertEquals(school, viewModel.cache[0])
        assertEquals(school, viewModel.cache[1])
    }

    @Test
    fun testRefreshData() {
        viewModel.fetchSchools()

        viewModel.refreshData()

        assertNotNull(viewModel.schoolListLiveData.value)
        assertEquals(1, viewModel.schoolListLiveData.value?.size)
        assertEquals(school, viewModel.schoolListLiveData.value?.get(0))
    }

    @Test
    fun testSearchSchool_returnsNonEmptySearchResults() {
        val schoolName = "School name"
        coEvery { school.schoolName } returns schoolName
        coEvery { repo.fetchSchoolByName(any()) } returns flow { emit(ArrayList<School>().apply { add(school) }) }

        viewModel.searchSchool(schoolName)

        assertNotNull(viewModel.searchSchoolLiveData.value)
        assertEquals(viewModel.searchSchoolLiveData.value?.isNotEmpty(), true)
    }

    @Test
    fun testSearchSchool_returnsEmptySearchResults() {
        val schoolName = "School name"
        coEvery { school.schoolName } returns schoolName
        coEvery { repo.fetchSchoolByName(any()) } returns flow { emit(emptyList() ) }

        viewModel.searchSchool("query")

        assertNotNull(viewModel.searchSchoolLiveData.value)
        assertEquals(viewModel.searchSchoolLiveData.value?.isEmpty(), true)
    }

    @Test
    fun testFilterSchool_returnsNonEmptyFilteredResult() {
        val schoolName = "School name"
        coEvery { school.schoolName } returns schoolName
        viewModel.fetchSchools()

        viewModel.filterSchool(schoolName)

        assertNotNull(viewModel.cache)
        assertEquals(viewModel.searchSchoolLiveData.value?.isNotEmpty(), true)
    }

    @Test
    fun testFilterSchool_returnsEmptyFilteredResult() {
        val schoolName = "School name"
        coEvery { school.schoolName } returns schoolName
        viewModel.fetchSchools()

        viewModel.filterSchool("query")

        assertNotNull(viewModel.cache)
        assertEquals(viewModel.searchSchoolLiveData.value?.isEmpty(), true)
    }
}