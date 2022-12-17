package com.example.test20221209_audreyange_nycschools.view.fragment

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.test20221209_audreyange_nycschools.R
import com.example.test20221209_audreyange_nycschools.base.BaseFragment
import com.example.test20221209_audreyange_nycschools.databinding.SchoolListFragmentBinding
import com.example.test20221209_audreyange_nycschools.listener.SchoolItemActionListener
import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.view.adapter.SchoolListAdapter
import com.example.test20221209_audreyange_nycschools.viewmodel.SchoolListViewModel

class SchoolListFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: SchoolListFragmentBinding
    private lateinit var viewModel: SchoolListViewModel
    private var adapter: SchoolListAdapter? = null
    private var mode: SurfaceMode = SurfaceMode.DEFAULT
    private lateinit var searchView : SearchView
    private var query : CharSequence? = null
    private val itemClickListener = object : SchoolItemActionListener {
        override fun onSchoolItemClicked(view: View, school: School) {
            when (view.id) {
                R.id.call -> startActivitySafely(getPhoneIntent(school))
                R.id.website -> startActivitySafely(getWebsiteIntent(school))
                R.id.map -> startActivitySafely(getDirectionsIntent(school))
                else -> parentFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    SchoolDetailsFragment.newInstance(school),
                    SchoolDetailsFragment.TAG
                ).addToBackStack(null)
                    .commit()
            }
        }
    }
    private val listScrollListener = object : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            // We load more when 3 conditions are met: the UI is not already in the loading state, the user in not searching for a school and when we reach the last item in the list.
            if (!binding.swipeRefreshLayout.isRefreshing && mode == SurfaceMode.DEFAULT && layoutManager.findLastVisibleItemPosition() == (recyclerView.adapter?.itemCount?.minus(
                    1
                ) ?: -1)
            ) {
                binding.swipeRefreshLayout.isRefreshing = viewModel.canLoadMore()
                viewModel.loadMore()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = savedInstanceState?.getCharSequence(SEARCH_QUERY_TEXT)
        mode = if (!TextUtils.isEmpty(query)) SurfaceMode.SEARCH else SurfaceMode.DEFAULT
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SchoolListAdapter(ArrayList(), itemClickListener)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addOnScrollListener(listScrollListener)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.isRefreshing = true
        if (mode == SurfaceMode.DEFAULT) {
            viewModel.fetchSchools()
        } else {
            viewModel.filterSchool(query.toString())
        }
    }

    override fun initFragmentView(inflater: LayoutInflater): View {
        binding = SchoolListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun getActionBarViewData(): ActionBarViewData =
        ActionBarViewData(getString(R.string.school_list_title), R.drawable.ic_home)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = (menu.findItem(R.id.search).actionView) as SearchView
        searchView.apply {
            queryHint = getString(R.string.school_search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setOnQueryTextListener(searchQueryListener)
        }
        searchView.setOnQueryTextFocusChangeListener { _: View, hasFocus: Boolean ->
            mode = if (hasFocus) SurfaceMode.SEARCH else SurfaceMode.DEFAULT
            Log.i(TAG, "Activated SurfaceMode : $mode")
            if (mode == SurfaceMode.DEFAULT) {
                viewModel.fetchSchools()
            }
        }
        val searchCloseButton =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        searchCloseButton?.setOnClickListener {
            searchView.setQuery("", false)
            searchView.isIconified = true
            searchView.onActionViewCollapsed()
        }
        // Restore the search query when the query is not empty
        if (!query.isNullOrEmpty()) {
            searchView.setQuery(query, false)
            searchView.isIconified = false
        }
    }

    private val searchQueryListener: OnQueryTextListener = object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (TextUtils.isEmpty(query)) {
                return false
            }
            Log.i(TAG, "school search query text $query")
            viewModel.searchSchool(query?.trim() ?: "")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            viewModel.filterSchool(newText?.trim() ?: "")
            return true
        }
    }

    override fun observeViewModel() {
        viewModel.schoolListLiveData.observe(
            this,
            Observer { schoolList ->
                binding.swipeRefreshLayout.isRefreshing = false
                if (viewModel.fetchType == SchoolListViewModel.FetchType.LOAD_MORE) {
                    adapter?.addSchools(schoolList)
                    return@Observer
                }
                handleViewsVisibility(schoolList.isNotEmpty())
                adapter?.setSchoolList(schoolList)
            })
        viewModel.searchSchoolLiveData.observe(
            this
        ) { response ->
            handleViewsVisibility(response.isNotEmpty())
            adapter?.setSchoolList(response)
        }
    }

    override fun onRefresh() {
        //refresh data list here then after we get the fetching response, we set the loading state to false.
        Log.i(TAG, "refreshing data")
        viewModel.refreshData()
    }

    private fun handleViewsVisibility(shouldShowList: Boolean = true) {
        if (shouldShowList) {
            binding.recyclerView.visibility = View.VISIBLE
            binding.emptyListView.visibility = View.GONE
        } else {
            binding.recyclerView.visibility = View.GONE
            binding.emptyListView.visibility = View.VISIBLE
            binding.emptyListView.text =
                getString(if (mode == SurfaceMode.SEARCH) R.string.empty_search_result_text else R.string.empty_list_text)
        }

    }

    private fun startActivitySafely(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to start intent : $intent", e)
            // We could display a toast here if the operation fails.
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::searchView.isInitialized) {
            outState.putCharSequence(SEARCH_QUERY_TEXT, searchView.query)
        }
    }

    /**
     * Represents the different UI states. The DEFAULT state will show the regular school list
     * fetched when the app started. The SEARCH state is triggered when the search icon is clicked.
     * This would mean we're showing
     */
    private enum class SurfaceMode {
        DEFAULT,
        SEARCH
    }

    companion object {
        const val TAG = "SchoolListFragment"
        const val SEARCH_QUERY_TEXT = "search_query_text"

        @JvmStatic
        fun getWebsiteIntent(school: School): Intent {
            val website = StringBuilder(school.website)
            if (website.startsWith("www")) {
                website.insert(0, "https://")
            }
            return Intent(Intent.ACTION_VIEW, Uri.parse(website.toString()))
        }

        @JvmStatic
        fun getPhoneIntent(school: School): Intent {
            return Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${school.phone}") }
        }

        @JvmStatic
        fun getDirectionsIntent(school: School): Intent {
            val uri = Uri.parse("geo:${school.latitude},${school.longitude}(${school.schoolName})")
            return Intent(Intent.ACTION_VIEW, uri)
        }
    }
}