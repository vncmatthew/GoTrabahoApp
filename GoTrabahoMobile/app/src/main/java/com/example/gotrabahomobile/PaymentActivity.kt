package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gotrabahomobile.DTO.PaymentDTO
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.PaymentRemote.PaymentInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private lateinit var input_email:EditText
    private lateinit var btn_pay: Button
    private lateinit var email: String
    var customerEmail: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        email = email ?: ""
        Log.d("PaymentActivity", "Bundle: $email")

        val userID = intent.getIntExtra("userID", 0)
        getEmail(userID)


        btn_pay = findViewById(R.id.buttonPayWithMaya)
        val invoiceLinkText = findViewById<TextView>(R.id.textViewInvoiceLink)
//        invoiceLinkText.text = email
        btn_pay.setOnClickListener {
            paymentBooking()
        }

    }

    private fun paymentBooking() {
        val selectedDateTime = intent.getStringExtra("selectedDateTime")
        val userID = intent.getIntExtra("userID", 0)
        val customerID = intent.getIntExtra("customerID", 0)
        val customerName = intent.getStringExtra("customerName")
        val userEmail = "testing072301@gmail.com"
        val invoiceLinkText = findViewById<TextView>(R.id.textViewInvoiceLink)



//        val emailBook = email ?: ""
        val Paymentservice = PaymentInstance.retrofitBuilder



        val paymentInfo = PaymentDTO(email = userEmail)

        Paymentservice.paymentBook(userEmail).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val invoiceResponse = response.body()
                    Toast.makeText(this@PaymentActivity, "THIS SHIT WORKS", Toast.LENGTH_SHORT).show()

                    invoiceLinkText.text = invoiceResponse?.string()

                } else {
                    // Handle error
                    Toast.makeText(this@PaymentActivity, "Failed to generate invoice.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@PaymentActivity, "Network error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getEmail(userId: Int) {
        val call = UserInstance.retrofitBuilder

        call.getUser(userId).enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) { val data = response.body()
                if (response.isSuccessful) {
                        email = data?.email.toString()

                    val invoiceLinkText = findViewById<TextView>(R.id.textViewInvoiceLink)
                    invoiceLinkText.text = email
                    }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("Failed to get email", "${t}" )
            }

        })

    }
}
