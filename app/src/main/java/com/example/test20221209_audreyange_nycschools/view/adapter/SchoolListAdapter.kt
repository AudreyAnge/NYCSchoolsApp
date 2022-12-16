package com.example.test20221209_audreyange_nycschools.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test20221209_audreyange_nycschools.databinding.SchoolItemBinding
import com.example.test20221209_audreyange_nycschools.listener.SchoolItemActionListener
import com.example.test20221209_audreyange_nycschools.model.School

/**
 * Adapter that presents a [School] item.
 */
class SchoolListAdapter(
    private var schools: ArrayList<School>,
    private val itemClickListener: SchoolItemActionListener
) :
    RecyclerView.Adapter<SchoolListAdapter.SchoolViewHolder>() {

    class SchoolViewHolder(private val itemBinding: SchoolItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: School, itemClickListener: SchoolItemActionListener) {
            itemBinding.schoolName.text = item.schoolName
            itemBinding.location.text = item.address
            itemBinding.startTime.text = item.startTime
            itemBinding.endTime.text = item.endTime
            itemBinding.call.setOnClickListener { v ->
                itemClickListener.onSchoolItemClicked(
                    view = v,
                    school = item
                )
            }
            itemBinding.website.setOnClickListener { v ->
                itemClickListener.onSchoolItemClicked(
                    view = v,
                    school = item
                )
            }
            itemBinding.map.setOnClickListener { v ->
                itemClickListener.onSchoolItemClicked(
                    view = v,
                    school = item
                )
            }
            itemView.setOnClickListener { v ->
                itemClickListener.onSchoolItemClicked(
                    view = v,
                    school = item
                )
            }
        }
    }

    fun addSchools(schoolList: List<School>) {
        val size = schools.size
        schools.addAll(schoolList)
        notifyItemRangeChanged(size-1, schools.size)
    }

    @SuppressLint("NotifyDataSetChanged") // Refreshing the whole list
    fun setSchoolList(schoolList: List<School>) {
        schools = ArrayList(schoolList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val binding: SchoolItemBinding =
            SchoolItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SchoolViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {
        val school = schools[position]
        holder.bind(school, itemClickListener)
    }

    override fun getItemCount(): Int {
        return schools.size
    }
}