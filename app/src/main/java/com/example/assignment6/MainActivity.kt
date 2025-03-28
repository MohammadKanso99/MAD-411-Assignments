package com.example.assignment6

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.io.FileNotFoundException
import java.io.IOException
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import java.io.File

private const val FILE_NAME = "expense.txt"

class MainActivity : AppCompatActivity() {

    private val expenseArray = mutableListOf<Expense>()
    private lateinit var editTextName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var editTextDate: EditText
    private lateinit var submitButton: Button
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var financialTipsButton: Button
    private lateinit var footerFragment: FooterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ActivityLifecycle", "onCreate called")

        addHeaderFragment()
        footerFragment = FooterFragment()
        addFooterFragment()

        editTextName = findViewById(R.id.editTextName)
        editTextAmount = findViewById(R.id.editTextAmount)
        editTextDate = findViewById(R.id.editTextDate)
        submitButton = findViewById(R.id.submitButton)
        expenseRecyclerView = findViewById(R.id.expenseRecyclerView)
        financialTipsButton = findViewById(R.id.financialTipsButton)

        expenseAdapter = ExpenseAdapter(expenseArray, this::deleteExpense, this::showDetails)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = expenseAdapter

        financialTipsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.theglobeandmail.com/investing/personal-finance/"))
            startActivity(intent)
        }

        submitButton.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val amount = editTextAmount.text.toString().trim().toDoubleOrNull()
            val date = editTextDate.text.toString().trim()

            if (name.isEmpty()) {
                editTextName.error = "Invalid Name!"
                return@setOnClickListener
            }
            if (amount == null || amount <= 0) {
                editTextAmount.error = "Invalid Amount"
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                editTextDate.error = "Invalid Date"
                return@setOnClickListener
            }

            val expense = Expense(name, amount, date)
            expenseArray.add(expense)
            expenseAdapter.notifyItemInserted(expenseArray.size - 1)

            editTextName.text.clear()
            editTextAmount.text.clear()
            editTextDate.text.clear()
            update()
            saveExpensesToFile(this, expenseArray)
        }

        expenseArray.clear()
        expenseArray.addAll(loadExpensesFromFile(this))
        expenseAdapter.notifyDataSetChanged()
        update()
    }

    private fun addHeaderFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.header, HeaderFragment())
            .commit()
    }

    private fun addFooterFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.footer, footerFragment)
            .commit()
    }

    private fun update() {
        val total = expenseArray.sumOf { it.amount }
        footerFragment.update(total)
    }

    fun deleteExpense(position: Int) {
        expenseArray.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
        update()
        saveExpensesToFile(this, expenseArray)
    }

    private fun showDetails(expense: Expense) {
        val intent = Intent(this, ExpenseDetailsActivity::class.java).apply {
            putExtra("NAME", expense.name)
            putExtra("AMOUNT", expense.amount)
            putExtra("DATE", expense.date)
        }
        startActivity(intent)
    }

    private fun saveExpensesToFile(context: Context, expenseList: List<Expense>) {
        try {
            val json = Gson().toJson(expenseList)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { it.write(json.toByteArray()) }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses: ${e.message}")
        }
    }

    private fun loadExpensesFromFile(context: Context): MutableList<Expense> {
        val expenseList = mutableListOf<Expense>()
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return expenseList

            val json = file.readText()
            val loadedExpenses: List<Expense> = Gson().fromJson(json, object : TypeToken<List<Expense>>() {}.type)
            expenseList.addAll(loadedExpenses)
            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expenseList
    }
}
