package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class FreelancerBugReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_bug_report)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        val submitReportButton = findViewById<Button>(R.id.buttonFreelancerReportBugSubmit)
        val reportBugTitleEditText = findViewById<EditText>(R.id.editTextFreelancerReportBugTitle)
        val reportBugDescEditText = findViewById<EditText>(R.id.editTextFreelancerReportBugDesc)

        var title = reportBugTitleEditText.text.toString()
        var desc = reportBugDescEditText.text.toString()

        submitReportButton.setOnClickListener {

        }

    }
}