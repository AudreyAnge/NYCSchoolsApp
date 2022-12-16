package com.example.test20221209_audreyange_nycschools.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Represents a school item to show on home screen.
 */
@Parcelize
data class School(
    @SerializedName("dbn") val id: String,
    @SerializedName("school_name") val schoolName: String,
    @SerializedName("overview_paragraph") val description: String,
    @SerializedName("location") val address: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("website") val website: String,
    @SerializedName("total_students") val numberOfStudents: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
) : Parcelable
