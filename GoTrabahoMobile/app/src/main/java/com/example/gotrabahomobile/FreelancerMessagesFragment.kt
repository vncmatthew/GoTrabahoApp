package com.example.gotrabahomobile

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.ChatUserAdapter
import com.example.gotrabahomobile.Model.ChatRoom
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.FragmentFreelancerMessagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FreelancerMessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreelancerMessagesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var selectedService: String? = null

    private lateinit var userRecyclerView: RecyclerView
    var userList = ArrayList<UserFirebase>()
    private var _binding: FragmentFreelancerMessagesBinding? = null
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    private lateinit var sharedPreferences: SharedPreferences





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
        _binding = FragmentFreelancerMessagesBinding.inflate(inflater, container, false)

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
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

            Log.d("FreelancerMessagesFrag", "Button Clicked")
            Log.d("FreelancerMessagesFrag", "${email}")
            Log.d("FreelancerMessagesFrag", "${fullName}")
            Log.d("FreelancerMessagesFrag", Id.toString())
            Log.d("FreelancerMessagesFrag", userId.toString())
        }



        //select service spinner
        val spinner: Spinner = _binding!!.dropdownServices
        Log.d("FreelancerMessagesFragment", "Spinner found: $spinner")

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.serviceTypes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        Log.d("FreelancerMessagesFragment", "Adapter set: ${spinner.adapter}")

        var isFirstTimeInitialization = true
        val savedService = sharedPreferences.getString("selectedServiceKey", null)
        savedService?.let {
            val position = adapter.getPosition(it)
            spinner.setSelection(position)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedService = parent.getItemAtPosition(position) as? String
                val editor = sharedPreferences.edit()
                editor.putString("selectedServiceKey", selectedService)
                editor.apply()
                getChatRooms(selectedService!!)
                if (!isFirstTimeInitialization) {
                    var frg: Fragment? = null
                    frg = childFragmentManager.findFragmentByTag("FreelancerMessagesFragment") ?: return
                    val ft = childFragmentManager.beginTransaction()
                    ft.detach(frg!!)
                    ft.attach(frg!!)
                    ft.commit()
                } else {
                    isFirstTimeInitialization = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(requireContext(), "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecyclerView = _binding!!.rvFreelancerMessageList

        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        //getUsersList()

    }


    private fun getChatRooms(selectService: String) {
        Log.d("Tag", "Check")
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userId = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userId")

        val bookingChatRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("ChatRoom")
        bookingChatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatroomList = mutableListOf<ChatRoom>()
                Log.d("UserId Check", "$userId")
                for (dataSnapShot: DataSnapshot in dataSnapshot.children) {
                    val chatroom = dataSnapShot.getValue(ChatRoom::class.java)
                    Log.d("Checker", "${chatroom?.freelancerId}")
                    if (chatroom != null && (chatroom.customerId == userId || chatroom.freelancerId == userId) && !chatroom.chatroomId.contains("nego")) {
                        chatroomList.add(chatroom)
                    }
                }

                fetchChatRoomsForService(chatroomList, selectService)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DEBUG", "Failed to read value.", databaseError.toException())
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun fetchChatRoomsForService(chatroomList: List<ChatRoom>, serviceSelect: String) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("UserFirebase")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userIdSet = chatroomList.flatMap { listOf(it.customerId, it.freelancerId) }.toSet()
                val userList = mutableListOf<UserFirebase>()

                for (dataSnapShot: DataSnapshot in dataSnapshot.children) {
                    val user = dataSnapShot.getValue(UserFirebase::class.java)
                    if (user != null && user.userId in userIdSet) {
                        userList.add(user)
                    }
                }

                val service = ServicesInstance.retrofitBuilder
                val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0

                service.getServiceIdByFreelancer(freelancerId, serviceSelect).enqueue(object : Callback<Services> {
                    override fun onResponse(call: Call<Services>, response: Response<Services>) {
                        if (response.isSuccessful) {
                            try {
                                val serviceId = response.body()!!.serviceId
                                val serviceName = response.body()!!.name
                                val chatRoomAdapter = ChatUserAdapter(
                                    requireContext(),
                                    chatroomList as ArrayList<ChatRoom>,
                                    serviceId!!,
                                    serviceName
                                )
                                userRecyclerView.adapter = chatRoomAdapter
                            } catch (e: NullPointerException) {
                                Log.e("Service Error", "Service details are missing")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Services>, t: Throwable) {
                        Log.e("FreelancerMessagesFragment", t.toString())
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DEBUG", "Failed to read value.", databaseError.toException())
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FreelancerMessagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FreelancerMessagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}