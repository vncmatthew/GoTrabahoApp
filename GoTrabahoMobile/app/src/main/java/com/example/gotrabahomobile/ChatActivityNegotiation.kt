package com.example.gotrabahomobile

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.ChatAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.ArrayList

@Suppress("DEPRECATION")
class ChatActivityNegotiation : AppCompatActivity() {


    val CHANNEL_ID ="channelID"
    val CHANNEL_NAME ="channelName"
    val NOTIF_ID = 0

    private lateinit var etMessage: EditText
    private lateinit var btnSendMessage: ImageButton
    private lateinit var btnSetPrice: Button
    private lateinit var btnConfirmSetPrice: Button
    private lateinit var tvUserName: TextView
    private lateinit var tvSetPrice:TextView
    private lateinit var chatRecyclerView: RecyclerView
    private var price:Double? = null
    private val email: String? = null
    private var userData: UserFirebase? = null
    var topic = ""
    var chatList = ArrayList<Chat>()

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_negotiation)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        etMessage = findViewById(R.id.etMessage)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        tvUserName = findViewById(R.id.tvUserName)
        btnSetPrice = findViewById(R.id.buttonChatSetPrice)


        createNotifChannel()


        //back button
        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        //get UserModel
            var userId = intent.getStringExtra("userId")
            var firstName = intent.getStringExtra("firstName")
            var lastName = intent.getStringExtra("lastName")
            var email = intent.getStringExtra("email")
            val chatroomId = intent.getStringExtra("chatroomId")

        tvUserName.setText(firstName + " " + lastName)
            firebaseUser = FirebaseAuth.getInstance().currentUser
            reference = FirebaseDatabase.getInstance().getReference("UserFirebase").child(userId!!)


        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(UserFirebase::class.java)
                tvUserName.text = (user!!.firstName+ " " + user!!.lastName)
            }
        })

        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message, chatroomId!!)
                etMessage.setText("")
                topic = "/topics/$userId"
                }

            }
        readMessage(firebaseUser!!.uid, userId,chatroomId!!)

        btnSetPrice.setOnClickListener{
            showDialog()

        }

    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, chatroomId: String) {
        // Get a reference to the specific chat room identified by chatroomId
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Chat/$chatroomId")

        // Create a HashMap to store the message details
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        hashMap["chatroomId"] = chatroomId

        // Use push() to generate a unique ID for each message and setValue() to save the message details
        reference.push().setValue(hashMap)
    }

    fun readMessage(senderId: String, receiverId: String, chatroomId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat").child(chatroomId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // Handle potential errors here
                Log.e("FirebaseReadError", error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    // Check if chat is not null before proceeding
                    if (chat != null) {
                        // Simplify the condition by removing redundant negations and the unsafe non-null assertion
                        if ((chat.senderId == senderId && chat.receiverId == receiverId) ||
                            (chat.senderId == receiverId && chat.receiverId == senderId)) {
                            chatList.add(chat)
                        }
                    }
                }


                val chatAdapter = ChatAdapter(this@ChatActivityNegotiation, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.set_price_dialog, null)


        val closeButton = dialogView.findViewById<ImageButton>(R.id.close_button)


        val tvSetPrice = dialogView.findViewById<EditText>(R.id.editTextSetPrice)

        val alertDialog = AlertDialog.Builder(this@ChatActivityNegotiation)
            .setView(dialogView)
            .create()

        val updateButton = dialogView.findViewById<Button>(R.id.buttonSetPriceConfirm)
        updateButton.setOnClickListener {

            val sqlId = intent.getStringExtra("sqlId")
            val serviceId = intent.getIntExtra("serviceId", 0)
            val name = intent.getStringExtra("serviceName")
            val negotiationId = intent.getIntExtra("negotiationId", 0)
            val freelancerPrice = tvSetPrice.text.toString().toDoubleOrNull()

            Log.d("ChatActivity", email.toString())
            //Service side Negotiation
            if (serviceId != 0) {
                val track = NegotiationInstance.retrofitBuilder
                track.getNegotiationTracker("$sqlId$serviceId$name").enqueue(object : Callback<Negotiation> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                        if (response.isSuccessful) {
                            Log.d("ChatActivity", email.toString())


                            val service = response.body()
                            service?.let {
                                val negotiation = Negotiation(
                                    negotiationId = service.negotiationId,
                                    freelancerPrice = freelancerPrice
                                )

                                patchNegotiation2(service.negotiationId!!, negotiation, service.customerId!!)
                                notifMessage("Price has been Updated", "The price is ${freelancerPrice}")
                                showBookingConfirmationToast()
                            }
                        }
                    }
                    override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                        Log.e("Checkit", "Failed to get negotiation tracker", t)
                        Toast.makeText(this@ChatActivityNegotiation, "Failed to get negotiation tracker", Toast.LENGTH_SHORT).show()
                    }
                })
                //User Side Negotiation
            } else {

                val negotiation = Negotiation(
                    negotiationId = negotiationId,
                    customerPrice = freelancerPrice
                )
                patchNegotiation(negotiationId, negotiation)
                notifMessage("Price has been Updated", "The price is ${freelancerPrice}")
                showBookingConfirmationToast()

            }

            alertDialog.dismiss()
        }

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }


        alertDialog.show()
    }



