package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import papaya.`in`.sendmail.SendMail

class FreelancerOTPActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var random : Int=0
    var email : String=""
    var password : String=""
    var userType : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_otpactivity)
        auth = FirebaseAuth.getInstance()

        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        userType = intent.getIntExtra("userType", 0)
        generateOTP()
        val otpEditText = findViewById<EditText>(R.id.otpEditText)
        val otpContainer = findViewById<TextInputLayout>(R.id.otpContainer)

        val resendOTP = findViewById<Button>(R.id.resendOTPButton)
        val verifyOTP = findViewById<Button>(R.id.verifyOTPButton)

        resendOTP.setOnClickListener {
            var email = intent.getStringExtra("email")

            var mail= SendMail("yorushushok@gmail.com","paiirzatmdtjsukk",email,"Login Signup app's OTP",
                "Your OTP is -> $random")
            mail.execute()
            Toast.makeText(this@FreelancerOTPActivity, "We sent an OTP to the email associated with your account, please check your email to verify with OTP", Toast.LENGTH_SHORT).show()
        }

        verifyOTP.setOnClickListener{

            val OTPCode = otpEditText.text.toString()
            var password = intent.getStringExtra("password")
            if(OTPCode.equals(random.toString()))

            {
                var intent= Intent(this@FreelancerOTPActivity,FreelancerIdentityVerificationActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this@FreelancerOTPActivity, "The OTP you have entered is wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun generateOTP(){
        random = (100000..999999).random()
        var email = intent.getStringExtra("email")
        var mail=SendMail("yorushushok@gmail.com","paiirzatmdtjsukk",email,"GoTrabaho Verification OTP",
            "Your OTP is -> $random")
        mail.execute()
    }
}