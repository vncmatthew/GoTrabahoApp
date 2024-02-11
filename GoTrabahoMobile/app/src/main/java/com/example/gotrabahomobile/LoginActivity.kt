package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gotrabahomobile.Model.Login
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btn_login = findViewById<Button>(R.id.buttonLogin)

        btn_login.setOnClickListener{loginUser()}

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
/*                        val firstName = user.firstName
                        val lastName = user.lastName
                        val fullName = "$firstName $lastName"*/
                        if (userID != null) {
                            val intent = Intent(this@LoginActivity, StartupActivity::class.java)
/*                            intent.putExtra("userID", userID)
                            intent.putExtra("fullName", fullName)
                            intent.putExtra("firstName", firstName)
                            intent.putExtra("lastName", lastName)*/
                            Log.d("NIGGER", "HELLO")
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Connection Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Details are incorrect", Toast.LENGTH_SHORT)
                        .show()
                }


            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d ("MainActivity", "$t Registration failed: ")
            }
        })
    }
}



