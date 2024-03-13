package com.example.gotrabahomobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerProfilePageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile_page)

        val cancelButton: Button = findViewById(R.id.buttonEditCustomerProfileCancel)
        cancelButton.setOnClickListener{
            finish()
        }
        val btnSaveProfile = findViewById<Button>(R.id.buttonEditCustomerProfileSave)
        val tvFirstName = findViewById<EditText>(R.id.editTextProfileCustomerFirstName)
        val tvLastName = findViewById<EditText>(R.id.editTextProfileCustomerLastName)
        val tvPhone = findViewById<EditText>(R.id.editTextProfileCustomerPhone)


        btnSaveProfile.setOnClickListener {
            val firstName = tvFirstName.text?.toString()
            val lastName = tvLastName.text?.toString()
            val phone = tvPhone.text?.toString()
            val userId = intent.getIntExtra("userId",0)
            val updateUser = User(
                userId = userId,
                firstName = firstName,
                lastName = lastName,
                contactNumber = phone
            )
            updateUser(updateUser)
        }

    }



    private fun updateUser(updateUser: User){
        val userId = intent.getIntExtra("userId",0)


        val call = UserInstance.retrofitBuilder
        call.patchUser(userId, updateUser).enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // Assuming you want to log the successful response body
                    val user = response.body()
                    if (user != null) {
                        Log.d("Successf", "User updated successfully: $user")
                    } else {
                        Log.d("Successf", "User updated successfully, but response body is null")
                    }

                    // Start the new activity
                    val intent = Intent(this@CustomerProfilePageActivity, CustomerMainActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                } else {
                    // Log the error response code and message
                    Log.d("Tag", "Response is not successful. Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}