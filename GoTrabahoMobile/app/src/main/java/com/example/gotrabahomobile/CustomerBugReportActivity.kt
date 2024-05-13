package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class CustomerBugReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_bug_report)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        val submitReportButton = findViewById<Button>(R.id.buttonReportBugSubmit)
        val reportBugTitleEditText = findViewById<EditText>(R.id.editTextReportBugTitle)
        val reportBugDescEditText = findViewById<EditText>(R.id.editTextReportBugDesc)

        var title = reportBugTitleEditText.text.toString()
        var desc = reportBugDescEditText.text.toString()

        submitReportButton.setOnClickListener {

        }
    }
}