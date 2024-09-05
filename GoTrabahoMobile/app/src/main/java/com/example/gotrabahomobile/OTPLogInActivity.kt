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
import papaya.`in`.sendmail.SendMail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.random.nextInt

class OTPLogInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var random : Int=0
    var email : String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otplog_in)
        auth = FirebaseAuth.getInstance()
        generateOTP()
        val otpEditText = findViewById<EditText>(R.id.otpEditText)
        val otpContainer = findViewById<TextInputLayout>(R.id.otpContainer)

        val resendOTP = findViewById<Button>(R.id.resendOTPButton)
        val verifyOTP = findViewById<Button>(R.id.verifyOTPButton)
//        val spanLogIn = SpannableString("Log In with Email and Password")
//
//        val logInSpan: ClickableSpan = object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                // Show the dialog when the text is clicked
//                val intent = Intent(this@OTPLogInActivity, LoginActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        spanLogIn.setSpan(logInSpan, 0, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        logInTextView.text = spanLogIn
//        logInTextView.movementMethod = LinkMovementMethod.getInstance()




        resendOTP.setOnClickListener {
            var email = intent.getStringExtra("email")

            var mail=SendMail("fxcsqz@gmail.com","spacecowboy13",email,"Login Signup app's OTP",
                "Your OTP is -> $random")
            mail.execute()
            Toast.makeText(this@OTPLogInActivity, "We sent an OTP to the email associated with your account, please check your email to verify with OTP", Toast.LENGTH_SHORT).show()
        }

        verifyOTP.setOnClickListener{

            val OTPCode = otpEditText.text.toString()
            var password = intent.getStringExtra("password")
            if(OTPCode.equals(random.toString()))
            auth.createUserWithEmailAndPassword(email,password!!).addOnCompleteListener {
                if(it.isSuccessful){
                    var intent=Intent(this@OTPLogInActivity,CustomerMainActivity::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@OTPLogInActivity, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun generateOTP(){
        random = (100000..999999).random()
        var email = intent.getStringExtra("email")
        var mail=SendMail("fxcsqz@gmail.com","spacecowboy13",email,"Login Signup app's OTP",
            "Your OTP is -> $random")
        mail.execute()
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


