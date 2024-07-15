package com.example.gotrabahomobile

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Model.Login
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class LoginActivity : AppCompatActivity() {



    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this@LoginActivity)
        auth = FirebaseAuth.getInstance()
        val btn_login = findViewById<Button>(R.id.buttonLogin)
        val txt_signup = findViewById<TextView>(R.id.textViewNoAccount)

        val text = "Don't have an account? Sign Up"
        val spanString = SpannableString(text)

        val signupText = object: ClickableSpan() {
            override fun onClick(widget: View) {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }


        spanString.setSpan(signupText,23,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txt_signup.text = spanString
        txt_signup.movementMethod = LinkMovementMethod.getInstance()
        btn_login.setOnClickListener{loginUser()}



        btn_login.setOnClickListener{
        loginUser()
        }


    }


    private fun loginUser() {
        val edit_Email = findViewById<EditText>(R.id.editTextLoginEmailAddress)
        val edit_Pass = findViewById<EditText>(R.id.editTextLoginPassword)

        val email = edit_Email.text.toString()
        val pass = edit_Pass.text.toString()

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(
                this@LoginActivity,
                "The email and password fields are required",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(
                this@LoginActivity,
                "The email is in the incorrect format",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val login = Login(email = email, password = pass)
        val login1 = UserInstance.retrofitBuilder

        login1.loginUser(login).enqueue(object : Callback<User> {
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
                        LoginUser(email, pass ,firstName,lastName, fullName, userID.toString(), userType, longitude, latitude, userID)
                        }else {
                            Toast.makeText(this@LoginActivity, "Connection Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this@LoginActivity, "The email and/or password is incorrect", Toast.LENGTH_SHORT).show()
                }
        }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d ("MainActivity", "Registration failed: ")
            }
        })
    }

    private fun LoginUser(email:String,password:String,firstName:String?, lastName:String?, fullName:String?, sqlId: String?, userType: Int?,
                             longitude: Double?, latitude: Double?, identification: Int?){
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {

                                if(userType == 1) {
                                    val intent =
                                        Intent(this@LoginActivity, CustomerMainActivity::class.java)
                                    intent.putExtra("sqlId", sqlId)
                                    intent.putExtra("userId", identification)
                                    intent.putExtra("email", email)
                                    intent.putExtra("firstName", firstName)
                                    intent.putExtra("lastName", lastName)
                                    intent.putExtra("fullName", fullName)
                                    intent.putExtra("longitude", longitude)
                                    intent.putExtra("latitude", latitude)
                                    startActivity(intent)
                                }
                                else if(userType == 2) {

                                    val call = FreelancerInstance.retrofitBuilder
                                    call.getFreelancerId(identification!!.toInt()).enqueue(object: Callback<Freelancer>{
                                        override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {

                                            if(response.isSuccessful){
                                                val freelancerId = response.body()
                                                var bro = freelancerId?.verificationStatus

                                                if (bro == 1 || bro == 3) {
                                                    Log.d("Identification@", "${identification}")
                                                    val intent =
                                                        Intent(this@LoginActivity, FreelancerMainActivity::class.java)
                                                    intent.putExtra("userId", identification)
                                                    intent.putExtra("freelancerId", freelancerId?.freelancerId)
                                                    intent.putExtra("firstName", firstName)
                                                    intent.putExtra("lastName", lastName)
                                                    intent.putExtra("fullName", fullName)
                                                    intent.putExtra("email", email)
                                                    intent.putExtra("userType", userType)
                                                    startActivity(intent)

                                                }
                                                else if(bro == 0){
                                                     Intent(this@LoginActivity, ApplicationConfirmationActivity::class.java)
                                                        intent.putExtra("userID", sqlId)
                                                        intent.putExtra("freelancerId", freelancerId?.freelancerId)
                                                        intent.putExtra("firstName", firstName)
                                                        intent.putExtra("lastName", lastName)
                                                        intent.putExtra("fullName", fullName)
                                                        intent.putExtra("email", email)
                                                        intent.putExtra("userType", userType)
                                                    startActivity(intent)
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                                            TODO("Not yet implemented")
                                        }
                                    })
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "email or password invalid",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

    }






