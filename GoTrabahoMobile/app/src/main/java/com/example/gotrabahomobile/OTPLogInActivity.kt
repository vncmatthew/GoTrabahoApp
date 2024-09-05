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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPLogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otplog_in)
        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val emailContainer = findViewById<TextInputLayout>(R.id.emailContainer)
        val otpEditText = findViewById<EditText>(R.id.otpEditText)
        val otpContainer = findViewById<TextInputLayout>(R.id.otpContainer)

        val logInTextView = findViewById<TextView>(R.id.textViewLogin)
        val resendOTP = findViewById<Button>(R.id.resendOTPButton)
        val sendOTP = findViewById<Button>(R.id.sendOTPButton)
        val verifyOTP = findViewById<Button>(R.id.verifyOTPButton)
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


        sendOTP.setOnClickListener{
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
                sendOTP.visibility = View.GONE
                emailContainer.visibility = View.GONE

                otpContainer.visibility = View.VISIBLE
                resendOTP.visibility = View.VISIBLE
                verifyOTP.visibility = View.VISIBLE

                emailContainer.helperText = " "

                Toast.makeText(this@OTPLogInActivity, "We sent an email to $email, please check your email to Log In with OTP", Toast.LENGTH_SHORT).show()
            }
        }

        resendOTP.setOnClickListener {
            val email = emailEditText.text.toString()
            sendVerificationCode(email)
            Toast.makeText(this@OTPLogInActivity, "We sent an email to $email, please check your email to Log In with OTP", Toast.LENGTH_SHORT).show()
        }

        verifyOTP.setOnClickListener{
            val email = emailEditText.text.toString()
            val OTPCode = otpEditText.text.toString()
            verifyOTP(OTPCode)
            startActivity(Intent(this@OTPLogInActivity, LoginActivity::class.java))
        }

    }

    private fun sendVerificationCode(email: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(this@OTPLogInActivity, "Verification successful", Toast.LENGTH_SHORT).show()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OTPLogInActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(this@OTPLogInActivity, "OTP sent successfully", Toast.LENGTH_SHORT).show()
            }
        }

        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)

        if (resendToken != null) {
            optionsBuilder.setForceResendingToken(resendToken!!)
        }

        optionsBuilder
            .setPhoneNumber(email)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
    private fun verifyOTP(otp: String) {
        if (storedVerificationId != null && otp.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
            signInWithPhoneAuthCredential(credential)
        } else {
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Proceed to next activity or update UI accordingly
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}