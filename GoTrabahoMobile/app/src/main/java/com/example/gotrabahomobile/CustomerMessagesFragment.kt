package com.example.gotrabahomobile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.ChatUserAdapter
import com.example.gotrabahomobile.Model.ChatRoom
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.databinding.FragmentCustomerMessagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


private lateinit var userRecyclerView: RecyclerView
var userList = ArrayList<UserFirebase>()
private var _binding: FragmentCustomerMessagesBinding? = null
var firebaseUser: FirebaseUser? = null
var reference: DatabaseReference? = null
var bookingDatabase: DatabaseReference? = null
var database: DatabaseReference? = null


/**
 * A simple [Fragment] subclass.
 * Use the [CustomerMessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerMessagesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCustomerMessagesBinding.inflate(inflater, container, false)

        arguments?.let {
            var Id = it.getInt("serviceId", 0)
            var userId = it.getInt("userId", 0)
            var freelancerId = it.getInt("freelancerId", 0)
            var firstName = it.getString("firstName").toString()
            var lastName = it.getString("lastName").toString()
            var email = it.getString("email").toString()

            var fullName = firstName + lastName

            firebaseUser = FirebaseAuth.getInstance().currentUser
            reference = FirebaseDatabase.getInstance().getReference("UserFirebase").child(userId!!.toString())
            bookingDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")
            Log.d("FreelancerMessagesFrag", "Button Clicked")
            Log.d("FreelancerMessagesFrag", "${email}")
            Log.d("FreelancerMessagesFrag", "${fullName}")
            Log.d("FreelancerMessagesFrag", Id.toString())
            Log.d("FreelancerMessagesFrag", userId.toString())
        }


        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userRecyclerView = _binding!!.rvCustomerMessageList

        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getChatRooms()

    }


    fun getChatRooms() {
        Log.d("Tag", "Check")
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userId = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userId")

        val bookingChatRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("ChatRoom")
        bookingChatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatroomList = mutableListOf<ChatRoom>()

                for (dataSnapShot: DataSnapshot in dataSnapshot.children) {
                    val chatroom = dataSnapShot.getValue(ChatRoom::class.java)
                    if (chatroom != null && (chatroom.customerId == userId || chatroom.freelancerId == userId)) {
                        chatroomList.add(chatroom)
                    }
                }

                displayChatRooms(chatroomList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DEBUG", "Failed to read value.", databaseError.toException())
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun displayChatRooms(chatroomList: List<ChatRoom>) {
        val serviceId = requireActivity().intent.getIntExtra("serviceId", 0)
        val serviceName = requireActivity().intent.getStringExtra("serviceName")
        val chatRoomAdapter = ChatUserAdapter(requireContext(), chatroomList as ArrayList<ChatRoom>, serviceId, serviceName)

        userRecyclerView.adapter = chatRoomAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerMessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerMessagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}