package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.FreelancerChatAdapter
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.UserFirebase
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

    private lateinit var userRecyclerView: RecyclerView
    var userList = ArrayList<UserFirebase>()
    private var _binding: FragmentFreelancerMessagesBinding? = null

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null

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
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatButton: Button = view.findViewById(R.id.buttonTestFree)

        userRecyclerView = view.findViewById(R.id.rvFreelancerMessageList)

        chatButton.setOnClickListener {

//            val intent = Intent(requireContext(), ChatActivity::class.java)
//            startActivity(intent)
        }
        getUsersList()
    }

    fun getUsersList() {
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
                val serviceId = requireActivity().intent.getIntExtra("serviceId",0)
                val serviceName = requireActivity().intent.getStringExtra("serviceName")
                val userAdapter = FreelancerChatAdapter(requireContext(), userList, serviceId, serviceName)

                userRecyclerView.adapter = userAdapter
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