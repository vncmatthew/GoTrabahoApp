package com.example.gotrabahomobile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.gotrabahomobile.DTO.PaymentDTO
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.BookingSummary
import com.example.gotrabahomobile.Model.User
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.PaymentRemote.PaymentInstance
import com.example.gotrabahomobile.Remote.UserRemote.UserInstance
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PaymentActivity : AppCompatActivity() {
    private lateinit var btn_pay: Button
    private lateinit var userEmail: String
    private lateinit var textInvoiceLink: TextView
    private lateinit var FreelancerName: TextView
    private lateinit var Service: TextView
    private lateinit var Date: TextView
    private lateinit var Time: TextView
    private lateinit var SetPrice: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        textInvoiceLink = findViewById(R.id.textViewInvoiceLink)
        btn_pay = findViewById(R.id.buttonPayWithMaya)
        userEmail = intent.getStringExtra("email").toString()
        //display user email
        Log.d("PaymentActivity", "Bundle: $userEmail")


        FreelancerName = findViewById(R.id.textViewBookingSummaryFreelancerName)
        Service = findViewById(R.id.textViewBookingSummaryService)
        Date = findViewById(R.id.textViewBookingSummaryDate)
        Time = findViewById(R.id.textViewBookingSummaryTime)
        SetPrice = findViewById(R.id.textViewBookingSummarySetPrice)

        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }
        getBookingSummary()

        btn_pay.setOnClickListener {
            paymentBooking()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getBookingSummary(){
        var currentDateTime= LocalDateTime.now()

        var time= currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).removeSuffix(" AM").removeSuffix(" PM")
        val bookingId = intent.getIntExtra("bookingId", 0)
        val call = BookingInstance.retrofitBuilder
        call.getBookingSummary(bookingId).enqueue(object: Callback<BookingSummary>{
            override fun onResponse(call: Call<BookingSummary>, response: Response<BookingSummary>) {
                if(response.isSuccessful){
                    val details = response.body()
                    FreelancerName.text = "Freelancer: " + details!!.freelancerName
                    Service.text = "Services: " + details!!.serviceName
                    Date.text = "Date: " + details!!.date
                    SetPrice.text = "Price Breakdown: \n" +
                                    "Commission Fee 15% (for Freelancer) = " + "₱" + (details!!.setPrice * 0.15) +
                                    "\n Total price =  " + "₱ " + details!!.setPrice
                    Time.text = "Time: " + time
                }
            }

            override fun onFailure(call: Call<BookingSummary>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun paymentBooking() {

        val Paymentservice = PaymentInstance.retrofitBuilder
        val paymentInfo = PaymentDTO(email = userEmail)
        val negotiationId = intent.getIntExtra("negotiationId", 0)
        val bookingId = intent.getIntExtra("bookingId", 0)
        if(negotiationId != 0) {
            Paymentservice.paymentBook(paymentInfo, negotiationId)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {

                            val jsonObject = response.body()
                            val invoiceResponse =
                                jsonObject?.let { JsonParser.parseString(it.string()).asJsonObject }

                            val url = Uri.parse(invoiceResponse?.get("invoiceUrl")?.asString)
                            val intent = Intent(Intent.ACTION_VIEW, url)
                            startActivity(intent)

/*                            setupInvoiceLink(
                                textInvoiceLink,
                                invoiceResponse?.get("invoiceUrl")?.asString
                            )*/


                            Toast.makeText(
                                this@PaymentActivity,
                                "Invoice URL has been generated, please click the 'Invoice Link' to copy it to your clipboard",
                                Toast.LENGTH_LONG
                            ).show()
                            // Log.d("Invoice URL", invoiceResponse?.string() ?: "Example invoice link")
                            Log.d(
                                "Invoice URL",
                                invoiceResponse?.toString() ?: "Example invoice link"
                            )
                            Log.d(
                                "Invoice URL",
                                invoiceResponse?.get("invoiceUrl")?.asString
                                    ?: "Example invoice link"
                            )
                            val booking = BookingInstance.retrofitBuilder
                            booking.updateBookingStatus(bookingId, 2)
                                .enqueue(object : Callback<Void> {
                                    override fun onResponse(
                                        call: Call<Void>,
                                        response: Response<Void>
                                    ) {
                                        val check = response.body()
                                        if (response.isSuccessful) {
                                            Log.d("SuccessPay", "Shce")
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }

                                })

                        } else {
                            // Handle error
                            Toast.makeText(
                                this@PaymentActivity,
                                "Failed to generate invoice.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        Toast.makeText(this@PaymentActivity, "Network error.", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
        else {
            Paymentservice.paymentBookCustomer(paymentInfo, bookingId)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {

                            val jsonObject = response.body()
                            val invoiceResponse =
                                jsonObject?.let { JsonParser.parseString(it.string()).asJsonObject }

                            val url = Uri.parse(invoiceResponse?.get("invoiceUrl")?.asString)
                            val intent = Intent(Intent.ACTION_VIEW, url)
                                startActivity(intent)


                            Toast.makeText(
                                this@PaymentActivity,
                                "Invoice URL has been generated, please click the 'Invoice Link' to copy it to your clipboard",
                                Toast.LENGTH_LONG
                            ).show()
                            // Log.d("Invoice URL", invoiceResponse?.string() ?: "Example invoice link")
                            Log.d(
                                "Invoice URL",
                                invoiceResponse?.toString() ?: "Example invoice link"
                            )
                            Log.d(
                                "Invoice URL",
                                invoiceResponse?.get("invoiceUrl")?.asString
                                    ?: "Example invoice link"
                            )
                            val booking = BookingInstance.retrofitBuilder
                            booking.updateBookingStatus(bookingId, 3)
                                .enqueue(object : Callback<Void> {
                                    override fun onResponse(
                                        call: Call<Void>,
                                        response: Response<Void>
                                    ) {
                                        val check = response.body()
                                        if (response.isSuccessful) {
                                            Log.d("SuccessPay", "Shce")
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        TODO("Not yet implemented")
                                    }

                                })

                        } else {
                            // Handle error
                            Toast.makeText(
                                this@PaymentActivity,
                                "Failed to generate invoice.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle failure
                        Toast.makeText(this@PaymentActivity, "Network error.", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }


    }

    private fun setupInvoiceLink(textView: TextView, url: String?) {
        val spannableString = SpannableString("Invoice Link")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {

                val clipboard =
                    widget.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                val clip = ClipData.newPlainText("Invoice Link", url)

                clipboard.setPrimaryClip(clip)
                Log.d("Invoice URL", "Invoice URL has been copied to clipboard")
                Toast.makeText(this@PaymentActivity, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = Color.BLUE // Set the text color
                ds.isUnderlineText = true

            }
        }

        spannableString.setSpan(clickableSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textInvoiceLink.text = spannableString
        textInvoiceLink.movementMethod = LinkMovementMethod.getInstance()
    }
}
