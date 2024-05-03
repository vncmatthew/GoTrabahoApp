package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerAccountFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_customer_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val customCardViewProfile = view.findViewById<androidx.cardview.widget.CardView>(R.id.profile)
        val customCardViewRewards = view.findViewById<androidx.cardview.widget.CardView>(R.id.cardRewards)

        val customCardViewLogout = view.findViewById<androidx.cardview.widget.CardView>(R.id.cardLogout)

        val name = customCardViewProfile.findViewById<TextView>(R.id.customerName)
        val counter = arguments?.getInt("counter", 0) ?: 0
        val userId = arguments?.getInt("userId", 0) ?: 0
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val email = arguments?.getString("email")
        val fullName = arguments?.getString("fullName")
        val longitude = arguments?.getDouble("longitude")
        val latitude = arguments?.getDouble("latitude")
        name.text = fullName
        Log.d("CheckMe", "${fullName}")


        customCardViewProfile.setOnClickListener {

            val intent = Intent(requireActivity(), CustomerProfilePageActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        customCardViewLogout.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        customCardViewRewards.setOnClickListener {
            val intent = Intent(requireActivity(), RewardsActivity::class.java)
            intent.putExtra("counter", counter)
            intent.putExtra("userId", userId)
            intent.putExtra("email", email)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("longitude", longitude)
            intent.putExtra("latitude", latitude)
            startActivity(intent)
        }

//        customCardViewPaymentOptions.setOnClickListener {
//            val intent = Intent(requireActivity(), CustomerProfilePageActivity::class.java)
//            startActivity(intent)
//        }
//
//        customCardViewLogout.setOnClickListener {
//            val intent = Intent(requireActivity(), CustomerProfilePageActivity::class.java)
//            startActivity(intent)
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}