package com.example.test20221209_audreyange_nycschools.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test20221209_audreyange_nycschools.viewmodel.ViewModelFactory

abstract class BaseFragment : Fragment() {
    val viewModelFactory: ViewModelFactory = ViewModelFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        observeViewModel()
        return initFragmentView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(getActionBarViewData().actionBarHomeIcon)
            title = getActionBarViewData().actionBarTitle
        }
    }

    /**
     * Returns the ActionBarViewData.
     */
    abstract fun getActionBarViewData(): ActionBarViewData

    abstract fun observeViewModel()

    /**
     * returns the view that will be used to create the fragment view.
     */
    abstract fun initFragmentView(inflater: LayoutInflater): View

    /**
     * Represents the action bar view data which includes the action bar title and the drawable res
     * icon to display as a home menu icon on the action bar.
     */
    data class ActionBarViewData(
        val actionBarTitle: String,
        @DrawableRes val actionBarHomeIcon: Int
    )
}