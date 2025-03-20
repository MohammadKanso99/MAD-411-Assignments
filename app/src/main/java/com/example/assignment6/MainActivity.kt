package com.example.assignment6

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    private val expenseArray = mutableListOf<Expense>()
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var editTextDate: EditText
    private lateinit var submitButton: Button
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var financialTipsButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ActivityLifecycle", "onCreate called")


        // using id to find UI elements
        editTextName = findViewById(R.id.editTextName)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDate = findViewById(R.id.editTextDate)
        submitButton = findViewById(R.id.submitButton)
        expenseRecyclerView = findViewById(R.id.expenseRecyclerView)
        financialTipsButton = findViewById(R.id.financialTipsButton)

        expenseAdapter = ExpenseAdapter(expenseArray, this::deleteExpense, this::showDetails)

        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = expenseAdapter

        financialTipsButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.theglobeandmail.com/business/adv/article-how-to-build-a-balanced-us-portfolio-in-2025/?utm_source=google&utm_medium=sem&utm_campaign=544123&utm_content=balanced_portfolio&gad_source=1&gclid=EAIaIQobChMIqcyewZCZjAMVPUn_AR0OdRxZEAAYAyAAEgLMN_D_BwE")
            startActivity(intent)
        }

        submitButton.setOnClickListener{
            val name = editTextName.text.toString().trim()
            val amount = editTextAmount.text.toString().trim().toDoubleOrNull()
            val date = editTextDate.text.toString().trim()

            if (name.isEmpty()){
                editTextName.error = "Invalid Name!"
                return@setOnClickListener
            }

            if (amount == null || amount <= 0){
                editTextAmount.error = "Invalid Amount"
                return@setOnClickListener
            }

            if(date.isEmpty()){
                editTextDate.error = "Invalid Date"
                return@setOnClickListener
            }

            val expense = Expense(name, amount, date)
            expenseArray.add(expense)
            expenseAdapter.notifyItemInserted(expenseArray.size - 1)

            // clear form
            editTextName.text.clear()
            editTextAmount.text.clear()
            editTextDate.text.clear()
        }

    }

    fun deleteExpense(position:Int){
        expenseArray.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
    }

    private fun showDetails(expense: Expense){
        val intent = Intent(this, ExpenseDetailsActivity::class.java)
        intent.putExtra("NAME", expense.name)
        intent.putExtra("AMOUNT", expense.amount)
        intent.putExtra("DATE", expense.date)

        // Start next activity
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")
    }
}
