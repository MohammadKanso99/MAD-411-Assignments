package com.example.assignment6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class DetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        // Retrieve data from the arguments
        val name = arguments?.getString("expenseName") ?: "No Name"
        val amount = arguments?.getDouble("expenseAmount") ?: 0.00
        val date = arguments?.getString("expenseDate") ?: "No Date"

        // Set data to the TextViews
        view.findViewById<TextView>(R.id.textViewName2).text = "Name: $name"
        view.findViewById<TextView>(R.id.textViewAmount2).text = "Amount: $amount"
        view.findViewById<TextView>(R.id.textViewDate).text = "Date: $date"

        return view
    }

}