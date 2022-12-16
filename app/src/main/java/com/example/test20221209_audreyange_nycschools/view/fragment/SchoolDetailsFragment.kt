package com.example.test20221209_audreyange_nycschools.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.test20221209_audreyange_nycschools.R
import com.example.test20221209_audreyange_nycschools.base.BaseFragment
import com.example.test20221209_audreyange_nycschools.databinding.SchoolDetailsBinding
import com.example.test20221209_audreyange_nycschools.model.School
import com.example.test20221209_audreyange_nycschools.model.SchoolScore
import com.example.test20221209_audreyange_nycschools.viewmodel.SchoolScoresViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class SchoolDetailsFragment : BaseFragment() {
    private lateinit var binding: SchoolDetailsBinding
    private lateinit var viewModel: SchoolScoresViewModel
    private var school: School? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[SchoolScoresViewModel::class.java]
        school = arguments?.getParcelable(SCHOOL_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.schoolName.text = school?.schoolName
        binding.description.text = school?.description
        viewModel.fetchSchoolScores(school?.id ?: "")
    }

    override fun getActionBarViewData(): ActionBarViewData =
        ActionBarViewData(school?.schoolName ?: getString(R.string.school_details_title), 0)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStackImmediate()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun observeViewModel() {
        viewModel.schoolScoresLiveData.observe(
            this,
            { response -> processChart(response) })
    }

    private fun processChart(scores: List<SchoolScore>) {
        if (scores.isEmpty()) {
            return
        }
        val score = scores[0]
        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(1f, score.satMath.toFloatOrNull() ?: 0f))
        entries.add(BarEntry(2f, score.satReading.toFloatOrNull() ?: 0f))
        entries.add(BarEntry(3f, score.satWriting.toFloatOrNull() ?: 0f))
        entries.add(BarEntry(4f, score.satTakers.toFloatOrNull() ?: 0f))

        val barDataSet = BarDataSet(entries, getString(R.string.sat_chart_label))
        barDataSet.colors = ColorTemplate.createColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f

        val barData = BarData(barDataSet)
        binding.chart.setFitBars(true)
        binding.chart.data = barData
        binding.chart.description.text = score.schoolName
        binding.chart.animateY(CHART_ANIM_MS)
    }

    private fun getXAxisValues(): ArrayList<String> {
        val axis: ArrayList<String> = ArrayList()
        axis.add(MATH)
        axis.add(READING)
        axis.add(WRITING)
        axis.add(TAKERS)
        return axis
    }

    override fun initFragmentView(inflater: LayoutInflater): View {
        binding = SchoolDetailsBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        const val TAG = "SchoolDetailsFragment"
        private const val SCHOOL_KEY = "school_key"
        private const val MATH = "Math"
        private const val READING = "Reading"
        private const val WRITING = "Writing"
        private const val TAKERS = "Takers"
        private const val CHART_ANIM_MS = 1000

        @JvmStatic
        fun newInstance(school: School): SchoolDetailsFragment {
            return SchoolDetailsFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(SCHOOL_KEY, school)
                arguments = bundle
            }
        }
    }
}