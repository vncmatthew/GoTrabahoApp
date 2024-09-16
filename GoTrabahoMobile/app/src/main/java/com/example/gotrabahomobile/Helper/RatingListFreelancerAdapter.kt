package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.RatingListDTO
import com.example.gotrabahomobile.databinding.ListItemRatingsBinding
import com.example.gotrabahomobile.databinding.ListItemServicesListEditDeleteBinding

class RatingListFreelancerAdapter(private val ratingList: List<RatingListDTO>, private val context: Context) :
    RecyclerView.Adapter<RatingListFreelancerAdapter.RatingListViewHolder>() {

    inner class RatingListViewHolder(val binding: ListItemRatingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingListViewHolder {
        return RatingListViewHolder(
            ListItemRatingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    override fun onBindViewHolder(holder: RatingListViewHolder, position: Int) {
        val rating = ratingList[position]

        holder.binding.apply {
            serviceNameRating.text = "${rating.serviceName}"
            ratingGiven.text = "${rating.rating}"
            commentGiven.text = "${rating.comments}"
        }
    }

}