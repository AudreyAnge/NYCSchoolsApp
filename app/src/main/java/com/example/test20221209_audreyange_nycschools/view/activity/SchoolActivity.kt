package com.example.test20221209_audreyange_nycschools.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.test20221209_audreyange_nycschools.R
import com.example.test20221209_audreyange_nycschools.databinding.SchoolActivityMainBinding
import com.example.test20221209_audreyange_nycschools.view.fragment.SchoolListFragment

class SchoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SchoolActivityMainBinding.inflate(LayoutInflater.from(applicationContext)).root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, SchoolListFragment(), SchoolListFragment.TAG)
                .commit()
        }
    }
}