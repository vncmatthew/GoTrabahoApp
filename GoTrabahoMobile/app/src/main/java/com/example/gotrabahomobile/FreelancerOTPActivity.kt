package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

class FreelancerOTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_otpactivity)

        val textViewInstructions = findViewById<TextView>(R.id.fTextViewOTPInstructions)
        val textViewPhoneNumberIns = findViewById<TextView>(R.id.fTextViewPhoneNumberInfo)
        var textViewPhoneNumber = findViewById<TextView>(R.id.fTextViewPhoneNumber)
        val sendOTPButton = findViewById<Button>(R.id.fButtonSendOTP)
        val verifyButton = findViewById<Button>(R.id.fButtonVerifyOTP)
        val resendButton = findViewById<Button>(R.id.fButtonResendOTP)

        val enterOTPEditText = findViewById<EditText>(R.id.fEnterOTPEditText)
        val enterOTPEditContainer = findViewById<TextInputLayout>(R.id.fEnterOTPContainer)
        val phoneNumberEditText = findViewById<EditText>(R.id.fVerifyPhoneNumberEditText)
        val phoneNumberContainer = findViewById<TextInputLayout>(R.id.fVerifyPhoneNumberContainer)

        sendOTPButton.setOnClickListener {

            val phoneNumber = phoneNumberEditText.text.toString()
            if(phoneNumber.isEmpty()) {
                phoneNumberContainer.helperText = "Required"
            }
            else if (!phoneNumber.matches("^9[0-9]{2}[0-9]{3}[0-9]{4}$".toRegex()))
            {
                phoneNumberContainer.helperText = "Must be a Valid Phone Number"
            }
            else if (phoneNumber.length != 10)
            {
                phoneNumberContainer.helperText = "Must be 10 digits"
            }
            else {
                //onCodeSent
                textViewInstructions.visibility = View.GONE
                phoneNumberContainer.visibility = View.GONE
                sendOTPButton.visibility = View.GONE

                enterOTPEditContainer.visibility = View.VISIBLE
                resendButton.visibility = View.VISIBLE
                verifyButton.visibility = View.VISIBLE
                textViewPhoneNumber.visibility = View.VISIBLE
                textViewPhoneNumberIns.visibility = View.VISIBLE
                resendButton.isEnabled = false

                val getPhoneNumber = "+63 $phoneNumber"
                textViewPhoneNumber.text = getPhoneNumber
            }
        }

        verifyButton.setOnClickListener {
            //if OTP is wrong textViewError visible, else hidden

            val email = intent.getStringExtra("email")

            val intent = Intent(this@FreelancerOTPActivity, FreelancerIdentityVerificationActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }

    private fun verifyPhoneNumber() {

    }

    private fun authenticateUser() {

    }

}
