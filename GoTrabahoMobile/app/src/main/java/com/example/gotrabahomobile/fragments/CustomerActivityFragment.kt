package com.example.gotrabahomobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.Helper.ActivityRecycleViewAdapter
import com.example.gotrabahomobile.Helper.BookingUserAdapter
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.databinding.FragmentCustomerActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerActivityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerActivityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentCustomerActivityBinding? = null
    private lateinit var bookingList: List<BookingUserDTO>
    private lateinit var rvAdapter: BookingUserAdapter
    private val binding get() = _binding!!

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

        _binding = FragmentCustomerActivityBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        getServiceList()
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookingList = ArrayList<BookingUserDTO>()

        // Initialize RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.activityRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = context?.let { ActivityRecycleViewAdapter(bookingList, it) }
    }



    private fun getServiceList(){
        val booking = BookingInstance.retrofitBuilder
        val userId = arguments?.getInt("userId", 0) ?: 0

        booking.getUserBookings(userId).enqueue(object : Callback<List<BookingUserDTO>> {
            override fun onResponse(
                call: Call<List<BookingUserDTO>>,
                response: Response<List<BookingUserDTO>>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    bookingList = response.body()!!

                    binding.activityRecycleView.apply {
                        rvAdapter =
                            BookingUserAdapter(requireContext(), bookingList)
                        adapter = rvAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
            override fun onFailure(call: Call<List<BookingUserDTO>>, t: Throwable) {
                TODO("Not yet implemented")
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
         * @return A new instance of fragment CustomerActivityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerActivityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}