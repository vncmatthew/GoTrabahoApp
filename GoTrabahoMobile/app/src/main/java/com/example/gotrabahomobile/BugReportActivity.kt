package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.gotrabahomobile.Model.BugReport
import com.example.gotrabahomobile.Remote.BugReportRemote.BugReportInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BugReportActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug_report)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        val submitReportButton = findViewById<Button>(R.id.buttonReportBugSubmit)
        val reportBugTitleEditText = findViewById<EditText>(R.id.editTextReportBugTitle)



        submitReportButton.setOnClickListener {
            sendBugReport()
            finish()
        }
    }

    fun sendBugReport(){

        var reportBugDescEditText = findViewById<EditText>(R.id.editTextReportBugDesc)
        var desc = reportBugDescEditText.text.toString()
        val userId = intent.getIntExtra("userId", 0)
        val bugReport = BugReport(
            userID = userId,
            featureID = "yes",
            description = desc
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