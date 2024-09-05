package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class OTPLogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var storedOtp: String? = null
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
                sendOTP(email)

                Toast.makeText(this@OTPLogInActivity, "We sent an email to $email, please check your email to Log In with OTP", Toast.LENGTH_SHORT).show()
            }
        }

        resendOTP.setOnClickListener {
            val email = emailEditText.text.toString()
            sendOTP(email)
            Toast.makeText(this@OTPLogInActivity, "We sent an email to $email, please check your email to Log In with OTP", Toast.LENGTH_SHORT).show()
        }

        verifyOTP.setOnClickListener{
            val email = emailEditText.text.toString()
            val OTPCode = otpEditText.text.toString()
            verifyOTP(OTPCode, email)
            startActivity(Intent(this@OTPLogInActivity, LoginActivity::class.java))
        }

    }

    private fun sendOTP(email: String) {
        val otp = generateOTP()
        storedOtp = otp

        // Send OTP via email using ACTION_SEND
        sendEmailWithOTP(email, otp)
        Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailWithOTP(email: String, otp: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Your OTP")
        intent.putExtra(Intent.EXTRA_TEXT, "Your OTP is: $otp")

        try {
            startActivity(Intent.createChooser(intent, "Send email..."))
        } catch (e: android.content.ActivityNotFoundException) {
            Toast.makeText(this, "No email client installed.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOTP(enteredOtp: String, email: String) {
        if (storedOtp != null && enteredOtp.isNotEmpty()) {
            if (storedOtp == enteredOtp) {
                signInWithEmailPassword(email)
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithEmailPassword(email: String) {

        auth.signInWithEmailAndPassword(email, "default_password")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent =
                        Intent(this@OTPLogInActivity, CustomerMainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun generateOTP(): String {
        return (100000..999999).random().toString()
    }

/*private fun getUser(email:String)
    {
    val login = UserInstance.retrofitBuilder
        login.loginUser(login).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        val userID = user.userId
                        val firstName = user.firstName
                        val lastName = user.lastName
                        val userType = user.userType
                        val longitude = user.longitude
                        val latitude = user.latitude
                        val fullName = "$firstName $lastName"
                        if (userID != null) {
                            LoginUser(
                                email,
                                pass,
                                firstName,
                                lastName,
                                fullName,
                                userID.toString(),
                                userType,
                                longitude,
                                latitude,
                                userID
                            )
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Connection Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "The email and/or password is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                    passwordContainer.helperText = "The email and/or password is incorrect"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MainActivity", "Registration failed: ")
            }
        })
    }*/
}
}

