package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

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
        return inflater.inflate(R.layout.fragment_freelancer_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatButton: Button = view.findViewById(R.id.buttonTestFree)



        val userId = arguments?.getInt("userId", 0) ?: 0
        val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val email = arguments?.getString("email")
        val fullName = arguments?.getString("fullName")

        Log.d("FreelancerMessagesFrag", "${email}")
        Log.d("FreelancerMessagesFrag", "${fullName}")
        Log.d("FreelancerMessagesFrag", freelancerId.toString())
        Log.d("FreelancerMessagesFrag", userId.toString())

        chatButton.setOnClickListener {
            Log.d("FreelancerMessagesFrag", "Button Clicked")
            val intent = Intent(requireContext(), ASampleChatActivity::class.java)
            intent.putExtra("fullName", fullName)
            intent.putExtra("freelancerId", freelancerId)
//            intent.putExtra("userId", userId)
            startActivity(intent)
        }

//        Log.d("FreelancerMessagesFrag", freelancerId.toString())
//        Log.d("FreelancerMessagesFrag", "${email}")
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