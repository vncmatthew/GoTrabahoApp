package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class ReportProblemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_problem)

        val cancelButton: Button = findViewById(R.id.buttonReportProblemCancel)
        cancelButton.setOnClickListener{
            finish()
        }
    }
}