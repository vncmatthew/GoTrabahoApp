package com.example.gotrabahomobile.Helper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gotrabahomobile.DTO.SubServicesTypes

class SubServiceAdapter(context: Context, private val subservice: List<SubServicesTypes> ):
    ArrayAdapter<SubServicesTypes>(context, android.R.layout.simple_spinner_item, subservice) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val subServicesType = getItem(position)
        view.findViewById<TextView>(android.R.id.text1).text = subServicesType?.subServiceName ?: ""
        return view
    }
}