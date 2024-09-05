package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class OTPLogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otplog_in)

        val sendOTPButton = findViewById<Button>(R.id.sendOTPButton)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)

        val logInTextView = findViewById<TextView>(R.id.textViewLogin)

        val spanLogIn = SpannableString("Log In with Email and Password")

        val logInSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Show the dialog when the text is clicked
                val intent = Intent(this@OTPLogInActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        spanLogIn.setSpan(logInSpan, 0, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        logInTextView.text = spanLogIn
        logInTextView.movementMethod = LinkMovementMethod.getInstance()

        sendOTPButton.setOnClickListener {
            //validations
            val email = emailEditText.text.toString()
            if(email.isEmpty()) {
                emailContainer.helperText = "Required"
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                emailContainer.helperText = "Invalid Email Address"
            }
            else {
                emailContainer.helperText = " "

                Toast.makeText(this@OTPLogInActivity, "We sent an email to $email, please check your email to Log In with OTP", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@OTPLogInActivity, LoginActivity::class.java))
            }
        }
    }
}