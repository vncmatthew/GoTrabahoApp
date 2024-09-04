package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class CustomerOTPActivity : AppCompatActivity() {
    lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customerotpactivity)

        val textViewInstructions = findViewById<TextView>(R.id.cTextViewOTPInstructions)
        val textViewPhoneNumberIns = findViewById<TextView>(R.id.cTextViewPhoneNumberInfo)
        var textViewPhoneNumber = findViewById<TextView>(R.id.cTextViewPhoneNumber)
        val sendOTPButton = findViewById<Button>(R.id.cButtonSendOTP)
        val verifyButton = findViewById<Button>(R.id.cButtonVerifyOTP)
        val resendButton = findViewById<Button>(R.id.cButtonResendOTP)

        val enterOTPEditText = findViewById<EditText>(R.id.cEnterOTPEditText)
        val enterOTPEditContainer = findViewById<TextInputLayout>(R.id.cEnterOTPContainer)
        val phoneNumberEditText = findViewById<EditText>(R.id.cVerifyPhoneNumberEditText)
        val phoneNumberContainer = findViewById<TextInputLayout>(R.id.cVerifyPhoneNumberContainer)

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
            val email = intent.getStringExtra("email")

            val intent = Intent(this@CustomerOTPActivity, LoginActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }

    private fun verifyPhoneNumber() {

    }

    private fun authenticateUser() {

    }

}