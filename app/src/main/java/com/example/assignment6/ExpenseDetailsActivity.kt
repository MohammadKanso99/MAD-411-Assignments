package com.example.assignment6

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ExpenseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val textViewName2: TextView = findViewById(R.id.textViewName2)
        val textViewAmount2: TextView = findViewById(R.id.textViewAmount2)
        val textViewDate: TextView = findViewById(R.id.textViewDate)

        val name = intent.getStringExtra("NAME")
        val amount = intent.getDoubleExtra("AMOUNT", 0.00)
        val date = intent.getStringExtra("DATE")

        textViewName2.text = "Name: $name"
        textViewAmount2.text = "Amount: $amount"
        textViewDate.text = "Date: $date"

    }
}