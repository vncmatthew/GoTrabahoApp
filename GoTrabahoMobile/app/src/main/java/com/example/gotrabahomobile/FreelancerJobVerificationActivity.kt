package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.FreelancerTesdaCertificate
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FreelancerJobVerificationActivity : AppCompatActivity() {


    var selectedItem: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_job_verification)

        val idType = findViewById<Spinner>(R.id.dropdownIDType)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.idType,
            android.R.layout.simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        idType.adapter = adapter

        idType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedItem = null
            }

        }



    }







}



