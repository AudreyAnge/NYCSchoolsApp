package com.example.test20221209_audreyange_nycschools.model

import com.google.gson.annotations.SerializedName

/**
 * Represents school scores to show in the details screen.
 */
data class SchoolScore(
    @SerializedName("dbn") val id: String,
    @SerializedName("school_name") val schoolName: String,
    @SerializedName("num_of_sat_test_takers") val satTakers: String,
    @SerializedName("sat_math_avg_score") val satMath: String,
    @SerializedName("sat_writing_avg_score") val satWriting: String,
    @SerializedName("sat_critical_reading_avg_score") val satReading: String
)
