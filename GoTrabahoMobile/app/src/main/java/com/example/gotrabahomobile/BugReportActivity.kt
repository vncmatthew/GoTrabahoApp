package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.gotrabahomobile.Model.BugReport
import com.example.gotrabahomobile.Remote.BugReportRemote.BugReportInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BugReportActivity : AppCompatActivity() {

private lateinit var reportBugTitleSpinner: Spinner
    var selectedFeature: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        val submitReportButton = findViewById<Button>(R.id.buttonReportBugSubmit)
        reportBugTitleSpinner = findViewById<Spinner>(R.id.dropdownReportBugTitle)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.bugTypes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reportBugTitleSpinner.adapter = adapter
        reportBugTitleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedFeature = parent.getItemAtPosition(position) as? String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@BugReportActivity, "Please Select a Title", Toast.LENGTH_SHORT).show()
            }
        }


        submitReportButton.setOnClickListener {

            sendBugReport(selectedFeature)
            finish()
        }
    }

    fun sendBugReport(feature: String?){

        var reportBugDescEditText = findViewById<EditText>(R.id.editTextReportBugDesc)

        var desc = reportBugDescEditText.text.toString()
        val userId = intent.getIntExtra("userId", 0)
        val bugReport = BugReport(
            userID = userId,
            featureID = feature,
            description = desc,
            status = 0
        )
        val call = BugReportInstance.retrofitBuilder
        call.insertBugReport(bugReport).enqueue(object: Callback<BugReport>{
            override fun onResponse(call: Call<BugReport>, response: Response<BugReport>) {
                if(response.isSuccessful){
                    Log.d("Bugreport", "Success")
                }
            }

            override fun onFailure(call: Call<BugReport>, t: Throwable) {
                Log.d("Bugreport", "Failure")
            }

        })
    }
}