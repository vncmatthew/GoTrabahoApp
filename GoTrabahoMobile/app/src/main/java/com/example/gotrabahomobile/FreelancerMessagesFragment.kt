package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.ServicesWUserId
import com.example.gotrabahomobile.Helper.FreelancerChatAdapter
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.FragmentCustomerHomeBinding
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
    private lateinit var serviceList: List<Services>
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null


    private val binding get() = _binding!!
    private lateinit var rvAdapter: ServiceAdapter


//    var Id: Int = 0
//    var userId: String = ""
//    var freelancerId: String = ""
//    var firstName: String = ""
//    var lastName: String = ""
//    var email: String = ""
//
//    var fullName: String = ""

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

            Log.d("FreelancerMessagesFrag", "Button Clicked")
            Log.d("FreelancerMessagesFrag", "${email}")
            Log.d("FreelancerMessagesFrag", "${fullName}")
            Log.d("FreelancerMessagesFrag", Id.toString())
            Log.d("FreelancerMessagesFrag", userId.toString())
        }


        val notifButton: ImageButton = _binding!!.include.notifNavbar

        notifButton.setOnClickListener {
            val intent = Intent(requireContext(), CustomerNotificationActivity::class.java)
            startActivity(intent)
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


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedService = parent.getItemAtPosition(position) as? String
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
        getUsersList()

    }


    fun getUsersList() {
        Log.d("Tag", "Check")
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UserFirebase")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("DEBUG", "Path: ${dataSnapshot.ref.path}")
                Log.d("DEBUG", "Data: ${dataSnapshot.value}")
                val user = dataSnapshot.getValue(UserFirebase::class.java)
                Log.d("DEBUG", "User: $user")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DEBUG", "Failed to read value.", databaseError.toException())
            }
        })

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(UserFirebase::class.java)
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    Log.d("TAG", "Value is: $user")
                    if (!user!!.userId.equals(firebase.uid)) {
                        Log.d("List of Users", "Value is: $user")
                        Log.d("List of Active Users", "Value is: $user")
                        userList.add(user)
                    }
                }

                    val service = ServicesInstance.retrofitBuilder
                    val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0

                    service.getServiceIdByFreelancer( freelancerId, selectedService!! ).enqueue(object: Callback<Services>{
                        override fun onResponse(call: Call<Services>, response: Response<Services>) {
                            if(response.isSuccessful){
                                try {
                                    val serviceId = response.body()!!.serviceId
                                    val serviceName = response.body()!!.name
                                    val userAdapter = FreelancerChatAdapter(
                                        requireContext(),
                                        userList,
                                        serviceId!!,
                                        serviceName
                                    )
                                    userRecyclerView.adapter = userAdapter
                                }catch (e: NullPointerException){

                                }
                            }
                        }

                        override fun onFailure(call: Call<Services>, t: Throwable) {
                            Log.d("FreelancerMessagesFragment", "${t}")
                        }

                    })
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