//User Side Negotiation
    private fun patchNegotiation(negotiationId: Int, negotiation: Negotiation) {
        val call = NegotiationInstance.retrofitBuilder
        call.patchNegotiation(negotiationId, negotiation).enqueue(object : Callback<Negotiation> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                if (response.isSuccessful) {
                    Log.d("Warframe", "${response.body()}")
                    val serviceIdcustomer: Int? = intent.getIntExtra("serviceIdcustomer", 0)
                    Log.d("ChatActivity", "$serviceIdcustomer")
                    val sqlId: Int? = intent.getIntExtra("sqlId",0)
                    Toast.makeText(this@ChatActivityNegotiation, "Update successful", Toast.LENGTH_SHORT).show()
                    checker(negotiationId, serviceIdcustomer!!, sqlId!! )

                } else {
                    Log.d("Checkit", "${response.body()}")
                    Toast.makeText(this@ChatActivityNegotiation, "Update failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                Log.e("Checkit", "Update failed", t)
                Toast.makeText(this@ChatActivityNegotiation, "Update failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checker(negotiationId: Int, serviceId: Int, userId: Int){

        val dialogView = layoutInflater.inflate(R.layout.set_price_dialog, null)
        val call = NegotiationInstance.retrofitBuilder

        call.getNegotiationPrice(negotiationId).enqueue(object: Callback<Double>{
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                if (response.isSuccessful) {
                    val finalPrice = response.body()



                    val call = NegotiationInstance.retrofitBuilder
                    call.getPriceChecker(negotiationId).enqueue(object: Callback<Boolean>{
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            var check = response.body()
                            if (response.isSuccessful){
                                if(check == true){
                                    try {
                                    val book = BookingInstance.retrofitBuilder

                                    book.getBookingDiscount(userId).enqueue(object:Callback<ResponseBody>{

                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {
                                            if(response.isSuccessful){
                                             var body = response.body()
                                             if(body!!.equals(true)){
                                                 Log.d("Nego", "true")
                                                 if (finalPrice != null) {
                                                     price = finalPrice - 100
                                                 }
                                             }else{
                                                 Log.d("Nego", "false")
                                                 price = finalPrice!!
                                             }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable
                                        ) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                    var newBooking = Booking(
                                        customerId = userId,
                                        bookingDatetime = LocalDate.now().toString(),
                                        amount = price,
                                        serviceFee = finalPrice!! * 0.15,
                                        bookingStatus = 1,
                                        serviceId = serviceId,
                                        negotiationId = negotiationId,
                                        paymentStatus = false,
                                        refundFreelancer = 0)

                                    book.insertBooking(newBooking).enqueue(object: Callback<Booking>{
                                        override fun onResponse(
                                            call: Call<Booking>,
                                            response: Response<Booking>
                                        ) {
                                            val booking = response.body()
                                            if(response.isSuccessful) {
                                                if (booking!!.equals("Successfully Created"))
                                                {
                                                //ADD NOTIFICATION

                                                    notifMessage(
                                                        "Booking is Successful",
                                                        "Booking has been made"
                                                    )
                                                val serviceId: Int? =
                                                    intent.getIntExtra("serviceId", 0)
                                                var userIdFirebase = intent.getStringExtra("userId")
                                                if (serviceId != 0) {

                                                    createBookingChatEntry(
                                                        booking!!.bookingId.toString(),
                                                        userIdFirebase!!,
                                                        firebaseUser!!.uid
                                                    )
                                                    { success ->
                                                        if (success) {
                                                            println("BookingChat entry created successfully")
                                                        } else {
                                                            println("Failed to create BookingChat entry")
                                                        }
                                                    }
                                                    Intent(
                                                        this@ChatActivityNegotiation,
                                                        FreelancerMainActivity::class.java
                                                    )
                                                    startActivity(intent)
                                                } else {
                                                    createBookingChatEntry(
                                                        booking!!.bookingId.toString(),
                                                        firebaseUser!!.uid,
                                                        userIdFirebase!!
                                                    )
                                                    { success ->
                                                        if (success) {
                                                            println("BookingChat entry created successfully")
                                                        } else {
                                                            println("Failed to create BookingChat entry")
                                                        }
                                                    }
                                                    Intent(
                                                        this@ChatActivityNegotiation,
                                                        CustomerMainActivity::class.java
                                                    )
                                                    startActivity(intent)
                                                }
                                            }
                                                else if(booking.equals("A booking with the same user and freelancer already exists."))
                                                {

                                                    Toast.makeText(this@ChatActivityNegotiation, "A booking with the same user and freelancer already exists", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            Log.d("Check", "${booking}")
                                        }

                                        override fun onFailure(call: Call<Booking>, t: Throwable) {
                                            Log.d("Check", "${t}")
                                        }

                                    })
                                    } catch (e: Exception) {
                                        Log.e("RetrofitError", "Foreign Key Constraint Violation", e)
                                    }

                                }
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })





    }
    //This is for Freelancer Side Negotiation
    private fun patchNegotiation2(negotiationId: Int, negotiation: Negotiation, userId: Int) {
        val call = NegotiationInstance.retrofitBuilder
        call.patchNegotiation(negotiationId, negotiation).enqueue(object : Callback<Negotiation> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                if (response.isSuccessful) {
                    Log.d("Warframe", "${response.body()}")
                    val serviceId = intent.getIntExtra("serviceId", 0)
                    Toast.makeText(this@ChatActivityNegotiation, "Update successful", Toast.LENGTH_SHORT).show()
                    checker(negotiationId, serviceId!!, userId!! )

                } else {
                    Log.d("Checkit", "${response.body()}")
                    Toast.makeText(this@ChatActivityNegotiation, "Update failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                Log.e("Checkit", "Update failed", t)
                Toast.makeText(this@ChatActivityNegotiation, "Update failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createNotifChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor= Color.BLUE
                enableLights(true)
            }
            val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun notifMessage(Title:String, Text:String){
        val intentT =Intent(this, ChatActivityNegotiation::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(intentT)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(this@ChatActivityNegotiation, CHANNEL_ID)
            .setContentTitle(Title)
            .setContentText(Text)
            .setSmallIcon(R.drawable.logo_blue_noname)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notifManger = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this@ChatActivityNegotiation,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notifManger.notify(NOTIF_ID,notif)
    }

    fun createBookingChatEntry(chatroomId: String, customerId: String, freelancerId: String, callback: (Boolean) -> Unit) {
        val bookingChatRef = FirebaseDatabase.getInstance().getReference("ChatRoom")
        val bookingChatId = bookingChatRef.push().key ?: return

            val hashMap: java.util.HashMap<String, Any?> = java.util.HashMap()
            hashMap["customerId"] = customerId
            hashMap["freelancerId"] = freelancerId
            hashMap["chatroomId"] = chatroomId

            bookingChatRef.child(bookingChatId).setValue(hashMap)
                .addOnSuccessListener {
                    callback(true)
                }
                .addOnFailureListener { exception ->
                    Log.w("Firebase", "Error creating BookingChat entry: ", exception)
                    callback(false)
                }

    }

    private fun bookingConfirmationToast() {
        val layoutInflater = this.layoutInflater
        val layout = layoutInflater.inflate(R.layout.toast_background, findViewById(R.id.toast_container))

        with(Toast(this)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.BOTTOM, 0, 40)
            show()
        }
    }

    fun showBookingConfirmationToast() {

        Handler(Looper.getMainLooper()).postDelayed({
            bookingConfirmationToast()

            }, 10000)
    }




}