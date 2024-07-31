package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gotrabahomobile.Model.Cities

class CitySpinnerAdapter(context: Context, private val cities: List<Cities>) : ArrayAdapter<Cities>(context, android.R.layout.simple_spinner_item, cities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val city = getItem(position)
        view.findViewById<TextView>(android.R.id.text1).text = city?.cityName ?: ""
        return view
    }
}