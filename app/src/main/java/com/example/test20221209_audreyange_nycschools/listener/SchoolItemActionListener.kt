package com.example.test20221209_audreyange_nycschools.listener

import android.view.View
import com.example.test20221209_audreyange_nycschools.model.School

/**
 * Listens to the item actions in the recyclerview
 */
interface SchoolItemActionListener {
    /**
     * Called when the user clicks on a school item
     */
    fun onSchoolItemClicked(view: View, school: School)
}