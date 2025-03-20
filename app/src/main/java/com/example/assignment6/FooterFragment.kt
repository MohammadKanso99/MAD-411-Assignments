package com.example.assignment6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FooterFragment : Fragment() {
    private var allExpenses: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        allExpenses = view.findViewById(R.id.allExpenses)
        // Inflate the layout for this fragment
        return view
    }

    fun update(total: Double){
        allExpenses?.text = "Total: $total"
    }

}